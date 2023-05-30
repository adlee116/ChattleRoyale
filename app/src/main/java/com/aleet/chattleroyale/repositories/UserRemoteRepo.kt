package com.aleet.chattleroyale.repositories

import com.aleet.chattleroyale.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import javax.inject.Inject

class UserRemoteRepo @Inject constructor(
    private val database: DatabaseRepositoryInterface,
    private val databaseAuthRepoInterface: DatabaseAuthRepoInterface
    ) {

    fun saveUser(user: User) {
        database.getReference(DatabaseRepositoryInterface.USERS).child(user.uid).setValue(user)
    }

    fun getUser(userId: String): Task<DocumentSnapshot> {
        return databaseAuthRepoInterface.getCrUser(userId)
//        return database.getReference(DatabaseRepositoryInterface.USERS).child(userId).get()
    }

//    fun joinRoom(roomCode: String, player: Player) {
//        database.getReference(DatabaseRepositoryInterface.ROOM).child(roomCode).child(
//            DatabaseRepositoryInterface.PLAYERS).child(player.id).setValue(player)
//    }

//    fun createRoomListener(request: ListenerRequest): ValueEventListener {
//        return database.getReference(DatabaseRepositoryInterface.ROOM).child(request.roomCode).addValueEventListener(request.listener)
//    }
//
//    fun getRoom(roomCode: String): Task<DataSnapshot> {
//        return database.getReference(DatabaseRepositoryInterface.ROOM).child(roomCode).get()
//    }
//
//    fun startGame(room: RoomModel) {
//        createRoom(room)
//    }
//
//    fun checkRoomExists() {
//
//    }
}