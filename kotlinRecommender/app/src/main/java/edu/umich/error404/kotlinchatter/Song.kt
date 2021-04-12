package edu.umich.error404.kotlinchatter

class Song(
            var song: String? = null,
            var artist: String? = null,
            var key: String? = null,
            var tempo: String? = null,
            var danceability: String? = null) {
    companion object {
        const val nFields = 5
    }
}