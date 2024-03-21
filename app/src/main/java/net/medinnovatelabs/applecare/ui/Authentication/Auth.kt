package net.medinnovatelabs.applecare.ui.Authentication

import android.content.Context
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import net.medinnovatelabs.applecare.R

class GoogleAuthClient(
	private val context: Context,
	private val oneTapClient: SignInClient
) {
	private val auth = Firebase.auth

	suspend fun signIn(): IntentSender? {
		val result = try {
			oneTapClient.beginSignIn(
				buildSignInRequest()
			).await()
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
		return result?.pendingIntent?.intentSender
	}

	private fun buildSignInRequest(): BeginSignInRequest {
		return BeginSignInRequest.builder()
			.setGoogleIdTokenRequestOptions(
				BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
					.setSupported(true)
					.setServerClientId(context.getString(R.string.web_client_id))
					.setFilterByAuthorizedAccounts(false)
					.build()
			)
			.setAutoSelectEnabled(true)
			.build()
	}
}