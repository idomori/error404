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
        startActivity(intent)
    }

    fun chooseEDM(view: View?){
        val intent = Intent(this, HiphopActivity::class.java)
        startActivity(intent)
    }

    fun choosePop(view: View?){
        val intent = Intent(this, HiphopActivity::class.java)
        startActivity(intent)
    }

    fun chooseRock(view: View?) {
        val intent = Intent(this, HiphopActivity::class.java)
        startActivity(intent)
    }

}