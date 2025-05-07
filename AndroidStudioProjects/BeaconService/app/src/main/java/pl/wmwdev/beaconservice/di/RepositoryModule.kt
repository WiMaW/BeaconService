package pl.wmwdev.beaconservice.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import pl.wmwdev.beaconservice.data.remote.ActionRepo

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideActionRepo() : ActionRepo {
        return ActionRepo()
    }
}