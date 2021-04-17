package edu.umich.error404.kotlinchatter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import edu.umich.error404.kotlinchatter.databinding.ActivityRecommendationBinding
import org.florescu.android.rangeseekbar.RangeSeekBar
import kotlin.math.abs
import kotlin.random.Random


class RecommendationActivity : AppCompatActivity() {
    private lateinit var recViewById: ActivityRecommendationBinding
    private var xdown: Float = 0f
    private var ydown: Float = 0f
    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    private var song:Song = Song()
    private var bpmVal:Double = 0.0
    private var keyVal:Int = 0
    private var danceabilityVal:Double = 0.0
    private var valenceVal:Double = 0.0
    private var energyVal:Double = 0.0

    private var preview_url:String = ""
    private var image_url:String = ""

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            recViewById.positionBar.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeTextView(currentPosition)
            var remainingTime = createTimeTextView(totalTime - currentPosition)

            if (preview_url == "null") {
                recViewById.elapsedTimeTextView.text = "null"
                recViewById.remainingTimeTextView.text = "null"
            }
            else {
                recViewById.elapsedTimeTextView.text = elapsedTime
                recViewById.remainingTimeTextView.text = "-$remainingTime"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recViewById = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(recViewById.root)

        // Temp toast message to test whether the activity has been refreshed
        // Toast.makeText(applicationContext,"Recommendation Activity Starts",Toast.LENGTH_SHORT).show()

        song = MainActivity.songList.peek()
        MainActivity.songList.poll()
        preview_url = song.preview_url.toString()
        image_url = song.image_url.toString()
        var artist_name = song.artistName
        var song_name = song.songName
        bpmVal = song.bpm!!
        keyVal = song.key!!
        danceabilityVal = song.danceability!!
        valenceVal = song.valence!!
        energyVal = song.energy!!
        recViewById.songTitle.text = song_name
        recViewById.artistName.text = artist_name
        //use sample urls for now

        if (preview_url == "null") {
            val toast = Toast.makeText(
                applicationContext,
                "Sorry, this song does not have a preview",
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        // Set MediaPlayer
        mp = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            if (preview_url != "null") {
                setDataSource(preview_url)
                prepare() // might take long! (for buffering, etc)
                start()  // Assume we play the music by default
            }
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

        //initSettingBars()
        initSettingRangeBars()
    }

    fun initSettingRangeBars() {
        recViewById.bpmRangeBar.setSelectedMinValue(MainActivity.minBpm)
        recViewById.bpmRangeBar.setSelectedMaxValue(MainActivity.maxBpm)

        recViewById.keysRangeBar.setSelectedMinValue(MainActivity.minKey)
        recViewById.keysRangeBar.setSelectedMaxValue(MainActivity.maxKey)

        recViewById.danceabilityRangeBar.setSelectedMinValue(MainActivity.minDanceability)
        recViewById.danceabilityRangeBar.setSelectedMaxValue(MainActivity.maxDanceability)

        recViewById.valenceRangeBar.setSelectedMinValue(MainActivity.minValence)
        recViewById.valenceRangeBar.setSelectedMaxValue(MainActivity.maxValence)

        recViewById.energyRangeBar.setSelectedMinValue(MainActivity.minEnergy)
        recViewById.energyRangeBar.setSelectedMaxValue(MainActivity.maxEnergy)
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
                            // remove all songs from the songList, and
                            MainActivity.songList.clear()
                            // restart recommendation with 5 random songs from liked song list
                            var sampleIndex:MutableList<Int> = sampleLikedSong()

                            val store = SongStore()  // TODO: restart recommendation with 5 random songs
                            song.songId?.let {
                                store.readSong(this@RecommendationActivity, it) {
                                    // refresh current activity
                                    val intent = intent
                                    finish()
                                    startActivity(intent)
                                }
                            }


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
                            handler.removeCallbacks(refresh)
                        }
                    ).setDuration(
                        2000
                    ).withColor(
                        Color.parseColor("#F38D6D")
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
                            if (!MainActivity.songList.isEmpty()) {
                                val intent = intent
                                finish()
                                startActivity(intent)
                            } else {
                                val store = SongStore()
                                song.songId?.let {
                                    store.readSong(this@RecommendationActivity, it) {
                                        val intent = intent
                                        finish()
                                        startActivity(intent)
                                    }
                                }
                            }
                        }
                    }
                    handler.postDelayed(refresh, 2000)

