package io.github.gary.eightysixpics

import org.json.JSONObject
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
            val data = JSONObject(connection.inputStream.bufferedReader().readText())
            println(data)
        } else {
            println("${connection?.responseMessage}")
        }
    }
}