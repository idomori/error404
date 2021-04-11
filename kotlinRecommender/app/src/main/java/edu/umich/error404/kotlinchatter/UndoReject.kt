package edu.umich.error404.kotlinchatter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import edu.umich.error404.kotlinchatter.databinding.ActivityUndoRejectBinding

class UndoReject : AppCompatActivity() {
    private lateinit var undorejectViewById: ActivityUndoRejectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        undorejectViewById = ActivityUndoRejectBinding.inflate(layoutInflater)
        setContentView(undorejectViewById.root)
    }

    fun goBack(view: View?) {
        finish()
    }
}