package com.example.slay_day;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
}