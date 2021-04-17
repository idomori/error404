package edu.umich.error404.kotlinchatter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.umich.error404.kotlinchatter.databinding.ActivitySearchBinding
import java.util.*

class searchActivity : AppCompatActivity() {
    // private lateinit var songs: SongListAdapter
    private lateinit var searchViewById: ActivitySearchBinding

    companion object {
        @JvmField
        var songNameList: Queue<SongPlaylistSearch> = LinkedList<SongPlaylistSearch>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }
}