package net.medinnovatelabs.applecare.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.medinnovatelabs.applecare.data.models.ClassifiedImage

@Database(entities = [ClassifiedImage::class], version = 1, exportSchema = false)
abstract class AppleCareDatabase : RoomDatabase() {
	abstract fun classifiedImageDao(): ClassifiedImageDao

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