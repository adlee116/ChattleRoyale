package com.aleet.chattleroyale.presentation.login

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.presentation.authorisation.SignInState
import com.aleet.chattleroyale.requestModels.LoginRequest
import com.aleet.chattleroyale.useCases.local.user.SaveUniqueUserIdUseCase
import com.aleet.chattleroyale.useCases.local.user.SaveUserLocallyUseCase
import com.aleet.chattleroyale.useCases.remote.auth.GetCurrentFireBaseUserUseCase
import com.aleet.chattleroyale.useCases.remote.auth.LoginUseCase
import com.aleet.chattleroyale.useCases.remote.user.CreateInitialUserUseCase
import com.aleet.chattleroyale.useCases.remote.user.GetCRUserUseCase
import com.aleet.chattleroyale.useCases.remote.user.GetCrUserRequest
import com.aleet.chattleroyale.utils.justValue
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getCurrentFireBaseUser: GetCurrentFireBaseUserUseCase,
    private val saveUserLocallyUseCase: SaveUserLocallyUseCase,
    private val getCRUserUseCase: GetCRUserUseCase,
    private val createInitialUserUseCase: CreateInitialUserUseCase,
    private val saveUniqueUserIdUseCase: SaveUniqueUserIdUseCase
) : ViewModel() {

    //TODO
    // Need to add an init, this is to first collect the check on whether the user is logged in already
    // And then to show a loading symobl while we wait. Once the check is complete, update the state,
    // So we know we have checked and then allow us to act on the screen

    //TODO
    // Need to add facebook login

    //TODO
    // Ensure that the facebook and google login are coming through the same route? Using a enum class for type (just a suggestion)



    private val _events: MutableStateFlow<LoginReaction?> = MutableStateFlow(null)
    val events: StateFlow<LoginReaction?> get() = _events.asStateFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _googleSignInClient = MutableLiveData<GoogleSignInClient>()
    val googleSignInClient: LiveData<GoogleSignInClient> = _googleSignInClient

    fun setGoogleSignInClient(googleSignInClient: GoogleSignInClient) {
        _googleSignInClient.value = googleSignInClient
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.let { fireBaseAuthWithGoogle(it.idToken!!) }
        } catch (e: ApiException) {
            Log.d("Login", "SignInResult:failed code=" + e.statusCode)
        }
    }

    private fun fireBaseAuthWithGoogle(idToken: String) {
        val fireBaseAuth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        fireBaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = fireBaseAuth.currentUser
                    createInitialUser(user)
                } else {
                    _events.value = LoginReaction.FailedLogin("Task failed")
                }
            }
    }

    private fun getCrUser(uid: String) {
        val roomRequest = GetCrUserRequest(
            uid, onSuccessListener, onFailureListener
        )
        getCRUserUseCase.invoke(viewModelScope, roomRequest)
    }

    private val onSuccessListener = OnSuccessListener<DocumentSnapshot> {
        if(it.exists()) {
            val user = it.toObject(User::class.java)
            user?.let { collectedUser ->
                viewModelScope.launch {
                    saveUserLocallyUseCase.justValue(collectedUser)
                    saveUniqueUserIdUseCase.justValue(collectedUser.uid)
                    _events.value = LoginReaction.SuccessfulLogin
                }
            }
        }
    }

    private val onFailureListener = OnFailureListener {
        Log.d("", it.message.toString())
    }

    private fun createInitialUser(firebaseUser: FirebaseUser?) {
        firebaseUser?.uid?.let { id ->
            val newUser = User().apply {
                uid = id
                userName = firebaseUser.displayName ?: ""
                email = firebaseUser.email ?: ""
                profilePicture = firebaseUser.photoUrl.toString()
                gamesWon = 0
                gamesPlayed = 0
                lastOnline = System.currentTimeMillis()
                this.friends = hashMapOf()
            }
            createInitialUserUseCase.invoke(viewModelScope, newUser) { result ->
                result.result(
                    onSuccess = { storeUser(newUser) },
                    onFailure = {
                        _events.value = LoginReaction.FailedLogin("Registration failed to create a new user")
                    }
                )
            }
        }
    }

    private fun storeUser(user: User) {
        saveUniqueUserIdUseCase.justValue(user.uid)
        saveUserLocallyUseCase.invoke(viewModelScope, user) { result ->
            result.result(
                onSuccess = { _events.value = LoginReaction.SuccessfulLogin },
                onFailure = {
                    _events.value = LoginReaction.FailedUserPersist("Unable to save user locally")
                }
            )
        }
    }

    fun checkIfUserAlreadyAuthorised() {
        getCurrentFireBaseUser.invoke(viewModelScope) { result ->
            result.result(
                onSuccess = {
                    it?.let { getCrUser(it.uid) }
                },
                onFailure = {}
            )
        }
    }

    fun process(viewEvent: LoginViewEvent) {
        when (viewEvent) {
            is LoginViewEvent.LoginClicked -> validateFieldsAndLogin(viewEvent.loginRequest)
            is LoginViewEvent.OtherAuthClicked -> {}
            LoginViewEvent.RegisterClicked -> {}
        }
    }

    private fun login(loginRequest: LoginRequest) {
        loginUseCase.invoke(viewModelScope, loginRequest) { result ->
            result.result(
                onSuccess = {
                    it.addOnSuccessListener { authResult ->
                        authResult.user?.uid?.let { userId -> getCrUser(userId) }
                    }
                    it.addOnFailureListener { authResult ->
                        _events.value = LoginReaction.FailedLogin(authResult.message ?: "Login Failed")
                    }
                },
                onFailure = {
                    _events.value = LoginReaction.FailedLogin(it.message ?: "Login failed")
                }
            )
        }
    }

    private fun emailValid(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    // TODO, extract this and write tests
    private fun passwordValid(password: String): Pair<Boolean, String> {
        val digitPattern = Pattern.compile(".*[0-9].*")
        val lowerCasePattern = Pattern.compile(".*[a-z].*")
        val upperCasePattern = Pattern.compile(".*[A-Z].*")
        val specialCharPattern = Pattern.compile(".*[!@#$%^&+=].*")
        val noSpacePattern = Pattern.compile("\\S+")
        val minLengthPattern = Pattern.compile(".{4,}")

        val feedback = StringBuilder("Password should contain: ")

        if (!digitPattern.matcher(password).matches()) {
            feedback.append("At least one digit. ")
        }
        if (!lowerCasePattern.matcher(password).matches()) {
            feedback.append("At least one lower case letter. ")
        }
        if (!upperCasePattern.matcher(password).matches()) {
            feedback.append("At least one upper case letter. ")
        }
        if (!specialCharPattern.matcher(password).matches()) {
            feedback.append("At least one special character (!@#$%^&+=). ")
        }
        if (!noSpacePattern.matcher(password).matches()) {
            feedback.append("No space. ")
        }
        if (!minLengthPattern.matcher(password).matches()) {
            feedback.append("Minimum length of 4 characters. ")
        }

        val isValid = digitPattern.matcher(password).matches() &&
                lowerCasePattern.matcher(password).matches() &&
                upperCasePattern.matcher(password).matches() &&
                specialCharPattern.matcher(password).matches() &&
                noSpacePattern.matcher(password).matches() &&
                minLengthPattern.matcher(password).matches()

        return Pair(isValid, feedback.toString())
    }

    private fun validateFieldsAndLogin(loginRequest: LoginRequest) {
        val validPassword = passwordValid(loginRequest.password)
        when {
            !emailValid(loginRequest.username) -> _events.value = LoginReaction.InvalidCredentials("Email invalid")
            !validPassword.first -> _events.value = LoginReaction.InvalidCredentials(validPassword.second)
            else -> {
                login(loginRequest)
            }
        }
        viewModelScope.launch { resetStateAfterDelay() }
    }

    // Resetting state so we can send the same state again
    private suspend fun resetStateAfterDelay() {
        delay(500)
        _events.value = null
    }

    sealed class LoginViewEvent {
        class LoginClicked(val loginRequest: LoginRequest) : LoginViewEvent()
        object RegisterClicked : LoginViewEvent()
        class OtherAuthClicked(authType: AuthType) : LoginViewEvent()
    }

    sealed class LoginReaction {
        object SuccessfulLogin : LoginReaction()
        class FailedLogin(val message: String) : LoginReaction()
        class InvalidCredentials(val message: String) : LoginReaction()
        class FailedUserPersist(val message: String) : LoginReaction()
    }

}

enum class AuthType {
    FIREBASE,
    FACEBOOK,
    GOOGLE
}

