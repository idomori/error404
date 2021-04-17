package edu.umich.error404.kotlinchatter

class SongPlaylistSearch(
    var image: String? = null,
    var name: String? = null,
    var url: String? = null
){
    companion object {
        const val nFields = 3
    }
}