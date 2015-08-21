package com.wezebra.zebraking.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView
{

	public RoundedImageView(Context context)
	{
		super(context);
	}

	public RoundedImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public RoundedImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas)
	{

		Drawable drawable = getDrawable();



		if (drawable == null)
		{
			return;
		}

		if (getWidth() == 0 || getHeight() == 0)
		{
			return;
		}
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Config.ARGB_8888, true);

		int w = getWidth();
		// int h = getHeight();

		Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
		canvas.drawBitmap(roundBitmap, 1, 1, null);

		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#ebebeb"));

		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);

		// 图片边缘加上圆环
		canvas.drawCircle(w / 2, w / 2, w / 2 - 1, paint);

	}

	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius)
	{
		Bitmap sbmp;
		// if (bmp.getWidth() != radius || bmp.getHeight() != radius)
		sbmp = Bitmap.createScaledBitmap(bmp, radius - 2, radius - 2, false);
		// else
		// sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		// paint.setColor(Color.parseColor("#BAB399"));
		// paint.setColor(Color.parseColor("#000000"));
		canvas.drawCircle(sbmp.getWidth() / 2, sbmp.getHeight() / 2,
		        sbmp.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

}
