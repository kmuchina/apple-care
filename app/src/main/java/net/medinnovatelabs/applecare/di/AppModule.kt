package net.medinnovatelabs.applecare.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.medinnovatelabs.applecare.data.AppleCareDatabase
import net.medinnovatelabs.applecare.data.ClassifiedImageDao
import net.medinnovatelabs.applecare.data.ClassifiedImageRepository
import net.medinnovatelabs.applecare.data.ClassifiedImageRepositoryImpl
import net.medinnovatelabs.applecare.firebasedata.AuthRepository
import net.medinnovatelabs.applecare.firebasedata.AuthRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun providesFirebaseAuth() = FirebaseAuth.getInstance()

	@Provides
	@Singleton
	fun providesRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository {
		return AuthRepositoryImpl(firebaseAuth)
	}

	@Provides
	@Singleton
	fun providesClassifiedImageDao(appleCareDatabase: AppleCareDatabase): ClassifiedImageDao {
		return appleCareDatabase.classifiedImageDao()
	}

	@Provides
	@Singleton
	fun providesAppleCareDatabase(@ApplicationContext context: Context): AppleCareDatabase {
		return AppleCareDatabase.getDatabase(context)
	}

	@Provides
	fun providesClassifiedImageRepository(classifiedImageDao: ClassifiedImageDao): ClassifiedImageRepository {
		return ClassifiedImageRepositoryImpl(classifiedImageDao)
	}
}

//https://chat.whatsapp.com/DtWoGm2OXRVD576aidnnW6
