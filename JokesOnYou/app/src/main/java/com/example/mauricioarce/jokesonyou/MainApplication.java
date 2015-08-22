package com.example.mauricioarce.jokesonyou;

import android.speech.tts.TextToSpeech;

import com.activeandroid.ActiveAndroid;

import java.util.Locale;

/**
 * Created by Mauricio Arce on 20/08/2015.
 */
public class MainApplication extends com.activeandroid.app.Application implements TextToSpeech.OnInitListener {

    private static TextToSpeech speech;
    private static final short STATUS = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        speech = new TextToSpeech(this, this);
    }

    @Override
    public void onInit(int i) {
        if (STATUS == TextToSpeech.SUCCESS) {
            Locale locale = new Locale("es");
            speech.setLanguage(locale);
        }
    }

    @Override
    public void onTerminate() {
        if (speech != null) {
            speech.stop();
            speech.shutdown();
        }
        super.onTerminate();
    }

    public static void speakOut(String text) {
        try {
            speech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
