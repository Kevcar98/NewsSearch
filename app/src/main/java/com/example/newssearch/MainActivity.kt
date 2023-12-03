// Import necessary packages and classes
package com.example.newssearch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import java.util.*

// Define the main activity class and implement the TextToSpeech OnInitListener interface
class MainActivity : AppCompatActivity(), OnInitListener {

    // Declare lateinit variables for RecyclerView and TextToSpeech
    private lateinit var recyclerView: RecyclerView
    private lateinit var textToSpeech: TextToSpeech

    // Override the onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Add a divider between news items
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Initialize TextToSpeech
        textToSpeech = TextToSpeech(this, this)

        // Set language and API key
        val language = "en"
        val apiKey = "710119f4520a4c25b4ab12e46322e7db"

        // Get references to UI elements
        val TUserSearchText = findViewById<EditText>(R.id.UserInputTitleSearch)
        val TUserSearchDate = findViewById<EditText>(R.id.UserInputDateSearch)
        val searchButton = findViewById<Button>(R.id.button)

        // Set onClickListener for the search button
        searchButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Get user input for search text and date
                val userSearchText = TUserSearchText.text.toString()
                val userSearchDate = TUserSearchDate.text.toString()

                // Make API request
                searchNews(userSearchText, userSearchDate, userSearchDate, language, apiKey)
            }
        })
    }

    // Function to make a Retrofit API request for news
    private fun searchNews(
        query: String,
        date: String,
        date1: String,
        language: String,
        apiKey: String
    ) {
        // Build Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create NewsApiService instance
        val service = retrofit.create(NewsApiService::class.java)
        val call = service.getNews(query, date, date1, language, apiKey)

        // Enqueue the API call
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                // Handle successful API response
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    if (articles != null) {
                        // Display the results in RecyclerView
                        displayNews(articles)
                    }
                } else {
                    // Display an error message
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Display an error message for API call failure
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to use TextToSpeech to read out the news article description
    private fun speakOut(description: String) {
        if (textToSpeech.isLanguageAvailable(Locale.US) != TextToSpeech.LANG_MISSING_DATA &&
            textToSpeech.isLanguageAvailable(Locale.US) != TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            // Speak out the description
            val text = description
            val params = HashMap<String, String>()
            params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "messageId"
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params)
        } else {
            // Handle language not supported or missing data
        }
    }

    // Override onDestroy to stop and shutdown TextToSpeech when the activity is destroyed
    override fun onDestroy() {
        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
        textToSpeech.shutdown()
        super.onDestroy()
    }

    // Function to display news articles in RecyclerView
    private fun displayNews(articles: List<NewsArticle>) {
        val adapter = NewsAdapter(articles, object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, article: NewsArticle) {
                // Handle item click (e.g., display details in a Toast)
                Toast.makeText(
                    this@MainActivity,
                    "Clicked on: ${article.title}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onWebViewClick(position: Int, article: NewsArticle) {
                // Handle WebView button click (open article in a web view)
                val intent = Intent(this@MainActivity, ArticleWebViewActivity::class.java)
                intent.putExtra(
                    "articleUrl",
                    article.url
                )
                startActivity(intent)
            }

            override fun onReadDescClick(position: Int, article: NewsArticle) {
                // Read out the article description using TextToSpeech
                Log.d("TAG", "Description=${article.description}")
                val descriptionMSG = if (!article.description.isNullOrEmpty()) {
                    "$article.description"
                } else {
                    "Description not available for ${article.title}"
                }
                speakOut(descriptionMSG)
            }
        })
        recyclerView.adapter = adapter
    }

    // Implement OnInitListener method for TextToSpeech initialization
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                // Handle language not supported or missing data
            }
        } else {
            // Handle initialization failure
        }
    }
}
