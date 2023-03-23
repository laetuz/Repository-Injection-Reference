package com.neotica.repositoryinjection.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.neotica.repositoryinjection.data.local.room.NewsRepository

//Step 9: Create ViewModelFactory class
//Step 9.1: Create a private constructor parameter to NewsRepository
class ViewModelFactory private constructor(private val newsRepository: NewsRepository):
//Step 9.2: Extend to NewInstanceFactory from ViewModelProvider
    ViewModelProvider.NewInstanceFactory() {
    //Step 9.3: Suppress names as UNCHECKED CAST
    @Suppress("UNCHECKED_CAST")
    //Step 9.4: Override the create method in the ViewModelFactory class.
    //responsible for creating an instance of a specified ViewModel class, based on the given modelClass parameter.
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    //Step 10 Create a companion object to obtain instance from this class
    companion object {
        @Volatile
        //Step 10.1 Create a nullable instance for this class
        private var instance: ViewModelFactory? = null
        //Step 10.2 Create a new function that returns to this class
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(NewsRepository.Injection.provideRepository(context))
            }.also { instance = it }
    }
}