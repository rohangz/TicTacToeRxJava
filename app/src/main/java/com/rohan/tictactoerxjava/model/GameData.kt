package com.rohan.tictactoerxjava.model

data class GameData(val x: Int, val y: Int)

data class GameUIData(val x: Int, val y: Int, val character: Char)

data class GameCompletionData(val message: String)
