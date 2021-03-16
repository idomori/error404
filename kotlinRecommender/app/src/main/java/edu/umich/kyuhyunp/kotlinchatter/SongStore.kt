package edu.umich.kyuhyunp.kotlinchatter

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONStringer


class SongStore {
    private val serverUrl = "https://159.65.222.2/"
    private var songFeature = JSONObject()

    fun postSong(context: Context, song: Song, completion: () -> Unit) {
        val queue = newRequestQueue(context)

        val jsonObj = mapOf(
            "track_id" to song.song
        )
        // val postRequest = JsonObjectRequest(
        //    serverUrl+"postsong/", JSONObject(jsonObj),
        //        { Log.d("postSong", "song entered!") },
        //        { error -> Log.e("postSong", error.localizedMessage ?: "JsonObjectRequest error") }


        // )




        val postRequest = JsonObjectRequest(serverUrl+"postsong/", JSONObject(jsonObj),

                { response ->
                    val songsReceived = try { response.getJSONArray("songs") } catch (e: JSONException) { JSONArray() }
                    val songEntry = songsReceived[0] as JSONObject
                    songFeature = songEntry
                    getSongInfo(context, SongListAdapter(context, ArrayList())) {}

                },
                { error ->
                    // TODO: Handle error
                    println(error.message)
                }

                /* { response ->
                    val songsReceived = try { response.getJSONArray("songs") } catch (e: JSONException) { JSONArray() }
                    for (i in 0 until songsReceived.length()) {
                        val songEntry = songsReceived[i] as JSONObject

                        songFeature = songEntry

                       /* if (songEntry.length() == Song.nFields) {
                            // chatts.add(Song(
                                   // song = chattEntry[0].toString()))

                        } else {
                            Log.e("getSongs", "Received unexpected number of fields: " + chattEntry.length().toString() + " instead of " + Song.nFields.toString())
                        } */
                    }
                    completion()
                }, { completion() } */

        )

        queue.add(postRequest)
    }

    fun getSongInfo(context: Context, songs: SongListAdapter, completion: () -> Unit) {
       // val queue = newRequestQueue(context)

        /* val getRequest = JsonObjectRequest(serverUrl+"getsongs/", null,
            { response ->
                val chattsReceived = try { response.getJSONArray("songs") } catch (e: JSONException) { JSONArray() }
                for (i in 0 until chattsReceived.length()) {
                    val chattEntry = chattsReceived[i] as JSONArray
                    if (chattEntry.length() == Song.nFields) {
                        chatts.add(Song(
                            song = chattEntry[0].toString()))
                    } else {
                        Log.e("getSongs", "Received unexpected number of fields: " + chattEntry.length().toString() + " instead of " + Song.nFields.toString())
                    }
                }
                completion()
            }, { completion() }
        ) */
        // queue.add(getRequest)
        songs.add(Song(song = songFeature.get("id").toString(),
                       key = songFeature.get("key").toString(),
                       tempo = songFeature.get("tempo").toString(),
                       danceability = songFeature.get("danceability").toString()))
        /*if (chattEntry.length() == Song.nFields) {

        } else {
            Log.e("getSongs", "Received unexpected number of fields: " + chattEntry.length().toString() + " instead of " + Song.nFields.toString())
        }*/
    }
}