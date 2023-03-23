package com.neotica.repositoryinjection.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neotica.repositoryinjection.data.local.room.NewsRepository

//Step 3: Create ViewModelFactory class
//Step 3.1: Create a private constructor parameter to NewsRepository
class ViewModelFactory private constructor(private val newsRepository: NewsRepository):
//Step 3.2: Extend to NewInstanceFactory from ViewModelProvider
    ViewModelProvider.NewInstanceFactory() {
    //Step 3.3: Supress
    @Suppress("UNCHECKED_CAST")
    //Step 3.4: Override fun
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
    }
}