package net.medinnovatelabs.applecare.ui.main.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.data.CommunityPostRepository
import net.medinnovatelabs.applecare.data.models.CommunityPost
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
	private val communityPostRepository: CommunityPostRepository
) : ViewModel() {
	val communityPosts = communityPostRepository.getCommunityPosts()
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
			initialValue = emptyList()
		)

	// Launched in viewModelScope (not a composable's scope) so the write still completes
	// when the screen that triggered it is popped right away.
	fun addPost(communityPost: CommunityPost) {
		viewModelScope.launch { communityPostRepository.insertCommunityPost(communityPost) }
	}

	fun deletePost(communityPost: CommunityPost) {
		viewModelScope.launch { communityPostRepository.deleteCommunityPost(communityPost) }
	}

	companion object {
		private const val TIMEOUT_MILLIS = 5_000L
	}
}
