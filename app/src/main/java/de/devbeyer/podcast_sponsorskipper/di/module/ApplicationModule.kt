package de.devbeyer.podcast_sponsorskipper.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.devbeyer.podcast_sponsorskipper.data.LocalDataManagerImpl
import de.devbeyer.podcast_sponsorskipper.data.local.PodcastDatabase
import de.devbeyer.podcast_sponsorskipper.data.local.dao.CategoryDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.data.remote.BackendAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.FileAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.RSSAPI
import de.devbeyer.podcast_sponsorskipper.data.repositories.FileRepositoryImpl
import de.devbeyer.podcast_sponsorskipper.data.repositories.PodcastRepositoryImpl
import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import de.devbeyer.podcast_sponsorskipper.domain.repositories.PodcastRepository
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.DeleteFileUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.DownloadFileUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.CompletedGuidedTourUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.GetCompletedGuidedTourUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.SetCompletedGuidedTourUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.DeleteLocalPodcastUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetEpisodesOfPodcastUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetLocalPodcastByUrl
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetLocalPodcastsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetRSSFeed
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetRemotePodcastsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.InsertPodcastUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideLocalDataManager(
        application: Application
    ): LocalDataManager = LocalDataManagerImpl(application)


    @Provides
    @Singleton
    fun provideCompletedGuidedTourUseCases(
        localDataManager: LocalDataManager
    ) = CompletedGuidedTourUseCases(
        GetCompletedGuidedTourUseCase(localDataManager),
        SetCompletedGuidedTourUseCase(localDataManager)
    )

    @Provides
    @Singleton
    fun provideBackendAPI(): BackendAPI {
        return Retrofit.Builder().baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(BackendAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideRSSAPI(): RSSAPI {
        return Retrofit.Builder()
            .baseUrl("https://placeholder.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(RSSAPI::class.java)
    }


    @Provides
    @Singleton
    fun provideFileAPI(): FileAPI {
        return Retrofit.Builder()
            .baseUrl("https://placeholder.com/")
            .build()
            .create(FileAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideFileRepository(fileAPI: FileAPI, applicationContext: Application): FileRepository =
        FileRepositoryImpl(fileAPI, applicationContext)

    @Provides
    @Singleton
    fun provideFileUseCases(
        fileRepository: FileRepository,
    ): FileUseCases {
        return FileUseCases(
            downloadFileUseCase = DownloadFileUseCase(fileRepository),
            deleteFileUseCase = DeleteFileUseCase(fileRepository),
        )
    }

    @Provides
    @Singleton
    fun providePodcastRepository(backendAPI: BackendAPI, rssAPI: RSSAPI): PodcastRepository =
        PodcastRepositoryImpl(backendAPI, rssAPI)

    @Provides
    @Singleton
    fun providePodcastUseCases(
        podcastRepository: PodcastRepository,
        podcastDao: PodcastDao,
        categoryDao: CategoryDao,
        episodeDao: EpisodeDao,
        fileUseCases: FileUseCases,
    ): PodcastsUseCases {
        return PodcastsUseCases(
            getRemotePodcastsUseCase = GetRemotePodcastsUseCase(podcastRepository),
            getLocalPodcastsUseCase = GetLocalPodcastsUseCase(podcastDao),
            insertPodcastUseCase = InsertPodcastUseCase(
                podcastDao = podcastDao,
                categoryDao = categoryDao,
                episodeDao = episodeDao,
                fileUseCases = fileUseCases,
            ),
            deleteLocalPodcastUseCase = DeleteLocalPodcastUseCase(
                podcastDao = podcastDao,
                episodeDao = episodeDao,
                fileUseCases = fileUseCases,
            ),
            getLocalPodcastByUrl = GetLocalPodcastByUrl(podcastDao),
            getRSSFeed = GetRSSFeed(podcastRepository),
            getEpisodesOfPodcastUseCase = GetEpisodesOfPodcastUseCase(episodeDao),
        )
    }


    @Provides
    @Singleton
    fun providePodcastDatabase(applicationContext: Application): PodcastDatabase {
        return Room.databaseBuilder(
            applicationContext,
            PodcastDatabase::class.java,
            "podcast-db",
        )
            //.createFromAsset("database/mydatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @Singleton
    fun providePodcastDao(podcastDatabase: PodcastDatabase): PodcastDao =
        podcastDatabase.podcastDao()

    @Provides
    @Singleton
    fun provideCategoryDao(podcastDatabase: PodcastDatabase): CategoryDao =
        podcastDatabase.categoryDao()

    @Provides
    @Singleton
    fun provideEpisodeDao(podcastDatabase: PodcastDatabase): EpisodeDao =
        podcastDatabase.episodeDao()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    /*
    @Provides
    fun provideWorkerFactory(podcastUseCases: PodcastsUseCases): WorkerFactory {
        return DownloadWorkerFactory(fileAPI)
    }
     */
}