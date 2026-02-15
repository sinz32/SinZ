package me.sinz.sinz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MetroActivity extends AppCompatActivity {

    //깃허브에 올릴 예정이라
    //url을   봇이   못하도록   자른
    //크롤링   찾지   의도적으로   것
    public final String METRO_URL = "http"+"s://met"+"ro.sin"+"z.me";

    private DrawerLayout drawer;
    private Toolbar title;
    private WebView web;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            drawer.openDrawer(Gravity.LEFT);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(1);
        title = SinZ.createTitle(this, "SinZ Metro");
        layout.addView(title);

        web = new WebView(this);
        web.clearCache(true);
        web.addJavascriptInterface(new WebLinker(), "SinZ");
        web.setWebChromeClient(new WebChromeClient());
        web.setWebViewClient(new WebViewClient());
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUserAgentString(settings.getUserAgentString() + " SinZ/" + SinZ.VERSION);
        web.loadUrl(METRO_URL);
        web.setBackgroundColor(Color.WHITE);
        web.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        layout.addView(web);

        LinearLayout left = SinZ.createDrawer(this, "SinZ Metro");
        TextView txt = new TextView(this);
        txt.setText("\n\n노선 목록 생성 중...\n(인터넷 연결 필요)");
        txt.setTextColor(Color.BLACK);
        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(18);
        left.addView(txt);

        drawer = new DrawerLayout(this);
        drawer.addView(layout);
        drawer.addView(left);
        setContentView(drawer);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        SinZ.preventEdgeToEdge(this);
//        SinZ.preventEdgeToEdge(this, drawer);
    }

    private void createLeftDrawer(String json) {
        runOnUiThread(() -> {
            try {
                JSONArray data = new JSONArray(json);
                final String[] regions = new String[data.length() + 2];
                final String[][] lineNames = new String[data.length()][];
                final String[][] lineIds = new String[data.length()][];
                final String[][] line = new String[data.length() + 2][];
                for (int n = 0; n < data.length(); n++) {
                    JSONObject datum = data.getJSONObject(n);
                    regions[n] = datum.getString("name");
                    JSONArray list = datum.getJSONArray("data");
                    lineNames[n] = new String[list.length()];
                    lineIds[n] = new String[list.length()];
                    line[n] = new String[list.length()];
                    if (datum.getBoolean("child")) {
                        for (int m = 0; m < list.length(); m++) {
                            JSONObject info = list.getJSONObject(m);
                            lineNames[n][m] = info.getString("name");
                            lineIds[n][m] = info.getString("id");
                        }
                        line[n] = lineNames[n];
                    } else {
                        lineNames[n][0] = regions[n];
                        lineIds[n][0] = list.getString(0);
                        line[n] = new String[0];
                    }
                }
                regions[regions.length - 2] = "기능 정보";
                regions[regions.length - 1] = "닫기";
                line[line.length - 2] = new String[0];
                line[line.length - 1] = new String[0];

                final ArrayList<HashMap<String, String>> locations = new ArrayList<>(); // 부모 리스트
                final ArrayList<ArrayList<HashMap<String, String>>> lines = new ArrayList<>(); // 자식 리스트

                for (int n = 0; n < regions.length; n++) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("region", regions[n]);
                    locations.add(map);

                    ArrayList<HashMap<String, String>> lis = new ArrayList<>();
                    for (int m = 0; m < line[n].length; m++) {
                        map = new HashMap<>();
                        map.put("line", " - " + line[n][m]);
                        lis.add(map);
                    }
                    lines.add(lis);
                }

                LinearLayout layout = SinZ.createDrawer(this, "SinZ Metro");

                ExpandableListView list = new ExpandableListView(this);
                list.setGroupIndicator(null);

                list.setAdapter(new SimpleExpandableListAdapter(
                        this, locations, android.R.layout.simple_list_item_1, new String[]{"region"},
                        new int[]{android.R.id.text1}, lines, R.layout.child_list_item, new String[]{"line"}, new int[]{android.R.id.text1, android.R.id.text2}
                ));

                list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int gPos, int cPos, long id) {
                        String lindId = lineIds[gPos][cPos];
                        web.loadUrl("javascript:loadData('" + lindId + "', true);");
                        drawer.closeDrawer(Gravity.LEFT);
                        return false;
                    }
                });
                list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int pos, long id) {
                        if (pos == line.length - 1) { //닫기
                            drawer.closeDrawer(Gravity.LEFT);
                        } else if (pos == line.length - 2) { //기능 정보
                            toast("야옹");
                        } else if (line[pos].length == 0) { //하위 메뉴가 없는 경우
                            String lindId = lineIds[pos][0];
                            web.loadUrl("javascript:loadData('" + lindId + "', true);");
                            drawer.closeDrawer(Gravity.LEFT);
                        } else { //하위 메뉴가 있는 경우
                            //아무 것도 안함
                        }
                        return false;
                    }
                });
                layout.addView(list);

                int pad = SinZ.dip2px(this, 5);
                list.setPadding(pad, pad, pad, pad);

                drawer.removeViewAt(1);
                drawer.addView(layout);
            } catch (Exception e) {
                toast("Failed to create metro line list\n" + e.toString());
            }
        });
    }

    private void toast(final String msg) {
        runOnUiThread(() -> SinZ.toast(this, msg).show());
    }

    private class WebLinker {

        @JavascriptInterface
        public void toast(final String msg) {
            new Handler().post(() -> MetroActivity.this.toast(msg));
        }

        @JavascriptInterface
        public void createDrawer(final String json) {
            new Handler().post(() -> createLeftDrawer(json));
        }

        @JavascriptInterface
        public void changeTitle(final String txt) {
            runOnUiThread(()->title.setTitle(txt));
        }

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
}