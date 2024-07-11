package de.devbeyer.podcast_sponsorskipper.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast

class PodcastPagingSource(
    private val backendAPI: BackendAPI,
    private val sources: String,
) : PagingSource<Int, Podcast>() {

    private var nrOfPages = 0

    override fun getRefreshKey(state: PagingState<Int, Podcast>): Int? {
        return state.anchorPosition?.let {
            val page = state.closestPageToPosition(it)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Podcast> {
        return try {
            val curPage = params.key ?: 0
            val response = backendAPI.getPodcasts(curPage)
            nrOfPages += response.podcasts.size
            LoadResult.Page(
                data = response.podcasts,
                nextKey = if (nrOfPages == response.nrOfPages) null else curPage + 1,
                prevKey = null,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}