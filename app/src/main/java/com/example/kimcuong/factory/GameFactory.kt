package com.example.kimcuong.factory

import com.example.kimcuong.R.drawable
import kotlin.random.Random

class GameFactory {
    var board: Array<Array<Int>>
    get() = field
    var allChess: IntArray

    init {
        board = Array(7) { Array(7) { 0 } }
        allChess = intArrayOf(
            drawable.gold,
            drawable.giap,
            drawable.mana,
            drawable.hp,
            drawable.attack
        )
        loadToMatrix()
    }


    fun loadToMatrix() {
        for (i in 0..board.size-1) {
            for (j in 0..board.get(0).size-1) {
                board.get(i)[j] = genarateValueMatrix(i, j)
            }
        }
    }

    fun getRandomNumber(): Int {
        return allChess[Random.nextInt(0, 5)]
    }

    fun getRandomExceptInput(v1: Int, v2: Int): Int {
        while (true) {
            val value: Int = getRandomNumber()
            if (value != v1 && value != v2) {
                return value
            }
        }
    }

    fun genarateValueMatrix(x: Int, y: Int): Int {
        if (x < 2 && y < 2) {
            return getRandomNumber()
        } else if (x > 1 && y < 2) {
            val v1: Int = board.get(x - 1).get(y)
            val v2: Int = board.get(x - 2).get(y)
            if (v1 != v2) {
                return getRandomNumber()
            } else {
                return getRandomExceptInput(v1, v2)
            }
        } else if (y > 1 && x < 2) {
            val v1: Int = board.get(x).get(y - 1)
            val v2: Int = board.get(x).get(y - 2)
            if (v1 != v2) {
                return getRandomNumber()
            } else {
                return getRandomExceptInput(v1, v2)
            }
        } else {
            val v1: Int = board.get(x).get(y - 1)
            val v2: Int = board.get(x - 1).get(y)
            return getRandomExceptInput(v1, v2)
        }
    }
}


