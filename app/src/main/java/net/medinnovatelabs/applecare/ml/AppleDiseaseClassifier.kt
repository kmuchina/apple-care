package net.medinnovatelabs.applecare.ml

import android.content.Context
import android.graphics.Bitmap
import com.google.ai.edge.litert.Accelerator
import com.google.ai.edge.litert.CompiledModel
import com.google.ai.edge.litert.Environment
import com.google.ai.edge.litert.TensorBuffer

/**
 * Runs the apple leaf disease model (`assets/apple_v2.tflite`) with the LiteRT CompiledModel API.
 *
 * Model contract: input is 1 x 128 x 128 x 3 float32 (NHWC, RGB, raw 0..255 values, no
 * normalization); output is 4 float scores in the order of [LABELS].
 *
 * Not thread-safe: create, use and close it on one single-thread dispatcher.
 */
class AppleDiseaseClassifier(
	context: Context,
	accelerator: Accelerator,
	environment: Environment
) : AutoCloseable {

	private val model = CompiledModel.create(
		context.assets,
		MODEL_ASSET,
		CompiledModel.Options(accelerator),
		environment
	)
	private val inputs = mutableListOf<TensorBuffer>()
	private val outputs = mutableListOf<TensorBuffer>()

	init {
		try {
			inputs += model.createInputBuffers()
			outputs += model.createOutputBuffers()
			// Warm-up run so GPU shader compilation doesn't delay the first real photo.
			infer(FloatArray(INPUT_SIZE))
		} catch (e: Exception) {
			close()
			throw e
		}
	}

	/** Returns one score per entry of [LABELS]. */
	fun infer(input: FloatArray): FloatArray {
		require(input.size == INPUT_SIZE) { "Expected $INPUT_SIZE values, got ${input.size}" }
		inputs[0].writeFloat(input)
		model.run(inputs, outputs)
		return outputs[0].readFloat()
	}

	override fun close() {
		(inputs + outputs).forEach { it.close() }
		model.close()
	}

	companion object {
		const val IMAGE_SIZE = 128
		private const val INPUT_SIZE = IMAGE_SIZE * IMAGE_SIZE * 3
		private const val MODEL_ASSET = "apple_v2.tflite"

		val LABELS = listOf("Apple Black Rot", "Apple Cedar Rust", "Healthy Apple", "Apple Scab")

		/** Scales [bitmap] to the model input size, as a software ARGB_8888 bitmap. */
		fun scaleToInput(bitmap: Bitmap): Bitmap = checkNotNull(
			Bitmap.createScaledBitmap(bitmap, IMAGE_SIZE, IMAGE_SIZE, false)
				// HARDWARE bitmaps (e.g. from ImageDecoder) can't be read with getPixels.
				.copy(Bitmap.Config.ARGB_8888, false)
		)

		/** Converts an [IMAGE_SIZE] x [IMAGE_SIZE] bitmap to the model's raw RGB float input. */
		fun preprocess(scaled: Bitmap): FloatArray {
			val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE).also {
				scaled.getPixels(it, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)
			}
			val out = FloatArray(INPUT_SIZE)
			pixels.forEachIndexed { i, p ->
				out[i * 3] = (p shr 16 and 0xFF).toFloat()
				out[i * 3 + 1] = (p shr 8 and 0xFF).toFloat()
				out[i * 3 + 2] = (p and 0xFF).toFloat()
			}
			return out
		}
	}
}
