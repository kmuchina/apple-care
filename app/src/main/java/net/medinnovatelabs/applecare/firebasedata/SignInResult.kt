package net.medinnovatelabs.applecare.firebasedata

data class SignInResult(
    val data: net.medinnovatelabs.applecare.firebasedata.UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)