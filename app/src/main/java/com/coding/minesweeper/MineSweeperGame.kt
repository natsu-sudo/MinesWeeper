package com.coding.minesweeper

import android.util.Log
import java.util.*


//this Class initialize the Setup which is required to Play the Game
class MineSweeperGame(private val size: Int, val numberBombs: Int) {
    private  var mineGrid: MineGrid = MineGrid(size)
    var isGameOver = false
    var isFlagMode = false
    private var isClearMode = true
    var flagCount = 0
        private set
    private var timeExpired = false


    //what do to when and cell was clicked
    fun handleCellClick(cell: Cell) {
        if (!isGameOver && !isGameWon && !timeExpired && !cell.isRevealed) {
            if (isClearMode) {
                clear(cell)
            } else if (isFlagMode) {
                flag(cell)
            }
        }
    }

    //this function clear near by grid if game is Still On
    private fun clear(cell: Cell) {
        val index: Int = getMineGrid().getCells().indexOf(cell)
        getMineGrid().getCells()[index].isRevealed=true
        if (cell.value == Constants.BOMB) {
            isGameOver = true
        } else if (cell.value == Constants.BLANK) {
            val toClear: MutableList<Cell> = ArrayList()
            val toCheckAdjacent: MutableList<Cell> = ArrayList()
            toCheckAdjacent.add(cell)
            while (toCheckAdjacent.size > 0) {
                val c = toCheckAdjacent[0]
                val cellIndex: Int = getMineGrid().getCells().indexOf(c)
                Log.d("TAG", "clear: $cellIndex")
                val cellPos: IntArray = getMineGrid().toXY(cellIndex)
                for (adjacent in getMineGrid().adjacentCells(cellPos[0], cellPos[1])) {
                    if (adjacent.value == Constants.BLANK) {
                        if (!toClear.contains(adjacent)) {
                            if (!toCheckAdjacent.contains(adjacent)) {
                                toCheckAdjacent.add(adjacent)
                            }
                        }
                    } else {
                        if (!toClear.contains(adjacent)) {
                            toClear.add(adjacent)
                        }
                    }
                }
                toCheckAdjacent.remove(c)
                toClear.add(c)
            }
            for (c in toClear) {
                c.isRevealed=true
            }
        }
    }

    //this funtion flag the grid if the flag toggle is on
    private fun flag(cell: Cell) {
        cell.isFlagged=!cell.isFlagged
        var count = 0
        for (c in getMineGrid().getCells()) {
            if (c.isFlagged) {
                count++
            }
        }
        flagCount = count
    }

    val isGameWon: Boolean
        get() {
            var numbersUnrevealed = 0
            for (c in getMineGrid().getCells()) {
                if (c.value != Constants.BOMB && c.value != Constants.BLANK && !c.isRevealed) {
                    numbersUnrevealed++
                }
            }
            return numbersUnrevealed == 0
        }

    //turn on the Flag toggle
    fun toggleMode() {
        isClearMode = !isClearMode
        isFlagMode = !isFlagMode
    }



    fun outOfTime() {
        timeExpired = true
    }


    fun getMineGrid(): MineGrid {
        return mineGrid
    }

    init {
        mineGrid= MineGrid(size)
        mineGrid.generateGrid(numberBombs)
    }
}
