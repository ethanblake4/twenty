package main

import java.util.*

object Main {

    private lateinit var board: Array<IntArray>
    private var noOut = false
    private val rnd = Random()
    private var done = false

    @JvmStatic fun main(args: Array<String>) {

        val random = { Pair(rnd.nextInt(board.size), rnd.nextInt(board.size)) }

        val marray = { intArrayOf(0, 0, 0, 0, 0) }
        board = arrayOf(marray(), marray(), marray(), marray(), marray())

        repeat(2) { random().let { (x, y) -> board[x][y] = 2 } }

        while (!done) {

            if(!noOut) (board.map { it.map { it.toString() }}).forEach {
                System.out.println((0..15).map{"-"}.reduce {a,c -> "$a$c"})
                it.forEach { i -> System.out.print(i + " | ") }
                System.out.println()
            } else noOut = false

            System.`in`.let { when(String(byteArrayOf(it.read().toByte()))) {
                    "w" -> move(1) "a" -> move(0)
                    "d" -> move(2) "s" -> move(3)
                    else -> noOut = true
            } }

            random().let { (x, y) -> if(board[x][y] == 0) board[x][y] = 2 }

            board.flatMap { it.toList() }.let {
                if (it.all { it != 0 }) { System.out.print("you lost!\n"); done = true }
                else if (it.any { it == 2048 }) { System.out.print("you won!\n"); done = true }
            }
        }
    }

    private fun move(direction: Int) = when (direction) {
        0 -> { // LEFT
            repeat(board.size) { runv(0 until board.size, 0 until board.size, board.size-1, 1) }
            runv2(0 until board.size, 0 until board.size, board.size-1, 1)
            runv(0 until board.size, 0 until board.size, board.size-1, 1)
        }
        1 -> { // UP
            repeat(board.size) { runh(0 until board.size, 0 until board.size, board.size-1, 1) }
            runh2(0 until board.size, 0 until board.size, board.size-1, 1)
            runh(0 until board.size, 0 until board.size, board.size-1, 1)
        }
        2 -> { // RIGHT
            repeat(board.size) { runv(0 until board.size, board.size - 1 downTo 0, 0, -1) }
            runv2(0 until board.size, board.size - 1 downTo 0, 0, -1)
            runv(0 until board.size, board.size - 1 downTo 0, 0, -1)
        }
        3 -> { // DOWN
            repeat(board.size) { runh(0 until board.size, board.size - 1 downTo 0, 0, -1) }
            runh2(0 until board.size, board.size - 1 downTo 0, 0, -1)
            runh(0 until board.size, board.size - 1 downTo 0, 0, -1)
        }
        else -> Unit
    }

    private fun runv(up1: IntProgression, up2: IntProgression, xind: Int, pos: Int) {
        up1.map { up2.map { ind -> if (board[it][ind] == 0) {
                board[it][ind] = if (ind == xind) 0 else board[it][ind + pos]
                if (ind != xind) board[it][ind + pos] = 0
        } } }
    }

    private fun runh(up1: IntProgression, up2: IntProgression, xind: Int, pos: Int) {
        up1.map { up2.map { ind -> if (board[ind][it] == 0) {
                board[ind][it] = if (ind == xind) 0 else board[ind + pos][it]
                if (ind != xind) board[ind + pos][it] = 0
        } } }
    }

    private fun runv2(up1: IntProgression, up2: IntProgression, xind: Int, pos: Int) {
        up1.map { up2.map { ind -> if (ind != xind && board[it][ind + pos] == board[it][ind]) {
                board[it][ind] *= 2; board[it][ind + pos] = 0
        } } }
    }

    private fun runh2(up1: IntProgression, up2: IntProgression, xind: Int, pos: Int) {
        up1.map { up2.map { ind -> if (ind != xind && board[ind][it] == board[ind + pos][it]) {
                board[ind][it] *= 2; board[ind + pos][it] = 0
        } } }
    }
}