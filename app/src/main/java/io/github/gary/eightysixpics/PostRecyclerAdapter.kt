package io.github.gary.eightysixpics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

/**
 * An adapter for the list of forum posts.
 */
class PostRecyclerAdapter(
    private var posts: List<Post>,
    private val postItemListener: PostItemListener
) : RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false),
        )
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        Picasso.get().load(post.thumbnail).fit().centerCrop().into(holder.thumbnailImage)
        holder.titleText.text = post.title
        holder.scoreText.text = post.score.toString()
        holder.awardText.text = post.awards.toString()
        // Hide the award info if the post has not received any awards.
        if (post.awards == 0) {
            holder.awardText.isVisible = false
            holder.awardImage.isVisible = false
        }
    }

    fun updatePosts(updatedPosts: List<Post>) {
        posts = updatedPosts
        notifyDataSetChanged()
    }

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val thumbnailImage: ImageView = view.findViewById(R.id.image_thumbnail)
        val titleText: TextView = view.findViewById(R.id.text_title)
        val scoreText: TextView = view.findViewById(R.id.text_score)
        val awardText: TextView = view.findViewById(R.id.text_award)
        val awardImage: ImageView = view.findViewById(R.id.image_award)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) = postItemListener.onClick(adapterPosition)
    }

    interface PostItemListener {
        fun onClick(position: Int)
    }
}