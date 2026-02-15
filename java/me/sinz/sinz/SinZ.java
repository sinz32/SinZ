package me.sinz.sinz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Insets;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SinZ {

    public static final String THIS_YEAR = "2026";

    public static Toolbar createTitle(AppCompatActivity ctx, String txt) {
        Toolbar title = new Toolbar(ctx);
        title.setTitle(txt);
        title.setTitleTextColor(Color.WHITE);
        title.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(-1, -2);
        margin.setMargins(0, 0, 0, dip2px(ctx, 8));
        title.setLayoutParams(margin);
        title.setElevation(dip2px(ctx, 5));
        ctx.setSupportActionBar(title);
        return title;
    }

    public static TextView copyright(Context ctx) {
        TextView txt = new TextView(ctx);
        txt.setText("ⓒ 2016-" + THIS_YEAR + " SinZ, All rights reserved.");
        txt.setTextSize(12);
        txt.setTextColor(Color.BLACK);
        txt.setGravity(Gravity.CENTER);
        int pad = dip2px(ctx, 5);
        txt.setPadding(pad, pad, pad, pad);
        return txt;
    }

    public static void preventEdgeToEdge(Activity ctx) {
        FrameLayout rootView = (FrameLayout) ctx.findViewById(android.R.id.content);
        View contentView = rootView.getChildAt(0);  //setContentView의 인자로 넘겼던 view

        //status bar의 자리에 들어갈 view
        View fakeStatusBar = new View(ctx);
        fakeStatusBar.setBackgroundColor(Color.BLACK);
        rootView.addView(fakeStatusBar, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));

        if (Build.VERSION.SDK_INT >= 35)
            rootView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    Insets sb = windowInsets.getInsets(WindowInsets.Type.systemBars());

                    ViewGroup.LayoutParams params = fakeStatusBar.getLayoutParams();
                    params.height = sb.top; //status bar와 높이가 동일하도록 설정
                    fakeStatusBar.setLayoutParams(params);

                    //상단 여백 빼고 설정 - 여기에 여백이 들어가면 status bar 부분이 비어버림
                    rootView.setPadding(sb.left, rootView.getPaddingTop(), sb.right, sb.bottom);

                    //상단 여백만 설정 - 위에서 여백 설정했던 view의 자식 view
                    contentView.setPadding(contentView.getPaddingLeft(), sb.top, contentView.getPaddingRight(), contentView.getPaddingBottom());
                    return WindowInsets.CONSUMED;
                }
            });
    }

    public static Toast toast(Context ctx, String msg) {
        Toast toast = new Toast(ctx);
        TextView txt = new TextView(ctx);
        txt.setText(msg);
        txt.setTextSize(13);
        txt.setTextColor(Color.WHITE);
        txt.setBackgroundColor(Color.parseColor("#BD424242"));
        int pad = dip2px(ctx, 8);
        txt.setPadding(pad, pad, pad, pad);
        toast.setView(txt);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }

    public static int dip2px(Context ctx, int dips) {
        return (int) Math.ceil(dips * ctx.getResources().getDisplayMetrics().density);
    }

}
