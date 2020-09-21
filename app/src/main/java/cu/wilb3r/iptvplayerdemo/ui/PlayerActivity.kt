package cu.wilb3r.iptvplayerdemo.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import cu.wilb3r.iptvplayerdemo.R
import cu.wilb3r.iptvplayerdemo.databinding.ActivityPlayerBinding
import cu.wilb3r.iptvplayerdemo.utils.invisible
import cu.wilb3r.iptvplayerdemo.utils.snack
import cu.wilb3r.iptvplayerdemo.utils.visible

class PlayerActivity : AppCompatActivity(), PlaybackPreparer {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var stream: String
    private lateinit var simpleExoPlayer: SimpleExoPlayer

    companion object {
        private val STREAM = "stream"

        @JvmStatic
        fun newIntent(context: Context, stream: String): Intent {
            return Intent(context, PlayerActivity::class.java).also {
                it.putExtra(STREAM, stream)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        stream = intent.getStringExtra("stream")
        setContentView(binding.root)

    }

    private fun initializePlayer() {

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this)

        val progressiveMediaSource = ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(
                this, Util.getUserAgent(
                    this, "IPTVPlayerDemo"
                )
            )
        ).createMediaSource(Uri.parse(stream))

        simpleExoPlayer.prepare(progressiveMediaSource, false, false)
        simpleExoPlayer.playWhenReady = true

        binding.playerView.setShutterBackgroundColor(Color.TRANSPARENT)
        binding.playerView.player = simpleExoPlayer
        binding.playerView.requestFocus()

        simpleExoPlayer.addListener( object : Player.EventListener{
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {

            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) {

            }

            override fun onPlayerError(error: ExoPlaybackException) {
                snack(error?.message!!, binding.root)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when(playbackState){
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

            override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) {

            }
        })

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
}
