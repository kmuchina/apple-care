package net.medinnovatelabs.applecare.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base64
import java.io.ByteArrayOutputStream

object BitmapConverter {
	fun convertBitmapToString(bitmap: Bitmap): String {
		val baos = ByteArrayOutputStream()
		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
		val byteArray = baos.toByteArray()
		return Base64.encodeBase64String(byteArray)
	}

	fun convertStringToBitmap(encodedString: String): Bitmap {
		return try {
			val encodeByte = Base64.decodeBase64(encodedString)
			BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
		} catch (e: Exception) {
			e.printStackTrace()
			Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
		}
	}
}