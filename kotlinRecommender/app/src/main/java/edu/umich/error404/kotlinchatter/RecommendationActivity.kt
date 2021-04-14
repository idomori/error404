package edu.umich.error404.kotlinchatter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import edu.umich.error404.kotlinchatter.databinding.ActivityRecommendationBinding
import kotlin.math.abs

class RecommendationActivity : AppCompatActivity() {
    private lateinit var recViewById: ActivityRecommendationBinding
    private var xdown: Float = 0f
    private var ydown: Float = 0f
    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    private var bpmVal = 0
    private var keysVal = 0
    private var danceabilityVal = 0
    private var valenceVal = 0
    private var energyVal = 1

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            recViewById.positionBar.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeTextView(currentPosition)
            recViewById.elapsedTimeTextView.text = elapsedTime

            var remainingTime = createTimeTextView(totalTime - currentPosition)
            recViewById.remainingTimeTextView.text = "-$remainingTime"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recViewById = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(recViewById.root)

        // Temp toast message to test whether the activity has been refreshed
        // Toast.makeText(applicationContext,"Recommendation Activity Starts",Toast.LENGTH_SHORT).show()

        var song = MainActivity.songList.peek()
        var preview_url = song.preview_url
        var image_url = song.image_url
        //use sample urls for now
//        var preview_url = "https://p.scdn.co/mp3-preview/641fd877ee0f42f3713d1649e20a9734cc64b8f9"
//        var image_url = "https://i.scdn.co/image/89b92c6b59131776c0cd8e5df46301ffcf36ed69"


        // Set MediaPlayer
        mp = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(preview_url)
            prepare() // might take long! (for buffering, etc)
            start()  // Assume we play the music by default
        }
        mp.isLooping = true //need to change later
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration

        // Set Album image
        Picasso.get().load(image_url).into(recViewById.albumPic)

        // Set Position Bar
        recViewById.positionBar.max = totalTime
        recViewById.positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        // Thread
        Thread(Runnable {
            while (mp != null) {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()

        initSettingBars()
    }

    fun initSettingBars() {
        // bpm Bar - actual range: [0,1100]
        recViewById.bpmBar.min = 0
        recViewById.bpmBar.max = 11
        recViewById.bpmBar.progress = bpmVal/100
        recViewById.bpmBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        var bpm = progress*100
                        recViewById.bpmValueTextView.text = bpm.toString()
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar) {
                }
                override fun onStopTrackingTouch(p0: SeekBar) {
                }
            }
        )
        // keys Bar - actual range: [0,11]
        recViewById.keysBar.min = 0
        recViewById.keysBar.max = 11
        recViewById.keysBar.progress = keysVal
        recViewById.keysBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        var keys = progress
                        recViewById.keysValueTextView.text = keys.toString()
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar) {
                }
                override fun onStopTrackingTouch(p0: SeekBar) {
                }
            }
        )
        // danceability Bar - actual range: [0,1]
        recViewById.danceabilityBar.min = 0
        recViewById.danceabilityBar.max = 10
        recViewById.danceabilityBar.progress = danceabilityVal*10
        recViewById.danceabilityBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        var danceability : Double = progress.toDouble()/10
                        recViewById.danceabilityValueTextView.text = danceability.toString()
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        // valenceValue Bar - actual range: [0,1]
        recViewById.valenceBar.min = 0
        recViewById.valenceBar.max = 10
        recViewById.valenceBar.progress = valenceVal*10
        recViewById.valenceBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        var valence : Double = progress.toDouble()/10
                        recViewById.valenceValueTextView.text = valence.toString()
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )


        // energy Bar - actual range: [0,10]
        recViewById.energyBar.min = 1
        recViewById.energyBar.max = 10
        recViewById.energyBar.progress = energyVal
        recViewById.energyBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        var energy = progress
                        recViewById.energyValueTextView.text = energy.toString()
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        super.dispatchTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                xdown = event.x
                ydown = event.y
            }
            MotionEvent.ACTION_UP -> {
                if ((xdown - event.x) > 100 && abs(event.y - ydown) < 100 && (recViewById.settingPanel.visibility == View.GONE)) {//swipe left: discard the song
                    // Change the app background color
                    recViewById.rootLayout.setBackgroundColor(Color.parseColor("#AFACAB"))

                    val handler: Handler = Handler()
                    val refresh = object : Runnable {
                        override fun run() {
                            mp.pause()
                            recViewById.rootLayout.setBackgroundColor(Color.WHITE)
                            // refresh current activity
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                    }
                    handler.postDelayed(refresh, 2000)

                    // Show a snack bar for undo option
                    val snack = Snackbar.make(
                        recViewById.rootLayout, // Parent view
                        "Song discarded! \nSearching the next song...", // Message to show
                        Snackbar.LENGTH_LONG //
                    ).setAction( // Set an action for snack bar
                        "Undo", // Action button text
                        { // UNDO Action button click listener
                            recViewById.rootLayout.setBackgroundColor(Color.WHITE)
                            handler.removeCallbacks(refresh);
                        }
                    ).setDuration(
                        2000
                    ).withColor(Color.parseColor("#F38D6D")
                    ).show() // Finally show the snack bar


                } else if ((xdown - event.x) < -100 && abs(event.y - ydown) < 100 && (recViewById.settingPanel.visibility == View.GONE)) {//swipe right: save the song
                    // Change the app background color
                    recViewById.rootLayout.setBackgroundColor(Color.parseColor("#F4F4B8"))

                    val handler: Handler = Handler()
                    val refresh = object : Runnable {
                        override fun run() {
                            mp.pause()
                            recViewById.rootLayout.setBackgroundColor(Color.WHITE)
                            // refresh current activity
                            val intent = intent
                            finish()
                            startActivity(intent)
                        }
                    }
                    handler.postDelayed(refresh, 2000)

                    // Show a snack bar for undo option
                    Snackbar.make(
                        recViewById.rootLayout, // Parent view
                        "Song Saved! \nSearching the next song...", // Message to show
                        Snackbar.LENGTH_LONG //
                    ).setAction( // Set an action for snack bar
                        "Undo", // Action button text
                        { // UNDO Action button click listener
                            recViewById.rootLayout.setBackgroundColor(Color.WHITE)
                            handler.removeCallbacks(refresh);
                        }
                    ).setDuration(
                        2000
                    ).withColor(Color.parseColor("#A5CA40")
                    ).show() // Finally show the snack bar
                }
            }
        }
        return false
    }

    fun playBtnClick(v: View) {
        if (mp.isPlaying) {
            // Stop the music
            mp.pause()
            recViewById.playBtn.setBackgroundResource(android.R.drawable.ic_media_play)

        } else {
            // Start the music
            mp.start()
            recViewById.playBtn.setBackgroundResource(android.R.drawable.ic_media_pause)
        }
    }

    fun settingBtnClick(v: View) {
        recViewById.settingPanel.setVisibility(View.VISIBLE);
    }

    fun endBtnClick(v: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    fun settingDoneBtnClick(v: View) {
        bpmVal = recViewById.bpmBar.progress
        keysVal = recViewById.keysBar.progress
        danceabilityVal = recViewById.danceabilityBar.progress
        valenceVal = recViewById.valenceBar.progress
        energyVal = recViewById.energyBar.progress

        recViewById.settingPanel.setVisibility(View.GONE);
    }

    fun createTimeTextView(time: Int): String {
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        var timeTextView = "$min:"
        if (sec < 10) timeTextView += "0"
        timeTextView += sec
        return timeTextView
    }

    fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar{
        this.view.setBackgroundColor(colorInt)
        return this
    }


}