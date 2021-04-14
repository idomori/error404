package edu.umich.error404.kotlinchatter

class Song(
            var songName: String? = null,
            var songId: String? = null,
            var artistName: String? = null,
            var bpm: Double? = null,
            var key: Int? = null,
            var danceability: Double? = null,
            var valence: Double? = null,
            var energy: Double? = null,
            var preview_url: String? = null,
            var image_url: String? = null
            ) {
    companion object {
        const val nFields = 10
    }
}