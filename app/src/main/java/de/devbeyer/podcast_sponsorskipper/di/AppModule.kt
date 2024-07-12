package de.devbeyer.podcast_sponsorskipper.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.devbeyer.podcast_sponsorskipper.data.LocalDataManagerImpl
import de.devbeyer.podcast_sponsorskipper.data.remote.BackendAPI
import de.devbeyer.podcast_sponsorskipper.data.repositories.PodcastRepositoryImpl
import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.repositories.PodcastRepository
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.backend.GetPodcastsUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.backend.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.CompletedGuidedTourUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.GetCompletedGuidedTourUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.SetCompletedGuidedTourUseCase
import de.devbeyer.podcast_sponsorskipper.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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
    fun providePodcastUseCases(podcastRepository: PodcastRepository): PodcastsUseCases {
        return PodcastsUseCases(GetPodcastsUseCase(podcastRepository))
    }
}