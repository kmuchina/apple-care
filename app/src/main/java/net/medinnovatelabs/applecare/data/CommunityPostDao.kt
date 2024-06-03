package net.medinnovatelabs.applecare.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.medinnovatelabs.applecare.data.models.CommunityPost

@Dao
interface CommunityPostDao {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(communityPost: CommunityPost)

	@Query("SELECT * FROM community_posts ORDER BY postTime ASC")
	fun getAllCommunityPosts(): Flow<List<CommunityPost>>

	@Delete
	suspend fun delete(communityPost: CommunityPost)
}