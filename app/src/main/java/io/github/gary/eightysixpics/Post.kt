package io.github.gary.eightysixpics

import android.graphics.Bitmap

data class Post(
    val author: String,
    val title: String,
    val thumbnail: Bitmap?,
    val upvotes: Int,
    val downvotes: Int,
    val awards: Int,
    val comments: Int,
    val dateCreated: Long
)