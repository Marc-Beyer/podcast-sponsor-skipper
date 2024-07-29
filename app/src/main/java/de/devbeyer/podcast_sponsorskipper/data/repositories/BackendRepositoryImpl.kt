package de.devbeyer.podcast_sponsorskipper.data.repositories

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import de.devbeyer.podcast_sponsorskipper.data.remote.BackendAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.PodcastPagingSource
import de.devbeyer.podcast_sponsorskipper.data.remote.RSSAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.dto.SubmitSponsorSectionBody
import de.devbeyer.podcast_sponsorskipper.domain.models.UserData
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Category
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastAndEpisodes
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import de.devbeyer.podcast_sponsorskipper.util.getCurrentISO8601Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.parsers.DocumentBuilderFactory

class BackendRepositoryImpl(
    private val backendAPI: BackendAPI,
    private val rssAPI: RSSAPI,
) : BackendRepository {
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

    override fun getRSSFeed(rssUrl: String): Flow<PodcastAndEpisodes?> = flow {
        val response = rssAPI.getRSSFeed(rssUrl)

        if (response.isSuccessful) {
            val rawResponse = response.body()
            val rssFeed = parseRSS(url = rssUrl, xml = rawResponse ?: "")
            emit(rssFeed)
        } else {
            emit(null)
        }
    }

    override fun submitSponsorSection(
        episodeUrl: String,
        podcastUrl: String,
        startPosition: Long,
        endPosition: Long,
        username: String,
        token: String
    ): Flow<Boolean> = flow {
        val response = backendAPI.submitSponsorSection(
            SubmitSponsorSectionBody(
                episodeUrl,
                podcastUrl,
                startPosition,
                endPosition,
                username,
                token
            )
        )
        Log.i("AAA", "submitSponsorSection REPO $episodeUrl $username $token isSuccessful ${response.isSuccessful}")
        emit(response.isSuccessful)
    }

    override fun register(): Flow<UserData?> = flow  {
        val response = backendAPI.register()

        if (response.isSuccessful) {
            val userData = response.body()
            emit(userData)
        } else {
            emit(null)
        }
    }

    private fun parseRSS(url: String, xml: String): PodcastAndEpisodes {
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
                    episodes.add(parseRSSItem(node))
                }
            }
        }

        Log.i("AAA", categories.joinToString(", "))

        return PodcastAndEpisodes(
            podcastWithRelations = PodcastWithRelations(
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
                    nrOdEpisodes = episodes.size,
                    copyright = copyright,
                    author = author,
                    fundingText = fundingText,
                    fundingUrl = fundingUrl,
                    imagePath = null,
                ),
                categories = categories.map { Category(name = it) },
            ),
            episodes = episodes,
        )
    }

    private fun parseRSSItem(node: Node): Episode {
        var episodeUrl = ""
        var episodeLength = -1
        var episodeType = ""
        var title = ""
        var guid = ""
        var link = ""
        var pubDate: LocalDateTime? = null
        var description = ""
        var duration = ""
        var imageUrl = ""
        var imagePath = null
        var explicit = true
        var block = false

        val childNodes = node.childNodes
        for (i in 0 until (childNodes?.length ?: 0)) {
            val node = childNodes?.item(i)
            when (node?.nodeName) {
                "title" -> title = node.textContent
                "link" -> link = node.textContent
                "guid" -> guid = node.textContent
                "description" -> description = node.textContent
                "itunes:explicit" -> explicit = node.textContent != "false"
                "itunes:block" -> block = node.textContent == "yes"
                "itunes:duration" -> duration = node.textContent
                "pubDate" -> {
                    var dateString = node.textContent
                    if (dateString.endsWith("GMT")) {
                        dateString = dateString.replace("GMT", "+0000")
                    }
                    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z")
                    val zonedDateTime = ZonedDateTime.parse(dateString, formatter)
                    pubDate =
                        zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

                }

                "enclosure" -> {
                    if (node is Element) {
                        episodeUrl = node.getAttribute("url")
                        episodeType = node.getAttribute("type")
                        episodeLength = node.getAttribute("length").toInt()
                    }
                }

                "itunes:image" -> {
                    if (node is Element) {
                        imageUrl = node.getAttribute("href")
                    }
                }
            }
        }

        return Episode(
            episodeUrl = episodeUrl,
            podcastId = 0,
            episodeLength = episodeLength,
            episodeType = episodeType,
            title = title,
            guid = guid,
            link = link,
            pubDate = pubDate ?: LocalDateTime.now(),
            description = description,
            duration = duration,
            imageUrl = imageUrl,
            imagePath = null,
            explicit = explicit,
            block = block,
            episodePath = null,
        )
    }
}