package com.example.helpinnocentbird

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    private var score=0
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

      score=intent.getIntExtra("score",0)
        tv_my_score.text="Your Score: $score"

        sharedPreferences=this.getSharedPreferences("Score", Context.MODE_PRIVATE)
        val highestScore=sharedPreferences.getInt("highestScore",0)

        if(score>=200){
            //when we reach 200 points
            tv_result_info.setText("You won the game")
            tv_highest_score.setText("Highest Score: $score")

            sharedPreferences.edit().putInt("highestScore",score).apply()
        }else if(score>=highestScore){
            //when current score is greater than previous high score
            tv_result_info.setText("Sorry,you lost the game")
            tv_highest_score.setText("Highest Score: $score")

            sharedPreferences.edit().putInt("highestScore",score).apply()
        }else{
            //when current score is less than previous highest score
            tv_result_info.setText("Sorry,you lost the game")
            tv_highest_score.setText("Highest Score: $highestScore")
        }
        button_again.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {

        val builder=AlertDialog.Builder(this)
        builder.setTitle("Help The Innocent Bird")
        builder.setMessage("Are you sure want to quit game?")
        builder.setCancelable(false)
        builder.setNegativeButton("Quit Game",DialogInterface.OnClickListener { dialog, which ->
            moveTaskToBack(true)
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        })
        builder.setPositiveButton("Cancel", { dialog, which ->
            dialog.cancel()
        })
        builder.create().show()
    }
}