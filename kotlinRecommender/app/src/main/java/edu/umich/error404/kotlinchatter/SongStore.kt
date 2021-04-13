package edu.umich.error404.kotlinchatter

import android.content.Context
import android.view.LayoutInflater
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import edu.umich.error404.kotlinchatter.databinding.ListitemChattBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class SongStore {
    private val serverUrl = "https://159.65.222.2/"
    private var songFeature = JSONObject()

    fun readPlaylist(context: Context, url: String, completion: () -> Unit) {
        val queue = newRequestQueue(context)
        val temp = serverUrl+"read_playlist/?playlist_id="+url
        val getRequest = JsonObjectRequest(temp, null,
                { response ->
                    val songsReceived = try { response.getJSONArray("result") } catch (e: JSONException) { JSONArray() }
                    val songEntry = songsReceived[0] as JSONObject
                    songFeature = songEntry
                    getSongInfo(context) {}
                },
                { error ->
                    // TODO: Handle error
                    println(error.message)
                }
        )
        queue.add(getRequest)
//        val url = URL(serverUrl+"read_playlist?"+"track_id="+url)
//
//        with(url.openConnection() as HttpURLConnection) {
//            requestMethod = "GET"  // optional default is GET
//
//            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")
//
//            inputStream.bufferedReader().use {
//                it.lines().forEach { line ->
//                    println(line)
//                }
//            }
//        }
    }

    fun readSong(context: Context, url: String, completion: () -> Unit) {
        val queue = newRequestQueue(context)
        val jsonObj = mapOf(
                "track_id" to url
        )
        val getRequest = JsonObjectRequest(serverUrl+"getsong", JSONObject(jsonObj),
                { response ->
                    val songsReceived = try { response.getJSONArray("result") } catch (e: JSONException) { JSONArray() }
                    val songEntry = songsReceived[0] as JSONObject
                    songFeature = songEntry
                    getSongInfo(context) {}
                },
                { error ->
                    // TODO: Handle error
                    println(error.message)
                }
        )

        queue.add(getRequest)
    }

    // postsong function is deleted from backend
    fun postSong(context: Context, song: Song, completion: () -> Unit) {
        val queue = newRequestQueue(context)

        val jsonObj = mapOf(
            "track_id" to song.songName
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
        MainActivity.songList.add(Song(songName = songFeature.get("name").toString(),
            songId = songFeature.get("track_id").toString(),
            artistName = songFeature.get("artist").toString(),
            key = songFeature.get("key").toString().toIntOrNull(),
            bpm = songFeature.get("tempo").toString().toIntOrNull(),
            danceability = songFeature.get("danceability").toString().toDoubleOrNull(),
            energy = songFeature.get("energy").toString().toDoubleOrNull(),
            valence = songFeature.get("valence").toString().toDoubleOrNull(),
            image_url = songFeature.get("image_url").toString(),
            preview_url = songFeature.get("preview_url").toString()
        ))
    }

}