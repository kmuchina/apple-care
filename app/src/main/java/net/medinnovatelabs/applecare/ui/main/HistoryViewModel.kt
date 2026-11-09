package net.medinnovatelabs.applecare.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.data.ClassifiedImageRepository
import net.medinnovatelabs.applecare.data.models.ClassifiedImage
import javax.inject.Inject

/**
 * Backs the History tab. Kept separate from CameraViewModel so that opening History
 * doesn't spin up the LiteRT model and its inference thread.
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
	private val classifiedImageRepository: ClassifiedImageRepository
) : ViewModel() {
	val classifiedImages = classifiedImageRepository.getClassifiedImages()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
			initialValue = emptyList()
		)

	fun delete(classifiedImage: ClassifiedImage) {
		viewModelScope.launch { classifiedImageRepository.deleteClassifiedImage(classifiedImage) }
	}

	companion object {
		private const val TIMEOUT_MILLIS = 5_000L
	}
}
