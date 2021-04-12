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
        setContentView(R.layout.activity_import)
    }

    fun submitSong(view: View?) {
        val intent = Intent(this, RecommendationActivity::class.java)
        startActivity(intent)
    }

    fun goBack(view: View?) {
        val intent = Intent(this, StartingActivity::class.java)
        startActivity(intent)
    }


}