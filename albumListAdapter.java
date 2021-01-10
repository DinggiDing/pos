package com.example.albumtab_spinner;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class albumListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<albumele> albumList;
    final String[] clothes = {"-- 종류 --", "상의", "하의"};

    public albumListAdapter(Context context, int layout, ArrayList<albumele> albumList) {
        this.context = context;
        this.layout = layout;
        this.albumList = albumList;
    }

    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtClo;
    }

    GradientDrawable drawable;

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtClo = (TextView) row.findViewById(R.id.txtClo);
            holder.imageView = (ImageView) row.findViewById(R.id.imgDog);
            drawable = (GradientDrawable) context.getDrawable(R.drawable.background_rounding);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        albumele albumele = albumList.get(position);
        holder.txtClo.setText(clothes[albumele.getMemo()]);

        holder.imageView.setImageURI(Uri.parse(albumele.getImage()));
        holder.imageView.setBackground(drawable);
        holder.imageView.setClipToOutline(true);

        return row;
    }
}
