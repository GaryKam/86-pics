package io.github.gary.eightysixpics

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.github.gary.eightysixpics.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), PostAdapter.PostItemListener {
    private lateinit var posts: List<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        GlobalScope.launch(Dispatchers.Default) {
            RedditApi.retrieveToken()
            posts = RedditApi.loadPosts()

            runOnUiThread {
                binding.postsRecyclerView.adapter = PostAdapter(posts, this@MainActivity)
            }
        }
    }

    override fun onClick(position: Int) {
        val post = posts[position]
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra("title", post.title)
        intent.putExtra("images", post.images.toTypedArray())
        startActivity(intent)
    }
}