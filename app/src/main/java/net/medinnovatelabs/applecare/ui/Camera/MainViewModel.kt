package net.medinnovatelabs.applecare.ui.Camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
	private val _bitmap = MutableStateFlow<Bitmap?>(null)
	val bitmaps = _bitmap.asStateFlow()

	private val _sheetPeekHeight = MutableStateFlow(0)
	val sheetPeekHeight = _sheetPeekHeight.asStateFlow()

	fun onTakePhoto(bitmap: Bitmap?) {
		if (bitmap == null) _sheetPeekHeight.value = 0
		else _sheetPeekHeight.value = 500
		_bitmap.value = bitmap
	}


}