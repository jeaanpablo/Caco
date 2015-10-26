package br.com.caco.adapters;

/**
 * Created by Jean Pablo Bosso on 26/10/2015.
 */

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
import br.com.caco.model.SalesParameter;

/**
 * Created by Jean Pablo Bosso on 22/10/2015.
 */
public class SalesParameterAdapter extends BaseAdapter {

    private Context context;
    private List<SalesParameter> salesParametersList;

    public SalesParameterAdapter(Context context,
                        List<SalesParameter> salesParametersList) {
        this.context = context;
        this.salesParametersList = salesParametersList;
    }

    @Override
    public int getCount() {

        return salesParametersList.size();
    }

    @Override
    public Object getItem(int position) {

        return salesParametersList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return salesParametersList.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SalesParameter item = salesParametersList.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_store_profile_sales_parameter_item, null);

        TextView textSalesParameterCondition = (TextView) v.findViewById(R.id.textViewSalesParameterCondition);
        TextView textSalesParameterPoints = (TextView) v.findViewById(R.id.textViewSalesParameterPoints);

        textSalesParameterCondition.setText(item.getTypePontuation());
        textSalesParameterPoints.setText(item.getPontuation()+" pontos");

        return v;
    }

}

