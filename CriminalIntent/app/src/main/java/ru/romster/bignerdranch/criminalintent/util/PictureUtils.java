package ru.romster.bignerdranch.criminalintent.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Created by romster on 08/05/16.
 */
public class PictureUtils {

	public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		float srcWidth = options.outWidth;
		float srcHeight = options.outHeight;
		int inSamppleSize = 1;
		if (srcHeight > destHeight || srcWidth > destWidth) {
			if(srcHeight > srcHeight) {
				inSamppleSize = Math.round(srcHeight / destHeight);
			} else {
				inSamppleSize = Math.round(srcWidth / destWidth);
			}
		}

		options = new BitmapFactory.Options();
		options.inSampleSize = inSamppleSize;

		return BitmapFactory.decodeFile(path, options);
	}

	public static Bitmap getScaledBitmap(String path, Activity activity) {
		Point size = new Point();
		activity.getWindowManager().getDefaultDisplay().getSize(size);
		return getScaledBitmap(path, size.x, size.y);
	}
}
