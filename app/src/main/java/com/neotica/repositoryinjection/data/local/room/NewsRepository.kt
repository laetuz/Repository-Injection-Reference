package com.neotica.repositoryinjection.data.local.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.neotica.repositoryinjection.BuildConfig
import com.neotica.repositoryinjection.data.local.entity.NewsEntity
import com.neotica.repositoryinjection.data.remote.response.NewsResponse
import com.neotica.repositoryinjection.data.remote.retrofit.ApiService
import com.neotica.repositoryinjection.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//Step 5
/*
class NewsRepository private constructor(
    //Step 6: Add private constructors to apiService, newsDao, appExecutors
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val appExecutors: AppExecutors
){
    //Step 7: declare MediatorLiveData from NewsEntity
    private val result = MediatorLiveData<com.neotica.repositoryinjection.data.Result<List<NewsEntity>>>()

    //Step 8:
    fun getHeadlineNews(): LiveData<Result<List<NewsEntity>>> {
        result.value = com.neotica.repositoryinjection.data.Result.Loading
        val client = apiService.getNews(BuildConfig.)
    }
}*/

class NewsRepository private constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<com.neotica.repositoryinjection.data.Result<List<NewsEntity>>>()

    fun getHeadlineNews(): LiveData<com.neotica.repositoryinjection.data.Result<List<NewsEntity>>> {
        result.value = com.neotica.repositoryinjection.data.Result.Loading
        val client = apiService.getNews(BuildConfig.API_KEY)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    val newsList = ArrayList<NewsEntity>()
                    appExecutors.diskIO.execute {
                        articles?.forEach { article ->
                            val isBookmarked = newsDao.isNewsBookmarked(article.title)
                            val news = NewsEntity(
                                article.title,
                                article.publishedAt,
                                article.urlToImage,
                                article.url,
                                isBookmarked
                            )
                            newsList.add(news)
                        }
                        newsDao.deleteAll()
                        newsDao.insertNews(newsList)
                    }
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                result.value = com.neotica.repositoryinjection.data.Result.Error(t.message.toString())
            }
        })
        val localData = newsDao.getNews()
        result.addSource(localData) { newData: List<NewsEntity> ->
            result.value = com.neotica.repositoryinjection.data.Result.Success(newData)
        }
        return result
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: NewsDao,
            appExecutors: AppExecutors
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }
}