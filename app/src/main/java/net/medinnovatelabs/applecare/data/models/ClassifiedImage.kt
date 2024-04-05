package net.medinnovatelabs.applecare.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "classified_images")
data class ClassifiedImage(
	@PrimaryKey(autoGenerate = true)
	val id: Long = 0,
	val name: String,
	val label: String,
	val confidence: Float,
)
