package com.squadsandshots_android.presentation.homePage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squadsandshots_android.firebaseModels.ListenerRequest
import com.squadsandshots_android.firebaseModels.RoomModel
import com.squadsandshots_android.presentation.login.LoginViewModel
import com.squadsandshots_android.presentation.models.CreateGameStart
import com.squadsandshots_android.presentation.models.GameType
import com.squadsandshots_android.presentation.models.JoinGameStart
import com.squadsandshots_android.presentation.models.StartGameDetails
import com.squadsandshots_android.useCases.CreateRoomListenerUseCase
import com.squadsandshots_android.useCases.CreateRoomUseCase
import com.squadsandshots_android.useCases.GetOrCreateUniqueUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor() : ViewModel() {

    var playerName: String = ""

    fun createStartGameDetails(gameType: GameType): StartGameDetails {
        return StartGameDetails(CreateGameStart(playerName, gameType), null)
    }

    fun createJoinGameDetails(gameCode: String): StartGameDetails {
        return StartGameDetails(null, JoinGameStart(playerName, gameCode))
    }
}