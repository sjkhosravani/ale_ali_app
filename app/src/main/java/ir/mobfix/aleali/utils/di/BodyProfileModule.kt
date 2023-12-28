package ir.mobfix.aleali.utils.di



import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.mobfix.aleali.data.models.login.BodyLogin
import ir.mobfix.aleali.data.models.profile.BodyUpdateProfile
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BodyProfileModule {

    @Provides
    @Singleton
    fun provideBodyProfile() = BodyUpdateProfile()

}