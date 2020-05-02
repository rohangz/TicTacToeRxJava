package com.rohan.tictactoerxjava.model

data class GameUserModel(
    val userName: String = "",
    val userFirstName: String = "",
    val userLastName: String = "",
    val userHighScore: Long = 0,
    val userToken: String = ""
)