package com.wezebra.zebraking.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wezebra.zebraking.R;

import java.util.ArrayList;

public class ImageChooserAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> dataList;
	private DisplayMetrics dm;

	public ImageChooserAdapter(Context c, ArrayList<String> dataList) {

		mContext = c;
		this.dataList = dataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);

	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
//		if (convertView == null) {

			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, dipToPx(60)));
			imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
			imageView.setBackgroundResource(R.drawable.example_border_small);
			imageView.setImageResource(R.drawable.camera_small);

//		} else {
//
//			imageView = (ImageView) convertView;
//			imageView.setScaleType(ImageView.ScaleType.CENTER);
//
//		}

		String path;
		if (dataList != null && position<dataList.size() )
			path = dataList.get(position);
		else
			path = "camera_default";
		Log.i("path", "path:"+path+"::position"+position);
		if (path.contains("default"))
			imageView.setImageResource(R.drawable.add_with_text);
		else{

			if (path.equals("null")) {
				imageView.setImageResource(R.drawable.camera_small);
			} else {
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(path, imageView);
			}

		}

		return imageView;

	}
	
	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

}
