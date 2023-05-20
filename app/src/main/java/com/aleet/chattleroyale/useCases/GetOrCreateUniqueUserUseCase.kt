package com.squadsandshots_android.useCases

import com.squadsandshots_android.core.utils.BaseUseCase
import com.squadsandshots_android.core.utils.Result
import com.squadsandshots_android.firebaseModels.Player
import com.squadsandshots_android.localStorage.LocalStorageInterface
import java.util.*
import javax.inject.Inject
import kotlin.Exception

class GetOrCreateUniqueUserUseCase @Inject constructor(
    val localStorage: LocalStorageInterface
) : BaseUseCase<Player, String>() {
    override suspend fun run(params: String): Result<Player, Exception> {
        return try {
            localStorage.getPlayer()?.let {
                if(it.name != params) {
                    it.name = params
                    localStorage.setPlayer(it)
                }
                return Result.Success(it)
            } ?: run {
                val newPlayerItem = Player(UUID.randomUUID().toString(), params)
                return Result.Success(newPlayerItem)
            }
        } catch (ex: Exception) {
            Result.Failure(ex)
        }
    }
}
