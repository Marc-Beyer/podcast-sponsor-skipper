package de.devbeyer.podcast_sponsorskipper.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations

class PodcastPagingSource(
    private val backendAPI: BackendAPI,
    private val search: String,
) : PagingSource<Int, PodcastWithRelations>() {

    private var nrOfPages = 0

    override fun getRefreshKey(state: PagingState<Int, PodcastWithRelations>): Int? {
        return state.anchorPosition?.let {
            val page = state.closestPageToPosition(it)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PodcastWithRelations> {
        return try {
            val curPage = params.key ?: 0
            val response = backendAPI.searchPodcasts(page = curPage, search = search)
            nrOfPages += response.podcasts.size
            LoadResult.Page(
                data = response.podcasts.map {
                    PodcastWithRelations(
                        podcast = Podcast(
                            id = it.id,
                            url = it.url,
                            title = it.title,
                            description = it.description,
                            link = it.link,
                            language = it.language,
                            imageUrl = it.imageUrl,
                            explicit = it.explicit,
                            locked = it.locked,
                            complete = it.complete,
                            lastUpdate = it.lastUpdate,
                            nrOdEpisodes = it.nrOdEpisodes,
                            copyright = it.copyright,
                            author = it.author,
                            fundingText = it.fundingText,
                            fundingUrl = it.fundingUrl,
                            imagePath = null,
                        ),
                        categories = it.categories
                    )
                },
                nextKey = if (nrOfPages == response.nrOfPages) null else curPage + 1,
                prevKey = null,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}