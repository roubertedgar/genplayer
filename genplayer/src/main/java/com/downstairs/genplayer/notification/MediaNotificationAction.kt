package com.downstairs.genplayer.notification

import com.downstairs.dsplayer.R

const val PLAYER_CONTROL_ACTION_PLAY = "com.downstairs.player.action.PLAY"
const val PLAYER_CONTROL_ACTION_PAUSE = "com.downstairs.player.action.PAUSE"
const val PLAYER_CONTROL_ACTION_FORWARD = "com.downstairs.player.action.FORWARD"
const val PLAYER_CONTROL_ACTION_BACKWARD = "com.downstairs.player.action.BACKWARD"
const val PLAYER_CONTROL_ACTION_STOP = "com.downstairs.player.action.STOP"

enum class MediaNotificationAction(val icon: Int, val title: String, val filter: String) {
    PLAY(R.drawable.ic_play_notification, "Play", PLAYER_CONTROL_ACTION_PLAY),
    PAUSE(R.drawable.ic_pause_notification, "Pause", PLAYER_CONTROL_ACTION_PAUSE),
    FORWARD(R.drawable.ic_forward_notification, "Forward", PLAYER_CONTROL_ACTION_FORWARD),
    BACKWARD(R.drawable.ic_backward_notification, "Backward", PLAYER_CONTROL_ACTION_BACKWARD),
    STOP(R.drawable.ic_close_notification, "Close", PLAYER_CONTROL_ACTION_STOP);

    companion object {
        fun forEach(emmit: (MediaNotificationAction) -> Unit) {
            values().forEach(emmit)
        }
    }
}