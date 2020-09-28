package cu.wilb3r.iptvplayerdemo.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import cu.wilb3r.iptvplayerdemo.R
import cu.wilb3r.iptvplayerdemo.db.IPTVDatabase
import cu.wilb3r.iptvplayerdemo.utils.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        IPTVDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideM3UDao(db: IPTVDatabase) = db.getM3UDao()

    @Singleton
    @Provides
    fun provideExoPlayer(@ApplicationContext app: Context) = SimpleExoPlayer.Builder(app).build()

    @Singleton
    @Provides
    fun provideGlideRequestManager(@ApplicationContext context: Context) = Glide.with(context)
        .setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_tv)
                .error(R.drawable.ic_tv)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
        )

}