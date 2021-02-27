package io.github.gary.eightysixpics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.squareup.picasso.Picasso
import io.github.gary.eightysixpics.databinding.ActivityPostBinding

/**
 * Displays a grid of images contained within a forum post.
 */
class PostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityPostBinding = DataBindingUtil.setContentView(this, R.layout.activity_post)

        binding.textTitle.text = intent.getStringExtra("title")

        val images = intent.getStringArrayExtra("images") as Array<String>
        binding.gridImages.adapter = PostGridAdapter(images)
    }

    inner class PostGridAdapter(private val images: Array<String>) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_image, parent, false)
            Picasso.get().load(images[position]).into(view as ImageView)
            return view
        }

        override fun getItem(position: Int) = null

        override fun getItemId(position: Int) = 0L

        override fun getCount() = images.size
    }
}