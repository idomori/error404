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
        val genre = intent.getStringExtra("genre")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hiphop)

        val listView = findViewById<ListView>(R.id.hiphop_songlist)

        if (genre == "hiphop")
            listView.adapter = HipHopList(this)
        else if (genre == "dance")
            listView.adapter = DanceList(this)
        else if (genre == "pop")
            listView.adapter = PopList(this)
        else if (genre == "rock")
            listView.adapter = RockList(this)
    }

    fun goBack(view: View?) {
        val intent = Intent(this, StartingActivity::class.java)
        startActivity(intent)
    }

    fun choosePlaylist(view: View?) {
        val store = SongStore()

        val playlistName = intent.getStringExtra("playlist")
        MainActivity.seedingUrl = playlistName
        store.readPlaylist(this, playlistName){
            val intent = Intent(this, RecommendationActivity::class.java)
            startActivity(intent)
        }
    }


    class HipHopList(context: Context): BaseAdapter() {

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
            songTextView.text = songs.get(position)

            val artistTextView = rowMain.findViewById<TextView>(R.id.artistTextView)
            artistTextView.text = artists.get(position)

            val albumImageView = rowMain.findViewById<ImageView>(R.id.album_imageView)
            Picasso.get().load(albumCovers.get(position)).into(albumImageView)

            val grayColor = Color.parseColor("#f5f5f5")

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

    class DanceList(context: Context): BaseAdapter() {

        private val mContext: Context

        private val songs = arrayListOf<String>(
                "Spinning (with Charli XCX & The 1975)", "Both of Us - Edit", "Tuesday", "Chemicals",
                "Straight to the Morning - Remix", "Shit Cant Make Anything",
                "Clouds (feat. The Wombats)", "Still Got It", "Modern Romance", "Haven't You Heard - Fitzy's Half Charged Mix"
        )

        private val artists = arrayListOf<String>(
                "No Rome, Charli XCX, The 1975", "Jayda G", "NEIL FRANCES", "SG Lewis", "Hot Chip, Dillon Francis, Jarvis Cocker",
                "Gerry Read", "Whethan, The Wombats", "Alex Frankel", "Catz 'n Dogz, Gerd Janson", "Alan Fitzpatrick, Patrice Rushen"
        )

        private val albumCovers = arrayListOf<String>(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFFvgTkWXrF_i9a5bDnXUGRRkSM4MeAJJ8Zw_DQPxtKdbfLuUv53jtVoqQzxjZxmOgdHk&usqp=CAU",
                "https://i.scdn.co/image/ab67616d0000b273158ced5d410610389e5ba8fe",
                "https://lite-images-i.scdn.co/image/ab67616d00001e0219a8410926d59298743aee0a",
                "https://i.scdn.co/image/ab67616d0000b2734d8a780ff1cbcd686cbe370a",
                "https://i.scdn.co/image/ab67616d0000b273ff5fd5ead76ddc8a4b38f0dd",
                "https://i.scdn.co/image/ab67616d0000b2733076a8b7d7a1de560744c1b2",
                "https://i.scdn.co/image/ab67616d0000b273808d80990a02b6dbfb09f8d3",
                "https://images.roughtrade.com/product/images/files/000/214/813/hero/AlexFrankel-StillGotIt_360x.jpg?1617791295",
                "https://i.scdn.co/image/ab67616d0000b2733ce83740ce3e06ebcbc0a432",
                "https://i.scdn.co/image/ab67616d0000b273bcae951e76eaf49a5a0ca588"

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
            songTextView.text = songs.get(position)

            val artistTextView = rowMain.findViewById<TextView>(R.id.artistTextView)
            artistTextView.text = artists.get(position)

            val albumImageView = rowMain.findViewById<ImageView>(R.id.album_imageView)
            Picasso.get().load(albumCovers.get(position)).into(albumImageView)

            val grayColor = Color.parseColor("#f5f5f5")

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

    class PopList(context: Context): BaseAdapter() {

        private val mContext: Context

        private val songs = arrayListOf<String>(
                "Kiss Me More (feat. SZA)", "Peaches (feat. Daniel Caesar & Giveon)", "MONTERO (Call Me By Your Name)",
                "Heartbreak Anniversary", "RAPSTAR", "Save Your Tears", "deja vu", "Levitating (feat. DaBaby)",
                "Leave The Door Open", "telepatía"
        )

        private val artists = arrayListOf<String>(
                "Doja Cat, SZA", "Justin Bieber, Daniel Caesar, Giveon", "Lil Nas X", "Giveon", "Polo G",
                "The Weeknd", "Olivia Rodrigo", "Dua Lipa, DaBaby", "Bruno Mars, Anderson .Paak", "Kali Uchis"
        )

        private val albumCovers = arrayListOf<String>(
                "https://pbs.twimg.com/media/EyyTXyFXIAkTW8g.jpg",
                "https://i.scdn.co/image/ab67616d0000b273e6f407c7f3a0ec98845e4431",
                "https://i.scdn.co/image/ab67616d0000b273f3e1aa1e218f9c59d9df8461",
                "https://i.scdn.co/image/ab67616d0000b273b0d2119e477ebbb543f19ed7",
                "https://i.scdn.co/image/ab67616d0000b273175715816616d0fb317fabeb",
                "https://i.scdn.co/image/ab67616d0000b2738863bc11d2aa12b54f5aeb36",
                "https://i.scdn.co/image/ab67616d0000b2735a61e19eaffec620c1899c47",
                "https://i.scdn.co/image/ab67616d0000b273c17893b4b866f75928d7e1c1",
                "https://i.scdn.co/image/ab67616d0000b2736f9e6abbd6fa43ac3cdbeee0",
                "https://i.scdn.co/image/ab67616d0000b273044a5466dac00f7b3c570b99"
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
            songTextView.text = songs.get(position)

            val artistTextView = rowMain.findViewById<TextView>(R.id.artistTextView)
            artistTextView.text = artists.get(position)

            val albumImageView = rowMain.findViewById<ImageView>(R.id.album_imageView)
            Picasso.get().load(albumCovers.get(position)).into(albumImageView)

            val grayColor = Color.parseColor("#f5f5f5")

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

    class RockList(context: Context): BaseAdapter() {

        private val mContext: Context

        private val songs = arrayListOf<String>(
                "Whole Lotta Love - 1990 Remaster", "All Along the Watchtower", "Back in Black",
                "Paint It, Black - Mono", "Rebel Rebel - 1999 Remaster", "Come Together - Remastered 2009",
                "Should I Stay or Should I Go - Remastered", "Baba O'Riley", "Sweet Emotion", "Another One Bites the Dust"
        )

        private val artists = arrayListOf<String>(
                "Led Zeppelin", "Jimi Hendrix", "AC/DC", "The Rolling Stones", "David Bowie", "The Beatles",
                "The Clash", "The Who", "Aerosmith", "Queen"
        )

        private val albumCovers = arrayListOf<String>(
                "https://i.scdn.co/image/ab67616d0000b273fc4f17340773c6c3579fea0d",
                "https://i.scdn.co/image/ab67616d0000b273522088789d49e216d9818292",
                "https://i.scdn.co/image/ab67616d0000b2730b51f8d91f3a21e8426361ae",
                "https://i.scdn.co/image/ab67616d0000b273bad7062c3fd2f2d037989694",
                "https://i.scdn.co/image/ab67616d0000b273ad22c83a6e1567f8453c32b3",
                "https://i.scdn.co/image/ab67616d0000b27340bfd4321b437f018ecc7555",
                "https://images-na.ssl-images-amazon.com/images/I/81I-aQnXPKL._AC_UL600_SR600,600_.jpg",
                "https://i.scdn.co/image/ab67616d0000b273fe24dcd263c08c6dd84b6e8c",
                "https://i.scdn.co/image/ab67616d0000b2739662c6535fb4bf5767e50f32",
                "https://i.scdn.co/image/ab67616d0000b273ba84c3e2d930822ae1c3ce22"

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
            songTextView.text = songs.get(position)

            val artistTextView = rowMain.findViewById<TextView>(R.id.artistTextView)
            artistTextView.text = artists.get(position)

            val albumImageView = rowMain.findViewById<ImageView>(R.id.album_imageView)
            Picasso.get().load(albumCovers.get(position)).into(albumImageView)

            val grayColor = Color.parseColor("#f5f5f5")

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