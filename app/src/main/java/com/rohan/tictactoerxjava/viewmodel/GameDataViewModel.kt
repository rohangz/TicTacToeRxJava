package com.rohan.tictactoerxjava.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.rohan.tictactoerxjava.GameApplication
import com.rohan.tictactoerxjava.model.GameData
import com.rohan.tictactoerxjava.model.GameUIData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameDataViewModel(application: Application) : AndroidViewModel(application) {


    val mCellData = ArrayList<ArrayList<Char>>(ArrayList())
    var mListener: IGameDataUIListener? = null
    val GAME_DRAW = -1
    val GAME_IN_PROGRESS = 0
    val PLAYER_ONE = 1
    val PLAYER_TWO = 2
    private var cellsFilled = 0
    private var currentPlayer = PLAYER_ONE
    private var capacity: Int = 3

    fun initList(capacity: Int) {
        this.capacity = capacity
        mCellData.clear()
        repeat(capacity) { i ->
            val arrayList = ArrayList<Char>(capacity)
            repeat(capacity) { j ->
                arrayList.add('-')
            }
            mCellData.add(arrayList)
        }
        currentPlayer = PLAYER_ONE
        cellsFilled = 0
    }

    fun processButtonClick(gameData: GameData) {
        if (currentPlayer == 1) {
            mCellData[gameData.x][gameData.y] = 'X'
            mListener?.onDataUpdate(GameUIData(gameData.x, gameData.y, 'X'))
        } else {
            mCellData[gameData.x][gameData.y] = 'O'
            mListener?.onDataUpdate(GameUIData(gameData.x, gameData.y, 'O'))
        }
        cellsFilled++
        val winner = getWinner()
        if (winner != GAME_IN_PROGRESS) {
            if (winner != GAME_DRAW)
                mListener?.onGameComplete("Player $currentPlayer Won the Game")
            else
                mListener?.onGameComplete("It was a Draw")
            resetGame()
        } else {
            if (currentPlayer == PLAYER_ONE)
                currentPlayer = PLAYER_TWO
            else
                currentPlayer = PLAYER_ONE
        }
    }

    private fun didRowScore(): Boolean {
        for (i in 0 until mCellData.size) {
            val initialElement = mCellData[i][0]
            for (j in 1 until mCellData.size) {
                if (mCellData[i][j] != initialElement || mCellData[i][j] == '-')
                    break
                else if (j == mCellData.size - 1)
                    return true
            }
        }
        return false
    }

    private fun didColumnScore(): Boolean {
        for (i in 0 until mCellData.size) {
            val initialELement = mCellData[0][i]
            for (j in 1 until mCellData.size) {
                if (mCellData[j][i] != initialELement || mCellData[j][i] == '-')
                    break
                else if (j == mCellData.size - 1)
                    return true
            }
        }
        return false

    }

    private fun didDiagonalScore(): Boolean {
        var i = 0
        var j = 0
        val inititalElement = mCellData[i][j]
        i++
        j++
        while (i < mCellData.size && j < mCellData.size) {
            if (mCellData[i][j] != inititalElement || mCellData[i][j] == '-')
                break
            else if (i == mCellData.size - 1)
                return true
            i++
            j++
        }
        i = 0
        j = mCellData.size - 1
        val secondInitialElement = mCellData[i][j]
        i++
        j--
        while (i < mCellData.size) {
            if (mCellData[i][j] != secondInitialElement || mCellData[i][j] == '-')
                break
            else if (i == mCellData.size - 1)
                return true
            i++
            j--
        }

        return false

    }

    private fun getWinner(): Int {
        if (didRowScore() || didColumnScore() || didDiagonalScore())
            return currentPlayer
        if (cellsFilled == mCellData.size * mCellData.size)
            return GAME_DRAW
        return GAME_IN_PROGRESS
    }

    private fun resetGame() {
        cellsFilled = 0
        repeat(mCellData.size) { i ->
            repeat(mCellData.size) { j ->
                mCellData[i][j] = '-'
            }
        }
        currentPlayer = PLAYER_ONE
    }


    fun getUserData() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = GameApplication.getInstance().networkService.getUserData("http://www.mocky.io/v2/5e9c90d730000062000a7eed")
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    Toast.makeText(
                        GameApplication.getInstance(),
                        "Login successful for ${response.body()!!.userName}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        GameApplication.getInstance(),
                        "Oops Something Went Wrong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    interface IGameDataUIListener {
        fun onDataUpdate(gameUIData: GameUIData)
        fun onGameComplete(message: String)
    }

}