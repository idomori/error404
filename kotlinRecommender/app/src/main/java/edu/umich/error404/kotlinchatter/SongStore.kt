package edu.umich.error404.kotlinchatter

import android.content.Context
import android.view.LayoutInflater
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import edu.umich.error404.kotlinchatter.databinding.ListitemChattBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class SongStore {
    private val serverUrl = "https://159.65.222.2/"
    private var songFeature = JSONObject()

    fun postSong(context: Context, song: Song, completion: () -> Unit) {
        val queue = newRequestQueue(context)

        val jsonObj = mapOf(
            "track_id" to song.song
        )

        val postRequest = JsonObjectRequest(serverUrl+"postsong/", JSONObject(jsonObj),

                { response ->
                    val songsReceived = try { response.getJSONArray("songs") } catch (e: JSONException) { JSONArray() }
                    val songEntry = songsReceived[0] as JSONObject
                    songFeature = songEntry
                    getSongInfo(context) {}
                },
                { error ->
                    // TODO: Handle error
                    println(error.message)
                }

        )

        queue.add(postRequest)
    }

    fun getSongInfo(context: Context, completion: () -> Unit) {

        MainActivity.songsList.add(Song(song = songFeature.get("name").toString(),
                       artist = ((songFeature.get("artists") as JSONArray).get(0) as JSONObject).get("name").toString(),
                       key = songFeature.get("key").toString(),
                       tempo = songFeature.get("tempo").toString(),
                       danceability = songFeature.get("danceability").toString()))

    }

}