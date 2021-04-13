package edu.umich.error404.kotlinchatter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.umich.error404.kotlinchatter.databinding.ActivityImportBinding

class ImportActivity : AppCompatActivity() {
    private lateinit var importViewById: ActivityImportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        importViewById = ActivityImportBinding.inflate(layoutInflater)
        setContentView(importViewById.root)
    }

    fun submitSong(view: View?) {
        val enteredUrl = importViewById.songLink.text.toString()
        val store = SongStore()
        if (enteredUrl.contains("playlist", ignoreCase = true)) {
            store.readPlaylist(this, enteredUrl) {}
        }
        else {
            store.readSong(this, enteredUrl) {}
        }
        val intent = Intent(this, RecommendationActivity::class.java)
        startActivity(intent)
    }

    fun goBack(view: View?) {
        val intent = Intent(this, StartingActivity::class.java)
        startActivity(intent)
    }


}