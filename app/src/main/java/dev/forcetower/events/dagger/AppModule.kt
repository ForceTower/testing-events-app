package dev.forcetower.events.dagger

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.forcetower.events.core.utils.GsonUtils
import dev.forcetower.events.core.validators.EmailValidator
import dev.forcetower.events.core.validators.Validator
import dev.forcetower.events.dagger.annotations.EmailValidators
import java.time.LocalDateTime
import java.time.ZonedDateTime
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, GsonUtils.LDT_DESERIALIZER)
            .serializeNulls()
            .create()
    }

    @Provides
    @Singleton
    @EmailValidators
    fun provideEmailValidator(): Validator<String> = EmailValidator()
}