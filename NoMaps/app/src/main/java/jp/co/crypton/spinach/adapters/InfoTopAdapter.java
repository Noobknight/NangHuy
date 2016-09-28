package jp.co.crypton.spinach.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.model.Information;

/**
 * Created by huy on 9/20/2016.
 */
public class InfoTopAdapter extends BaseAdapter {

    private Context context;
    private List<Information> datas;
    private OnShowMoreClickListener mOnShowMoreClickListener;


    private ViewHolder viewHolder = null;


    public InfoTopAdapter(Context context, List<Information> datas, OnShowMoreClickListener mOnShowMoreClickListener) {
        this.context = context;
        this.datas = datas;
        this.mOnShowMoreClickListener = mOnShowMoreClickListener;
    }

    public InfoTopAdapter(Context context, List<Information> result) {
        this.context = context;
        this.datas = result;
    }


    public interface OnShowMoreClickListener {
        void onClick(View view, int position);
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_info_top, null);
            viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.info_tv);
            viewHolder.txtDate = (TextView) rowView.findViewById(R.id.time_tv);
            viewHolder.imgShowMore = (ImageView) rowView.findViewById(R.id.arrow_bottom_iv);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        boolean isJp = false;
        if (Locale.getDefault().equals(Locale.JAPAN) || Locale.getDefault().equals(Locale.JAPANESE)) {
            isJp = true;
        }
        viewHolder.txtTitle.setText(isJp ? datas.get(position).getTitleJa() : datas.get(position).getTitleEn());
        viewHolder.txtDate.setText(datas.get(position).getDate());
        if (position == 0)
        {
            viewHolder.txtTitle.setMaxLines(Integer.MAX_VALUE);
        }
        if (datas.get(position).getShowAs() == 0)
        {
            viewHolder.imgShowMore.setVisibility(View.INVISIBLE);
        }
//        viewHolder.imgShowMore.setOnClickListener(new View.OnClickListener() {
//            boolean isTextViewClicked = false;
//            @Override
//            public void onClick(View view) {
//                view =(View) view.getParent();
//
////                mOnShowMoreClickListener.onClick(viewHolder.txtTitle, position);
//                ImageView expanded =(ImageView)view.findViewById(R.id.arrow_bottom_iv);
//                if(isTextViewClicked){
//                    //This will shrink textview to 2 lines if it is expanded.
//                    TextView number=(TextView)view.findViewById(R.id.info_tv);
//                    number.setMaxLines(1);
//                    expanded.setImageResource(R.drawable.ic_expanded_right);
//                    isTextViewClicked = false;
//                } else {
//                    //This will expand the textview if it is of 2 lines
//                    expanded.setImageResource(R.drawable.ic_expanded_right);
//                    TextView number=(TextView)view.findViewById(R.id.info_tv);
//                    number.setMaxLines(Integer.MAX_VALUE);
//                    isTextViewClicked = true;
//                }
//            }
//        });
        return rowView;
    }

    static class ViewHolder {
        TextView txtTitle;
        TextView txtDate;
        ImageView imgShowMore;
    }
}
