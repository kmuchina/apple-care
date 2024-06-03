package net.medinnovatelabs.applecare.ui.main.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
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
			started = SharingStarted.WhileSubscribed(CommunityViewModel.TIMEOUT_MILLIS),
			initialValue = emptyList()
		)

	suspend fun addPost(communityPost: CommunityPost) {
		communityPostRepository.insertCommunityPost(communityPost)
	}

	suspend fun deletePost(communityPost: CommunityPost) {
		communityPostRepository.deleteCommunityPost(communityPost)
	}

	companion object {
		private const val TIMEOUT_MILLIS = 5_000L
	}
}