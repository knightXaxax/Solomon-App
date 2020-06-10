package com.example.pbs_mobile.VoiceRecognition;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pbs_mobile.Activities.ScannerActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Solomon extends IntentService {

    public TextToSpeech textToSpeech;
    private Voice voice;
    private static AudioManager audioManager;
    private static int maxVolume;
    private static float percent = 0.9f;
    private static SpeechRecognizer speechRecognizer;
    private static RecognitionListener recognitionListener = null;
    private Intent speechRecognizingIntent;
    public static boolean ifYouWantSolomonToListen = false;
    public static boolean isSolomonListening = false;
    private boolean isSolomonSpeaksAndDoneSpeaking = false;

    public Solomon() {
        super("Solomon");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        maxVolume = (int) (maxVolume * percent);
        if (ifYouWantSolomonToListen != true) {
            stopSelf();
            unmute();
        } else {
            startListening();
        }
        return Service.START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
    }

    public void initializeSpeechToText() {
        textToSpeech = new TextToSpeech(getBaseContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (textToSpeech.getEngines().size() != 0) {
                            Set<String> a = new HashSet<>();
                            a.add("male");
                            voice = new Voice("en-us-x-sfg#male_3-local", new Locale("en", "US"), 400, 200, false, a);
                            textToSpeech.setVoice(voice);
                        } else {
                            Toast.makeText(getApplicationContext(), "Google TextToSpeech engine is not installed, please install it from the Google Playstore.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, "com.google.android.tts");
    }

    public void speak(String sentence) {
        unmute();
        if(Build.VERSION.SDK_INT >= 21) {
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, null);
        } else{
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public static void mute() {
        audioManager.setStreamVolume(audioManager.STREAM_MUSIC, 0, 0);
    }

    public static void unmute() {
        audioManager.setStreamVolume(audioManager.STREAM_MUSIC, maxVolume, 0);
    }

    private void startListening() {
        if(ifYouWantSolomonToListen != true) {
            stopSelf();
        } else {
            reset();
            initializeSpeechToText();
            speechRecognizer = speechRecognizer.createSpeechRecognizer(getApplicationContext());
            if (recognitionListener == null) {
                speechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
            } else {
                speechRecognizer.setRecognitionListener(recognitionListener);
            }
            speechRecognizingIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizingIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizingIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
            speechRecognizingIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
            speechRecognizingIntent.putExtra(RecognizerIntent.EXTRA_SECURE, true);
            speechRecognizingIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 3000);
            speechRecognizingIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000);
            speechRecognizingIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 7000);
            speechRecognizer.startListening(speechRecognizingIntent);
            isSolomonSpeaksAndDoneSpeaking = false;
            mute();
            Log.i("com.example.pbs_mobile", "solomon is listening...");
        }
    }

    private void restart() {
        if (ifYouWantSolomonToListen != true) {
            stopSelf();
            unmute();
        } else {
            try {
                if(isSolomonSpeaksAndDoneSpeaking == true) {
                    TimeUnit.SECONDS.sleep(5);
                }
                startListening();
            } catch (InterruptedException e) {
                reset();
                speak("There is an error in restart function master.");
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
            textToSpeech = null;
        }
        if(speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }

    public void processResult(String voiceMessage) {
        voiceMessage = voiceMessage.toLowerCase();

        if (isSolomonListening == true) {
            if (voiceMessage.indexOf("are you still there") != -1 || voiceMessage.indexOf("are you still with me") != -1) {
                speak("yes master, what can i help?");
            } else if (voiceMessage.indexOf("what") != -1) {
                if (voiceMessage.indexOf("name") != -1) {
                    speak("Hi my name is Solomon, nice to meet you!");
                }
            } else if (voiceMessage.indexOf("scan") !=  -1 || voiceMessage.indexOf("scanner") != -1) {
                ifYouWantSolomonToListen = false;
                speak("right away sir, opening the scanner");
                getApplication().startActivity(new Intent(getBaseContext(), ScannerActivity.class));
            } else if (voiceMessage.indexOf("stop talking") != -1) {
                speak("Are you sure you want to on silent mode?");
            }  else if (voiceMessage.indexOf("exit") != -1) {
                if (voiceMessage.indexOf("right now") != -1) {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                    ifYouWantSolomonToListen = false;
                } else if (voiceMessage.indexOf("the app") != -1) {
                    speak("Are you sure you want to exit the app?");
                }
            } else if (voiceMessage.indexOf("yes silent") != -1) {
                speak("silent mode on");
                isSolomonListening = false;
            } else {
                speak("sorry i don't get it.");
            }
            isSolomonSpeaksAndDoneSpeaking = true;
        } else {
            if (voiceMessage.indexOf("are you there") != -1 || voiceMessage.indexOf("are you with me") != -1) {
                speak("yes master, what is your wish?");
                isSolomonListening = true;
                isSolomonSpeaksAndDoneSpeaking = true;
            }
        }
    }

    private class SpeechRecognitionListener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params) {
            mute();
        }

        @Override
        public void onBeginningOfSpeech() {
            mute();
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            mute();
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            mute();
        }

        @Override
        public void onEndOfSpeech() {
            mute();
        }

        @Override
        public void onError(int error) {
            mute();
            Log.i("com.example.pbs_mobile", "solomon stops listening...");
            if(ifYouWantSolomonToListen != true) {
                stopSelf();
            } else {
                Log.i("com.example.pbs_mobile", "solomon is ready to listen again...");
                restart();
            }
        }

        @Override
        public void onResults(Bundle results) {
            mute();
            List<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            processResult(result.get(0));
            Log.i("com.example.pbs_mobile", result.toString());
            restart();
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            mute();
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            mute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        unmute();
        Log.i("com.example.pbs_mobile", "solomon will not listen");
    }
}
