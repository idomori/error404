package edu.umich.error404.kotlinchatter

class Song(
            var songName: String? = null,
            var songId: String? = null,
            var artistName: String? = null,
            var bpm: Int? = null,
            var key: Int? = null,
            var danceability: Double? = null,
            var valance: Double? = null,
            var energy: Double? = null
            ) {
    companion object {
        const val nFields = 8
    }
}