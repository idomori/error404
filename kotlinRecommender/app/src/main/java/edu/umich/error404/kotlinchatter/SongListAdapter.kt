package edu.umich.error404.kotlinchatter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import edu.umich.error404.kotlinchatter.databinding.ListitemChattBinding

class SongListAdapter(context: Context, users: ArrayList<Song?>) :
    ArrayAdapter<Song?>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)

        val listItemViewById = ListitemChattBinding.bind(convertView ?:
        LayoutInflater.from(context).inflate(R.layout.listitem_chatt, parent, false))


        item?.run {
            listItemViewById.songTextView.text = song
            listItemViewById.artistTextView.text = artist
            listItemViewById.keyTextView.text = "Key = " + key
            listItemViewById.tempoTextView.text = "Tempo = " + tempo
            listItemViewById.danceabilityTextView.text = "Danceability = " + danceability
           // Picasso.get().load(image_url).into(listItemViewById.albumImageView)
        }
        return listItemViewById.root
    }
}