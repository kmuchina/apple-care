package net.medinnovatelabs.applecare.data

import kotlinx.coroutines.flow.Flow
import net.medinnovatelabs.applecare.data.models.ClassifiedImage
import javax.inject.Inject

interface ClassifiedImageRepository {
	fun getClassifiedImages(): Flow<List<ClassifiedImage>>
	suspend fun insertClassifiedImage(classifiedImage: ClassifiedImage)
	suspend fun deleteClassifiedImage(classifiedImage: ClassifiedImage)
}

class ClassifiedImageRepositoryImpl @Inject constructor(
	private val classifiedImageDao: ClassifiedImageDao
) : ClassifiedImageRepository {

	override fun getClassifiedImages() = classifiedImageDao.getAllClassifiedImages()

	override suspend fun insertClassifiedImage(classifiedImage: ClassifiedImage) {
		return classifiedImageDao.insert(classifiedImage)
	}

	override suspend fun deleteClassifiedImage(classifiedImage: ClassifiedImage) {
		classifiedImageDao.delete(classifiedImage)
	}
}