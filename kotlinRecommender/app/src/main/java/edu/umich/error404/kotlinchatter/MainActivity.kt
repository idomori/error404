package edu.umich.error404.kotlinchatter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.umich.error404.kotlinchatter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var songs: SongListAdapter
    private lateinit var mainViewById: ActivityMainBinding
    companion object {
        @JvmField
        var songsList = arrayListOf<Song>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewById = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainViewById.root)

        songs = SongListAdapter(this, ArrayList())
        mainViewById.songListView.setAdapter(songs)

        // setup refreshContainer here later

        mainViewById.refreshContainer.setOnRefreshListener {
            refreshTimeline()
        }
        refreshTimeline()

    }

    private fun refreshTimeline() {
        val store = SongStore()
        songs.clear()
        for (i in 0 until songsList.size) {
            songs.add(songsList[i])
        }
        mainViewById.refreshContainer.isRefreshing = false
    }

    fun startPost(view: View?) {
        val intent = Intent(this, PostActivity::class.java)
        startActivity(intent)
    }

    fun getRecommendation(view: View?) {
        val intent = Intent(this, RecommendationActivity::class.java)
        startActivity(intent)
    }
}