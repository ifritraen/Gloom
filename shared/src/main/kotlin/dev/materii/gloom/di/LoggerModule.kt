package dev.materii.gloom.di

import dev.materii.gloom.util.Logger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun loggerModule() = module {

    singleOf(::Logger)

}