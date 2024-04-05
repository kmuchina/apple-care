package net.medinnovatelabs.applecare.firebasedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.medinnovatelabs.applecare.data.models.Resource
import javax.inject.Inject

data class SignInState(
	val isLoading: Boolean = false,
	val isSuccess: String? = "",
	val isError: String? = "",
	val isSignInSuccessful: Boolean = false,
	val signInError: String? = null
)

@HiltViewModel
class SignInViewModel @Inject constructor(
	private val repository: AuthRepository
) : ViewModel() {

	val _signInState = Channel<SignInState>()
	val signInState = _signInState.receiveAsFlow()
	private val _state = MutableStateFlow(SignInState())
	val state = _state.asStateFlow()

	fun onSignInResult(result: SignInResult) {
		_state.update {
			it.copy(
				isSignInSuccessful = result.data != null,
				signInError = result.errorMessage
			)
		}
	}

	fun loginUser(email: String, password: String) = viewModelScope.launch {
		repository.loginUser(email, password).collect { result ->
			when (result) {
				is Resource.Success -> {
					_signInState.send(SignInState(isSuccess = "Sign In Success"))
				}

				is Resource.Loading -> {
					_signInState.send(SignInState(isLoading = true))
				}

				is Resource.Error -> {
					_signInState.send(SignInState(isError = result.message))
				}
			}
		}
	}
}
