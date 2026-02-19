package me.sinz.sinz;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView web = new WebView(this);
        web.loadUrl("file:///android_asset/listening.html");
        web.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        web.setBackgroundColor(Color.argb(90, 0, 0, 0));
        setContentView(web);
        voiceInput();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    private void procCmd(String input) {
        String[] cmd = input.split(" ");
        String data = input.replaceFirst(cmd[0] + " ", "");
        if (cmd[0].equals("구글") && cmd[1].equals("검색")) {
            String word = data.replaceFirst(cmd[1] + " ", "");
            Uri uri = Uri.parse("https://www.google.com/search?q=" + word.replace(" ", "+"));
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } else if (cmd[0].equals("네이버") && cmd[1].equals("검색")) {
            String word = data.replaceFirst(cmd[1] + " ", "");
            Uri uri = Uri.parse("https://search.naver.com/search.naver?query=" + word.replace(" ", "+"));
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
        new Handler().postDelayed(() -> finish(), 1000);
    }

    private void voiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        final SpeechRecognizer stt = SpeechRecognizer.createSpeechRecognizer(this);
        stt.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                SinZ.vibrate(InputActivity.this, 100);
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
                SinZ.vibrate(InputActivity.this, 100);
                toast("Error: code " + error);
                stt.destroy();
                new Handler().postDelayed(()->finish(), 1000);
            }

            @Override
            public void onResults(Bundle results) {
                SinZ.vibrate(InputActivity.this, 100);
                final ArrayList<String> result = (ArrayList<String>) results.get(SpeechRecognizer.RESULTS_RECOGNITION);
                final String input = result.get(0);
                stt.destroy();
                procCmd(input);
                toast(input);
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }

        });
        stt.startListening(intent);
    }

    private void toast(final String msg) {
        runOnUiThread(() -> SinZ.toast(this, msg).show());
    }

}
