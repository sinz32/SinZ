package me.sinz.assistant;

import android.content.Intent;
import android.speech.RecognitionService;

public class AssistantService extends RecognitionService {
    @Override
    protected void onStartListening(Intent intent, Callback callback) {}

    @Override
    protected void onCancel(Callback callback) {}

    @Override
    protected void onStopListening(Callback callback) {}

}
