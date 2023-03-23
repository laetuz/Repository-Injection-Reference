package com.neotica.repositoryinjection.ui

import androidx.lifecycle.ViewModel
import com.neotica.repositoryinjection.data.local.room.NewsRepository

//Step 2: Create NewsViewModel
//Step 2.1 Set NewsRepository as the parameter and extend to ViewModel
class NewsViewModel(private val newsRepository: NewsRepository) :ViewModel() {
    //Step 2.2 Create function that get the getHeadlineNews from repository
    fun getHeadlineNews() = newsRepository.getHeadlineNews()
}