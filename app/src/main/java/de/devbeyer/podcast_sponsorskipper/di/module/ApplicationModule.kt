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
import de.devbeyer.podcast_sponsorskipper.data.local.dao.SponsorSectionDao
import de.devbeyer.podcast_sponsorskipper.data.remote.BackendAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.FileAPI
import de.devbeyer.podcast_sponsorskipper.data.remote.RSSAPI
import de.devbeyer.podcast_sponsorskipper.data.repositories.BackendRepositoryImpl
import de.devbeyer.podcast_sponsorskipper.data.repositories.FileRepositoryImpl
import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.repositories.BackendRepository
import de.devbeyer.podcast_sponsorskipper.domain.repositories.FileRepository
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.CompleteEpisodeUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.DeleteEpisodeUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.DownloadEpisodeUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.DownloadSponsorSectionsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.FavoriteEpisodeUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.GetEpisodeUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.GetSponsorSectionsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.MarkEpisodeCompleteUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.UpdateEpisodeUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.DeleteFileUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.DownloadFileUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.FileUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.file.StreamFileUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.CompletedGuidedTourUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.GetCompletedGuidedTourUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.SetCompletedGuidedTourUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.DeleteLocalPodcastUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetEpisodesOfPodcastUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetLocalPodcastByIdUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetLocalPodcastByUrlUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetLocalPodcastsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetRSSFeedUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetRemotePodcastsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.InsertPodcastUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.RateSponsorSectionUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.RegisterPodcastUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.SubmitSponsorSectionUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings.GetSettingsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings.SetBooleanSettingUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings.SetIntSettingUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings.SettingsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.user.GetUserUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.user.RegisterUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.user.UserUseCases
import de.devbeyer.podcast_sponsorskipper.util.Constants
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideUserUseCases(
        localDataManager: LocalDataManager,
        backendRepository: BackendRepository,
    ) = UserUseCases(
        RegisterUseCase(localDataManager, backendRepository),
        GetUserUseCase(localDataManager, backendRepository)
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
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://placeholder.com/")
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideFileAPI(retrofit: Retrofit): FileAPI {
        return retrofit.create(FileAPI::class.java)
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
            streamFileUseCase = StreamFileUseCase(fileRepository),
        )
    }

    @Provides
    @Singleton
    fun provideBackendRepository(backendAPI: BackendAPI, rssAPI: RSSAPI): BackendRepository =
        BackendRepositoryImpl(backendAPI, rssAPI)

    @Provides
    @Singleton
    fun providePodcastUseCases(
        backendRepository: BackendRepository,
        podcastDao: PodcastDao,
        categoryDao: CategoryDao,
        episodeDao: EpisodeDao,
        fileUseCases: FileUseCases,
        userUseCases: UserUseCases,
        sponsorSectionDao: SponsorSectionDao
    ): PodcastsUseCases {
        return PodcastsUseCases(
            getRemotePodcastsUseCase = GetRemotePodcastsUseCase(backendRepository),
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
            getLocalPodcastByUrlUseCase = GetLocalPodcastByUrlUseCase(podcastDao),
            getRSSFeedUseCase = GetRSSFeedUseCase(backendRepository),
            getEpisodesOfPodcastUseCase = GetEpisodesOfPodcastUseCase(episodeDao),
            submitSponsorSectionUseCase = SubmitSponsorSectionUseCase(
                backendRepository = backendRepository,
                userUseCases = userUseCases,
                sponsorSectionDao = sponsorSectionDao,
            ),
            rateSponsorSectionUseCase = RateSponsorSectionUseCase(
                backendRepository = backendRepository,
                userUseCases = userUseCases,
                sponsorSectionDao = sponsorSectionDao,
            ),
            getLocalPodcastByIdUseCase = GetLocalPodcastByIdUseCase(
                podcastDao = podcastDao,
            ),
            registerPodcastUseCase = RegisterPodcastUseCase(
                backendRepository = backendRepository,
            )
        )
    }

    @Provides
    @Singleton
    fun provideEpisodeUseCases(
        episodeDao: EpisodeDao,
        fileUseCases: FileUseCases,
        backendRepository: BackendRepository,
        sponsorSectionDao: SponsorSectionDao,
        podcastsDao: PodcastDao,
    ): EpisodeUseCases {
        return EpisodeUseCases(
            downloadEpisodeUseCase = DownloadEpisodeUseCase(
                episodeDao = episodeDao,
                fileUseCases = fileUseCases,
            ),
            downloadSponsorSectionsUseCase = DownloadSponsorSectionsUseCase(
                backendRepository = backendRepository,
                sponsorSectionDao = sponsorSectionDao,
            ),
            getSponsorSectionsUseCase = GetSponsorSectionsUseCase(
                sponsorSectionDao = sponsorSectionDao,
            ),
            completeEpisodeUseCase = CompleteEpisodeUseCase(
                episodeDao = episodeDao,
                fileUseCases = fileUseCases,
            ),
            markEpisodeCompleteUseCase = MarkEpisodeCompleteUseCase(
                episodeDao = episodeDao,
                fileUseCases = fileUseCases,
                podcastsDao = podcastsDao,
            ),
            deleteEpisodeUseCase = DeleteEpisodeUseCase(
                episodeDao = episodeDao,
                fileUseCases = fileUseCases,
            ),
            favoriteEpisodeUseCase = FavoriteEpisodeUseCase(
                episodeDao = episodeDao,
            ),
            getEpisodeUseCase = GetEpisodeUseCase(
                episodeDao = episodeDao,
            ),
            updateEpisodeUseCase = UpdateEpisodeUseCase(
                episodeDao = episodeDao,
            ),
        )
    }

    @Provides
    @Singleton
    fun provideSettingsUseCases(
        localDataManager: LocalDataManager,
    ): SettingsUseCases {
        return SettingsUseCases(
            getSettingsUseCase = GetSettingsUseCase(
                localDataManager = localDataManager
            ),
            setBooleanSettingUseCase = SetBooleanSettingUseCase(
                localDataManager = localDataManager
            ),
            setIntSettingUseCase = SetIntSettingUseCase(
                localDataManager = localDataManager
            ),
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
    fun provideSponsorSectionDao(podcastDatabase: PodcastDatabase): SponsorSectionDao =
        podcastDatabase.sponsorSectionDao()

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