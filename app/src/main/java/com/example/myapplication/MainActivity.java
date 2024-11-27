package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private final String DEFAULT_GREETING = "Chào bạn! Tôi là trợ lý chuyên về Công ty Denso. Tôi có thể giúp bạn với bất kỳ thông tin nào liên quan đến hoạt động toàn cầu, máy móc, quy trình sản xuất và sản phẩm của Denso. Bạn cần hỗ trợ gì thế?";
    ImageButton listenButton;
    EditText textPlaceholder;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    Context context = MainActivity.this;

    //text to speech
    TextToSpeech textToSpeech;

    ImageButton sendButton;

    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenButton = findViewById(R.id.listenButtonImg);
        textPlaceholder = findViewById(R.id.textPlaceholder);
        sendButton = findViewById(R.id.sendButton);

        listenButton.setOnClickListener(v -> speechToTextFunction());
        sendButton.setOnClickListener(v -> sendMessage());

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messageAdapter = new MessageAdapter();
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messageAdapter);

        // Add initial bot message
        messageAdapter.addMessage(new Message(DEFAULT_GREETING, false));
        textToSpeechFunction(DEFAULT_GREETING);
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
                    textToSpeech.setSpeechRate((float) 1.25);
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

    private void sendMessage() {
        String message = textPlaceholder.getText().toString().trim();
        if (!message.isEmpty()) {
            messageAdapter.addMessage(new Message(message, true));
            // textToSpeechFunction(message);
            textPlaceholder.setText("");
            
            // Scroll to bottom
            messagesRecyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        }
    }

}