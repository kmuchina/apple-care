package net.medinnovatelabs.applecare.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.medinnovatelabs.applecare.data.models.ClassifiedImage

@Dao
interface ClassifiedImageDao {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(classifiedImage: ClassifiedImage)

	@Query("SELECT * FROM classified_images ORDER BY id DESC")
	fun getAllClassifiedImages(): Flow<List<ClassifiedImage>>

	@Delete
	suspend fun delete(classifiedImage: ClassifiedImage)
}