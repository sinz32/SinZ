package me.sinz.assistant;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.service.voice.VoiceInteractionSessionService;

import me.sinz.sinz.InputActivity;

public class AssistantSessionService extends VoiceInteractionSessionService {

    @Override
    public VoiceInteractionSession onNewSession(Bundle bundle) {
        return new VoiceInteractionSession(this) {

            @Override
            public void onPrepareShow(Bundle args, int showFlags) {
                super.onPrepareShow(args, showFlags);
                if (Build.VERSION.SDK_INT >= 26) {
                    setUiEnabled(false);
                }
            }

            @Override
            public void onShow(Bundle args, int showFlags) {
                super.onShow(args, showFlags);
                Intent intent = new Intent(AssistantSessionService.this, InputActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        };
    }

}
