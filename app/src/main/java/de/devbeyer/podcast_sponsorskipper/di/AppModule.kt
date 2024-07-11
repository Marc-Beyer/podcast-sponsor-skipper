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
    fun provideBackendAPI():BackendAPI{
        return BackendAPI()
    }

    @Provides
    @Singleton
    fun providePodcastRepository():PodcastRepository = PodcastRepositoryImpl()

    @Provides
    @Singleton
    fun providePodcastUseCases():PodcastsUseCases{
        return PodcastsUseCases(GetPodcastsUseCase())
    }
}