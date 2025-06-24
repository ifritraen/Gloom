package dev.materii.gloom

import android.app.Application
import dev.materii.gloom.di.*
import dev.materii.gloom.di.module.platformModule
import dev.materii.gloom.di.module.viewModelModule
import dev.materii.gloom.ui.activity.viewmodel.OAuthViewModel
import dev.materii.gloom.util.VersionName
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class Gloom: Application() {

    override fun onCreate() {
        super.onCreate()
        VersionName = BuildConfig.VERSION_NAME

        startKoin {
            androidContext(this@Gloom)
            modules(
                httpModule(),
                loggerModule(),
                serviceModule(),
                repositoryModule(),
                managerModule(),
                viewModelModule(),
                platformModule(),
                module { viewModelOf(::OAuthViewModel) } // Cant group with the rest
            )
        }
    }
}
