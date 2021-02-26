package io.github.gary.eightysixpics

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object RedditApi {
    /** Reddit URL to get an access token. */
    private const val BASE_TOKEN_URL = "https://www.reddit.com/api/v1/access_token"

    /** Grant type for application-only authentication. */
    private const val GRANT_TYPE = "https://oauth.reddit.com/grants/installed_client"

    /** Randomly generated UUID to identify the user's device. */
    private val DEVICE_ID = UUID.randomUUID().toString()

    /** ID of the app assigned by Reddit. */
    private const val CLIENT_ID = "fUnN03g91GD3yA:"

    /** A token that allows us to create Reddit API requests. */
    private var accessToken = ""

    /**
     * Generates and saves a temporary access token from Reddit.
     * If an error occurs, the token will be set to an empty String.
     */
    fun retrieveToken() {
        val tokenUrl = URL("$BASE_TOKEN_URL?grant_type=$GRANT_TYPE&device_id=$DEVICE_ID&scope=read")
        val connection = tokenUrl.openConnection() as? HttpURLConnection
        accessToken = connection?.run {
            requestMethod = "POST"
            setRequestProperty(
                "Authorization",
                "Basic ${Base64.getEncoder().encodeToString(CLIENT_ID.toByteArray())}"
            )
            setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
            )
            val data = JSONObject(inputStream.bufferedReader().use(BufferedReader::readText))
            data.getString("access_token")
        } ?: ""
        // todo: remove
        Log.d("RedditApi", "token: $accessToken")
    }

    /**
     * Creates a list of [Post] items by parsing the Reddit JSON.
     */
    fun loadPosts(): List<Post> {
        if (accessToken.isEmpty()) {
            return emptyList()
        }
        val posts = mutableListOf<Post>()
        val url = URL("https://oauth.reddit.com/r/ft86/hot.json?raw_json=1")
        val connection = url.openConnection() as? HttpURLConnection
        if (connection != null) {
            connection.setRequestProperty("Authorization", "Bearer $accessToken")
            val data = connection.inputStream.bufferedReader().use(BufferedReader::readText)
            val json = JSONObject(data)
            val children = json.getJSONObject("data").getJSONArray("children")
            for (i in 0 until children.length()) {
                val post = children[i] as JSONObject
                // todo: remove
                Log.d("RedditApi", post.toString())
                // t3 is the prefix used by Reddit for objects of type "Link".
                if (post.getString("kind") != "t3") {
                    continue
                }
                val data = post.getJSONObject("data")
                val thumbnail = data.getString("thumbnail")
                // If a thumbnail does not exist, then there are no images in the forum post.
                if (!thumbnail.endsWith(".jpg")) {
                    continue
                }
                posts.add(
                    Post(
                        data.getString("thumbnail"),
                        data.getString("author"),
                        data.getString("title"),
                        loadImages(data),
                        data.getInt("score"),
                        data.getInt("total_awards_received"),
                        data.getLong("created_utc")
                    )
                )
            }
        }
        return posts
    }

    /**
     * Returns a URL for each image in the forum post.
     */
    private fun loadImages(json: JSONObject): List<String> {
        val images = mutableListOf<String>()
        if (json.getString("url").endsWith(".jpg")) {
            // The post is simply a standalone image.
            images.add(json.getString("url"))
        } else if (json.has("is_gallery")) {
            // The post contains a gallery of images.
            val mediaData = json.getJSONObject("media_metadata")
            val galleryItems = json.getJSONObject("gallery_data").getJSONArray("items")
            for (i in 0 until galleryItems.length()) {
                val item = galleryItems[i] as JSONObject
                val media = mediaData.getJSONObject(item.getString("media_id"))
                if (media.getString("status") == "valid" && media.getString("e") == "Image") {
                    val resolutions = media.getJSONArray("p")
                    val image = resolutions.getJSONObject(resolutions.length() - 1).getString("u")
                    images.add(image)
                }
            }
        }
        return images
    }
}