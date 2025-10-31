package com.example.slay_day;
import org.junit.Test;

import java.util.*;

public class test{
    @Test
    public void test(){
        int EnemyHp=5, PlayerHp=10;
        int[][] EnemyState = new int[5][5];//やけどで例えると一次はやけどかどうか、二次はやけどが何ターン続くか
        //[0][0]がやけど
        //この下でバットていうカード効果を使用
        EnemyState=match(EnemyState);
    }
    //この下の奴がバットっていうカードの効果をプログラムしてる。
    private int bat(int EnemyHp){
        int batDamage=2;//攻撃力
        int ans=0;//返り値
        ans=EnemyHp-batDamage;
        return ans;
    }

    private int punch(int EnemyHp){
        int punchDamage=1;//攻撃力
        int ans=0;//返り値
        ans=EnemyHp-punchDamage;
        return ans;
    }

    private int kick(int EnemyHp){
        int kickDamage=1;//攻撃力
        int ans=0;//返り値
        ans=EnemyHp-kickDamage;
        return ans;
    }

    private int tennensui(int PlayerHp){
        int heal=2;//回復量
        int ans=0;//返り値
        ans=PlayerHp+heal;
        return ans;
    }

    private int sportsDrink(int PlayerHp){
        int heal=4;//回復量
        int ans=0;//返り値
        ans=PlayerHp+heal;
        return ans;
    }

    private int fire(int EnemyHp){
        int fireDamage=4;//回復量
        int ans=0;//返り値
        ans=EnemyHp-fireDamage;
        return ans;
    }

    private int[][] match(int[][] EnemyState){
        int yakedoFlag=1;
        int yakedoTurn=1;

        EnemyState[0][0]=yakedoFlag;
        EnemyState[0][1]=yakedoTurn;

        return EnemyState;
    }

    private int fireMagicBook(int[][] EnemyState){
        int EnemyHidame=2;//被ダメージ2倍
        if(EnemyState[0][0]==1){
            return EnemyHidame;
        }
        return 1;
    }



}
