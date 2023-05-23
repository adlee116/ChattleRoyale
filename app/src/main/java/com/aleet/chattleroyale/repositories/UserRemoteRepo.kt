package com.aleet.chattleroyale.repositories

import com.aleet.chattleroyale.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class RoomModelRepository @Inject constructor(
    private val database: DatabaseRepositoryInterface
    ) {

    fun createUser(room: User) {
        database.getReference(DatabaseRepositoryInterface.ROOM).child(room.roomCode).setValue(room)
    }

    fun joinRoom(roomCode: String, player: Player) {
        database.getReference(DatabaseRepositoryInterface.ROOM).child(roomCode).child(
            DatabaseRepositoryInterface.PLAYERS).child(player.id).setValue(player)
    }

    fun createRoomListener(request: ListenerRequest): ValueEventListener {
        return database.getReference(DatabaseRepositoryInterface.ROOM).child(request.roomCode).addValueEventListener(request.listener)
    }

    fun getRoom(roomCode: String): Task<DataSnapshot> {
        return database.getReference(DatabaseRepositoryInterface.ROOM).child(roomCode).get()
    }

    fun startGame(room: RoomModel) {
        createRoom(room)
    }

    fun checkRoomExists() {

    }
}