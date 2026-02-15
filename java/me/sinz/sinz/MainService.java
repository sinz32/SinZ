package me.sinz.sinz;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MainService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        voiceInput();
        return START_NOT_STICKY;
    }

    private void procCmd(String input) {
        String cmd = input.split(" ")[0];
        String data = input.replaceFirst(cmd + " ", "");

    }

    private void voiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        final SpeechRecognizer stt = SpeechRecognizer.createSpeechRecognizer(this);
        stt.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float rmsdB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {
                toast("Error: code " + error);
                stt.destroy();
            }

            @Override
            public void onResults(Bundle results) {
                final ArrayList<String> result = (ArrayList<String>) results.get(SpeechRecognizer.RESULTS_RECOGNITION);
                final String input = result.get(0);
                stt.destroy();
                toast(input);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}

        });
        stt.startListening(intent);
    }

    private void toast(String msg) {
        SinZ.toast(this, msg).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
