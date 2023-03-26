package com.neotica.repositoryinjection.ui

import androidx.lifecycle.ViewModel
import com.neotica.repositoryinjection.data.local.entity.NewsEntity
import com.neotica.repositoryinjection.data.local.room.NewsRepository

//Step 8: Create NewsViewModel
//Step 8.1 Set NewsRepository as the parameter and extend to ViewModel
class NewsViewModel(private val newsRepository: NewsRepository) :ViewModel() {
    //Step 8.2 Create function that get the getHeadlineNews from repository
    fun getHeadlineNews() = newsRepository.getHeadlineNews()

    //Step 14: Add bookmark functions to viewmodel as well.
    //Step 14.1: Function to get bookmark
    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()
    //Step 14.2: Function to save(set) bookmark
    fun saveNews(news: NewsEntity) {
        newsRepository.setBookmarkedNews(news, true)
    }
    //Step 14.3: Function to delete bookmark
    fun deleteNews(news: NewsEntity) {
        newsRepository.setBookmarkedNews(news, false)
    }
}