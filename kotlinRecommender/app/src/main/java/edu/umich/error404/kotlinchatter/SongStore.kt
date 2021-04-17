package edu.umich.error404.kotlinchatter

import android.content.Context
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class SongStore{
    private val serverUrl = "https://159.65.222.2/"
    private var songFeature = JSONObject()

    fun readPlaylist(context: Context, url: String, completion: () -> Unit) {
        val queue = newRequestQueue(context)
        val temp = serverUrl+"read_playlist/?playlist_id="+url
        val getRequest = JsonObjectRequest(temp, null,
            { response ->
                val songsReceived = try {
                    response.getJSONArray("result")
                } catch (e: JSONException) {
                    JSONArray()
                }
                for (i in 0 until songsReceived.length()) {
                    val songEntry = songsReceived[i] as JSONObject
                    songFeature = songEntry
                    getSongInfo(context) {}
                }

                // val i = Intent(context, RecommendationActivity::class.java)
                // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // context.startActivity(i)
                completion()
            },
            { error ->
                // TODO: Handle error
                println(error.message)
            }
        )
        queue.add(getRequest)
    }

    fun readSong(context: Context, url: String, completion: () -> Unit) {
        val queue = newRequestQueue(context)
        val temp = serverUrl+"getsong/?track_id="+url
        val getRequest = JsonObjectRequest(temp, null,
            { response ->
                val songsReceived = try {
                    response.getJSONArray("result")
                } catch (e: JSONException) {
                    JSONArray()
                }
                for (i in 0 until songsReceived.length()) {
                    val songEntry = songsReceived[i] as JSONObject
                    songFeature = songEntry
                    getSongInfo(context) {}
                }
                completion();
            },
            { error ->
                // TODO: Handle error
                println(error.message)
            }
        )

        queue.add(getRequest)
    }

    fun submitSongName(context: Context, songName : String, completion: () -> Unit){
        val queue = newRequestQueue(context)
        //search_playlist/?playlist_name_name=**input**&search_for=track
        val temp = serverUrl+"search_playlist/?search_param=" + songName +  "&search_for=track"

        //val temp = serverUrl+"search_playlist/?playlist_name=**input**&search_for=playlist\n"
        val getRequest = JsonObjectRequest(temp, null,
                { response ->
                    val songsReceived = try {
                        response.getJSONArray("result")
                    } catch (e: JSONException) {
                        JSONArray()
                    }
                    for (i in 0 until songsReceived.length()) {
                        val songEntry = songsReceived[i] as List<String>

                        searchActivity.songNameList.add(
                                SongPlaylistSearch(
                                        image = songEntry[0],
                                        name = songEntry[1],
                                        url = songEntry[2]
                                )
                        )
                    }
                    completion();
                },
                { error ->
                    // TODO: Handle error
                    println(error.message)
                }
        )

        queue.add(getRequest)
    }

    fun submitPlaylistName(context: Context, playListName : String, completion: () -> Unit){
        val queue = newRequestQueue(context)
        //search_playlist/?playlist_name_name=**input**&search_for=track
        val temp = serverUrl+"search_playlist/?search_param=" + playListName +  "&search_for=playlist"

        val getRequest = JsonObjectRequest(temp, null,
                { response ->
                    val songsReceived = try {
                        response.getJSONArray("result")
                    } catch (e: JSONException) {
                        JSONArray()
                    }
                    for (i in 0 until songsReceived.length()) {
                        val songEntry = songsReceived[i] as List<String>

                        searchActivity.songNameList.add(
                                SongPlaylistSearch(
                                        image = songEntry[0],
                                        name = songEntry[1],
                                        url = songEntry[2]
                                )
                        )
                    }
                    completion();
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

        val postRequest = JsonObjectRequest(serverUrl + "postsong/", JSONObject(jsonObj),

            { response ->
                val songsReceived = try {
                    response.getJSONArray("songs")
                } catch (e: JSONException) {
                    JSONArray()
                }
                val songEntry = songsReceived[0] as JSONObject
                songFeature = songEntry
                MainActivity.songList.add(
                    Song(
                        songName = songFeature.get("name").toString(),
                        artistName = ((songFeature.get("artists") as JSONArray).get(0) as JSONObject).get(
                            "name"
                        ).toString(),
                        key = songFeature.get("key").toString().toInt(),
                        bpm = songFeature.get("tempo").toString().toDouble(),
                        danceability = songFeature.get("danceability").toString().toDouble()
                    )
                )
            },
            { error ->
                // TODO: Handle error
                println(error.message)
            }

        )

        queue.add(postRequest)
    }

    fun getSongInfo(context: Context, completion: () -> Unit) {
        MainActivity.songList.add(
            Song(
                songName = songFeature.get("track_name").toString(),
                songId = songFeature.get("track_id").toString(),
                artistName = songFeature.get("artist").toString(),
                key = songFeature.get("key").toString().toIntOrNull(),
                bpm = songFeature.get("tempo").toString().toDoubleOrNull(),
                danceability = songFeature.get("danceability").toString().toDoubleOrNull(),
                energy = songFeature.get("energy").toString().toDoubleOrNull(),
                valence = songFeature.get("valence").toString().toDoubleOrNull(),
                image_url = songFeature.get("image_url").toString(),
                preview_url = songFeature.get("preview_url").toString()
            )
        )
    }



}