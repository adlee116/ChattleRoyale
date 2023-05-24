package com.aleet.chattleroyale.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleet.chattleroyale.models.User
import com.aleet.chattleroyale.useCases.local.user.GetUniqueUserIdUseCase
import com.aleet.chattleroyale.useCases.local.user.GetUserLocallyUseCase
import com.aleet.chattleroyale.utils.justValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class UserProfileViewModel @Inject constructor(
    private val getUserLocallyUseCase: GetUserLocallyUseCase,
    private val getUniqueUserIdUseCase: GetUniqueUserIdUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _failure = MutableStateFlow(false)
    val failure = _failure.asStateFlow()

    init {
        _loading.value = true
        fetchUser()
    }

    private fun fetchUser() {
        val userId = getUniqueUserIdUseCase.justValue(Unit)?.toInt()
        userId?.let {
            getUserLocallyUseCase.invoke(viewModelScope, userId) { result ->
                result.result(
                    onSuccess = { _user.value = it },
                    onFailure = { _failure.value = true }
                )
            }
        }
    }

    private fun getLocalDetailsOrCollectFirebaseUser() {
        // TODO save some shit down to local to get by this now , like fb id
        // If we have lost it, go and get fb user again
    }

//    fun onNameChange(newName: String) {
//        val currentUser = _user.value ?: return
//        if (currentUser.displayName != newName) {
//            currentUser.displayName = newName
//            updateUser(currentUser)
//        }
//    }
//
//    fun onPasswordChange(newPassword: String) {
//        // TODO: Implement password change
//    }
//
//    fun onNotificationChange(newStatus: Boolean) {
//        val currentUser = _user.value ?: return
//        if (currentUser.notificationsEnabled != newStatus) {
//            currentUser.notificationsEnabled = newStatus
//            updateUser(currentUser)
//        }
//    }
//
//    private fun updateUser(user: User) {
//        viewModelScope.launch {
//            userRepository.updateUser(user)
//        }
//    }
}
