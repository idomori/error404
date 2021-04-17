package edu.umich.error404.kotlinchatter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.squareup.picasso.Picasso
import edu.umich.error404.kotlinchatter.databinding.ListitemChattBinding

class SongListAdapter(context: Context, users: ArrayList<Song?>) :
    ArrayAdapter<Song?>(context, 0, users) {
    private var curSongId:String = ""
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)

        val listItemViewById = ListitemChattBinding.bind(
            convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.listitem_chatt, parent, false)
        )


        item?.run {
            curSongId = songId.toString()
            listItemViewById.songTextView.text = songName
            listItemViewById.artistTextView.text = artistName
            Picasso.get().load(image_url).into(listItemViewById.albumImageView)

            listItemViewById.spotifyBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("spotify:track:" + curSongId)
                intent.putExtra(
                    Intent.EXTRA_REFERRER,
                    Uri.parse("android://" + BuildConfig.APPLICATION_ID)
                )
                context.startActivity(intent)
            }
        }
        return listItemViewById.root
    }

}