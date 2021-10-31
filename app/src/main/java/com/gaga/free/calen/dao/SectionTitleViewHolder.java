package com.gaga.free.calen.dao;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gaga.customcalendar2.R;


public class SectionTitleViewHolder extends RecyclerView.ViewHolder {
    private TextView txtSection;

    public TextView getTxtSection() {
        return txtSection;
    }

    public void setTxtSection(TextView txtSection) {
        this.txtSection = txtSection;
    }

    public SectionTitleViewHolder(View v) {
        super(v);
        txtSection = (TextView) v.findViewById(R.id.txtSection);
    }
}
