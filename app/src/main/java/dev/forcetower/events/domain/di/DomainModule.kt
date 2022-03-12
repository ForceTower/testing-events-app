package dev.forcetower.events.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.forcetower.events.domain.validator.AndroidEmailValidator
import dev.forcetower.events.domain.validator.Validator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    @Singleton
    fun emailValidator(): Validator<String> = AndroidEmailValidator()
}