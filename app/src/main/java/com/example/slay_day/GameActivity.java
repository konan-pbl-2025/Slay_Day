package com.example.slay_day;

import android.content.Intent;
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
    private ArrayList<Integer> useCard = new ArrayList<>();
    private HashSet<Integer> useCardSet = new HashSet<>();


    private double PlayerMaxHP = 20;
    private double PlayerHP = PlayerMaxHP;
    private double PlayerDef = 0;//防御カード使った時どれぐらいダメージ軽減するか
    private double PlayerATKUP = 1;
    private double PlayerHealUP = 1;

    private double heroUP=1;//ヒーローマンと使ったときの上昇率
    private double EnemyHP = 100;
    private double EnemyATK = 2;
    private double EnemyDefDown=1;//被ダメージ何倍か


    private int[][] EnemyState = new int[5][5];//やけどで例えると一次はやけどかどうか、二次はやけどが何ターン続くか
    private double totalHeal=0;//どれだけ回復したか
    private double totalDamage=0;//どれだけ攻撃したか

    private int useSize=0;
    private int turnCount = 1; // 最初のターンは1から開始
    boolean Dochange=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cardColor=randomColor();
        cardNum=randomNum();
        cardType=randomType();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        TextView TEXEnemyHP = findViewById(R.id.ENEHP);
        TextView yaku = (TextView)findViewById(R.id.yaku);
        TEXEnemyHP.setText(String.valueOf(EnemyHP));

        TextView use1 = (TextView)findViewById(R.id.use1);
        TextView use2 = (TextView)findViewById(R.id.use2);
        TextView use3 = (TextView)findViewById(R.id.use3);
        TextView use4 = (TextView)findViewById(R.id.use4);
        TextView use5 = (TextView)findViewById(R.id.use5);


        use1.setText("");
        use2.setText("");
        use3.setText("");
        use4.setText("");
        use5.setText("");
        updatePlayerHPDisplay();


        //カードの色関連
        ImageView col1 = (ImageView)findViewById(R.id.imageView3);
        ImageView col2 = (ImageView)findViewById(R.id.imageView2);
        ImageView col3 = (ImageView)findViewById(R.id.imageView6);
        ImageView col4 = (ImageView)findViewById(R.id.imageView13);
        ImageView col5 = (ImageView)findViewById(R.id.imageView14);
        TextView TEXTurnCount = findViewById(R.id.textView);
        TEXTurnCount.setText("ターン: " + String.valueOf(turnCount));
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
        String[] cardID = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","あ","い"};
        String [] ID = new String[5];
        for(int i=0;i<5;i++){
            char letter = (char) ('a' + (cardType.get(i) - 1));
            ID[i] = Character.toString(letter);
        }
        for(int i=0;i<5;i++){
            int resId = getResources().getIdentifier(ID[i], "drawable", getPackageName());
            if(i==0) card1.setImageResource(resId);
            if(i==1) card2.setImageResource(resId);
            if(i==2) card3.setImageResource(resId);
            if(i==3) card4.setImageResource(resId);
            if(i==4) card5.setImageResource(resId);
        }
        if(cardType.get(0)==27){
            card1.setImageResource(R.drawable.a1);
        }
        if(cardType.get(1)==27){
            card2.setImageResource(R.drawable.a1);
        }
        if(cardType.get(2)==27){
            card3.setImageResource(R.drawable.a1);
        }
        if(cardType.get(3)==27){
            card4.setImageResource(R.drawable.a1);
        }
        if(cardType.get(4)==27){
            card5.setImageResource(R.drawable.a1);
        }

        if(cardType.get(0)==28){
            card1.setImageResource(R.drawable.a2);
        }
        if(cardType.get(1)==28){
            card2.setImageResource(R.drawable.a2);
        }
        if(cardType.get(2)==28){
            card3.setImageResource(R.drawable.a2);
        }
        if(cardType.get(3)==28){
            card4.setImageResource(R.drawable.a2);
        }
        if(cardType.get(4)==28){
            card5.setImageResource(R.drawable.a2);
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

        //ボタンの定義
        Button changeButton = (Button) findViewById(R.id.button2);
        Button PlayButton = (Button) findViewById(R.id.button3);
        Button resetButton = (Button) findViewById(R.id.reset);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentHand.clear();
                useSize=0;
                use1.setText("");
                use2.setText("");
                use3.setText("");
                use4.setText("");
                use5.setText("");
                useCard.clear();
                if(Dochange){
                    //Dochange=false;
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
                    if(cardType.get(0)==27){
                        card1.setImageResource(R.drawable.a1);
                    }
                    if(cardType.get(1)==27){
                        card2.setImageResource(R.drawable.a1);
                    }
                    if(cardType.get(2)==27){
                        card3.setImageResource(R.drawable.a1);
                    }
                    if(cardType.get(3)==27){
                        card4.setImageResource(R.drawable.a1);
                    }
                    if(cardType.get(4)==27){
                        card5.setImageResource(R.drawable.a1);
                    }

                    if(cardType.get(0)==28){
                        card1.setImageResource(R.drawable.a2);
                    }
                    if(cardType.get(1)==28){
                        card2.setImageResource(R.drawable.a2);
                    }
                    if(cardType.get(2)==28){
                        card3.setImageResource(R.drawable.a2);
                    }
                    if(cardType.get(3)==28){
                        card4.setImageResource(R.drawable.a2);
                    }
                    if(cardType.get(4)==28){
                        card5.setImageResource(R.drawable.a2);
                    }


                    //カードの数字
                    for(int i=0;i<5;i++){
                        if(i==0) cardNum1.setText(String.valueOf(cardNum.get(i)));
                        if(i==1) cardNum2.setText(String.valueOf(cardNum.get(i)));
                        if(i==2) cardNum3.setText(String.valueOf(cardNum.get(i)));
                        if(i==3) cardNum4.setText(String.valueOf(cardNum.get(i)));
                        if(i==4) cardNum5.setText(String.valueOf(cardNum.get(i)));
                    }

                    // CardDataに格納するための情報決定(ダイアログ情報を更新するため)by廣瀬
                    for (int i = 0; i < 5; i++){
                        int colorIndex = cardColor.get(i);
                        int cardNumValue = cardNum.get(i);
                        int cardTypeValue = cardType.get(i); // カードの種類も利用可能

                        String cardName;
                        String cardEffect;
                        int colorInt; // ダイアログの背景色用 (Color.REDなどのARGB値)

                        switch (cardTypeValue) {
                            case 1: // cardTypeが1の場合（例として）
                                cardName = "バット(No." + cardNumValue + ")";
                                cardEffect = "敵に" + cardNumValue * 3  + "ダメージを与える。";
                                break;
                            case 2: // cardTypeが2の場合
                                cardName = "パンチ(No." + cardNumValue + ")";
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
                                cardName = "エクゾディア(頭)(No." + cardNumValue + ")";
                                cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                                break;
                            case 18:
                                cardName = "エクゾディア(右腕)(No." + cardNumValue + ")";
                                cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                                break;
                            case 19:
                                cardName = "エクゾディア(左腕)(No." + cardNumValue + ")";
                                cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                                break;
                            case 20:
                                cardName = "エクゾディア(右脚)(No." + cardNumValue + ")";
                                cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                                break;
                            case 21:
                                cardName = "エクゾディア(左脚)(No." + cardNumValue + ")";
                                cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                                break;
                            case 22:
                                cardName = "リーフ(No." + cardNumValue + ")";
                                cardEffect = "敵に4ダメージを与える。";
                                break;
                            case 23:
                                cardName = "肥料(No." + cardNumValue + ")";
                                cardEffect = "自分の最大体力を＋2する。";
                                break;
                            case 24:
                                cardName = "木の魔導書(No." + cardNumValue + ")";
                                cardEffect = "自分の最大体力を＋4する。";
                                break;
                            case 25:
                                cardName = "だいちのいかり(No." + cardNumValue + ")";
                                cardEffect = "現在の自分の体力分のダメージを与える。";
                                break;
                            case 26:
                                cardName = "炎の魔導書(No." + cardNumValue + ")";
                                cardEffect = "火傷している敵の受けるダメージが4倍になる。";
                                break;
                            case 27:
                                cardName = "滝の魔導書(No." + cardNumValue + ")";
                                cardEffect = "自分が敵に与えたダメージの分自分を回復する。";
                                break;
                            case 28:
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
                    }
                }
            }
        });

        PlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList <Integer> useCardNum = new ArrayList<>();
                ArrayList <Integer> useCardColor = new ArrayList<>();
                int total=0;
                for(int i:useCard){
                    useCardNum.add(cardNum.get(i));
                    useCardColor.add(cardColor.get(i));
                }
                String ans=judgeHand(useCardNum,useCardColor);
                yaku.setText(ans);
                if(!ans.equals("ブタ")){
                    if(ans.equals("ツーペア")){
                        EnemyDefDown=0.5;
                        PlayerHealUP=0.5;
                    }
                    if(ans.equals("ストレート")){
                        EnemyDefDown=0.5;
                        PlayerHealUP=0.5;
                    }
                    if(ans.equals("フラッシュ")){
                        EnemyDefDown=0.5;
                        PlayerHealUP=0.5;
                    }
                    if(ans.equals("フルハウス")){
                        EnemyDefDown=0.5;
                        PlayerHealUP=0.5;
                    }
                    if(ans.equals("ストレートフラッシュ")){
                        EnemyDefDown=0.5;
                        PlayerHealUP=0.5;
                    }
                    for(int i:useCard){
                        if(cardType.get(i)==1) bat();
                        if(cardType.get(i)==2) punch();
                        if(cardType.get(i)==3) kick();
                        if(cardType.get(i)==4) tennensui();
                        if(cardType.get(i)==5) sportsDrink();
                        if(cardType.get(i)==6) fire();
                        if(cardType.get(i)==7) match();
                        if(cardType.get(i)==8) fireMagicBook();
                        if(cardType.get(i)==9) firePunch();
                        if(cardType.get(i)==10) leather();
                        if(cardType.get(i)==11) iron();
                        if(cardType.get(i)==12) heroMant();
                        if(cardType.get(i)==13) aqour();
                        if(cardType.get(i)==14) aqourJet();
                        if(cardType.get(i)==15) waterMagicBook();
                        if(cardType.get(i)==16) tunami();
                        if(cardType.get(i)==17) bat();//考え中
                        if(cardType.get(i)==18) leaf();
                        if(cardType.get(i)==19) hiryou();
                        if(cardType.get(i)==20) treeMagicBook();
                        if(cardType.get(i)==21) angerOfEarth();
                        if(cardType.get(i)==22) flameMagikBook();
                        if(cardType.get(i)==23) takiMagicBook();
                        if(cardType.get(i)==24) forestMagicBook();

                    }
                }else{
                    if(useSize>1) {
                        String ButaMessage =
                                "役がブタの場合はカードは一つしか使えません"; // \nで改行
                        Toast.makeText(GameActivity.this, ButaMessage, Toast.LENGTH_LONG).show();
                        return;
                    }else{
                        for(int i:useCard){
                            if(cardType.get(i)==1) bat();
                            if(cardType.get(i)==2) punch();
                            if(cardType.get(i)==3) kick();
                            if(cardType.get(i)==4) tennensui();
                            if(cardType.get(i)==5) sportsDrink();
                            if(cardType.get(i)==6) fire();
                            if(cardType.get(i)==7) match();
                            if(cardType.get(i)==8) fireMagicBook();
                            if(cardType.get(i)==9) firePunch();
                            if(cardType.get(i)==10) leather();
                            if(cardType.get(i)==11) iron();
                            if(cardType.get(i)==12) heroMant();
                            if(cardType.get(i)==13) aqour();
                            if(cardType.get(i)==14) aqourJet();
                            if(cardType.get(i)==15) waterMagicBook();
                            if(cardType.get(i)==16) tunami();
                            if(cardType.get(i)==17) bat();//考え中
                            if(cardType.get(i)==18) leaf();
                            if(cardType.get(i)==19) hiryou();
                            if(cardType.get(i)==20) treeMagicBook();
                            if(cardType.get(i)==21) angerOfEarth();
                            if(cardType.get(i)==22) flameMagikBook();
                            if(cardType.get(i)==23) takiMagicBook();
                            if(cardType.get(i)==24) forestMagicBook();

                        }
                    }
                }
                TextView TEXEnemyHP = findViewById(R.id.ENEHP);
                TEXEnemyHP.setText(String.valueOf(EnemyHP));

                int damageFromEnemy = enemyAttack();
                updatePlayerHPDisplay();
                String resultMessage =
                        "カード使用結果: ダメージ " + totalDamage + " / 回復 " + totalHeal +
                                "\n敵の攻撃: " + damageFromEnemy + " ダメージ受けた！"; // \nで改行
                if(EnemyHP<=0){
                        Intent intent = new Intent(GameActivity.this, GameClearActivity.class);
                        startActivity(intent);
                }else if(PlayerHP<=0){
                        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                        startActivity(intent);
                }
                Toast.makeText(GameActivity.this, resultMessage, Toast.LENGTH_LONG).show();
                // 🔴 【追加】ターン数をインクリメントし、画面を更新
                turnCount++;
                TextView TEXTurnCount = findViewById(R.id.textView);
                TEXTurnCount.setText("ターン: " + String.valueOf(turnCount));

                // 🔴 【追加】ターン開始時のカウンター/バフをリセット
                totalDamage = 0;
                totalHeal = 0;

                //選択状態解除
                useSize=0;
                use1.setText("");
                use2.setText("");
                use3.setText("");
                use4.setText("");
                use5.setText("");
                useCard.clear();

                // 1. 古いリストをクリア
                currentHand.clear();
                cardColor.clear();
                cardNum.clear();
                cardType.clear();

                // 2. 新しいカードを生成
                cardColor = randomColor();
                cardNum = randomNum();
                cardType = randomType();

                // 3. UI要素を再度取得 (onCreateのコードを複製)
                // 🔴 注意: このブロックをonCreateのコードに合わせて完全に追加してください
                String [] ID = new String[5];
                String cardName;
                String cardEffect;
                int colorInt;

                // --- 3-A: カードの色関連の再設定 ---
                ImageView col1 = (ImageView)findViewById(R.id.imageView3);
                ImageView col2 = (ImageView)findViewById(R.id.imageView2);
                ImageView col3 = (ImageView)findViewById(R.id.imageView6);
                ImageView col4 = (ImageView)findViewById(R.id.imageView13);
                ImageView col5 = (ImageView)findViewById(R.id.imageView14);

                for(int i=0;i<5;i++){
                    // 既存の長いif文のロジックをここに複製
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

                // --- 3-B: カードの種類（画像）の再設定 ---
                ImageView card1 = (ImageView)findViewById(R.id.imageView11);
                ImageView card2 = (ImageView)findViewById(R.id.imageView12);
                ImageView card3 = (ImageView)findViewById(R.id.imageView8);
                ImageView card4 = (ImageView)findViewById(R.id.imageView10);
                ImageView card5 = (ImageView)findViewById(R.id.imageView9);

                String [] id = new String[5];
                for(int i=0;i<5;i++){
                    char letter =(char)('a'+(cardType.get(i)-1));
                    ID[i]=Character.toString(letter);
                }
                for(int i=0;i<5;i++){
                    int resId = getResources().getIdentifier(ID[i], "drawable", getPackageName());
                    // 既存の長いif文のロジックをここに複製
                    if(i==0) card1.setImageResource(resId);
                    if(i==1) card2.setImageResource(resId);
                    if(i==2) card3.setImageResource(resId);
                    if(i==3) card4.setImageResource(resId);
                    if(i==4) card5.setImageResource(resId);
                }

                if(cardType.get(0)==27){ card1.setImageResource(R.drawable.a1); }
                if(cardType.get(1)==27){ card2.setImageResource(R.drawable.a1); }
                if(cardType.get(2)==27){ card3.setImageResource(R.drawable.a1); }
                if(cardType.get(3)==27){ card4.setImageResource(R.drawable.a1); }
                if(cardType.get(4)==27){ card5.setImageResource(R.drawable.a1); }

                if(cardType.get(0)==28){ card1.setImageResource(R.drawable.a2); }
                if(cardType.get(1)==28){ card2.setImageResource(R.drawable.a2); }
                if(cardType.get(2)==28){ card3.setImageResource(R.drawable.a2); }
                if(cardType.get(3)==28){ card4.setImageResource(R.drawable.a2); }
                if(cardType.get(4)==28){ card5.setImageResource(R.drawable.a2); }

                // --- 3-C: カードの数字の再設定 ---
                TextView cardNum1 = (TextView)findViewById(R.id.textView4);
                TextView cardNum2 = (TextView)findViewById(R.id.textView5);
                TextView cardNum3 = (TextView)findViewById(R.id.textView8);
                TextView cardNum4 = (TextView)findViewById(R.id.textView6);
                TextView cardNum5 = (TextView)findViewById(R.id.textView7);

                for(int i=0;i<5;i++){
                    // 既存の長いif文のロジックをここに複製
                    if(i==0) cardNum1.setText(String.valueOf(cardNum.get(i)));
                    if(i==1) cardNum2.setText(String.valueOf(cardNum.get(i)));
                    if(i==2) cardNum3.setText(String.valueOf(cardNum.get(i)));
                    if(i==3) cardNum4.setText(String.valueOf(cardNum.get(i)));
                    if(i==4) cardNum5.setText(String.valueOf(cardNum.get(i)));

                }

                // 4. currentHandリストの再構築（onCreateの最後のforループから）
                ImageView[] cardTapViews = new ImageView[5]; // リスナー再設定用
                // ... (cardTapViews の findViewById 取得) ...
                cardTapViews[0] = (ImageView)findViewById(R.id.imageView11);
                cardTapViews[1] = (ImageView)findViewById(R.id.imageView12);
                cardTapViews[2] = (ImageView)findViewById(R.id.imageView8);
                cardTapViews[3] = (ImageView)findViewById(R.id.imageView10);
                cardTapViews[4] = (ImageView)findViewById(R.id.imageView9);

                for (int i = 0; i < 5; i++){
                    // 既存のcurrentHand再構築ロジックをすべて複製
                    // ... (colorIndex, cardNumValue, cardTypeValue の取得) ...
                    // ... (switch文による cardName, cardEffect, colorInt の決定) ...
                    int colorIndex = cardColor.get(i);
                    int cardNumValue = cardNum.get(i);
                    int cardTypeValue = cardType.get(i);
                    colorInt = Color.GRAY;
                    switch (cardTypeValue) {
                        case 1: // cardTypeが1の場合（例として）
                            cardName = "バット(No." + cardNumValue + ")";
                            cardEffect = "敵に" + cardNumValue * 3  + "ダメージを与える。";
                            break;
                        case 2: // cardTypeが2の場合
                            cardName = "パンチ(No." + cardNumValue + ")";
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
                            cardName = "エクゾディア(頭)(No." + cardNumValue + ")";
                            cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                            break;
                        case 18:
                            cardName = "エクゾディア(右腕)(No." + cardNumValue + ")";
                            cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                            break;
                        case 19:
                            cardName = "エクゾディア(左腕)(No." + cardNumValue + ")";
                            cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                            break;
                        case 20:
                            cardName = "エクゾディア(右脚)(No." + cardNumValue + ")";
                            cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                            break;
                        case 21:
                            cardName = "エクゾディア(左脚)(No." + cardNumValue + ")";
                            cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                            break;
                        case 22:
                            cardName = "リーフ(No." + cardNumValue + ")";
                            cardEffect = "敵に4ダメージを与える。";
                            break;
                        case 23:
                            cardName = "肥料(No." + cardNumValue + ")";
                            cardEffect = "自分の最大体力を＋2する。";
                            break;
                        case 24:
                            cardName = "木の魔導書(No." + cardNumValue + ")";
                            cardEffect = "自分の最大体力を＋4する。";
                            break;
                        case 25:
                            cardName = "だいちのいかり(No." + cardNumValue + ")";
                            cardEffect = "現在の自分の体力分のダメージを与える。";
                            break;
                        case 26:
                            cardName = "炎の魔導書(No." + cardNumValue + ")";
                            cardEffect = "火傷している敵の受けるダメージが4倍になる。";
                            break;
                        case 27:
                            cardName = "滝の魔導書(No." + cardNumValue + ")";
                            cardEffect = "自分が敵に与えたダメージの分自分を回復する。";
                            break;
                        case 28:
                            cardName = "森の魔導書(No." + cardNumValue + ")";
                            cardEffect = "自分の最大体力を＋10する。";
                            break;

                        default:
                            cardName = "不明なカード";
                            cardEffect = "効果なし";
                            break;
                    }

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
                            break; // breakを追加
                    }

                    CardData newCard = new CardData(cardName, cardEffect, cardNumValue, colorInt);
                    currentHand.add(newCard);

                    // 🔴 クリックリスナーも再設定 (これで新しいカード情報でダイアログが開く)
                    final int cardIndex = i;
                    cardTapViews[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CardData selectedCard = currentHand.get(cardIndex);
                            showCardDetail(selectedCard,cardIndex);
                        }
                    });
                }

                // ----------------------------------------------------

                // 5. ターン固有のバフ/デバフをリセット
                PlayerDef = 0;
                heroUP = 1;
                EnemyDefDown = 1;
                // ... (他のリセットが必要なら追加) ...
                useCard.clear();
                useCardSet.clear();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                useSize=0;
                use1.setText("");
                use2.setText("");
                use3.setText("");
                use4.setText("");
                use5.setText("");
                useCard.clear();
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
                    cardName = "バット(No." + cardNumValue + ")";
                    cardEffect = "敵に" + cardNumValue * 3  + "ダメージを与える。";
                    break;
                case 2: // cardTypeが2の場合
                    cardName = "パンチ(No." + cardNumValue + ")";
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
                    cardName = "エクゾディア(頭)(No." + cardNumValue + ")";
                    cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                    break;
                case 18:
                    cardName = "エクゾディア(右腕)(No." + cardNumValue + ")";
                    cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                    break;
                case 19:
                    cardName = "エクゾディア(左腕)(No." + cardNumValue + ")";
                    cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                    break;
                case 20:
                    cardName = "エクゾディア(右脚)(No." + cardNumValue + ")";
                    cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                    break;
                case 21:
                    cardName = "エクゾディア(左脚)(No." + cardNumValue + ")";
                    cardEffect = "このカードが5枚揃うと無条件に勝利する。";
                    break;
                case 22:
                    cardName = "リーフ(No." + cardNumValue + ")";
                    cardEffect = "敵に4ダメージを与える。";
                    break;
                case 23:
                    cardName = "肥料(No." + cardNumValue + ")";
                    cardEffect = "自分の最大体力を＋2する。";
                    break;
                case 24:
                    cardName = "木の魔導書(No." + cardNumValue + ")";
                    cardEffect = "自分の最大体力を＋4する。";
                    break;
                case 25:
                    cardName = "だいちのいかり(No." + cardNumValue + ")";
                    cardEffect = "現在の自分の体力分のダメージを与える。";
                    break;
                case 26:
                    cardName = "炎の魔導書(No." + cardNumValue + ")";
                    cardEffect = "火傷している敵の受けるダメージが4倍になる。";
                    break;
                case 27:
                    cardName = "滝の魔導書(No." + cardNumValue + ")";
                    cardEffect = "自分が敵に与えたダメージの分自分を回復する。";
                    break;
                case 28:
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
                    showCardDetail(selectedCard,cardIndex);
                }
            });
        }

    }//ここまでmain

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

    private void showCardDetail(CardData card, int i) {
        TextView use1 = (TextView)findViewById(R.id.use1);
        TextView use2 = (TextView)findViewById(R.id.use2);
        TextView use3 = (TextView)findViewById(R.id.use3);
        TextView use4 = (TextView)findViewById(R.id.use4);
        TextView use5 = (TextView)findViewById(R.id.use5);
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
                // カードを登録
                if(!useCard.contains(i)) {
                    useCard.add(i);
                    useCardSet.add(i);
                    useSize++;
                    if(i==0){
                        use1.setText(String.valueOf(useSize));
                    }
                    if(i==1){
                        use2.setText(String.valueOf(useSize));
                    }
                    if(i==2){
                        use3.setText(String.valueOf(useSize));
                    }
                    if(i==3){
                        use4.setText(String.valueOf(useSize));
                    }
                    if(i==4){
                        use5.setText(String.valueOf(useSize));
                    }
                }

                Toast.makeText(GameActivity.this, card.name + "を使います！", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // ダイアログを閉じる
            }
        });

        dialog.show();
        WindowManager.LayoutParams wlp = dialog.getWindow().getAttributes();
        wlp.gravity = Gravity.TOP; // 画面の上部に配置
        dialog.getWindow().setAttributes(wlp);
    }//ここまでshowcard

    private int enemyAttack() {
        int damageTaken = (int)EnemyATK; // 敵の攻撃力

        // 敵の状態や防御カードによる軽減処理（必要に応じて追加）
        // if (PlayerHasDefense) damageTaken -= 1; など

        PlayerHP -= damageTaken;

        // 🔴トースト表示を削除
        // Toast.makeText(GameActivity.this, "敵から " + damageTaken + " ダメージ受けた！", Toast.LENGTH_LONG).show();

        return damageTaken; // 🔴受けたダメージを返す
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
            cardType.add(rand.nextInt(28)+1);
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

    private void bat(){
        int batDamage=2;//バットの攻撃力
        EnemyHP=EnemyHP-batDamage*EnemyDefDown;
        totalDamage+=batDamage*EnemyDefDown;
    }

    private void punch(){
        int punchDamage=1;//攻撃力
        EnemyHP=EnemyHP-punchDamage*EnemyDefDown*heroUP;
        totalDamage+=punchDamage*EnemyDefDown*heroUP;
    }

    private void kick(){
        int kickDamage=1;//攻撃力
        EnemyHP=EnemyHP-kickDamage*EnemyDefDown+heroUP;
        totalDamage=kickDamage*EnemyDefDown+heroUP;
    }

    private void tennensui(){
        int heal=2;//回復量
        PlayerHP=PlayerHP+heal*PlayerHealUP;
        totalHeal+=heal*PlayerHealUP;
        if(PlayerHP>PlayerMaxHP) PlayerHP=PlayerMaxHP;
    }

    private void sportsDrink(){
        int heal=4;//回復量
        PlayerHP=PlayerHP+heal*PlayerHealUP;
        totalHeal+=heal*PlayerHealUP;
        if(PlayerHP>PlayerMaxHP) PlayerHP=PlayerMaxHP;
    }

    private void fire(){
        int fireDamage=4;
        EnemyHP=EnemyHP-fireDamage*EnemyDefDown;
        totalDamage=fireDamage*EnemyDefDown;
    }

    private void match(){
        int yakedoFlag=1;
        int yakedoTurn=1;

        EnemyState[0][0]=yakedoFlag;
        EnemyState[0][1]=yakedoTurn;

    }

    private void fireMagicBook(){
        if(EnemyState[0][0]==1) {
            EnemyDefDown*=2;
        }
    }

    private void firePunch(){
        EnemyState[0][0]=1;//やけど状態にする
        EnemyState[0][1]=2;//何ターン続くか
        EnemyHP-=EnemyHP*EnemyDefDown;
        totalDamage+=EnemyHP*EnemyDefDown;
    }

    private void leather(){
        PlayerDef+=1;
    }

    private void iron(){
        PlayerDef+=5;
    }

    private void heroMant(){
        heroUP=2;
    }

    private void aqour(){
        EnemyHP-=2*EnemyDefDown;
        PlayerHP+=2*PlayerHealUP;
        totalDamage+=2*EnemyDefDown;
        totalHeal+=2*PlayerHealUP;
        if(PlayerHP>PlayerMaxHP) PlayerHP=PlayerMaxHP;
    }

    private void aqourJet(){
        EnemyHP-=2*EnemyDefDown;
        PlayerHP+=4*PlayerHealUP;
        totalDamage+=2*EnemyDefDown;
        totalHeal+=4*PlayerHealUP;
        if(PlayerHP>PlayerMaxHP) PlayerHP=PlayerMaxHP;
    }

    private void waterMagicBook(){
        PlayerHP+=10*PlayerHealUP;
        totalHeal+=10*PlayerHealUP;
        if(PlayerHP>PlayerMaxHP) PlayerHP=PlayerMaxHP;
    }

    private void tunami(){
        EnemyHP-=totalHeal;
        totalDamage+=totalHeal;
    }

    private void leaf(){
        EnemyHP-=4;
        totalDamage+=4;
    }

    private void hiryou(){
        PlayerMaxHP+=2;
    }

    private void treeMagicBook(){
        PlayerMaxHP+=4;
    }

    private void angerOfEarth(){
        EnemyHP-=PlayerHP;
        totalDamage+=PlayerHP;
    }

    private void flameMagikBook(){
        EnemyDefDown*=4;
    }

    private void takiMagicBook(){
        EnemyHP-=totalDamage;
        totalDamage+=totalHeal;
    }

    private void forestMagicBook(){
        PlayerMaxHP+=10;

    }

    private void updatePlayerHPDisplay() {
        TextView TEXPlayerHP = findViewById(R.id.pleyerHP);

        // 🔴 画面表示を更新 (String.formatを使用して小数点以下を切り捨て)
        // この形式が、My HP: xx/yy の表示に最適です。
        String hpText = String.format("My HP: %.0f/%.0f", PlayerHP, PlayerMaxHP);
        TEXPlayerHP.setText(hpText);
    }







    public static String judgeHand(ArrayList<Integer> cardNum, ArrayList<Integer> cardColor) {

        int n = cardNum.size();
        if (n < 1 || n > 5) return "カード枚数エラー";

        // --- 数字カウント ---
        Map<Integer, Integer> numCount = new HashMap<>();
        // --- 色カウント ---
        Map<Integer, Integer> colorCount = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int num = cardNum.get(i);
            int col = cardColor.get(i);

            numCount.merge(num, 1, Integer::sum);
            colorCount.merge(col, 1, Integer::sum);
        }

        // --- ソートしてストレート判定 ---
        ArrayList<Integer> nums = new ArrayList<>(cardNum);
        Collections.sort(nums);

        boolean isStraight = true;
        if (n >= 3) { // ストレートは3枚以上から成立可能にする
            for (int i = 0; i < n - 1; i++) {
                if (nums.get(i) + 1 != nums.get(i + 1)) {
                    isStraight = false;
                    break;
                }
            }
        } else {
            isStraight = false;
        }

        // --- フラッシュ判定 ---
        boolean isFlush = (n >= 3) && colorCount.containsValue(n);

        // --- 重複数カウント ---
        List<Integer> counts = new ArrayList<>(numCount.values());
        counts.sort(Collections.reverseOrder()); // 大きい順

        // --- 役判定 ---

        // ● 枚数1
        if (n == 1) return "ブタ";

        // ● 枚数2
        if (n == 2) {
            if (counts.get(0) == 2) return "ワンペア";
            return "ブタ";
        }

        // ● 枚数3
        if (n == 3) {
            if (counts.get(0) == 3) return "スリーカード";
            if (counts.get(0) == 2) return "ワンペア";
            return "ブタ";
        }

        // ● 枚数4 or 5 で共通の役
        if (isStraight && isFlush) return "ストレートフラッシュ";
        if (counts.get(0) == 4) return "フォーカード";
        if (counts.size() == 2 && counts.get(0) == 3) return "フルハウス"; // 3+1 or 3+2
        if (isFlush) return "フラッシュ";
        if (isStraight) return "ストレート";
        if (counts.get(0) == 3) return "スリーカード";
        if (counts.size() == 3 && counts.get(0) == 2 && counts.get(1) == 2) return "ツーペア";
        if (counts.get(0) == 2) return "ワンペア";

        return "ブタ";
    }

}