package net.medinnovatelabs.applecare.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import net.medinnovatelabs.applecare.data.models.ClassifiedImage
import net.medinnovatelabs.applecare.ml.AppleV2
import net.medinnovatelabs.applecare.utils.BitmapConverter.convertBitmapToString
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TensorFLowHelper {

	const val IMAGESIZE = 128

	private val classLabels =
		arrayOf("Apple Black Rot", "Apple Cedar Rust", "Healthy Apple", "Apple Scab")

	// Loading the model is expensive, so it is created once and reused for the app's lifetime.
	private var model: AppleV2? = null

	/**
	 * Classifies [image], which must already be [IMAGESIZE] x [IMAGESIZE].
	 * Runs inference synchronously: call it from a background dispatcher.
	 */
	@WorkerThread
	@Synchronized
	fun classifyImage(image: Bitmap, context: Context): ClassifiedImage {
		val model = model ?: AppleV2.newInstance(context.applicationContext).also { model = it }

		val inputFeature0: TensorBuffer =
			TensorBuffer.createFixedSize(intArrayOf(1, IMAGESIZE, IMAGESIZE, 3), DataType.FLOAT32)

		val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * IMAGESIZE * IMAGESIZE * 3)
		byteBuffer.order(ByteOrder.nativeOrder())

		val intValues = IntArray(IMAGESIZE * IMAGESIZE)
		image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

		// Extract R, G and B for each pixel. The model expects raw 0..255 float values.
		for (pixel in intValues) {
			byteBuffer.putFloat((pixel shr 16 and 0xFF).toFloat())
			byteBuffer.putFloat((pixel shr 8 and 0xFF).toFloat())
			byteBuffer.putFloat((pixel and 0xFF).toFloat())
		}
		inputFeature0.loadBuffer(byteBuffer)

		val confidences = model.process(inputFeature0).outputFeature0AsTensorBuffer.floatArray

		var maxPos = 0
		var maxConfidence = 0f
		for (i in confidences.indices) {
			if (confidences[i] > maxConfidence) {
				maxConfidence = confidences[i]
				maxPos = i
			}
		}

		return ClassifiedImage(
			name = convertBitmapToString(image),
			label = classLabels[maxPos],
			confidence = maxConfidence
		)
	}
}
