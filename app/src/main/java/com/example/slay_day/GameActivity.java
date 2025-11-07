package com.example.slay_day;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

    }








    public void cardRandom(){
        Random rand = new Random();
        ArrayList <Integer> cardColler = new ArrayList<>();
        ArrayList <Integer> cardNum = new ArrayList<>();
        ArrayList <Integer> cardType = new ArrayList<>();
        for(int i=0;i<5;i++){
            cardType.add(1);
        }
        for(int i=0;i<5;i++){
            cardColler.add(rand.nextInt(3));
        }
        for(int i=0;i<5;i++){
            cardNum.add(rand.nextInt(5)+1);
        }
    }
}