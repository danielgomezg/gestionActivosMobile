package com.example.sca_app_v1.home_app.company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sca_app_v1.R;

import java.util.List;

public class CompanyAdapter extends ArrayAdapter<CompanyItem> {
    public CompanyAdapter(Context context, List<CompanyItem> companies) {
        super(context, 0, companies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.company_select);
        CompanyItem company = getItem(position);

        if (company != null) {
            textViewName.setText(company.getName());
        }

        return convertView;
    }
}