package net.medinnovatelabs.applecare.ui.Camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import net.medinnovatelabs.applecare.data.ClassifiedImageRepository
import net.medinnovatelabs.applecare.data.models.ClassifiedImage
import net.medinnovatelabs.applecare.utils.BitmapConverter.convertStringToBitmap
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
	private val classifiedImageRepository: ClassifiedImageRepository
) : ViewModel() {
	private val _bitmap = MutableStateFlow<Bitmap?>(null)
	val bitmaps = _bitmap.asStateFlow()

	private val _label = MutableStateFlow<String>("")
	val label = _label.asStateFlow()

	private val _confidence = MutableStateFlow<Float?>(0f)
	val confidence = _confidence.asStateFlow()

	private val _sheetPeekHeight = MutableStateFlow(0)
	val sheetPeekHeight = _sheetPeekHeight.asStateFlow()

	val classifiedImages = classifiedImageRepository.getClassifiedImages()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
			initialValue = emptyList()
		)

	suspend fun onTakePhoto(classifiedImage: ClassifiedImage) {
		_sheetPeekHeight.value = 500

		_bitmap.value = convertStringToBitmap(classifiedImage.name)
		_label.value = classifiedImage.label
		_confidence.value = classifiedImage.confidence

		classifiedImageRepository.insertClassifiedImage(classifiedImage)
	}

	suspend fun onDelete(classifiedImage: ClassifiedImage) {
		classifiedImageRepository.deleteClassifiedImage(classifiedImage)
	}

	fun reset() {
		_sheetPeekHeight.value = 0
		_bitmap.value = null
		_label.value = ""
		_confidence.value = 0f
	}

	companion object {
		private const val TIMEOUT_MILLIS = 5_000L
	}
}