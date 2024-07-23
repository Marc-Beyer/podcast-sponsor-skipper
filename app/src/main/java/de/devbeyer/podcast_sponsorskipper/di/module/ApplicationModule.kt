package de.devbeyer.podcast_sponsorskipper.di.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.devbeyer.podcast_sponsorskipper.data.LocalDataManagerImpl
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.data.local.PodcastDatabase
import de.devbeyer.podcast_sponsorskipper.data.local.dao.CategoryDao
import de.devbeyer.podcast_sponsorskipper.data.local.dao.EpisodeDao
import de.devbeyer.podcast_sponsorskipper.data.remote.BackendAPI
import de.devbeyer.podcast_sponsorskipper.data.repositories.PodcastRepositoryImpl
import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.repositories.PodcastRepository
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetLocalPodcastsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.GetRemotePodcastsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.InsertPodcastUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.CompletedGuidedTourUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.GetCompletedGuidedTourUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.SetCompletedGuidedTourUseCase
import de.devbeyer.podcast_sponsorskipper.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun providePodcastRepository(backendAPI: BackendAPI): PodcastRepository =
        PodcastRepositoryImpl(backendAPI)

    @Provides
    @Singleton
    fun providePodcastUseCases(
        podcastRepository: PodcastRepository,
        podcastDao: PodcastDao,
    ): PodcastsUseCases {
        return PodcastsUseCases(
            getRemotePodcastsUseCase = GetRemotePodcastsUseCase(podcastRepository),
            getLocalPodcastsUseCase = GetLocalPodcastsUseCase(podcastDao),
            insertPodcastUseCase = InsertPodcastUseCase(podcastDao),
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
}