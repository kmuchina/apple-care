package net.medinnovatelabs.applecare.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object BitmapConverter {
	fun convertBitmapToString(bitmap: Bitmap): String {
		val baos = ByteArrayOutputStream()
		bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
		val byteArray = baos.toByteArray()
		// NO_WRAP matches the previous (Apache commons-codec) output, so existing rows still decode.
		return Base64.encodeToString(byteArray, Base64.NO_WRAP)
	}

	fun convertStringToBitmap(encodedString: String): Bitmap {
		return try {
			val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
			BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
				?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
		} catch (e: Exception) {
			e.printStackTrace()
			Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
		}
	}
}
