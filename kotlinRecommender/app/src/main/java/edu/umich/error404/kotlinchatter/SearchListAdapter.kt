package edu.umich.error404.kotlinchatter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.squareup.picasso.Picasso
import edu.umich.error404.kotlinchatter.databinding.ListitemSearchBinding


class SearchListAdapter(context: Context, users: ArrayList<SongPlaylistSearch?>) :

    ArrayAdapter<SongPlaylistSearch?>(context, 0, users) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val item = getItem(position)

            val listItemViewById = ListitemSearchBinding.bind(
                    convertView ?: LayoutInflater.from(context)
                            .inflate(R.layout.listitem_search, parent, false)
            )


            item?.run {

                listItemViewById.nameTextView.text = name
                listItemViewById.urlTextView.text = url
                Picasso.get().load(image).into(listItemViewById.songImage)

                listItemViewById.root.setOnClickListener {
                    val store = SongStore()
                    url?.let{
                        if (it.contains("playlist", ignoreCase = true)) {
                            store.readPlaylist(context,it) {
                                val intent = Intent(context, RecommendationActivity::class.java)
                                if(MainActivity.songList.isEmpty()) {
                                    val toast = Toast.makeText(
                                            context.applicationContext,
                                            "Sorry. We can not find any related song. Please select a new item.",
                                            Toast.LENGTH_SHORT
                                    )
                                    toast.setGravity(Gravity.CENTER, 0, 0)
                                    toast.show()
                                }
                                else {
                                    context.startActivity(intent)
                                }
                            }
                        } else {
                            store.readSong(context, it) {
                                val intent = Intent(context, RecommendationActivity::class.java)
                                if(MainActivity.songList.isEmpty()) {
                                    val toast = Toast.makeText(
                                            context.applicationContext,
                                            "Sorry. We can not find any related song. Please select a new item.",
                                            Toast.LENGTH_SHORT
                                    )
                                    toast.setGravity(Gravity.CENTER, 0, 0)
                                    toast.show()
                                }
                                else {
                                    context.startActivity(intent)
                                }
                            }

                        }

                    }
                }



            }
            return listItemViewById.root
        }
}