package com.example.janken

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val comHand = (Math.random() * 3).toInt()
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
    }
}