package com.tutorix.tutorialspoint.players;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public final class SimpleExoPlayerView  extends PlayerView {

    public SimpleExoPlayerView(Context context) {
        super(context);
    }

    public SimpleExoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Switches the view targeted by a given {@link SimpleExoPlayer}.
     *
     * @param player The player whose target view is being switched.
     * @param oldPlayerView The old view to detach from the player.
     * @param newPlayerView The new view to attach to the player.
     */
    public static void switchTargetView(
            @NonNull SimpleExoPlayer player,
            @Nullable com.google.android.exoplayer2.ui.SimpleExoPlayerView oldPlayerView,
            @Nullable com.google.android.exoplayer2.ui.SimpleExoPlayerView newPlayerView) {
        PlayerView.switchTargetView(player, oldPlayerView, newPlayerView);
    }

}