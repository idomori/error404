package edu.umich.kyuhyunp.kotlinchatter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.umich.kyuhyunp.kotlinchatter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var songs: SongListAdapter
    private lateinit var mainViewById: ActivityMainBinding

    private fun refreshTimeline() {
        val store = SongStore()

        // songs.clear()
        /*store.getSongs(this, songs) {
            // stop the refreshing animation upon completion:
            mainViewById.refreshContainer.isRefreshing = false
        }*/
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

    fun startPost(view: View?) {
        val intent = Intent(this, PostActivity::class.java)
        startActivity(intent)
    }
}