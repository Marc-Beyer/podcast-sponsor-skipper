package de.devbeyer.podcast_sponsorskipper.data.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.data.remote.BackendAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.PodcastPagingSource
import de.devbeyer.podcast_sponsorskipper.data.remote.RSSAPI
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.repositories.PodcastRepository
import de.devbeyer.podcast_sponsorskipper.util.getCurrentISO8601Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class PodcastRepositoryImpl(
    private val backendAPI: BackendAPI,
    private val rssAPI: RSSAPI,
): PodcastRepository {
    override fun getPodcasts(search: String): Flow<PagingData<PodcastWithRelations>> {
        return Pager(
            config = PagingConfig(pageSize = 12),
            pagingSourceFactory = {
                PodcastPagingSource(backendAPI, search)
            }
        ).flow
    }

    override fun searchPodcasts(search: String): Flow<PagingData<PodcastWithRelations>> {
        return Pager(
            config = PagingConfig(pageSize = 12),
            pagingSourceFactory = {
                PodcastPagingSource(backendAPI, search)
            }
        ).flow
    }

    override fun getRSSFeed(rssUrl: String): Flow<PodcastWithRelations> = flow {
        val response = rssAPI.getRSSFeed(rssUrl)

        if (response.isSuccessful) {
            val rawResponse = response.body()
            Log.i("AAA","Raw Response: $rawResponse")
            val rssFeed = parseRSS(url = rssUrl, xml = rawResponse ?: "")
            emit(rssFeed)
        } else {
            throw Exception("Failed to fetch RSS feed: ${response.message()}")
        }
    }

    private fun parseRSS(url: String, xml: String): PodcastWithRelations {
        var title = ""
        var description = ""
        var link = ""
        var language = ""
        var copyright = ""
        var imageUrl = ""
        var explicit = true
        var locked = false
        var complete = false
        var author = ""
        var fundingText = ""
        var fundingUrl = ""
        var nrOdEpisodes = 0

        val categories: MutableSet<String> = mutableSetOf()
        val episodes = mutableListOf<Episode>()

        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document: Document = builder.parse(InputSource(StringReader(xml)))

        val channelNode = document.getElementsByTagName("channel").item(0)
        val childNodes = channelNode?.childNodes
        for (i in 0 until (childNodes?.length ?: 0)) {
            val node = childNodes?.item(i)
            when (node?.nodeName) {
                "title" -> title = node.textContent
                "description" -> description = node.textContent
                "link" -> link = node.textContent
                "copyright" -> copyright = node.textContent
                "language" -> language = node.textContent
                "itunes:explicit" -> explicit = node.textContent != "false"
                "podcast:locked" -> locked = node.textContent != "false"
                "itunes:complete" -> complete = node.textContent != "false"
                "itunes:author" -> author = node.textContent
                "podcast:funding" -> {
                    fundingText = node.textContent
                    if (node is Element) {
                        fundingUrl = node.getAttribute("url")
                    }
                }
                "itunes:image" -> {
                    if (node is Element) {
                        imageUrl = node.getAttribute("href")
                    }
                }
                "itunes:category" -> {
                    if (node is Element) {
                        categories.add(node.getAttribute("text"))
                    }
                }
                "item" -> {
                    nrOdEpisodes++;
                    //episodes.add(Episode.parseXml(url, node){})
                }
            }
        }

        Log.i("AAA", categories.joinToString(", "))

        //return PodcastAndEpisodes(podcastWithRelations =
        return PodcastWithRelations(
                podcast = Podcast(
                    url = url,
                    title = title,
                    description = description,
                    link = link,
                    language = language,
                    imageUrl = imageUrl,
                    explicit = explicit,
                    locked = locked,
                    complete = complete,
                    lastUpdate = getCurrentISO8601Time(),
                    nrOdEpisodes = nrOdEpisodes,
                    copyright = copyright,
                    author = author,
                    fundingText = fundingText,
                    fundingUrl = fundingUrl,
                    imagePath = null,
                ),
                categories = categories.map { Category(name = it) },
            )//,
        //    episodes = episodes,
        //)
    }
}