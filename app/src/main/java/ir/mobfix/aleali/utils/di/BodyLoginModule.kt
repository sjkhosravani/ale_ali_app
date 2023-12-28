package ir.mobfix.aleali.utils.di



import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.mobfix.aleali.data.models.login.BodyLogin
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BodyLoginModule {

    @Provides
    @Singleton
    fun provideUserBody() = BodyLogin()

}