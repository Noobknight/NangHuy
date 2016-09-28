package jp.co.crypton.spinach.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import jp.co.crypton.spinach.R;
import jp.co.crypton.spinach.model.DocDownload;
import jp.co.crypton.spinach.model.ListData;

/**
 * Created by huy on 9/15/2016.
 */
public class DownloadAdapter extends BaseAdapter {
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private Context context;
    private List<ListData> datas;


    public DownloadAdapter(Context context, List<ListData> result) {
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
            rowView = inflater.inflate(R.layout.row_download, null);
            viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.row_download_txt);
            viewHolder.iconImage = (ImageView) rowView.findViewById(R.id.row_download_image);

            rowView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.txtTitle.setText(datas.get(position).getUserName());
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
        ImageView iconImage;
    }
}
