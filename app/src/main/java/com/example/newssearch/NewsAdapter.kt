// Import necessary packages and classes
package com.example.newssearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Define the NewsAdapter class as a subclass of RecyclerView.Adapter
class NewsAdapter(private val articles: List<NewsArticle>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    // Inner class representing a ViewHolder for each item in the RecyclerView
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val buttonWebView: Button = itemView.findViewById(R.id.buttonWebView)
        val buttonReadDesc: Button = itemView.findViewById(R.id.buttonReadDesc)

        // Initialize the ViewHolder
        init {
            // Set click listeners for item click, WebView button click, and Read Description button click
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position, articles[position])
                }
            }
            buttonReadDesc.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onReadDescClick(position, articles[position])
                }
            }

            buttonWebView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onWebViewClick(position, articles[position])
                }
            }
        }

        // Bind the data to the ViewHolder
        fun bind(article: NewsArticle) {
            titleTextView.text = article.title
        }
    }

    // Interface for handling item click events
    interface OnItemClickListener {
        fun onItemClick(position: Int, article: NewsArticle)
        fun onWebViewClick(position: Int, article: NewsArticle)
        fun onReadDescClick(position: Int, article: NewsArticle)
    }

    // Create ViewHolder instances
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each item in the RecyclerView
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(itemView)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    // Return the number of items in the RecyclerView
    override fun getItemCount(): Int {
        return articles.size
    }
}
