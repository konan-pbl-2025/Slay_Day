package com.example.slay_day;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    Random rand = new Random();
    ArrayList <Integer> cardColler = new ArrayList<>();//0が赤,1が青,2が緑
    ArrayList <Integer> cardNum = new ArrayList<>();//カードの番号
    ArrayList <Integer> cardType = new ArrayList<>();//カードの種類

    int PlayerHP = rand.nextInt(10)+1;
    int EnemyHP = rand.nextInt(10)+1;
    int EnemyATK = rand.nextInt(10)+1;
    int[][] EnemyState = new int[5][5];//やけどで例えると一次はやけどかどうか、二次はやけどが何ターン続くか

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cardColler=randomColler(cardColler);
        cardNum=randomNum(cardNum);
        cardType=randomType(cardType);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

    }

    private ArrayList randomColler(ArrayList cardColler){
        Random rand = new Random();
        for(int i=0;i<5;i++){
            cardColler.add(rand.nextInt(3));
        }
        return cardColler;
    }
    private ArrayList randomType(ArrayList cardType){
        Random rand = new Random();
        for(int i=0;i<5;i++){
            cardType.add(1);
        }
        return cardType;
    }
    private ArrayList randomNum(ArrayList cardNum){
        Random rand = new Random();
        for(int i=0;i<5;i++){
            cardNum.add(rand.nextInt(5)+1);
        }
        return cardNum;
    }

}