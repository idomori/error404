package edu.umich.error404.kotlinchatter

class Song(
            var song: String? = null,
            var artist: String? = null,
            var track_id: String? = null,
            var key: String? = null,
            var tempo: String? = null,
            var danceability: String? = null,
            var preview_url: String? = null,
            var image_url: String? = null
            ) {
    companion object {
        const val nFields = 5
    }
}