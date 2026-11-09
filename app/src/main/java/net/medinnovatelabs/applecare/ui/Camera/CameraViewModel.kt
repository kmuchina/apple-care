package net.medinnovatelabs.applecare.ui.Camera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.edge.litert.Accelerator
import com.google.ai.edge.litert.LiteRtException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.data.ClassifiedImageRepository
import net.medinnovatelabs.applecare.data.models.ClassifiedImage
import net.medinnovatelabs.applecare.ml.AppleDiseaseClassifier
import net.medinnovatelabs.applecare.ml.PlayServicesEnvironment
import net.medinnovatelabs.applecare.utils.BitmapConverter.convertBitmapToString
import net.medinnovatelabs.applecare.utils.BitmapConverter.convertStringToBitmap
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
	@ApplicationContext private val appContext: Context,
	private val classifiedImageRepository: ClassifiedImageRepository
) : ViewModel() {
	private val _bitmap = MutableStateFlow<Bitmap?>(null)
	val bitmaps = _bitmap.asStateFlow()

	private val _label = MutableStateFlow("")
	val label = _label.asStateFlow()

	private val _confidence = MutableStateFlow<Float?>(0f)
	val confidence = _confidence.asStateFlow()

	private val _sheetPeekHeight = MutableStateFlow(0)
	val sheetPeekHeight = _sheetPeekHeight.asStateFlow()

	/** Non-null when the model couldn't be loaded or run. */
	private val _modelError = MutableStateFlow<String?>(null)
	val modelError = _modelError.asStateFlow()

	// One single-thread executor owns the model: it is created, run and closed only here.
	// This scope is separate from viewModelScope, which is already cancelled when
	// onCleared runs, so closing the model from it would never execute.
	private val inferenceExecutor = Executors.newSingleThreadExecutor()
	private val inferenceScope =
		CoroutineScope(SupervisorJob() + inferenceExecutor.asCoroutineDispatcher())
	private var classifier: AppleDiseaseClassifier? = null

	init {
		// Load (and warm up) the model as soon as the camera screen opens.
		inferenceScope.launch { loadClassifier() }
	}

	/** Classifies [bitmap] off the main thread, then shows and stores the result. */
	fun classify(bitmap: Bitmap) {
		inferenceScope.launch {
			val classifier = loadClassifier() ?: return@launch
			try {
				val scaled = AppleDiseaseClassifier.scaleToInput(bitmap)
				val scores = classifier.infer(AppleDiseaseClassifier.preprocess(scaled))
				val best = scores.indices.maxBy { scores[it] }
				showResult(
					ClassifiedImage(
						name = convertBitmapToString(scaled),
						label = AppleDiseaseClassifier.LABELS[best],
						confidence = scores[best]
					)
				)
			} catch (e: LiteRtException) {
				Log.e(TAG, "Inference failed", e)
				_modelError.value = e.message ?: "Couldn't analyse the photo"
			}
		}
	}

	/** Runs on the inference thread. Prefers the GPU and falls back to the CPU. */
	private suspend fun loadClassifier(): AppleDiseaseClassifier? {
		classifier?.let { return it }
		val environment = try {
			PlayServicesEnvironment.get(appContext)
		} catch (e: Exception) {
			Log.e(TAG, "LiteRT from Google Play services is unavailable", e)
			_modelError.value = "The AI model isn't available on this device (needs Google Play services)"
			return null
		}
		val created = try {
			AppleDiseaseClassifier(appContext, Accelerator.GPU, environment)
		} catch (gpuError: LiteRtException) {
			Log.w(TAG, "GPU unavailable, falling back to CPU", gpuError)
			try {
				AppleDiseaseClassifier(appContext, Accelerator.CPU, environment)
			} catch (cpuError: LiteRtException) {
				Log.e(TAG, "Couldn't load the model", cpuError)
				_modelError.value = cpuError.message ?: "Couldn't load the AI model"
				return null
			}
		}
		classifier = created
		return created
	}

	private fun showResult(classifiedImage: ClassifiedImage) {
		// Set the label before the bitmap: the sheet appears when the bitmap becomes non-null.
		_label.value = classifiedImage.label
		_confidence.value = classifiedImage.confidence
		_bitmap.value = convertStringToBitmap(classifiedImage.name)
		_sheetPeekHeight.value = 500

		viewModelScope.launch {
			classifiedImageRepository.insertClassifiedImage(classifiedImage)
		}
	}

	fun clearModelError() {
		_modelError.value = null
	}

	fun reset() {
		_sheetPeekHeight.value = 0
		_bitmap.value = null
		_label.value = ""
		_confidence.value = 0f
	}

	override fun onCleared() {
		inferenceScope.launch { classifier?.close() }.invokeOnCompletion {
			inferenceScope.cancel()
		}
		inferenceExecutor.shutdown()
	}

	companion object {
		private const val TAG = "CameraViewModel"
	}
}
