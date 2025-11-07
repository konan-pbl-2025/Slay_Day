package com.example.slay_day;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    Random rand = new Random();
    ArrayList <Integer> cardColler = new ArrayList<>();//0が赤,1が青,2が緑
    ArrayList <Integer> cardNum = new ArrayList<>();//カードの番号
    ArrayList <Integer> cardType = new ArrayList<>();//カードの種類

    private ArrayList<CardData> currentHand = new ArrayList<>();

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

        //カードの色関連
        ImageView col1 = (ImageView)findViewById(R.id.imageView3);
        ImageView col2 = (ImageView)findViewById(R.id.imageView2);
        ImageView col3 = (ImageView)findViewById(R.id.imageView6);
        ImageView col4 = (ImageView)findViewById(R.id.imageView13);
        ImageView col5 = (ImageView)findViewById(R.id.imageView14);
        for(int i=0;i<5;i++){
            if(cardColler.get(i)==0&&i==0) col1.setImageResource(R.drawable.red_element);
            if(cardColler.get(i)==1&&i==0) col1.setImageResource(R.drawable.blue_element);
            if(cardColler.get(i)==2&&i==0) col1.setImageResource(R.drawable.green_element);

            if(cardColler.get(i)==0&&i==1) col2.setImageResource(R.drawable.red_element);
            if(cardColler.get(i)==1&&i==1) col2.setImageResource(R.drawable.blue_element);
            if(cardColler.get(i)==2&&i==1) col2.setImageResource(R.drawable.green_element);

            if(cardColler.get(i)==0&&i==2) col3.setImageResource(R.drawable.red_element);
            if(cardColler.get(i)==1&&i==2) col3.setImageResource(R.drawable.blue_element);
            if(cardColler.get(i)==2&&i==2) col3.setImageResource(R.drawable.green_element);

            if(cardColler.get(i)==0&&i==3) col4.setImageResource(R.drawable.red_element);
            if(cardColler.get(i)==1&&i==3) col4.setImageResource(R.drawable.blue_element);
            if(cardColler.get(i)==2&&i==3) col4.setImageResource(R.drawable.green_element);

            if(cardColler.get(i)==0&&i==4) col5.setImageResource(R.drawable.red_element);
            if(cardColler.get(i)==1&&i==4) col5.setImageResource(R.drawable.blue_element);
            if(cardColler.get(i)==2&&i==4) col5.setImageResource(R.drawable.green_element);

        }

        //カードの種類（表示だけはunicordの文字コードで管理してます）
        ImageView card1 = (ImageView)findViewById(R.id.imageView11);
        ImageView card2 = (ImageView)findViewById(R.id.imageView12);
        ImageView card3 = (ImageView)findViewById(R.id.imageView8);
        ImageView card4 = (ImageView)findViewById(R.id.imageView10);
        ImageView card5 = (ImageView)findViewById(R.id.imageView9);
        String[] cardID = {"a","b","c","d","e"};
        String [] ID = new String[5];
        for(int i=0;i<5;i++){
            char letter =(char)('a'+(cardType.get(i)-1));
            ID[i]=Character.toString(letter);
        }
        for(int i=0;i<5;i++){
            int resId = getResources().getIdentifier(ID[i], "drawable", getPackageName());
            if(i==0) card1.setImageResource(resId);
            if(i==1) card2.setImageResource(resId);
            if(i==2) card3.setImageResource(resId);
            if(i==3) card4.setImageResource(resId);
            if(i==4) card5.setImageResource(resId);
        }


        //カードの数字
        TextView cardNum1 = (TextView)findViewById(R.id.textView4);
        TextView cardNum2 = (TextView)findViewById(R.id.textView5);
        TextView cardNum3 = (TextView)findViewById(R.id.textView8);
        TextView cardNum4 = (TextView)findViewById(R.id.textView6);
        TextView cardNum5 = (TextView)findViewById(R.id.textView7);

        for(int i=0;i<5;i++){
            if(i==0) cardNum1.setText(String.valueOf(cardNum.get(i)));
            if(i==1) cardNum2.setText(String.valueOf(cardNum.get(i)));
            if(i==2) cardNum3.setText(String.valueOf(cardNum.get(i)));
            if(i==3) cardNum4.setText(String.valueOf(cardNum.get(i)));
            if(i==4) cardNum5.setText(String.valueOf(cardNum.get(i)));
        }

        ImageView[] cardTapViews = new ImageView[5];
        cardTapViews[0] = (ImageView)findViewById(R.id.imageView11);
        cardTapViews[1] = (ImageView)findViewById(R.id.imageView12);
        cardTapViews[2] = (ImageView)findViewById(R.id.imageView8);
        cardTapViews[3] = (ImageView)findViewById(R.id.imageView10);
        cardTapViews[4] = (ImageView)findViewById(R.id.imageView9);

        // 3つのArrayListを結合し、currentHandを作成し、リスナーを設定
        for(int i = 0; i < 5; i++){
            int colorIndex = cardColler.get(i);
            int cardNumValue = cardNum.get(i);
            int cardTypeValue = cardType.get(i); // カードの種類も利用可能

            // CardDataに格納するための情報決定
            String cardName;
            String cardEffect;
            int colorInt; // ダイアログの背景色用 (Color.REDなどのARGB値)

            // 🔴 ランダムな色に基づいてカード名と効果を設定するロジック
            switch (colorIndex) {
                case 0: // 赤
                    cardName = "攻撃カード (タイプ" + cardTypeValue + ")";
                    cardEffect = "敵に" + cardNumValue * 2 + "ダメージを与えます。";
                    colorInt = Color.RED;
                    break;
                case 1: // 青
                    cardName = "防御カード (タイプ" + cardTypeValue + ")";
                    cardEffect = cardNumValue + "のブロックを獲得します。";
                    colorInt = Color.BLUE;
                    break;
                case 2: // 緑
                    cardName = "スキルカード (タイプ" + cardTypeValue + ")";
                    cardEffect = "エナジーを" + cardNumValue + "回復します。";
                    colorInt = Color.GREEN;
                    break;
                default:
                    cardName = "不明なカード";
                    cardEffect = "効果なし";
                    colorInt = Color.GRAY;
            }

            // 🔴 currentHandリストにCardDataオブジェクトを格納
            CardData newCard = new CardData(cardName, cardEffect, cardNumValue, colorInt);
            currentHand.add(newCard);

            // 🔴 クリックリスナーの設定
            final int cardIndex = i;
            cardTapViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardData selectedCard = currentHand.get(cardIndex);
                    showCardDetail(selectedCard);
                }
            });
        }

    }

    // 仮のカードデータ
    public class CardData {
        String name;
        String effect;
        int number;
        int colorResId; // 色リソースIDやARGB値

        public CardData(String name, String effect, int number, int color) {
            this.name = name;
            this.effect = effect;
            this.number = number;
            this.colorResId = color;
        }
    }
    private void showCardDetail(CardData card) {
        // 1. カスタムスタイルを適用したBuilderの作成 (画面上部配置用)
        // R.style.TopHalfDialogStyle は前の回答で作成したスタイルです
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TopHalfDialogStyle);

        // 2. カスタムレイアウトのインフレーション
        LayoutInflater inflater = getLayoutInflater();
        final View customView = inflater.inflate(R.layout.custom_dialog_content, null);

        // 3. レイアウト内の要素にカード情報を設定
        TextView nameText = customView.findViewById(R.id.card_name_text);
        TextView effectText = customView.findViewById(R.id.card_effect_text);
        TextView numberText = customView.findViewById(R.id.card_number_text);
        View colorView = customView.findViewById(R.id.card_color_view);
        Button backButton = customView.findViewById(R.id.button_back);
        Button useButton = customView.findViewById(R.id.button_use);

        nameText.setText(card.name);
        effectText.setText(card.effect);
        numberText.setText(String.valueOf(card.number));
        colorView.setBackgroundColor(card.colorResId); // 色を設定

        // 4. Viewを設定
        builder.setView(customView);

        // 5. ダイアログの作成と表示
        final AlertDialog dialog = builder.create();

        // 6. ボタンのクリックリスナーを設定
        // 「もどる」ボタン (左下)
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // ダイアログを閉じる
                Toast.makeText(GameActivity.this, "カード詳細を閉じました。", Toast.LENGTH_SHORT).show();
            }
        });

        // 「つかう」ボタン (右下)
        useButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // カードを使用する処理をここに記述
                Toast.makeText(GameActivity.this, card.name + "を使います！", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // ダイアログを閉じる
            }
        });

        dialog.show();
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.TOP; // 画面の上部に配置
        dialog.getWindow().setAttributes(wlp);
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
            cardType.add(rand.nextInt(5)+1);
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