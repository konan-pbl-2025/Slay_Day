package com.example.slay_day;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    ArrayList <Integer> cardColler = new ArrayList<>();//0が赤,1が青,2が緑
    ArrayList <Integer> cardNum = new ArrayList<>();//カードの番号
    ArrayList <Integer> cardType = new ArrayList<>();//カードの種類

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

    }

    private void cardRandom(ArrayList cardColler, ArrayList cardNum, ArrayList cardType){
        Random rand = new Random();
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