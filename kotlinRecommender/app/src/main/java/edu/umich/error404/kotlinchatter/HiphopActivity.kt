package edu.umich.error404.kotlinchatter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class HiphopActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiphop)

        val listView = findViewById<ListView>(R.id.hiphop_songlist)

        listView.adapter = MyCustomAdapter(this)
    }

    fun goBack(view: View?) {
        val intent = Intent(this, StartingActivity::class.java)
        startActivity(intent)
    }

    class MyCustomAdapter(context: Context): BaseAdapter() {

        private val mContext: Context

        private val songs = arrayListOf<String>(
            "Wants and Needs (feat. Lil Baby)", "4 Da Gang (with Roddy Ricch)",
            "Rule #1 (feat. Lil Yachty)", "Beat Box 3 (feat. DaBaby)", "Time Today",
            "Headshot (feat. Polo G & Fivio Foreign)", "On Me", "What's Next", "Masterpiece", "Poppin",
            "Pop It (feat. Megan Thee Stallion)", "Real As It Gets (feat. EST Gee)", "AP - Music from film Boogie",
            "Run it Up (feat. Offset & Moneybagg Yo)", "Still Trappin' (with King Von)", "Back in Blood (feat. Lil Durk)"
        )

        private val artists = arrayListOf<String>(
            "Drake, Lil Baby", "42 Dugg, Roddy Ricch", "DDG, OG Parker, Lil Yachty", "SpotemGottem, DaBaby",
            "Moneybagg Yo", "Lil Tjay, Polo G, Fivio Foreign", "Lil Baby", "Drake", "DaBaby", "Lakeyah, Gucci Mane",
            "Bankroll Freddie, Megan Thee Stallion", "Lil Baby, EST Gee", "Pop Smoke", "Lil Tjay, Offset, Moneybagg Yo",
            "Lil Durk, King Von", "Pooh Shiesty, Lil Durk"
        )

        private val albumCovers = arrayListOf<String>(
            "https://lite-images-i.scdn.co/image/ab67616d00001e02bba7cfaf7c59ff0898acba1f",
            "https://images.complex.com/complex/images/c_fill,dpr_auto,f_auto,q_auto,w_1400/fl_lossy,pg_1/ralcyp4q8jticdnfehqc/dugg?fimg-ssr-default",
            "https://i.scdn.co/image/ab67616d0000b273d0e6375edf1bd35f50228f3b",
            "https://i.scdn.co/image/ab67616d0000b273637d8f4420da84bb0b50e757",
            "https://i.scdn.co/image/ab67616d0000b2731c8bef7c365a852c21acf002",
            "https://i.scdn.co/image/ab67616d0000b273c3c0cf41da2bb35e19acc875",
            "https://i.scdn.co/image/ab67616d0000b2738de3ce24866dcc8ffddbebac",
            "https://i.scdn.co/image/ab67616d0000b2738b20e4631fa15d3953528bbc",
            "https://i.scdn.co/image/ab67616d0000b2737b41da110df7023757e8f8fa",
            "https://i.scdn.co/image/ab67616d0000b273d0d59631f0c11267ab4c5249",
            "https://lite-images-i.scdn.co/image/ab67616d00001e0209004b0db2a54c8008cf0b92",
            "https://images.complex.com/complex/images/c_fill,dpr_auto,f_auto,q_90,w_1400/fl_lossy,pg_1/sgmglgrddhanxqk24jyw/lil-baby-as-real-as-it-gets-cover",
            "https://i.scdn.co/image/ab67616d0000b273910d332c82ea0e868127f9a0",
            "https://i.scdn.co/image/ab67616d0000b2735af0305457ff290e06dcc4c2",
            "https://i.scdn.co/image/ab67616d0000b273e67a56591cf8bcfcb1450980",
            "https://i.scdn.co/image/ab67616d0000b27332c5d1e207364562fe2160b7"
        )

        init {
            this.mContext = context
        }

        //number of rows in the list
        override fun getCount(): Int {
            return songs.size
            //return 20
        }


        //rendering each row
        override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.activity_hiphop_row, viewGroup , false)

            val songTextView = rowMain.findViewById<TextView>(R.id.songTextView)
            //songTextView.text = "Song " + (position.toLong() + 1)
            songTextView.text = songs.get(position)

            val artistTextView = rowMain.findViewById<TextView>(R.id.artistTextView)
            //artistTextView.text = "Artist " + (position.toLong() + 1)
            artistTextView.text = artists.get(position)

            val albumImageView = rowMain.findViewById<ImageView>(R.id.album_imageView)
            //albumImageView.setImageURI(albumCovers.get(position))
            Picasso.get().load(albumCovers.get(position)).into(albumImageView)



            val grayColor = Color.parseColor("#d3d3d3")

            if (position % 2 == 0) {
                rowMain.setBackgroundColor(grayColor)
            }

            return rowMain
        }


        //ignore for now
        override fun getItem(position: Int): Any {
            return "TEST STRING"
        }

        //also ignore for now
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
    }
}