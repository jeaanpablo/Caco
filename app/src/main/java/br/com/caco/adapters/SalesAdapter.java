package br.com.caco.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.com.caco.R;
import br.com.caco.model.Sales;

/**
 * Created by Jean Pablo Bosso on 22/10/2015.
 */
public class SalesAdapter extends BaseAdapter {

    private Context context;
    private List<Sales> salesList;

    public SalesAdapter(Context context,
                        List<Sales> salesList) {
        this.context = context;
        this.salesList = salesList;
    }

    @Override
    public int getCount() {

        return salesList.size();
    }

    @Override
    public Object getItem(int position) {

        return salesList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return salesList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sales item = salesList.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_store_profile_sales_item, null);

        TextView textSalesName = (TextView) v.findViewById(R.id.textViewStoreProfileListSalesName);

        TextView textSalesExpireDate = (TextView) v.findViewById(R.id.textViewStoreProfileListSalesExpire);
        Button buttonGetSale = (Button) v.findViewById(R.id.buttonStoreProfileListSalesGet);

        textSalesName.setText(item.getDescription());
        textSalesExpireDate.setText(""+item.getExpirationDate().getTime());
        buttonGetSale.setText(""+item.getTradePoints());

        return v;
    }

}
