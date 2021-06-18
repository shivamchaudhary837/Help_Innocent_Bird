package com.example.helpinnocentbird

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var animation: Animation
    private lateinit var mediaPlayer: MediaPlayer
    private var status=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      animation=AnimationUtils.loadAnimation(this,R.anim.anim)
        bird.animation=animation
        enemy1.animation=animation
        enemy2.animation=animation
        enemy3.animation=animation
        coin.animation=animation


    }

    override fun onResume() {
        super.onResume()
        mediaPlayer= MediaPlayer.create(this,R.raw.help_bird_app_src_main_res_raw_audio_for_game)
        mediaPlayer.start()

        volume.setOnClickListener {
            if(!status){
                mediaPlayer.setVolume(0F,0F)
                 volume.setImageResource(R.drawable.volume_off)
                status=true
            }else{
                mediaPlayer.setVolume(1F,1F)
                volume.setImageResource(R.drawable.volume_up)
                status=false
            }
        }

        button_start.setOnClickListener {
            mediaPlayer.reset()
            volume.setImageResource(R.drawable.volume_up)

            startActivity(Intent(this,GameActivity::class.java))
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
    }
}