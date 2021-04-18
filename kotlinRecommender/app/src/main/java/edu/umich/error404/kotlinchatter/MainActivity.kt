package edu.umich.error404.kotlinchatter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.umich.error404.kotlinchatter.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var songs: SongListAdapter
    private lateinit var mainViewById: ActivityMainBinding
    companion object {
        @JvmField
        // audio feature range
        var settingEnabled:Boolean = false
        var minBpm:Double = 0.0
        var maxBpm:Double = 300.0
        var minKey:Int = 0
        var maxKey:Int = 11
        var minDanceability:Double = 0.0
        var maxDanceability:Double = 1.0
        var minValence:Double = 0.0
        var maxValence:Double = 1.0
        var minEnergy:Double = 0.0
        var maxEnergy:Double = 1.0

        // information about the seeding song/playlist
        var seedingUrl:String = ""

        // song storage
        var songList: Queue<Song> = LinkedList<Song>()
        var LikedSongList = arrayListOf<Song>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewById = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewById.root)

        songs = SongListAdapter(this, ArrayList())
        mainViewById.songListView.setAdapter(songs)

        for (i in 0 until LikedSongList.size) {
            songs.add(LikedSongList[i])
        }
        //setup refreshContainer here
        mainViewById.refreshContainer.setOnRefreshListener {
//            //refreshTimeline()
            mainViewById.refreshContainer.isRefreshing = false
       }
//        refreshTimeline()

    }

    private fun refreshTimeline() {
        val store = SongStore()

        for (i in 0 until LikedSongList.size) {
            songs.add(LikedSongList[i])
        }
        LikedSongList.clear()
        mainViewById.refreshContainer.isRefreshing = false
    }

//    fun startPost(view: View?) {
//        val intent = Intent(this, StartingActivity::class.java)
//        startActivity(intent)
//    }
//
//    fun getRecommendation(view: View?) {
//        val intent = Intent(this, RecommendationActivity::class.java)
//        startActivity(intent)
//    }
}