                    MainActivity.LikedSongList.add(song)
                    // Show a snack bar for undo option
                    Snackbar.make(
                        recViewById.rootLayout, // Parent view
                        "Song Saved! \nSearching the next song...", // Message to show
                        Snackbar.LENGTH_LONG //
                    ).setAction( // Set an action for snack bar
                        "Undo", // Action button text
                        { // UNDO Action button click listener
                            recViewById.rootLayout.setBackgroundColor(Color.WHITE)
                            MainActivity.LikedSongList.remove(song)
                            handler.removeCallbacks(refresh)
                        }
                    ).setDuration(
                        2000
                    ).withColor(
                        Color.parseColor("#A5CA40")
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
        if (MainActivity.settingEnabled == false) {
            disableSettingBars()
        }
        else {
            enableSettingBars()
        }
        recViewById.settingPanel.setVisibility(View.VISIBLE)
        recViewById.positionBar.isEnabled  = false
        recViewById.settingBtn.isEnabled = false
        recViewById.spotifyBtn.isEnabled = false
        recViewById.endBtn.isEnabled = false
    }

    fun settingSwitchClick(v: View) {
        if (recViewById.settingSwitch.isChecked == false) {  //switch OFF
            MainActivity.settingEnabled = false
            disableSettingBars()
            var d = getResources().getDrawable(R.drawable.roundcorner_disabled)
            recViewById.settingBtn.background = d
            recViewById.settingBtnLable.text = "Recommander \n Settings \n (Disabled)"
        }
        else { // switch ON
            MainActivity.settingEnabled = true
            enableSettingBars()
            var d = getResources().getDrawable(R.drawable.roundcorner)
            recViewById.settingBtn.background = d
            recViewById.settingBtnLable.text = "Recommander \n Settings \n (Enabled)"

        }
    }

    fun endBtnClick(v: View) {
        mp.pause()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun openSpotify(view: View) {
        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse("spotify:track:" + "61bwFjzXGG1x2aZsANdLyl") //test track
        intent.data = Uri.parse("spotify:track:" + song.songId)

        intent.putExtra(
            Intent.EXTRA_REFERRER,
            Uri.parse("android://" + this.packageName)
        )
        this.startActivity(intent)
    }


    fun settingDoneBtnClick(v: View) {
        // update the audio range values
        MainActivity.minBpm = recViewById.bpmRangeBar.selectedMinValue.toDouble()
        MainActivity.maxBpm = recViewById.bpmRangeBar.selectedMaxValue.toDouble()
        MainActivity.minKey = recViewById.keysRangeBar.selectedMinValue.toInt()
        MainActivity.maxKey = recViewById.keysRangeBar.selectedMaxValue.toInt()
        MainActivity.minDanceability = recViewById.danceabilityRangeBar.selectedMinValue.toDouble()
        MainActivity.maxDanceability = recViewById.danceabilityRangeBar.selectedMaxValue.toDouble()
        MainActivity.minValence = recViewById.valenceRangeBar.selectedMinValue.toDouble()
        MainActivity.maxValence = recViewById.valenceRangeBar.selectedMaxValue.toDouble()
        MainActivity.minEnergy = recViewById.energyRangeBar.selectedMinValue.toDouble()
        MainActivity.maxEnergy = recViewById.energyRangeBar.selectedMaxValue.toDouble()

        // enable elements in the back
        recViewById.settingPanel.setVisibility(View.GONE)
        recViewById.positionBar.isEnabled  = true
        recViewById.settingBtn.isEnabled = true
        recViewById.spotifyBtn.isEnabled = true
        recViewById.endBtn.isEnabled = true
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

    fun enableSettingBars() {
        recViewById.settingPanel.setBackgroundColor(getResources().getColor(R.color.yellow2))
        recViewById.bpmRangeBar.isEnabled = true
        recViewById.keysRangeBar.isEnabled = true
        recViewById.danceabilityRangeBar.isEnabled = true
        recViewById.valenceRangeBar.isEnabled = true
        recViewById.energyRangeBar.isEnabled = true
    }


    fun disableSettingBars() {
        recViewById.settingPanel.setBackgroundColor(getResources().getColor(R.color.discard))
        recViewById.bpmRangeBar.isEnabled = false
        recViewById.keysRangeBar.isEnabled = false
        recViewById.danceabilityRangeBar.isEnabled = false
        recViewById.valenceRangeBar.isEnabled = false
        recViewById.energyRangeBar.isEnabled = false

    }

    fun sampleLikedSong(): MutableList<Int> {
//        var sampleIndex = mutableListOf<Int>()
//        // if less than 5 liked songs, no need to sample
//        if (MainActivity.LikedSongList.size <= 5) {
//            for(i in 0..MainActivity.LikedSongList.size-1) {
//                sampleIndex.add(i)
//            }
//        }
//        else {
//            sampleIndex = List(5) { Random.nextInt(0, MainActivity.LikedSongList.size-1) }.toMutableList()
//        }
        var list = MutableList(MainActivity.LikedSongList.size) { index -> 0 + index }
        var shuffedList = list.shuffled()
        var size:Int = if (MainActivity.LikedSongList.size <5){ MainActivity.LikedSongList.size} else{5}
        var sampleIndex = shuffedList.subList(0,size).toMutableList()
        return sampleIndex
    }




}

