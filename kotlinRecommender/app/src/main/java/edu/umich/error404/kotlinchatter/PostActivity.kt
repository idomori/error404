package edu.umich.error404.kotlinchatter

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.umich.error404.kotlinchatter.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    private lateinit var postViewById: ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        postViewById = ActivityPostBinding.inflate(layoutInflater)
        setContentView(postViewById.root)
    }

    fun submitSong(view: View?) {
        val enteredSong = Song(
            song = postViewById.songLinkTextView.text.toString())

        val store = SongStore()
        // val songListAdapter = SongListAdapter(this, ArrayList())

        store.postSong(this, enteredSong) {}
        finish()
    }

}