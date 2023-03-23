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

//Step 2: Create a news Repository class
class NewsRepository private constructor(
    //Step 3: Add private constructors to apiService, newsDao, appExecutors
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val appExecutors: AppExecutors
) {
    //Step 4: Declare MediatorLiveData from NewsEntity
    private val result = MediatorLiveData<com.neotica.repositoryinjection.data.Result<List<NewsEntity>>>()

    //Step 5: Create a function for livedata
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

    //Step 6: Create a companion object containing instance of NewsRepository
    companion object {
        //Step 6.1 Create @Volatile annotation
        @Volatile
        //Step 6.2 Declare NewsRepository as a nullable instance
        private var instance: NewsRepository? = null
        //Step 6.3 Create a function with parameters containing ApiService, Dao
        //and AppExecutors from utils/AppExecutors to access the database from the nbackground
        fun getInstance(
            apiService: ApiService,
            newsDao: NewsDao,
            appExecutors: AppExecutors
        //Step 6.4 Returns instance to NewsRepository
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }
}