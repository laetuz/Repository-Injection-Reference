package com.neotica.repositoryinjection.ui

import androidx.lifecycle.ViewModel
import com.neotica.repositoryinjection.data.local.room.NewsRepository

//Step 8: Create NewsViewModel
//Step 8.1 Set NewsRepository as the parameter and extend to ViewModel
class NewsViewModel(private val newsRepository: NewsRepository) :ViewModel() {
    //Step 8.2 Create function that get the getHeadlineNews from repository
    fun getHeadlineNews() = newsRepository.getHeadlineNews()
}