package com.aleet.chattleroyale.useCases.remote.user

import com.aleet.chattleroyale.repositories.UserRemoteRepo
import com.aleet.chattleroyale.utils.BaseUseCase
import com.aleet.chattleroyale.utils.Result
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class GetCRUserUseCase @Inject constructor(
    private val userRemoteRepo: UserRemoteRepo
): BaseUseCase<Boolean, GetCrUserRequest>() {
    override suspend fun run(params: GetCrUserRequest): Result<Boolean, Exception> {
        return try {
            val userSnapshot = userRemoteRepo.getUser(params.id)
            userSnapshot.addOnSuccessListener(params.onSuccessListener)
            userSnapshot.addOnFailureListener(params.onFailureListener)
            Result.Success(true)
        } catch (ex: Exception) {
            Result.Success(false)
        }
    }
}

data class GetCrUserRequest(
    val id: String,
    val onSuccessListener: OnSuccessListener<DocumentSnapshot>,
    val onFailureListener: OnFailureListener
)