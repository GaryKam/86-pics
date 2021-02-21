package io.github.gary.eightysixpics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val posts: List<Post>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.thumbnailImage.setImageBitmap(post.thumbnail)
        holder.titleText.text = post.title
        holder.upvotesText.text = post.upvotes.toString()
    }

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnailImage: ImageView = view.findViewById(R.id.image_thumbnail)
        val titleText: TextView = view.findViewById(R.id.text_title)
        val upvotesText: TextView = view.findViewById(R.id.text_upvotes)
    }
}



