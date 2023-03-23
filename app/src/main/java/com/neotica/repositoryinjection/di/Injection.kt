package com.neotica.repositoryinjection.di

import android.content.Context
import com.neotica.repositoryinjection.data.local.room.NewsDatabase
import com.neotica.repositoryinjection.data.local.room.NewsRepository
import com.neotica.repositoryinjection.data.remote.retrofit.ApiConfig
import com.neotica.repositoryinjection.utils.AppExecutors

//Step 7: Create object for Injection
object Injection {
    //Step 7.1: Create a function that returns to NewsRepository
    // with context as a parameter
    fun provideRepository(context: Context): NewsRepository {
        //Step 7.2: Create variable that contains Service, database, dao, and appExecutors
        val apiService = ApiConfig.getApiService()
        //Step 7.3: Provide context to database
        val database = NewsDatabase.getInstance(context)
        val dao = database.newsDao()
        val appExecutors = AppExecutors()
        //Step 7.4: Returns instances from apiService, dao, and appExecutors
        return NewsRepository.getInstance(apiService, dao, appExecutors)
    }
}