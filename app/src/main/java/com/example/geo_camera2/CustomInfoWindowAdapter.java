package com.example.geo_camera2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window, null);
    }

    private void rendoWindowText(Marker marker, View view) {
//        String title = marker.getTitle();
//        TextView tvTitle = (TextView) view.findViewById(R.id.customWindowTitle);
//        if(!title.equals("")){
//            tvTitle.setText(title);
//        }
//
//        String content = marker.getTitle();
//        TextView tvContent = (TextView) view.findViewById(R.id.customWindowContent);
//        if(!title.equals("")){
//            tvContent.setText(content);
//        }

        String snippetImage = marker.getSnippet();
        ImageView ivImage =  view.findViewById(R.id.customWindowImage);
            Bitmap eachBitmap = BitmapFactory.decodeFile(snippetImage);
            ivImage.setImageBitmap(eachBitmap);

     }
    @Override
    public View getInfoWindow(Marker marker) {
        rendoWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendoWindowText(marker, mWindow);
        return mWindow;
    }
}
