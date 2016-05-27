package csie.yuntech.edu.tw.yangyang_eat_fanfan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class yangyang_homepage extends AppCompatActivity {
    Button button_start, button_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yangyang_homepage);

        button_start = (Button) findViewById(R.id.button_start);
        button_setting = (Button) findViewById(R.id.button_setting);

        //開始按鈕  變換到random店家的頁面
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_start = new Intent();
                intent_start.setClass(yangyang_homepage.this, yangyang_random_code.class);
                startActivity(intent_start);
            }
        });

        //設定按鈕  變換到設定資訊的頁面
        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setting = new Intent();
                intent_setting.setClass(yangyang_homepage.this, yangyang_setting_code.class);
                startActivity(intent_setting);
            }
        });
    }

}
