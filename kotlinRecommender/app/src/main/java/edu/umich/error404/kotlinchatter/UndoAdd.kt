package edu.umich.error404.kotlinchatter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import edu.umich.error404.kotlinchatter.databinding.ActivityUndoAddBinding

class UndoAdd : AppCompatActivity() {
    private lateinit var undoaddViewById: ActivityUndoAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        undoaddViewById = ActivityUndoAddBinding.inflate(layoutInflater)
        setContentView(undoaddViewById.root)
    }

    fun goBack(view: View?) {
        finish()
    }
}