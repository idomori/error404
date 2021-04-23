package edu.umich.error404.kotlinchatter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.umich.error404.kotlinchatter.databinding.ActivityImportBinding


class ImportActivity : AppCompatActivity() {
    private val serverUrl = "https://159.65.222.2/"
    private lateinit var importViewById: ActivityImportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        importViewById = ActivityImportBinding.inflate(layoutInflater)
        setContentView(importViewById.root)

        importViewById.songLink.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        importViewById.songName.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
        importViewById.playListName.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })
    }

    fun submitSong(view: View?) {
        val enteredUrl = importViewById.songLink.text.toString()
        MainActivity.seedingUrl = enteredUrl
        val store = SongStore()
        if (enteredUrl.contains("playlist", ignoreCase = true)) {
            store.readPlaylist(this, enteredUrl) {
                val intent = Intent(this, RecommendationActivity::class.java)
                if(MainActivity.songList.isEmpty()) {
                    val toast = Toast.makeText(
                            applicationContext,
                            "Sorry. We can not find any related song. Please enter a new url",
                            Toast.LENGTH_SHORT
                    )
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }
                else {
                    startActivity(intent)
                }
            }
        }
        else {
            store.readSong(this, enteredUrl) {
                val intent = Intent(this, RecommendationActivity::class.java)
                if(MainActivity.songList.isEmpty()) {
                    val toast = Toast.makeText(
                            applicationContext,
                            "Sorry. We can not find any related song. Please enter a new url",
                            Toast.LENGTH_SHORT
                    )
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                }
                else {
                    startActivity(intent)
                }
            }
        }
        // populateSongList()

    }

    fun goBack(view: View?) {
        val intent = Intent(this, StartingActivity::class.java)
        startActivity(intent)
    }

    fun hideKeyboard(view: View?) {
        val inputMethodManager: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun submitSongName(view: View?){
        val store = SongStore()

        val songName = importViewById.songName.text.toString()

        store.submitSongName(this, songName){
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

    }

    fun submitPlaylistName(view: View?){
        val store = SongStore()

        val playListName = importViewById.playListName.text.toString()

        store.submitPlaylistName(this, playListName){
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

    }

}
