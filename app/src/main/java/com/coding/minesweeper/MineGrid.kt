package com.coding.minesweeper

import java.util.*


class MineGrid(private val size: Int) {
    private val cells: MutableList<Cell>

    fun generateGrid(totalBombs: Int) {
        var bombsPlaced = 0
        while (bombsPlaced < totalBombs) {
            val x = Random().nextInt(size)
            val y = Random().nextInt(size)
            if (cellAt(x, y)!!.value == Constants.BLANK) {
                cells[x + y * size] = Cell(Constants.BOMB)
                bombsPlaced++
            }
        }
        for (x in 0 until size) {
            for (y in 0 until size) {
                if (cellAt(x, y)!!.value != Constants.BOMB) {
                    val adjacentCells = adjacentCells(x, y)
                    var countBombs = 0
                    for (cell in adjacentCells) {
                        if (cell.value == Constants.BOMB) {
                            countBombs++
                        }
                    }
                    if (countBombs > 0) {
                        cells[x + y * size] = Cell(countBombs)
                    }
                }
            }
        }
    }

    private fun cellAt(x: Int, y: Int): Cell? {
        return if (x < 0 || x >= size || y < 0 || y >= size) {
            null
        } else cells[x + y * size]
    }

    fun adjacentCells(x: Int, y: Int): List<Cell> {
        val adjacentCells: MutableList<Cell> = ArrayList()
        val cellsList: MutableList<Cell?> = ArrayList()
        cellsList.add(cellAt(x - 1, y))
        cellsList.add(cellAt(x + 1, y))
        cellsList.add(cellAt(x - 1, y - 1))
        cellsList.add(cellAt(x, y - 1))
        cellsList.add(cellAt(x + 1, y - 1))
        cellsList.add(cellAt(x - 1, y + 1))
        cellsList.add(cellAt(x, y + 1))
        cellsList.add(cellAt(x + 1, y + 1))
        for (cell in cellsList) {
            if (cell != null) {
                adjacentCells.add(cell)
            }
        }
        return adjacentCells
    }



    fun toXY(index: Int): IntArray {
        val y = index / size
        val x = index - y * size
        return intArrayOf(x, y)
    }

    fun revealAllBombs() {
        for (c in cells) {
            if (c.value == Constants.BOMB) {
                c.isRevealed=true
            }
        }
    }

    fun getCells(): List<Cell> {
        return cells
    }

    init {
        cells = ArrayList()
        for (i in 0 until size * size) {
            cells.add(Cell(Constants.BLANK))
        }
    }
}
