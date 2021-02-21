package io.github.gary.eightysixpics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.github.gary.eightysixpics.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        GlobalScope.launch(Dispatchers.Default) {
            //RedditApi.retrieveToken()
            val posts = RedditApi.loadPosts()

            runOnUiThread {
                binding.postsRecyclerView.adapter = PostAdapter(posts)
            }
        }
    }
}