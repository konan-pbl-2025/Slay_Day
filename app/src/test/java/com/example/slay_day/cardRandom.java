package com.example.slay_day;
import org.junit.Test;

import java.util.*;
import java.util.Random;

public class cardRandom{
    @Test
    public void cardRandom(){
        Random rand = new Random();
        ArrayList <Integer> cardColler = new ArrayList<>();
        ArrayList <Integer> cardNum = new ArrayList<>();
        ArrayList <Integer> cardType = new ArrayList<>();//0が赤,1が青,2が緑
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

    private void judgeColor(List<Integer> card){
        for(int i=0;i<5;i++){
            if(card.get(i)==0){
                System.out.printf("赤 ");
            }
            if(card.get(i)==1){
                System.out.printf("青 ");
            }
            if(card.get(i)==2){
                System.out.printf("緑 ");
            }

        }

    }

}