package io.github.gary.eightysixpics

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.databinding.DataBindingUtil
import io.github.gary.eightysixpics.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

/**
 * Displays rows of forum posts.
 * Clicking a post will navigate to a [PostActivity].
 */
class MainActivity : AppCompatActivity(), PostRecyclerAdapter.PostItemListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var posts: List<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        GlobalScope.launch(Dispatchers.Default) {
            RedditApi.retrieveToken()
            posts = RedditApi.loadPosts()

            runOnUiThread {
                binding.postsRecyclerView.adapter = PostRecyclerAdapter(posts, this@MainActivity)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Allows menu option icons to be visible.
        (menu as? MenuBuilder)?.setOptionalIconsVisible(true)
        menuInflater.inflate(R.menu.menu_sort_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        GlobalScope.launch(Dispatchers.Default) {
            val posts = when (item.itemId) {
                R.id.menu_item_hot, R.id.menu_item_new, R.id.menu_item_top, R.id.menu_item_rising ->
                    RedditApi.loadPosts(item.title.toString().toLowerCase(Locale.ROOT))
                else -> emptyList()
            }
            if (posts.isNotEmpty()) {
                runOnUiThread {
                    (binding.postsRecyclerView.adapter as? PostRecyclerAdapter)?.updatePosts(posts)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(position: Int) {
        val post = posts[position]
        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra("title", post.title)
        intent.putExtra("images", post.images.toTypedArray())
        startActivity(intent)
    }
}