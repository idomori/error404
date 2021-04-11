package edu.umich.kyuhyunp.kotlinchatter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.umich.kyuhyunp.kotlinchatter.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    private lateinit var postViewById: ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postViewById = ActivityPostBinding.inflate(layoutInflater)
        setContentView(postViewById.root)
    }

    fun submitSong(view: View?) {
        val songs = Song(
            song = postViewById.songTextView.text.toString())

        val store = SongStore()
        // val songListAdapter = SongListAdapter(this, ArrayList())
        store.postSong(this, songs) {}

        // store.getSongs(this, songListAdapter) {}


        finish()
    }

}