package io.github.gary.eightysixpics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.github.gary.eightysixpics.databinding.ActivityPostBinding

/**
 * Displays the images contained within a forum post.
 */
class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPostBinding = DataBindingUtil.setContentView(this, R.layout.activity_post)

        binding.textTitle.text = intent.getStringExtra("title")

        val images = intent.getStringArrayExtra("images") as Array<String>
        binding.gridImages.adapter = PostImageAdapter(images)
    }
}