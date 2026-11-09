package net.medinnovatelabs.applecare.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.medinnovatelabs.applecare.data.models.ClassifiedImage
import net.medinnovatelabs.applecare.data.models.CommunityPost

@Database(entities = [ClassifiedImage::class, CommunityPost::class], version = 1, exportSchema = true)
abstract class AppleCareDatabase : RoomDatabase() {
	abstract fun classifiedImageDao(): ClassifiedImageDao
	abstract fun communityPostDao(): CommunityPostDao

	companion object {
		@Volatile
		private var Instance: AppleCareDatabase? = null
		fun getDatabase(context: Context): AppleCareDatabase {
			return Instance ?: synchronized(this) {
				Room.databaseBuilder(context, AppleCareDatabase::class.java, "ac_db")
					.build()
					.also { Instance = it }
			}
		}
	}
}