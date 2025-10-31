package com.example.slay_day;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

    }

    private void showCardDetail() {
        // 1. Builderの作成
        // (this) は、ダイアログを表示するアクティビティ（GameActivity）を指定
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. ダイアログの要素を設定
        builder.setTitle("確認") // ダイアログのタイトル
                .setMessage("行動を決定しますか？") // 表示するメッセージ

                // Positive Button (肯定的ボタン：例. OK, 決定) の設定
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    // ボタンがクリックされた時の処理を記述
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // 「はい」が押された時の処理
                        Toast.makeText(GameActivity.this, "行動を決定しました！", Toast.LENGTH_SHORT).show();
                    }
                })

                // Negative Button (否定的ボタン：例. キャンセル, いいえ) の設定
                .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                    // ボタンがクリックされた時の処理を記述
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // ダイアログを閉じる処理（通常は自動で閉じますが、明示的に記述することも可能）
                        dialog.dismiss();
                        Toast.makeText(GameActivity.this, "キャンセルしました。", Toast.LENGTH_SHORT).show();
                    }
                });

        // 3. ダイアログの作成と表示
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}