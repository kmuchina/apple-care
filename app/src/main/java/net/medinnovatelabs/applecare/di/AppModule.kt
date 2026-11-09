package net.medinnovatelabs.applecare.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.medinnovatelabs.applecare.data.AppleCareDatabase
import net.medinnovatelabs.applecare.data.ClassifiedImageDao
import net.medinnovatelabs.applecare.data.ClassifiedImageRepository
import net.medinnovatelabs.applecare.data.ClassifiedImageRepositoryImpl
import net.medinnovatelabs.applecare.data.CommunityPostDao
import net.medinnovatelabs.applecare.data.CommunityPostRepository
import net.medinnovatelabs.applecare.data.CommunityPostRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun providesAppleCareDatabase(@ApplicationContext context: Context): AppleCareDatabase {
		return AppleCareDatabase.getDatabase(context)
	}

	@Provides
	@Singleton
	fun providesClassifiedImageDao(appleCareDatabase: AppleCareDatabase): ClassifiedImageDao {
		return appleCareDatabase.classifiedImageDao()
	}

	@Provides
	fun providesClassifiedImageRepository(classifiedImageDao: ClassifiedImageDao): ClassifiedImageRepository {
		return ClassifiedImageRepositoryImpl(classifiedImageDao)
	}

	@Provides
	@Singleton
	fun providesCommunityPostDao(appleCareDatabase: AppleCareDatabase): CommunityPostDao {
		return appleCareDatabase.communityPostDao()
	}

	@Provides
	fun providesCommunityPostRepository(communityPostDao: CommunityPostDao): CommunityPostRepository {
		return CommunityPostRepositoryImpl(communityPostDao)
	}

}
