package io.github.gary.eightysixpics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

class PostImageAdapter(private val images: Array<String>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_post_image, parent, false)
        val image: ImageView = view.findViewById(R.id.image)
        Picasso.get().load(images[position]).into(image)
        return view
    }

    override fun getItem(position: Int) = null

    override fun getItemId(position: Int) = 0L

    override fun getCount() = images.size
}