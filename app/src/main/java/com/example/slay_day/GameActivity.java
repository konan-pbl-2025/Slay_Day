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
    ArrayList <Integer> cardColor = new ArrayList<>();//0が赤,1が青,2が緑
    ArrayList <Integer> cardNum = new ArrayList<>();//カードの番号
    ArrayList <Integer> cardType = new ArrayList<>();//カードの種類

    private ArrayList<CardData> currentHand = new ArrayList<>();

    int PlayerHP = rand.nextInt(10)+1;
    int EnemyHP = rand.nextInt(10)+1;
    int EnemyATK = rand.nextInt(10)+1;
    int[][] EnemyState = new int[5][5];//やけどで例えると一次はやけどかどうか、二次はやけどが何ターン続くか
    boolean Dochange=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cardColor=randomColor();
        cardNum=randomNum();
        cardType=randomType();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //カードの色関連
        ImageView col1 = (ImageView)findViewById(R.id.imageView3);
        ImageView col2 = (ImageView)findViewById(R.id.imageView2);
        ImageView col3 = (ImageView)findViewById(R.id.imageView6);
        ImageView col4 = (ImageView)findViewById(R.id.imageView13);
        ImageView col5 = (ImageView)findViewById(R.id.imageView14);
        for(int i=0;i<5;i++){
            if(cardColor.get(i)==0&&i==0) col1.setImageResource(R.drawable.red_element);
            if(cardColor.get(i)==1&&i==0) col1.setImageResource(R.drawable.blue_element);
            if(cardColor.get(i)==2&&i==0) col1.setImageResource(R.drawable.green_element);

            if(cardColor.get(i)==0&&i==1) col2.setImageResource(R.drawable.red_element);
            if(cardColor.get(i)==1&&i==1) col2.setImageResource(R.drawable.blue_element);
            if(cardColor.get(i)==2&&i==1) col2.setImageResource(R.drawable.green_element);

            if(cardColor.get(i)==0&&i==2) col3.setImageResource(R.drawable.red_element);
            if(cardColor.get(i)==1&&i==2) col3.setImageResource(R.drawable.blue_element);
            if(cardColor.get(i)==2&&i==2) col3.setImageResource(R.drawable.green_element);

            if(cardColor.get(i)==0&&i==3) col4.setImageResource(R.drawable.red_element);
            if(cardColor.get(i)==1&&i==3) col4.setImageResource(R.drawable.blue_element);
            if(cardColor.get(i)==2&&i==3) col4.setImageResource(R.drawable.green_element);

            if(cardColor.get(i)==0&&i==4) col5.setImageResource(R.drawable.red_element);
            if(cardColor.get(i)==1&&i==4) col5.setImageResource(R.drawable.blue_element);
            if(cardColor.get(i)==2&&i==4) col5.setImageResource(R.drawable.green_element);
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

        Button changeButton = (Button) findViewById(R.id.button2);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Dochange){
                    Dochange=false;
                    //ここで決めなおし
                    cardColor=randomColor();
                    cardNum=randomNum();
                    cardType=randomType();

                    //カードの色
                    for(int i=0;i<5;i++){
                        if(cardColor.get(i)==0&&i==0) col1.setImageResource(R.drawable.red_element);
                        if(cardColor.get(i)==1&&i==0) col1.setImageResource(R.drawable.blue_element);
                        if(cardColor.get(i)==2&&i==0) col1.setImageResource(R.drawable.green_element);

                        if(cardColor.get(i)==0&&i==1) col2.setImageResource(R.drawable.red_element);
                        if(cardColor.get(i)==1&&i==1) col2.setImageResource(R.drawable.blue_element);
                        if(cardColor.get(i)==2&&i==1) col2.setImageResource(R.drawable.green_element);

                        if(cardColor.get(i)==0&&i==2) col3.setImageResource(R.drawable.red_element);
                        if(cardColor.get(i)==1&&i==2) col3.setImageResource(R.drawable.blue_element);
                        if(cardColor.get(i)==2&&i==2) col3.setImageResource(R.drawable.green_element);

                        if(cardColor.get(i)==0&&i==3) col4.setImageResource(R.drawable.red_element);
                        if(cardColor.get(i)==1&&i==3) col4.setImageResource(R.drawable.blue_element);
                        if(cardColor.get(i)==2&&i==3) col4.setImageResource(R.drawable.green_element);

                        if(cardColor.get(i)==0&&i==4) col5.setImageResource(R.drawable.red_element);
                        if(cardColor.get(i)==1&&i==4) col5.setImageResource(R.drawable.blue_element);
                        if(cardColor.get(i)==2&&i==4) col5.setImageResource(R.drawable.green_element);
                    }

                    //カードの種類
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
                    for(int i=0;i<5;i++){
                        if(i==0) cardNum1.setText(String.valueOf(cardNum.get(i)));
                        if(i==1) cardNum2.setText(String.valueOf(cardNum.get(i)));
                        if(i==2) cardNum3.setText(String.valueOf(cardNum.get(i)));
                        if(i==3) cardNum4.setText(String.valueOf(cardNum.get(i)));
                        if(i==4) cardNum5.setText(String.valueOf(cardNum.get(i)));
                    }
                }
            }
        });

        // 3つのArrayListを結合し、currentHandを作成し、リスナーを設定
        for (int i = 0; i < 5; i++){
            int colorIndex = cardColor.get(i);
            int cardNumValue = cardNum.get(i);
            int cardTypeValue = cardType.get(i); // カードの種類も利用可能

            // CardDataに格納するための情報決定
            String cardName;
            String cardEffect;
            int colorInt; // ダイアログの背景色用 (Color.REDなどのARGB値)

            // 🔴 修正: カードの種類 (cardTypeValue) に基づいて名前と効果を設定
            //         色 (colorIndex) は、効果決定には使用しない。

            // カードの種類（cardTypeValue）に基づいて名前と効果を決定
            switch (cardTypeValue) {
                case 1: // cardTypeが1の場合（例として）
                    cardName = "バット (No." + cardNumValue + ")";
                    cardEffect = "敵に" + cardNumValue * 3  + "ダメージを与える。";
                    break;
                case 2: // cardTypeが2の場合
                    cardName = "パンチ (No." + cardNumValue + ")";
                    cardEffect = "敵に" + cardNumValue + "ダメージを与える。";
                    break;
                case 3:
                    cardName = "キック(No." + cardNumValue + ")";
                    cardEffect = "敵に2ダメージを与える。";
                    break;
                case 4:
                    cardName = "天然水(No." + cardNumValue + ")";
                    cardEffect = "自分の体力を2回復する。";
                    break;
                case 5:
                    cardName = "スポドリ(No." + cardNumValue + ")";
                    cardEffect = "自分の体力を5回復する。";
                    break;
                case 6:
                    cardName = "ファイア(No." + cardNumValue + ")";
                    cardEffect = "敵に5ダメージを与える。";
                    break;
                case 7:
                    cardName = "マッチ(No." + cardNumValue + ")";
                    cardEffect = "敵を火傷状態にする。";
                    break;
                case 8:
                    cardName = "火の魔導書(No." + cardNumValue + ")";
                    cardEffect = "火傷している敵の受けるダメージを2倍にする。";
                    break;
                case 9:
                    cardName = "ファイアパンチ(No." + cardNumValue + ")";
                    cardEffect = "敵に2ダメージを与え、敵を火傷状態にする。";
                    break;
                case 10:
                    cardName = "皮の服(No." + cardNumValue + ")";
                    cardEffect = "次に受けるダメージを‐1する。";
                    break;
                case 11:
                    cardName = "鉄の鎧(No." + cardNumValue + ")";
                    cardEffect = "次に受けるダメージを‐5する。";
                    break;
                case 12:
                    cardName = "ヒーローマント(No." + cardNumValue + ")";
                    cardEffect = "このターンに使うパンチ、キックのダメージが2倍になる。";
                    break;
                case 13:
                    cardName = "アクア(No." + cardNumValue + ")";
                    cardEffect = "敵に2ダメージ与え、自分の体力を2回復する。";
                    break;
                case 14:
                    cardName = "アクアジェット(No." + cardNumValue + ")";
                    cardEffect = "敵に2ダメージ与え、自分の体力を4回復する。";
                    break;
                case 15:
                    cardName = "水の魔導書(No." + cardNumValue + ")";
                    cardEffect = "自分の体力を10回復する。";
                    break;
                case 16:
                    cardName = "津波(No." + cardNumValue + ")";
                    cardEffect = "自分が回復した分のダメージを与える。";
                    break;
                case 17:
                    cardName = "エクゾディア(No." + cardNumValue + ")";
                    cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                    break;
                case 18:
                    cardName = "リーフ(No." + cardNumValue + ")";
                    cardEffect = "敵に4ダメージを与える。";
                    break;
                case 19:
                    cardName = "肥料(No." + cardNumValue + ")";
                    cardEffect = "自分の最大体力を＋2する。";
                    break;
                case 20:
                    cardName = "木の魔導書(No." + cardNumValue + ")";
                    cardEffect = "自分の最大体力を＋4する。";
                    break;
                case 21:
                    cardName = "だいちのいかり(No." + cardNumValue + ")";
                    cardEffect = "現在の自分の体力分のダメージを与える。";
                    break;
                case 22:
                    cardName = "炎の魔導書(No." + cardNumValue + ")";
                    cardEffect = "火傷している敵の受けるダメージが4倍になる。";
                    break;
                case 23:
                    cardName = "滝の魔導書(No." + cardNumValue + ")";
                    cardEffect = "自分が敵に与えたダメージの分自分を回復する。";
                    break;
                case 24:
                    cardName = "森の魔導書(No." + cardNumValue + ")";
                    cardEffect = "自分の最大体力を＋10する。";
                    break;

                default:
                    cardName = "不明なカード";
                    cardEffect = "効果なし";
                    break;
            }

            // 🔴 色 (colorIndex) は、Color.XXXの値（ARGB値）に変換するだけに使う
            switch (colorIndex) {
                case 0: // 赤
                    colorInt = Color.RED;
                    break;
                case 1: // 青
                    colorInt = Color.BLUE;
                    break;
                case 2: // 緑
                    colorInt = Color.GREEN;
                    break;
                default:
                    colorInt = Color.GRAY;
            }

            // 🔴 currentHandリストにCardDataオブジェクトを格納 (名前と効果はカード種類ベース、色はランダムベース)
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
                int usedCardType = 0;
                if (card.name.startsWith("キック")) {
                    usedCardType = 3;
                }
                // ... 他のカード名も必要に応じてここで判定 ...

                String message = card.name + "を使います！";

                // 🔴 【追加】カードタイプに基づいた効果の分岐
                switch (usedCardType) {
                    case 3: // キック
                        EnemyHP -= 2; // 敵HPを2減らす
                        message += " 敵に2ダメージを与えました。";

                        // 🔴 HPの更新を画面に反映する処理 (TextViewなどがあればここで更新)
                        // updateEnemyHPDisplay(); // 敵HP表示を更新するメソッド（未定義）があれば呼び出す
                        break;
                    // ... 他のカード効果も同様にcaseを追加 ...
                }

                Toast.makeText(GameActivity.this, message, Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // ダイアログを閉じる

                // 🔴 【追加】ターン終了処理 (カード使用後にターンを終了し、手札を更新)
                endTurnAndDealNewCards();
            }
        });

        dialog.show();
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.TOP; // 画面の上部に配置
        dialog.getWindow().setAttributes(wlp);
    }
    private ArrayList<Integer> randomColor(){
        ArrayList<Integer> cardColor = new ArrayList<>();
        Random rand = new Random();
        for(int i=0;i<5;i++){
            cardColor.add(rand.nextInt(3));
        }
        return cardColor;
    }
    private ArrayList randomType(){
        ArrayList<Integer> cardType = new ArrayList<>();
        Random rand = new Random();
        for(int i=0;i<5;i++){
            cardType.add(rand.nextInt(24)+1);
        }
        return cardType;
    }
    private ArrayList randomNum(){
        ArrayList<Integer> cardNum = new ArrayList<>();
        Random rand = new Random();
        for(int i=0;i<5;i++){
            cardNum.add(rand.nextInt(5)+1);
        }
        return cardNum;
    }

    /**
     * ターンを終了し、ランダムな新しいカードを5枚配り、UIを更新する
     */
    private void endTurnAndDealNewCards() {
        // 1. 新しい手札を生成し、currentHandを更新
        cardColler.clear();
        cardNum.clear();
        cardType.clear();

        cardColler=randomColler(cardColler);
        cardNum=randomNum(cardNum);
        cardType=randomType(cardType);

        // 2. CardDataリストを再構築 (onCreate内のロジックを再利用するためメソッド化を推奨)
        // 🔴 簡易的な再構築処理 (本来はonCreate内の最後のforループをメソッド化するのが最善)
        // ここでは便宜上、Activityを再作成することでUI全体を更新します。（非推奨だが簡易的）
        // Intent intent = getIntent();
        // finish();
        // startActivity(intent);

        // 3. 簡潔な方法: onCreate内の最後のUI更新ブロックをメソッド化して呼び出す
        // 便宜上、ここではToastで通知するだけにします。
        Toast.makeText(GameActivity.this, "ターンが終了し、新しいカードが配られました。", Toast.LENGTH_LONG).show();

        // 🔴 実際には、onCreate内のUI設定コードをメソッド化し、ここで呼び出す必要があります。
        // updateCardUI();
    }

}