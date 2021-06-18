package com.example.helpinnocentbird

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_game.*



class GameActivity : AppCompatActivity() {
    private var touchControl = false
    private var beginControl = false

    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    private lateinit var runnable2: Runnable
    private lateinit var handler2: Handler

    //sound
    private lateinit var mediaPlayer: MediaPlayer
    private var status=false

    //birdx and birdy are position
    private var birdX: Int = 0
    private var birdY: Int = 0
    private var enemy1x: Int = 0
    private var enemy2x: Int = 0
    private var enemy3x: Int = 0
    private var coin1x: Int = 0
    private var coin2x: Int = 0

    private var enemy1y: Int = 0
    private var enemy2y: Int = 0
    private var enemy3y: Int = 0
    private var coin1y: Int = 0
    private var coin2y: Int = 0

    //these are dimensions
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

//total chances
    var right=3
     var score=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        constraint_layout.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent): Boolean {


                tv_start_info.visibility = View.INVISIBLE
                if (!beginControl) {
                    beginControl = true

                    screenWidth = constraint_layout.width
                    screenHeight = constraint_layout.height

                    birdX = img_bird.x.toInt()
                    birdY = img_bird.y.toInt()

                    handler = Handler()
                    runnable = Runnable {
                        run {
                            moveToBird()
                            enemyControl()
                            collisionControl()

                        }
                    }
                    handler.post(runnable)
                } else {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        touchControl = true
                    }
                    if (event.action == MotionEvent.ACTION_UP) {
                        touchControl = false
                    }
                }


                return true
            }
        })


    }

    override fun onStop() {
        super.onStop()
        mediaPlayer.stop()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer= MediaPlayer.create(this,R.raw.help_bird_app_src_main_res_raw_audio_for_game)
        mediaPlayer.start()

        game_volume.setOnClickListener {
            if(!status){
                mediaPlayer.setVolume(0F,0F)
                game_volume.setImageResource(R.drawable.volume_off)
                status=true
            }else{
                mediaPlayer.setVolume(1F,1F)
                game_volume.setImageResource(R.drawable.volume_up)
                status=false
            }
        }

    }
    override fun onBackPressed() {
       // this.onDestroy()
        android.os.Process.killProcess(android.os.Process.myPid())

        super.onBackPressed()
    }

    private fun collisionControl() {
        val centreEnemy1x=enemy1x+img_enemy1.width/2
        val centreEnemy1y=enemy1y+img_enemy1.height/2

        if(centreEnemy1x>=birdX &&
            centreEnemy1x<=(birdX+img_bird.width)&&
            centreEnemy1y>=birdY  &&
            centreEnemy1y<=(birdY+img_bird.height)
        ){
            enemy1x=screenWidth+200
            right--
        }
        //enemy2
        val centreEnemy2x=enemy2x+img_enemy2.width/2
        val centreEnemy2y=enemy2y+img_enemy2.height/2

        if(centreEnemy2x>=birdX &&
            centreEnemy2x<=(birdX+img_bird.width)&&
            centreEnemy2y>=birdY  &&
            centreEnemy2y<=(birdY+img_bird.height)
        ){
            enemy2x=screenWidth+200
            right--
        }
        //enemy3
        val centreEnemy3x=enemy3x+img_enemy3.width/2
        val centreEnemy3y=enemy3y+img_enemy3.height/2

        if(centreEnemy3x>=birdX &&
            centreEnemy3x<=(birdX+img_bird.width)&&
            centreEnemy3y>=birdY  &&
            centreEnemy3y<=(birdY+img_bird.height)
        ){
            enemy3x=screenWidth+200
            right--
        }
        //for coin1
        val centreCoin1x=coin1x+img_coin.width/2
        val centreCoin1y=coin1y+img_coin.height/2

        if(centreCoin1x>=birdX &&
            centreCoin1x<=(birdX+img_bird.width)&&
            centreCoin1y>=birdY  &&
            centreCoin1y<=(birdY+img_bird.height)
        ){
            coin1x=screenWidth+200
            score=score+10
            tv_score.text=score.toString()
        }
        //for coin2
        val centreCoin2x=coin2x+img_coin2.width/2
        val centreCoin2y=coin2y+img_coin2.height/2

        if(centreCoin2x>=birdX &&
            centreCoin2x<=(birdX+img_bird.width)&&
            centreCoin2y>=birdY  &&
            centreCoin2y<=(birdY+img_bird.height)
        ){
            coin2x=screenWidth+200
            score=score+10
            tv_score.text=score.toString()
        }

          if(right>0 && score<200){
              if(right==2){
                  right1.setImageResource(R.drawable.favorite_grey)
              }

              if(right==1){
                  right2.setImageResource(R.drawable.favorite_grey)
              }
              handler.postDelayed(runnable, 20)
          }else if(score>=200){
              handler.removeCallbacks(runnable)
              constraint_layout.isEnabled=false
              tv_start_info.isVisible=true
              tv_start_info.text=getString(R.string.congrats)
              img_enemy1.isVisible=false
              img_enemy2.isVisible=false
              img_enemy3.isVisible=false
              img_coin.isVisible=false
              img_coin2.isVisible=false

              handler2=Handler()
              runnable2= Runnable {
                  run {
                  birdX=birdX+(screenWidth/300)
                      img_bird.x=birdX.toFloat()
                      img_bird.y=screenHeight/2f

                      if(birdX<=screenWidth){
                          handler2.postDelayed(runnable2,20)
                      }else{
                          handler2.removeCallbacks(runnable2)
                          mediaPlayer.reset()
                          startActivity(Intent(this,ResultActivity::class.java).apply {
                              putExtra("score",score)
                          })
                          finish()
                      }

                  }
              }
              handler2.post(runnable2)
          }else if(right==0){
              handler.removeCallbacks(runnable)
              right3.setImageResource(R.drawable.favorite_grey)
              startActivity(Intent(this,ResultActivity::class.java).apply {
                  putExtra("score",score)
              })
              finish()
          }
    }

    private fun enemyControl() {
        img_enemy1.visibility = View.VISIBLE
        img_enemy2.visibility = View.VISIBLE
        img_enemy3.visibility = View.VISIBLE
        img_coin.visibility = View.VISIBLE
        img_coin2.visibility = View.VISIBLE

        enemy1x = enemy1x - (screenWidth / 150)
//for speed up
        if(score>50 && score<=100){
            enemy1x = enemy1x - (screenWidth / 140)
        }
        if(score>100 && score<=150){
            enemy1x = enemy1x - (screenWidth / 130)
        }
        if(score>150){
            enemy1x = enemy1x - (screenWidth / 100)
        }
        /////
        if (enemy1x < 0) {
            enemy1x = screenWidth + 200
            enemy1y = Math.floor(Math.random() * screenHeight).toInt()

            if (enemy1y <= 0) {
                //for vertical outward
                enemy1y = 0
            }

            if (enemy1y >= (screenHeight - img_enemy1.height)) {
                //for down outward
                enemy1y = screenHeight - img_enemy1.height
            }
        }
        img_enemy1.x=enemy1x.toFloat()
        img_enemy1.y=enemy1y.toFloat()
///for enemy2
        enemy2x = enemy2x - (screenWidth / 140)

        if(score>50 && score<=100){
            enemy2x = enemy2x - (screenWidth / 130)
        }
        if(score>100 && score<=150){
            enemy2x = enemy2x - (screenWidth / 110)
        }
        if(score>150){
            enemy2x = enemy2x - (screenWidth / 90)
        }


        if (enemy2x < 0) {
            enemy2x = screenWidth + 200
            enemy2y = Math.floor(Math.random() * screenHeight).toInt()

            if (enemy2y <= 0) {
                enemy2y = 0
            }

            if (enemy2y >= (screenHeight - img_enemy2.height)) {
                enemy2y = screenHeight - img_enemy2.height
            }
        }
        img_enemy2.x=enemy2x.toFloat()
        img_enemy2.y=enemy2y.toFloat()
//for enemy3
        enemy3x = enemy3x - (screenWidth / 130)
        if(score>50 && score<=100){
            enemy3x = enemy3x - (screenWidth / 120)
        }
        if(score>100 && score<=150){
            enemy3x = enemy3x - (screenWidth / 100)
        }
        if(score>150){
            enemy3x = enemy3x - (screenWidth / 90)
        }

        if (enemy3x < 0) {
            enemy3x = screenWidth + 200
            enemy3y = Math.floor(Math.random() * screenHeight).toInt()

            if (enemy3y <= 0) {
                enemy3y = 0
            }

            if (enemy3y >= (screenHeight - img_enemy3.height)) {
                enemy3y = screenHeight - img_enemy3.height
            }
        }
        img_enemy3.x=enemy3x.toFloat()
        img_enemy3.y=enemy3y.toFloat()

        //for coin1
        coin1x = coin1x - (screenWidth / 120)
        if (coin1x < 0) {
            coin1x = screenWidth + 200
            coin1y = Math.floor(Math.random() * screenHeight).toInt()

            if (coin1y <= 0) {
                coin1y = 0
            }

            if (coin1y >= (screenHeight - img_coin.height)) {
                coin1y = screenHeight - img_coin.height
            }
        }
        img_coin.x=coin1x.toFloat()
        img_coin.y=coin1y.toFloat()

        //for coin2
        coin2x = coin2x - (screenWidth / 110)
        if (coin2x < 0) {
            coin2x = screenWidth + 200
            coin2y = Math.floor(Math.random() * screenHeight).toInt()

            if (coin2y <= 0) {
                coin2y = 0
            }

            if (coin2y >= (screenHeight - img_coin2.height)) {
                coin2y = screenHeight - img_coin2.height
            }
        }
        img_coin2.x=coin2x.toFloat()
        img_coin2.y=coin2y.toFloat()
    }

    private fun moveToBird() {

        if (touchControl) {
            //for action up
            birdY = birdY - (screenHeight / 50)
        } else {
            //for action down
            birdY = birdY + (screenHeight / 50)
        }
        if (birdY <= 0) {
            birdY = 0
        }
        //img_bird.height means height of bird like 20
        if (birdY >= (screenHeight - img_bird.height)) {
            birdY = screenHeight - img_bird.height
        }

        img_bird.y = birdY.toFloat()
    }
}