package com.rohan.tictactoerxjava.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProviders
import com.rohan.tictactoerxjava.R
import com.rohan.tictactoerxjava.model.GameData
import com.rohan.tictactoerxjava.model.GameUIData
import com.rohan.tictactoerxjava.viewmodel.GameDataViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), GameDataViewModel.IGameDataUIListener {

    private lateinit var mCommunicatorObservable: Observable<GameData>
    private var mEmitter: ObservableEmitter<GameData>? = null
    private lateinit var mViewModel: GameDataViewModel
    private val cellData: ArrayList<ArrayList<Button>> = ArrayList()
    private val capacity: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        mViewModel = ViewModelProviders.of(this).get(GameDataViewModel::class.java).apply {
            mListener = this@GameActivity
            this.initList(capacity)
        }
        initList(capacity)
    }
    private fun initList(gridSize: Int) {
        mEmitter = null
        mCommunicatorObservable = Observable.create { emitter -> mEmitter = emitter }
        mCommunicatorObservable.subscribe(mViewModel::processButtonClick)
        gridInflater(gridSize)
        mViewModel.getUserData()

    }

    override fun onDataUpdate(gameUIData: GameUIData) {
        cellData[gameUIData.x][gameUIData.y].text = gameUIData.character.toString()
        cellData[gameUIData.x][gameUIData.y].isClickable = false
    }

    override fun onGameComplete(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        resetInGameUI()
    }

    private fun resetInGameUI() {
        repeat(cellData.size) { i ->
            repeat(cellData.size) { j ->
                cellData[i][j].text = "-"
                cellData[i][j].isClickable = true
            }
        }
    }
    private fun gridInflater(gridSize: Int) {
        inflateButtons(gridSize)
        setConstraints(gridSize)
    }
    private fun inflateButtons(gridSize: Int) {
        var counter = 1000
        repeat(gridSize) { i ->
            val inflatedRow = ArrayList<Button>(gridSize)
            repeat(gridSize) { j ->
                val button = Button(this)
                button.id = counter
                button.layoutParams = ConstraintLayout.LayoutParams(0,0)
                button.text = "-"
                button.setOnClickListener { mEmitter?.onNext(GameData(i, j)) }
                counter++
                inflatedRow.add(button)
                game_view_root.addView(button)
            }
            cellData.add(inflatedRow)
        }
    }

    private fun setConstraints(gridSize: Int) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(game_view_root)
        repeat(gridSize) { i ->
            repeat(gridSize) { j ->
                if(j==0)
                    constraintSet.connect(cellData[i][j].id, ConstraintSet.START, game_view_root.id, ConstraintSet.START)
                if(j==gridSize-1)
                    constraintSet.connect(cellData[i][j].id, ConstraintSet.END, game_view_root.id, ConstraintSet.END)
                if(j>0)
                    constraintSet.connect(cellData[i][j].id, ConstraintSet.START, cellData[i][j-1].id, ConstraintSet.END)
                if(j<gridSize-1)
                    constraintSet.connect(cellData[i][j].id, ConstraintSet.END, cellData[i][j+1].id, ConstraintSet.START)
                if(i==0)
                    constraintSet.connect(cellData[i][j].id, ConstraintSet.TOP, game_view_root.id, ConstraintSet.TOP)
                if(i==gridSize-1)
                    constraintSet.connect(cellData[i][j].id, ConstraintSet.BOTTOM, game_view_root.id, ConstraintSet.BOTTOM)
                if(i>0)
                    constraintSet.connect(cellData[i][j].id, ConstraintSet.TOP, cellData[i-1][j].id, ConstraintSet.BOTTOM)
                if(i<gridSize-1)
                    constraintSet.connect(cellData[i][j].id, ConstraintSet.BOTTOM, cellData[i+1][j].id, ConstraintSet.TOP)

            }
        }
        game_view_root.setConstraintSet(constraintSet)
    }
}
