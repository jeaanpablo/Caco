package br.com.caco.gui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;

import br.com.caco.R;
import br.com.caco.util.GradientOverImageDrawable;
import br.com.caco.util.NotifyingScrollView;

public class StoreProfileActivity extends Activity {

    private Drawable mActionBarBackgroundDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile);

        mActionBarBackgroundDrawable = new ColorDrawable(Color.parseColor("#00b4bb"));
        mActionBarBackgroundDrawable.setAlpha(0);

        getActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);



        //TextView captionTitle = (TextView) findViewById(R.id.captionTitleView);
        //TextView captionBody = (TextView) findViewById(R.id.captionTextView);
        ImageView imageView = (ImageView) findViewById(R.id.image_header);

        //captionTitle.setTextColor(Color.WHITE);
        //captionBody.setTextColor(Color.WHITE);

        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.goku);
        int gradientStartColor = Color.argb(0, 0, 0, 0);
        int gradientEndColor = Color.argb(255, 0, 0, 0);
        GradientOverImageDrawable gradientDrawable = new GradientOverImageDrawable(getResources(), image);
        gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);

        imageView.setImageDrawable(gradientDrawable);



        ((NotifyingScrollView) findViewById(R.id.scroll_view)).setOnScrollChangedListener(mOnScrollChangedListener);
    }

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            final int headerHeight = findViewById(R.id.image_header).getHeight() - getActionBar().getHeight();
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
        }
    };
}
