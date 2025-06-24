package dev.materii.gloom.di

import dev.materii.gloom.domain.manager.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun managerModule() = module {

    singleOf(::AuthManager)
    singleOf(::DialogManager)
    singleOf(::DownloadManager)
    singleOf(::LibraryManager)
    singleOf(::ShareManager)
    singleOf(::ToastManager)

}