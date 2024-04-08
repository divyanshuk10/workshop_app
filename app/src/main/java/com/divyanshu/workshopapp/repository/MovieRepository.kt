package com.divyanshu.workshopapp.repository

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.divyanshu.workshopapp.model.Content
import com.divyanshu.workshopapp.model.MovieData
import com.divyanshu.workshopapp.utils.Utils
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MovieRepository @Inject constructor(private val applicationContext: Context) :
    PagingSource<Int, Content>() {


    override fun getRefreshKey(state: PagingState<Int, Content>): Int? = null

    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Content> = try {
        val currentPage = params.key ?: 1

        val response = Utils.readJsonFromAssets(
            applicationContext,
            "CONTENTLISTINGPAGE-PAGE${currentPage}.json"
        )

        val dataModel: MovieData? = json.decodeFromString(response ?: "")

        val contentListData = dataModel?.page?.contentItems?.content ?: emptyList()

        LoadResult.Page(
            data = contentListData,
            prevKey = if (currentPage == 1) null else -1,
            nextKey = currentPage.plus(1)
        )

    } catch (e: Exception) {
        LoadResult.Error(e)
    }



}