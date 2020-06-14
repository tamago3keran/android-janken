package com.example.janken

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    val rock = 0
    val scissors = 1
    val paper = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val id = intent.getIntExtra("MY_HAND", 0)

        val myHand: Int
        myHand = when(id) {
            R.id.rock -> {
                myHandImage.setImageResource(R.drawable.human_rock)
                rock
            }
            R.id.scissors -> {
                myHandImage.setImageResource(R.drawable.human_scissors)
                scissors
            }
            R.id.paper -> {
                myHandImage.setImageResource(R.drawable.human_paper)
                paper
            }
            else -> rock
        }

        val comHand = getHand()
        when(comHand) {
            rock -> comHandImage.setImageResource(R.drawable.machine_rock)
            scissors -> comHandImage.setImageResource(R.drawable.machine_scissors)
            paper -> comHandImage.setImageResource(R.drawable.machine_paper)
        }

        val gameResult = (comHand - myHand + 3) % 3
        when(gameResult) {
            0 -> resultLabel.setText(R.string.result_draw)
            1 -> resultLabel.setText(R.string.result_win)
            2 -> resultLabel.setText(R.string.result_lose)
        }
        backButton.setOnClickListener { finish() }
        saveData(myHand, comHand, gameResult)
    }

    private fun saveData(myHand: Int, comHand: Int, gameResult: Int) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val lastGameResult = pref.getInt("GAME_RESULT", -1)

        val edtWinningStreakCount: Int =
            when {
                lastGameResult == 2 && gameResult == 2 ->
                    winningStreakCount + 1
                else ->
                    0
            }

        pref.edit {
            putInt("GAME_COUNT", gameCount + 1)
            putInt("WINNING_STREAK_COUNT", edtWinningStreakCount)
            putInt("LAST_MY_HAND", myHand)
            putInt("LAST_COM_HAND", comHand)
            putInt("BEFORE_LAST_COM_HAND", lastComHand)
            putInt("GAME_RESULT", gameResult)
        }
    }

    private fun getHand(): Int {
        var hand = (Math.random() * 3).toInt()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val gameCount = pref.getInt("GAME_COUNT", 0)
        val winningStreakCount = pref.getInt("WINNING_STREAK_COUNT", 0)
        val lastMyHand = pref.getInt("LAST_MY_HAND", 0)
        val lastComHand = pref.getInt("LAST_COM_HAND", 0)
        val beforeLastComHand = pref.getInt("BEFORE_LAST_COM_HAND", 0)
        val gameResult = pref.getInt("GAME_RESULT", -1)

        if (gameCount == 1) {
            if (gameResult == 2) {
                while (lastComHand == hand) {
                    hand = (Math.random() * 3).toInt()
                }
            } else if (gameResult == 1) {
                hand = (lastMyHand - 1 + 3) % 3
            } else if (winningStreakCount > 0) {
                if (beforeLastComHand == lastComHand) {
                    while (lastComHand == hand) {
                        hand = (Math.random() * 3).toInt()
                    }
                }
            }
        }
        return hand
    }
}