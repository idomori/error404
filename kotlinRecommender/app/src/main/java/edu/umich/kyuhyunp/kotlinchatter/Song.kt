package edu.umich.kyuhyunp.kotlinchatter

class Song(
            var song: String? = null,
            var key: String? = null,
            var tempo: String? = null,
            var danceability: String? = null) {
    companion object {
        const val nFields = 4
    }
}