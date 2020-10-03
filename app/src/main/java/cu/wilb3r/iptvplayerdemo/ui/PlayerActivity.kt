package cu.wilb3r.iptvplayerdemo.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import cu.wilb3r.iptvplayerdemo.data.M3UItem
import cu.wilb3r.iptvplayerdemo.databinding.ActivityPlayerBinding
import cu.wilb3r.iptvplayerdemo.utils.Constant.STREAM
import cu.wilb3r.iptvplayerdemo.utils.invisible
import cu.wilb3r.iptvplayerdemo.utils.snack
import cu.wilb3r.iptvplayerdemo.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity(), PlaybackPreparer {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var m3uItem: M3UItem
    @Inject
    lateinit var simpleExoPlayer: SimpleExoPlayer

    companion object {
        private val M3UITEM = "m3uitem"

        @JvmStatic
        fun newIntent(context: Context, stream: M3UItem): Intent {
            return Intent(context, PlayerActivity::class.java).also {
                it.putExtra(M3UITEM, stream)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        m3uItem = intent.getSerializableExtra("m3uitem") as M3UItem
        setContentView(binding.root)

    }

    private fun initializePlayer() {

//        simpleExoPlayer = SimpleExoPlayer.Builder(this).build().apply {
//            //setAudioAttributes(audioAttributes, true)
//            setHandleAudioBecomingNoisy(true)
//        }

        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {

            }

            override fun onPlayerError(error: ExoPlaybackException) {
                snack(error?.message!!, binding.root)
                binding.progressBar.invisible()
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        binding.progressBar.visible()
                    }
                    Player.STATE_READY -> {
                        binding.progressBar.invisible()
                    }
                    Player.STATE_IDLE -> {
                        simpleExoPlayer.release()
                    }
                    Player.STATE_ENDED -> {
                        finish()
                    }
                }
            }

            override fun onLoadingChanged(isLoading: Boolean) {

            }

            override fun onPositionDiscontinuity(reason: Int) {

            }

            override fun onRepeatModeChanged(repeatMode: Int) {

            }

            override fun onTimelineChanged(timeline: Timeline, reason: Int) {

            }
        })

        val mediaSource = compileMediaSource(Uri.parse(m3uItem.mStreamURL), null)

        simpleExoPlayer.run {

            prepare(mediaSource!!, false, false)
        }
        simpleExoPlayer.playWhenReady = true

        binding.playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        binding.playerView.player = simpleExoPlayer
        binding.playerView.requestFocus()
    }

    private fun compileMediaSource(
        uri: Uri,
        @Nullable overrideExtension: String?
    ): MediaSource? {
        val dataSourceFactory = DefaultHttpDataSourceFactory(
            "IPTVPlayerDemo"
        )
        val type =
            Util.inferContentType(uri, overrideExtension)
        return when (type) {
            C.TYPE_DASH -> DashMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
            C.TYPE_SS -> SsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
            C.TYPE_HLS -> HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
            C.TYPE_OTHER -> ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri)
            else -> {
                throw IllegalStateException("Unsupported type: $type")
            }
        }
    }

    private fun releasePlayer() {
        simpleExoPlayer.release()
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) initializePlayer()
    }

    public override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23) initializePlayer()
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) releasePlayer()
    }

    override fun preparePlayback() {
        simpleExoPlayer.retry()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        simpleExoPlayer.release()
        finish()
    }
}
