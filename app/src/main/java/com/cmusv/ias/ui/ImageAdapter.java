package com.cmusv.ias.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Resources mResources;
    // Keep all Images in array
    private int[] mThumbIds;
    private int img_width;
    private int img_height;
 
    // Constructor
    public ImageAdapter(Context c, Resources r, int[] thumbs, int w, int h) {
        mContext = c;
        mResources = r;
        mThumbIds = thumbs;
        img_width = w;
        img_height = h;
    }
 
    @Override
    public int getCount() {
        return mThumbIds.length;
    }
 
    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        try {
        	imageView.setImageBitmap(decodeSampledBitmapFromResource(mResources, mThumbIds[position], img_width, img_height));
        }
        catch(Exception e){
        	Log.v("EXCEPTION","Out of memory exception");
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(img_width, img_height));
        return imageView;
    }
    
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight)
    {
    	Bitmap img = null;
    	try {
    		final BitmapFactory.Options options = new BitmapFactory.Options();	    
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);
            options.inJustDecodeBounds = false;
            img = BitmapFactory.decodeResource(res, resId, options);
        }
        catch(Exception e){
        	Log.v("EXCEPTION","Out of memory exception");
        }
    	return img; 
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {	
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }	
}