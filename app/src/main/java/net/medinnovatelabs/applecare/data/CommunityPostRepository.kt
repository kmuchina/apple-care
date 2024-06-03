package net.medinnovatelabs.applecare.data

import kotlinx.coroutines.flow.Flow
import net.medinnovatelabs.applecare.data.models.CommunityPost
import javax.inject.Inject

interface CommunityPostRepository {
	fun getCommunityPosts(): Flow<List<CommunityPost>>
	suspend fun insertCommunityPost(communityPost: CommunityPost)
	suspend fun deleteCommunityPost(communityPost: CommunityPost)
}

class CommunityPostRepositoryImpl @Inject constructor(
	private val communityPostDao: CommunityPostDao
) : CommunityPostRepository {

	override fun getCommunityPosts() = communityPostDao.getAllCommunityPosts()

	override suspend fun insertCommunityPost(communityPost: CommunityPost) {
		return communityPostDao.insert(communityPost)
	}

	override suspend fun deleteCommunityPost(communityPost: CommunityPost) {
		communityPostDao.delete(communityPost)
	}
}