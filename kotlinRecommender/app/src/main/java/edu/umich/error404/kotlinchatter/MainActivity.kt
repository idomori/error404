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
        //var songsList = arrayListOf<Song>()
        var songsList: Queue<Song> = LinkedList<Song>()
        var LikedSongsList = arrayListOf<Song>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewById = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewById.root)

        songs = SongListAdapter(this, ArrayList())
        mainViewById.songListView.setAdapter(songs)

        // setup refreshContainer here
        mainViewById.refreshContainer.setOnRefreshListener {
            refreshTimeline()
        }
        refreshTimeline()

    }

    private fun refreshTimeline() {
        val store = SongStore()
        LikedSongsList.clear()
        for (i in 0 until LikedSongsList.size) {
            songs.add(LikedSongsList[i])
        }
        mainViewById.refreshContainer.isRefreshing = false
    }

    fun startPost(view: View?) {
        val intent = Intent(this, StartingActivity::class.java)
        startActivity(intent)
    }

    fun getRecommendation(view: View?) {
        val intent = Intent(this, RecommendationActivity::class.java)
        startActivity(intent)
    }
}