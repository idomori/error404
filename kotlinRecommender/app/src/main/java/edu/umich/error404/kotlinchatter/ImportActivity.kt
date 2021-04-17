package edu.umich.error404.kotlinchatter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.umich.error404.kotlinchatter.databinding.ActivityImportBinding

class ImportActivity : AppCompatActivity() {
    private val serverUrl = "https://159.65.222.2/"
    private lateinit var importViewById: ActivityImportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        importViewById = ActivityImportBinding.inflate(layoutInflater)
        setContentView(importViewById.root)
    }

    fun submitSong(view: View?) {

        val enteredUrl = importViewById.songLink.text.toString()
        MainActivity.seedingUrl = enteredUrl
        val store = SongStore()
        if (enteredUrl.contains("playlist", ignoreCase = true)) {
            MainActivity.seedingType = "playlist"
            store.readPlaylist(this, enteredUrl) {
                val intent = Intent(this, RecommendationActivity::class.java)
                startActivity(intent)
            }
        }
        else {
            MainActivity.seedingType = "song"
            store.readSong(this, enteredUrl) {
                val intent = Intent(this, RecommendationActivity::class.java)
                startActivity(intent)
            }
        }
        // populateSongList()

    }

    fun goBack(view: View?) {
        val intent = Intent(this, StartingActivity::class.java)
        startActivity(intent)
    }

    fun submitSongName(view: View?){
        val store = SongStore()

        val songName = importViewById.songName.text.toString()

        store.submitSongName(this, songName){
            val intent = Intent(this, searchActivity::class.java)
            startActivity(intent)
        }

    }

    fun submitPlaylistName(view: View?){
        val store = SongStore()

        val playListName = importViewById.playListName.text.toString()

        store.submitPlaylistName(this, playListName){
            val intent = Intent(this, searchActivity::class.java)
            startActivity(intent)
        }

    }

    /*
    fun populateSongList() {

        MainActivity.songList.add(
            Song(
                songName = "Welcome to the Machine",
                songId = "5VWC7v2dC2K0SIIjT9WTLN",
                artistName = "Pink Floyd",
                key = 0,
                bpm = 133.273,
                danceability = 0.38,
                energy = 0.36,
                valence = 0.0362,
                image_url = "https://i.scdn.co/image/ab67616d0000b273d8fa5ac6259dba33127b398a",
                preview_url = "https://p.scdn.co/mp3-preview/81b353c6cc187c13ae81b9cc79df63742fabac89?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "Cowboys from Hell",
                songId = "2SgbR6ttzoNlCRGQOKjrop",
                artistName = "Pantera",
                key = 2,
                bpm = 114.816,
                danceability = 0.38,
                energy = 0.908,
                valence = 0.437,
                image_url =  "https://i.scdn.co/image/ab67616d0000b2730ce52f4ba340a1e459e6a978",
                preview_url = "https://p.scdn.co/mp3-preview/3c390eff507ec1287a1dae98e32787d652f52de5?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "Dark Star - Single Version; 2001 Remaster",
                songId = "0BBcbPbTWW8MaLKIDHtsAi",
                artistName = "Grateful Dead",
                key = 2,
                bpm = 95.525,
                danceability =  0.434,
                energy = 0.16,
                valence = 0.376,
                image_url = "https://i.scdn.co/image/ab67616d0000b273994cd04e29dc23b23d262d8f",
                preview_url = "https://p.scdn.co/mp3-preview/7ad922d06f309c6b31ab823bdf6b7f320bbba688?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "In My Time of Dying - 1990 Remaster",
                songId = "5g9VOUgrn7ozYE3ZQm8w41",
                artistName = "Led Zeppelin",
                key = 2,
                bpm =  101.855,
                danceability = 0.33,
                energy = 0.843,
                valence = 0.474,
                image_url = "https://i.scdn.co/image/ab67616d0000b2732abc2d86a442efb6cc631d0a",
                preview_url = "https://p.scdn.co/mp3-preview/1930386cbcee121cf4b8acdca8588a77ad3d6c90?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "Babe I'm Gonna Leave You",
                songId = "4OMu5a8sFpcRCPCcsoEaov",
                artistName = "Led Zeppelin",
                key = 4,
                bpm =  136.058,
                danceability =  0.404,
                energy = 0.435,
                valence = 0.156,
                image_url =  "https://i.scdn.co/image/ab67616d0000b2736f2f499c1df1f210c9b34b32",
                preview_url =  "https://p.scdn.co/mp3-preview/e82d356bea5c8244cee4e6d5447b46b87852dc11?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "Heaven and Hell - 2008 Remaster",
                songId = "3Jl5GohfNwozDmpzmQBLDI",
                artistName = "Black Sabbath",
                key = 3,
                bpm = 91.122,
                danceability = 0.329,
                energy = 0.673,
                valence = 0.323,
                image_url =  "https://i.scdn.co/image/ab67616d0000b273eab13a1a01fb57d6e9e8ddaf",
                preview_url = "https://p.scdn.co/mp3-preview/1b9d17b76a0e509fefc7f892ed272922ded183c7?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "Cat Scratch Fever",
                songId = "6nxlHmpBJYicXHC0mvcKOm",
                artistName = "Ted Nugent",
                key = 4,
                bpm = 127.131,
                danceability = 0.549,
                energy = 0.916,
                valence = 0.632,
                image_url =  "https://i.scdn.co/image/ab67616d0000b273fbddf0944c41f58a5589eb92",
                preview_url = "https://p.scdn.co/mp3-preview/1a08de6d022323bdf923f888c65fd6d078706e81?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "I Don't Wanna Stop",
                songId = "5axOkQnmQmwtjr4bv1Xt7i",
                artistName = "Ozzy Osbourne",
                key = 5,
                bpm = 137.937,
                danceability = 0.578,
                energy = 0.937,
                valence = 0.769,
                image_url = "https://i.scdn.co/image/ab67616d0000b273cffbd6e4a4dbdfd0d5ffd7a4",
                preview_url = "https://p.scdn.co/mp3-preview/9a5c54f15cea386d7530cc17857721ed6f69171a?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "N.I.B.",
                songId = "62oNfnQqObaqARM0DTibAL",
                artistName = "Black Sabbath",
                key = 4,
                bpm = 103.122,
                danceability = 0.329,
                energy =  0.556,
                valence = 0.43,
                image_url =  "https://i.scdn.co/image/ab67616d0000b2731311a92b0ca83a5154c5a5e7",
                preview_url = "https://p.scdn.co/mp3-preview/82ab9db4cbf7987f7bb3dd912d3e4f7b4f020961?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "Still of the Night - 2018 Remaster",
                songId = "7AtwQRLPdttsxcLM5tPL0t",
                artistName = "Whitesnake",
                key = 4,
                bpm = 100.175,
                danceability = 0.334,
                energy = 0.854,
                valence = 0.284,
                image_url = "https://i.scdn.co/image/ab67616d0000b273facf92d793609aa3bc5cc4e7",
                preview_url = "https://p.scdn.co/mp3-preview/0af528a927300f843b4bdbb237021021d3315895?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "Fall to Pieces",
                songId = "2amzrvbxYiq8AxGntIiw5V",
                artistName = "Velvet Revolver",
                key = 6,
                bpm = 132.759,
                danceability = 0.464,
                energy = 0.887,
                valence =  0.23,
                image_url = "https://i.scdn.co/image/ab67616d0000b273e7d4bfff8e7e87c6090591b6",
                preview_url = "https://p.scdn.co/mp3-preview/9f4815d8344d65b40530646cf140bfab475171d3?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )
        MainActivity.songList.add(
            Song(
                songName = "Walk This Way",
                songId = "5SZ6zX4rOrEQferfFC2MfP",
                artistName = "Aerosmith",
                key = 5,
                bpm = 108.705,
                danceability = 0.329,
                energy = 0.73,
                valence = 0.894,
                image_url = "https://i.scdn.co/image/ab67616d0000b2739662c6535fb4bf5767e50f32",
                preview_url = "https://p.scdn.co/mp3-preview/f744fafea7f807dd79503249f4238a21393daf8f?cid=7a03c05c75c04958926b5213cda242f3"
            )
        )

    }
    */

}