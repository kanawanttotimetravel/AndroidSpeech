package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private final String DEFAULT_GREETING = "Xin chào";
    ImageButton listenButton;
    EditText textPlaceholder;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Context context = MainActivity.this;

    //text to speech
    TextToSpeech textToSpeech;
    Button speakButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenButton = findViewById(R.id.listenButtonImg);
        textPlaceholder = findViewById(R.id.textPlaceholder);
        speakButton = findViewById(R.id.speakButton);

        listenButton.setOnClickListener(v -> speechToTextFunction());

        speakButton.setOnClickListener(v -> {
            if ( textPlaceholder.getText().toString().isEmpty() ){
                String greeting = DEFAULT_GREETING;
                textPlaceholder.setText(greeting);
                textToSpeechFunction(greeting);
            } else {
                textToSpeechFunction( textPlaceholder.getText().toString() );
            }
        });
    }

    public void speechToTextFunction(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() );

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Đang nghe");

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn\\'t support speech input" ,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void textToSpeechFunction(String text){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if ( status != TextToSpeech.ERROR ){
                    textToSpeech.setLanguage( new Locale("vi_VN") );
                    textToSpeech.setSpeechRate((float) 1.5);
                    textToSpeech.speak(text , TextToSpeech.QUEUE_FLUSH , null, TAG);

                }
            }
        });
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {

                List<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String text = result.get(0);
                textPlaceholder.setText(text);

            }
        }
    }

}