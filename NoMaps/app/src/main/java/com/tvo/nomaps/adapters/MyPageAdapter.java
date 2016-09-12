package com.tvo.nomaps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tvo.nomaps.R;

/**
 * Created by huy on 8/31/2016.
 */
public class MyPageAdapter extends BaseAdapter {

    private Context context;
    private String[] titles;

    public MyPageAdapter(Context context, String[] titles) {
        this.context = context;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder = null;
        if(rowView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_my_page, null);
            viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.activity_txtContentRow);
            rowView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTitle.setText(titles[position]);
        return rowView;
    }

    static class ViewHolder{
        TextView txtTitle;
    }

}
