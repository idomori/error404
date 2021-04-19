package edu.umich.error404.kotlinchatter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import edu.umich.error404.kotlinchatter.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    // private lateinit var songs: SongListAdapter
    private lateinit var searchViewById: ActivitySearchBinding
    private lateinit var songs: SearchListAdapter

    companion object {
        @JvmField
        var songNameList = arrayListOf<SongPlaylistSearch>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchViewById = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(searchViewById.root)

        songs =  SearchListAdapter(this, ArrayList())
        searchViewById.songPlaylistListView.setAdapter(songs)

        for (i in 0 until songNameList.size) {
            songs.add(songNameList[i])
        }

        onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
                songNameList.clear()
            }
        })

    }





}