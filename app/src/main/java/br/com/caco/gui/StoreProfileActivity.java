package br.com.caco.gui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.caco.R;
import br.com.caco.adapters.SalesAdapter;
import br.com.caco.adapters.SalesParameterAdapter;
import br.com.caco.model.Sales;
import br.com.caco.model.SalesParameter;
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

        ImageView imageView = (ImageView) findViewById(R.id.image_header);


        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.goku);
        int gradientStartColor = Color.argb(0, 0, 0, 0);
        int gradientEndColor = Color.argb(255, 0, 0, 0);
        GradientOverImageDrawable gradientDrawable = new GradientOverImageDrawable(getResources(), image);
        gradientDrawable.setGradientColors(gradientStartColor, gradientEndColor);

        imageView.setImageDrawable(gradientDrawable);

        List<Sales> list = new ArrayList<Sales>();
        List<SalesParameter> listSalesParameter= new ArrayList<SalesParameter>();

        for (int i = 0; i < 3; i++) {
            Sales item = new Sales("Pé de meia furado", 1212121, Calendar.getInstance());
            SalesParameter itemS = new SalesParameter("Gaste acima de R$70", 1000);
            list.add(item);
            listSalesParameter.add(itemS);
        }

        final SalesAdapter adapter = new SalesAdapter(getApplicationContext(), list);
        ListView listView = (ListView) findViewById(R.id.listViewSales);
        listView.setAdapter(adapter);

        final SalesParameterAdapter adapterSalesParameter = new SalesParameterAdapter(getApplicationContext(), listSalesParameter);
        ListView listViewSlesParameter = (ListView) findViewById(R.id.listViewSalesParameter);
        listViewSlesParameter.setAdapter(adapterSalesParameter);

        setListViewHeightBasedOnChildren(adapter, listView);
        setListViewHeightBasedOnChildren(adapterSalesParameter, listViewSlesParameter);

        getActionBar().setTitle("");

        ((NotifyingScrollView) findViewById(R.id.scroll_view)).setOnScrollChangedListener(mOnScrollChangedListener);
    }

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            final int headerHeight = findViewById(R.id.image_header).getHeight() - getActionBar().getHeight();
            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
            final int newAlpha = (int) (ratio * 255);

            if(newAlpha >= 229.5)
            {
                getActionBar().setTitle("Mr Kistch");
            }
            else
            {
                getActionBar().setTitle("");
            }

            mActionBarBackgroundDrawable.setAlpha(newAlpha);
        }
    };


    public static void setListViewHeightBasedOnChildren(BaseAdapter listAdapter, ListView listView) {

        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
