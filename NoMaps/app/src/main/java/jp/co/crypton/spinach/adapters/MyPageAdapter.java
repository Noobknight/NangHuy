package jp.co.crypton.spinach.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import jp.co.crypton.spinach.model.ListData;

import java.util.List;

import jp.co.crypton.spinach.R;


/**
 * Created by huy on 8/31/2016.
 */
public class MyPageAdapter extends BaseAdapter {
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private Context context;
//    private String[] titles;
    private List<ListData> datas;

//    public MyPageAdapter(Context context, String[] titles) {
//        this.context = context;
//        this.datas = titles;
//    }
    public MyPageAdapter(Context context, List<ListData> result) {
        this.context = context;
        this.datas = result;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder = null;
        if(rowView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.row_my_page, null);
            viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.title_content_row);
            viewHolder.txtDate = (TextView) rowView.findViewById(R.id.rowdatetime);
            viewHolder.iconImage = (ImageView) rowView.findViewById(R.id.icon_row);

            rowView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTitle.setText(datas.get(position).getUserName());
        String string = datas.get(position).getDate();
        String[] parts = string.split(" ");
        if (parts.length > 0)
        {
            viewHolder.txtDate.setText(parts[0]);
        }
        final ViewHolder finalViewHolder = viewHolder;
        imageLoader.loadImage(datas.get(position).getIcon(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    finalViewHolder.iconImage.setImageBitmap(loadedImage);
                }
            }
            @Override
            public void onLoadingFailed(String var1, View var2, FailReason var3) {
            }
        });
        return rowView;
    }

    static class ViewHolder{
        TextView txtTitle;
        TextView txtDate;
        ImageView iconImage;
    }

}
