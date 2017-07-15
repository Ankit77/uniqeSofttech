package com.app.uniqesofttech;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.uniqesofttech.model.PaymentModel;

import java.util.ArrayList;

/**
 * Created by chandnichudasama on 18/06/17.
 */

public class SpinnerAdapter extends BaseAdapter {

    Context c;
    ArrayList<PaymentModel> objects;

    public SpinnerAdapter(Context context, ArrayList<PaymentModel> objects) {
        super();
        this.c = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        PaymentModel cur_obj = objects.get(position);
        LayoutInflater inflater = ((Activity) c).getLayoutInflater();
        View row = inflater.inflate(R.layout.row_spinner_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.row_spinner_item_tvitem);
        label.setText(cur_obj.getPaymentmode());
        return row;
    }
}
