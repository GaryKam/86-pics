package io.github.gary.eightysixpics

/**
 * Information regarding a single post in a Reddit subforum.
 */
data class Post(
    val thumbnail: String,
    val author: String,
    val title: String,
    val images: List<String>,
    val score: Int,
    val awards: Int,
    val dateCreated: Long
)