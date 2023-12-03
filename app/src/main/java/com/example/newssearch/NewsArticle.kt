package com.example.newssearch

// Define a data class for representing a news article
data class NewsArticle(
    val title: String,         // Title of the news article
    val description: String,   // Description or summary of the news article
    val url: String            // URL of the news article
)

// Define a data class for representing a response from the News API
data class NewsResponse(
    val articles: List<NewsArticle>,   // List of news articles received in the response
)
