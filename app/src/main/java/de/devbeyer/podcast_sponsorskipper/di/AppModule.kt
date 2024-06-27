package de.devbeyer.podcast_sponsorskipper.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.devbeyer.podcast_sponsorskipper.data.LocalDataManagerImpl
import de.devbeyer.podcast_sponsorskipper.domain.LocalDataManager
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.CompletedGuidedTourUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.GetCompletedGuidedTourUseCase
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.SetCompletedGuidedTourUseCase
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
}