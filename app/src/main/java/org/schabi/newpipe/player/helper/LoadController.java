package org.schabi.newpipe.player.helper;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.upstream.DefaultAllocator;

public class LoadController extends DefaultLoadControl {

    public static final String TAG = "LoadController";

    // Nexos Tube: arranque más rápido y menos parones que los defaults de ExoPlayer
    // (50s / 50s / 2.5s / 5s, sin back-buffer).
    private static final int MIN_BUFFER_MS = 50_000;
    private static final int MAX_BUFFER_MS = 120_000;
    private static final int BUFFER_FOR_PLAYBACK_MS = 1_500;
    private static final int BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS = 3_000;
    private static final int TARGET_BUFFER_BYTES = 150 * 1024 * 1024;
    private static final int BACK_BUFFER_DURATION_MS = 30_000;

    private boolean preloadingEnabled = true;

    public LoadController() {
        super(new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE),
                MIN_BUFFER_MS,
                MAX_BUFFER_MS,
                BUFFER_FOR_PLAYBACK_MS,
                BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS,
                TARGET_BUFFER_BYTES,
                /* prioritizeTimeOverSizeThresholds= */ true,
                BACK_BUFFER_DURATION_MS,
                /* retainBackBufferFromKeyframe= */ true);
    }

    @Override
    public void onPrepared() {
        preloadingEnabled = true;
        super.onPrepared();
    }

    @Override
    public void onStopped() {
        preloadingEnabled = true;
        super.onStopped();
    }

    @Override
    public void onReleased() {
        preloadingEnabled = true;
        super.onReleased();
    }

    @Override
    public boolean shouldContinueLoading(final long playbackPositionUs,
                                         final long bufferedDurationUs,
                                         final float playbackSpeed) {
        if (!preloadingEnabled) {
            return false;
        }
        return super.shouldContinueLoading(
                playbackPositionUs, bufferedDurationUs, playbackSpeed);
    }

    public void disablePreloadingOfCurrentTrack() {
        preloadingEnabled = false;
    }
}
