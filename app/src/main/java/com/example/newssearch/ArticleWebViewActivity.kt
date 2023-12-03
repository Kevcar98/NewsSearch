// Import necessary packages and classes
package com.example.newssearch

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

// Define the ArticleWebViewActivity class as a subclass of AppCompatActivity
class ArticleWebViewActivity : AppCompatActivity() {

    // Override the onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout for the activity
        setContentView(R.layout.activity_article_web_view)

        // Find the WebView component in the layout
        val webView: WebView = findViewById(R.id.webView)

        // Get the article URL from the intent
        val articleUrl = intent.getStringExtra("articleUrl")

        // Check if the article URL is not empty or null
        if (!articleUrl.isNullOrEmpty()) {
            // Enable JavaScript in the WebView settings
            webView.settings.javaScriptEnabled = true

            // Set a WebViewClient to handle navigation within the WebView
            webView.webViewClient = WebViewClient()

            // Load the article URL in the WebView
            webView.loadUrl(articleUrl)
        }
    }
}
