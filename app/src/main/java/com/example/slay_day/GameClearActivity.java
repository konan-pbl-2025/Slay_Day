package com.example.slay_day;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class GameClearActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);
        ImageButton restartButton = (ImageButton) findViewById(R.id.imageButton10);
        restartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(GameClearActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        }
}
