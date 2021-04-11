package edu.umich.kyuhyunp.kotlinchatter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import edu.umich.kyuhyunp.kotlinchatter.databinding.ListitemChattBinding

class SongListAdapter(context: Context, users: ArrayList<Song?>) :
    ArrayAdapter<Song?>(context, 0, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)

        val listItemViewById = ListitemChattBinding.bind(convertView ?:
        LayoutInflater.from(context).inflate(R.layout.listitem_chatt, parent, false))

        item?.run {
            listItemViewById.songTextView.text = key

        }
        return listItemViewById.root
    }
}