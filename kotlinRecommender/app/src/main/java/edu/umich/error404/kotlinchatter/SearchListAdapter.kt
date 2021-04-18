package edu.umich.error404.kotlinchatter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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


            }
            return listItemViewById.root
        }
}