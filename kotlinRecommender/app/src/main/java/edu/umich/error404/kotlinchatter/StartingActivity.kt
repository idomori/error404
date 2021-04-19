package edu.umich.error404.kotlinchatter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.umich.error404.kotlinchatter.databinding.ActivityStartingBinding

class StartingActivity : AppCompatActivity() {
    private lateinit var startViewById: ActivityStartingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startViewById = ActivityStartingBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_starting)
    }

    fun importSong(view: View?) {
        val intent = Intent(this, ImportActivity::class.java)
        startActivity(intent)
    }

    fun chooseHiphop(view: View?){
        val intent = Intent(this, HiphopActivity::class.java)
        intent.putExtra("genre", "hiphop")
        intent.putExtra("playlist", "https://open.spotify.com/playlist/5H1j8FNrXB1rvq1S6M1azV?si=k8c9Q-6qSpWKqWVIBD3FyA")
        startActivity(intent)
    }

    fun chooseDance(view: View?){
        val intent = Intent(this, HiphopActivity::class.java)
        intent.putExtra("genre", "dance")
        intent.putExtra("playlist", "https://open.spotify.com/playlist/5JZk7WmIWxMe5ZZDROUzk3?si=G3FDqcyQSMq0u3rXMr3oUQ")
        startActivity(intent)
    }

    fun choosePop(view: View?){
        val intent = Intent(this, HiphopActivity::class.java)
        intent.putExtra("genre", "pop")
        intent.putExtra("playlist", "https://open.spotify.com/playlist/6tKBMThPGSL34DufPc2LNR?si=T62SqpasTbCVpSHOSYbx6w")
        startActivity(intent)
    }

    fun chooseRock(view: View?) {
        val intent = Intent(this, HiphopActivity::class.java)
        intent.putExtra("genre", "rock")
        intent.putExtra("playlist", "https://open.spotify.com/playlist/59nlz5BO4LsgJcCkTcmPrz?si=Bz4bBOncRc61LlJJBVfPdw")
        startActivity(intent)
    }

}