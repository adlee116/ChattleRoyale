package com.aleet.chattleroyale.repositories

import javax.inject.Inject

class RoomModelRepository @Inject constructor(
    private val database: DatabaseRepositoryInterface
    ) {

//    fun createRoom(room: RoomModel) {
//        database.getReference(DatabaseRepositoryInterface.ROOM).child(room.roomCode).setValue(room)
//    }
//
//    fun joinRoom(roomCode: String, player: Player) {
//        database.getReference(DatabaseRepositoryInterface.ROOM).child(roomCode).child(
//            DatabaseRepositoryInterface.PLAYERS).child(player.id).setValue(player)
//    }
//
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