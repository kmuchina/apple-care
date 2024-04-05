package net.medinnovatelabs.applecare.firebasedata

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import net.medinnovatelabs.applecare.data.models.Resource

interface AuthRepository {
	fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
	fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>
}