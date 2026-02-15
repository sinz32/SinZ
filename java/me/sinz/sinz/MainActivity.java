package me.sinz.sinz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        Toolbar title = SinZ.createTitle(this, "SinZ");
        layout.addView(title);

        String[] menus = {"음성 입력 실행", "실시간 전철 위치", "교통카드 잔액조회", "여자친구 노래방 수록곡", "앱 정보 & 도움말"};
        ListView list = new ListView(this);
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menus));
        list.setOnItemClickListener((adapterView, view, pos, id) -> {
            switch ((int)id) {
                case 0:
                    startService(new Intent(this, MainService.class));
                    break;
                case 1:
                    startActivity(new Intent(this, MetroActivity.class));
                    break;
                case 2:
//                    startActivity(new Intent(this, TrainCardActivity.class));
                    break;
                case 3:
//                    startActivity(new Intent(this, GFriendActivity.class));
                    break;
                case 4:

                    break;
            }
        });
        int pad = SinZ.dip2px(this, 16);
        list.setPadding(pad, pad, pad, pad);
        layout.addView(list);

        FrameLayout layout0 = new FrameLayout(this);
        layout0.addView(layout);
        TextView cr = SinZ.copyright(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, -2);
        params.gravity = Gravity.BOTTOM;
        cr.setLayoutParams(params);
        layout0.addView(cr);
        setContentView(layout0);
        SinZ.preventEdgeToEdge(this);
    }
}