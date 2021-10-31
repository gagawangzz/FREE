package com.gaga.free.calen.dao;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gaga.customcalendar2.R;


public class SectionDataViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private TextView txtDate;
    private TextView txtRemark;
    private TextView txtSubject;
    private View bottomLine;

    public View getBottomLine() {
        return bottomLine;
    }

    public void setBottomLine(View bottomLine) {
        this.bottomLine = bottomLine;
    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(TextView txtTitle) {
        this.txtTitle = txtTitle;
    }

    public TextView getTxtSubmissionDate() {
        return txtDate;
    }

    public void setTxtDate(TextView txtDate) {
        this.txtDate = txtDate;
    }

    public TextView getTxtRemark() {
        return txtRemark;
    }

    public void setTxtRemark(TextView txtRemark) {
        this.txtRemark = txtRemark;
    }

    public TextView getTxtSubject() {
        return txtSubject;
    }

    public void setTxtSubject(TextView txtSubject) {
        this.txtSubject = txtSubject;
    }

    public SectionDataViewHolder(View v) {
        super(v);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtDate = (TextView) v.findViewById(R.id.txtDate);
        txtRemark = (TextView) v.findViewById(R.id.txtRemark);
        txtSubject = (TextView) v.findViewById(R.id.txtSubject);
    }
}
