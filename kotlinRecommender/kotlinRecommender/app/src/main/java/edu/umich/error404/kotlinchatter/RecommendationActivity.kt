package edu.umich.error404.kotlinchatter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import edu.umich.error404.kotlinchatter.databinding.ActivityRecommendationBinding
import kotlin.math.abs

class RecommendationActivity : AppCompatActivity() {
    private lateinit var recViewById: ActivityRecommendationBinding
    private var xdown: Float = 0f
    private var ydown: Float = 0f

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        super.dispatchTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                xdown = event.x
                ydown = event.y
            }
            MotionEvent.ACTION_UP -> {
                if ((xdown - event.x) > 100 && abs(event.y - ydown) < 100) {
                    val intent = Intent(this, UndoReject::class.java)
                    intent.putExtra("CHATTARRAY", "Song Rejected")
                    startActivity(intent)
                }
                else if ((xdown - event.x) < -100 && abs(event.y - ydown) < 100) {
                    val intent = Intent(this, UndoAdd::class.java)
                    intent.putExtra("CHATTARRAY", "Song Accepted")
                    startActivity(intent)
                }
            }
        }
        return false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recViewById = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(recViewById.root)
    }
}