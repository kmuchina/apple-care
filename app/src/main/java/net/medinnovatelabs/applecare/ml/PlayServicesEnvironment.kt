package net.medinnovatelabs.applecare.ml

import android.content.Context
import com.google.ai.edge.litert.Environment
import com.google.android.gms.litert.LiteRt
import com.google.android.gms.litert.client.LiteRtInitializationOptions
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await

/**
 * The single LiteRT [Environment] for the process, provided by Google Play services.
 * The first call may download the LiteRT module, so call it off the main thread.
 */
object PlayServicesEnvironment {
	private val mutex = Mutex()
	private var environment: Environment? = null

	suspend fun get(context: Context): Environment = mutex.withLock {
		environment ?: LiteRt.initialize(
			context.applicationContext,
			LiteRtInitializationOptions.builder().setEnableGpu(true).build()
		).await().also { environment = it }
	}
}
