package io.github.gary.eightysixpics

import android.graphics.BitmapFactory
import androidx.core.graphics.scale
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
    private var accessToken = ""

    fun retrieveToken() {
        val tokenUrl = URL("$BASE_TOKEN_URL?grant_type=$GRANT_TYPE&device_id=$DEVICE_ID&scope=read")
        val connection = tokenUrl.openConnection() as? HttpURLConnection
        if (connection != null) {
            connection.requestMethod = "POST"
            connection.setRequestProperty(
                "Authorization",
                "Basic ${Base64.getEncoder().encodeToString(CLIENT_ID.toByteArray())}"
            )
            connection.setRequestProperty(
                "Content-Type",
                "application/x-www-form-urlencoded"
            )
            val data =
                JSONObject(connection.inputStream.bufferedReader().use(BufferedReader::readText))
            println(data)
            accessToken = data.getString("access_token")
        } else {
            println("${connection?.responseMessage}")
            accessToken = ""
        }
    }

    fun loadPosts(): List<Post> {
        if (accessToken.isNotEmpty()) {
            val url = URL("https://oauth.reddit.com/r/ft86/hot")
            val connection = url.openConnection() as? HttpURLConnection
            if (connection != null) {
                connection.setRequestProperty("Authorization", "Bearer $accessToken")
                val data = connection.inputStream.bufferedReader().use(BufferedReader::readText)
                val json = JSONObject(data)
                val children = json.getJSONObject("data").getJSONArray("children")
                val posts = mutableListOf<Post>()
                for (i in 0 until children.length()) {
                    val post = children[i] as JSONObject
                    if (post.getString("kind") == "t3") {
                        println(post)
                        val postData = post.getJSONObject("data")
                        val thumbnailImage =
                            when (val thumbnail = postData.getString("thumbnail")) {
                                "self", "default" -> continue
                                else -> URL(thumbnail).openConnection().getInputStream().use {
                                    val bitmap = BitmapFactory.decodeStream(it)
                                    bitmap.scale(150, 150)
                                }
                            }
                        posts.add(
                            Post(
                                postData.getString("author"),
                                postData.getString("title"),
                                thumbnailImage,
                                postData.getInt("ups"),
                                postData.getInt("downs"),
                                postData.getInt("total_awards_received"),
                                postData.getInt("num_comments"),
                                postData.getLong("created_utc")
                            )
                        )
                    }
                }
                return posts
            }
        }
        return emptyList()
    }
}