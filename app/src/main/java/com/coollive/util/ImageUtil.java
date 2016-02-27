package com.coollive.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageUtil {
	
	
   /**
    * Bitmap <br>
    * Converted to the processed image blur (inputBitmap, radius)
    * @param inputBitmap
    * @param radius
    */
   public static Bitmap toBlurBitmap(Bitmap inputBitmap, int radius) {
      Bitmap bitmap = inputBitmap.copy(inputBitmap.getConfig(), true);
      
      if (radius < 1) {
         return (null);
      }
      
      int w = bitmap.getWidth();
      int h = bitmap.getHeight();
      
      int[] pix = new int[w * h];
      bitmap.getPixels(pix, 0, w, 0, 0, w, h);
      
      int wm = w - 1;
      int hm = h - 1;
      int wh = w * h;
      int div = radius + radius + 1;
      
      int r[] = new int[wh];
      int g[] = new int[wh];
      int b[] = new int[wh];
      int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
      int vmin[] = new int[Math.max(w, h)];
      
      int divsum = (div + 1) >> 1;
      divsum *= divsum;
      int dv[] = new int[256 * divsum];
      for (i = 0; i < 256 * divsum; i++) {
         dv[i] = (i / divsum);
      }
      
      yw = yi = 0;
      
      int[][] stack = new int[div][3];
      int stackpointer;
      int stackstart;
      int[] sir;
      int rbs;
      int r1 = radius + 1;
      int routsum, goutsum, boutsum;
      int rinsum, ginsum, binsum;
      
      for (y = 0; y < h; y++) {
         rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
         for (i = -radius; i <= radius; i++) {
            p = pix[yi + Math.min(wm, Math.max(i, 0))];
            sir = stack[i + radius];
            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);
            rbs = r1 - Math.abs(i);
            rsum += sir[0] * rbs;
            gsum += sir[1] * rbs;
            bsum += sir[2] * rbs;
            if (i > 0) {
               rinsum += sir[0];
               ginsum += sir[1];
               binsum += sir[2];
            } else {
               routsum += sir[0];
               goutsum += sir[1];
               boutsum += sir[2];
            }
         }
         stackpointer = radius;
         
         for (x = 0; x < w; x++) {
            
            r[yi] = dv[rsum];
            g[yi] = dv[gsum];
            b[yi] = dv[bsum];
            
            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;
            
            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];
            
            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];
            
            if (y == 0) {
               vmin[x] = Math.min(x + radius + 1, wm);
            }
            p = pix[yw + vmin[x]];
            
            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);
            
            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];
            
            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;
            
            stackpointer = (stackpointer + 1) % div;
            sir = stack[(stackpointer) % div];
            
            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];
            
            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];
            
            yi++;
         }
         yw += w;
      }
      
      for (x = 0; x < w; x++) {
         rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
         yp = -radius * w;
         for (i = -radius; i <= radius; i++) {
            yi = Math.max(0, yp) + x;
            
            sir = stack[i + radius];
            
            sir[0] = r[yi];
            sir[1] = g[yi];
            sir[2] = b[yi];
            
            rbs = r1 - Math.abs(i);
            
            rsum += r[yi] * rbs;
            gsum += g[yi] * rbs;
            bsum += b[yi] * rbs;
            
            if (i > 0) {
               rinsum += sir[0];
               ginsum += sir[1];
               binsum += sir[2];
            } else {
               routsum += sir[0];
               goutsum += sir[1];
               boutsum += sir[2];
            }
            
            if (i < hm) {
               yp += w;
            }
         }
         yi = x;
         stackpointer = radius;
         for (y = 0; y < h; y++) {
            // Preserve alpha channel: ( 0xff000000 & pix[yi] )
            pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
            
            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;
            
            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];
            
            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];
            
            if (x == 0) {
               vmin[y] = Math.min(y + r1, hm) * w;
            }
            p = x + vmin[y];
            
            sir[0] = r[p];
            sir[1] = g[p];
            sir[2] = b[p];
            
            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];
            
            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;
            
            stackpointer = (stackpointer + 1) % div;
            sir = stack[stackpointer];
            
            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];
            
            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];
            
            yi += w;
         }
      }
      
      bitmap.setPixels(pix, 0, w, 0, 0, w, h);
      
      return (bitmap);
   }
  
   
   
   @SuppressWarnings("deprecation")
   @SuppressLint("NewApi")
   public static void resizeDrawable(Resources res, View view, int drawable, int width, int height){
	   
	   BitmapFactory.Options options = new BitmapFactory.Options();
	   //options.inSampleSize = 8;
	   options.inJustDecodeBounds = true;
	   int sampleSize =  calculateInSampleSize(options, width, height);
	   
	   //Bitmap sourceBitmap = BitmapFactory.decodeResource(res, drawable, options);
	   //Bitmap.createScaledBitmap(sourceBitmap, width, height, true);
	   Bitmap scaledBitmap = decodeSampledBitmapFromResource(res, drawable,
			   width, height, sampleSize);
	   BitmapDrawable drawableBitmap = new BitmapDrawable(res, scaledBitmap);
	   
	   int sdk = android.os.Build.VERSION.SDK_INT;
		try {
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				view.setBackgroundDrawable(drawableBitmap);
			} else {
				view.setBackground(drawableBitmap);
			}
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
   }
   
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight, int sampleSize) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options optionsCal = new BitmapFactory.Options();
	    optionsCal.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, optionsCal);

	    // Calculate inSampleSize
	    optionsCal.inSampleSize = sampleSize;

	    // Decode bitmap with inSampleSize set
	    optionsCal.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, optionsCal);
	}	
	
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options optionsCal = new BitmapFactory.Options();
	    optionsCal.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, optionsCal);

	    // Calculate inSampleSize
	    optionsCal.inSampleSize = calculateInSampleSize(optionsCal, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    optionsCal.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, optionsCal);
	}	
	
	public static int calculateInSampleSize(
           BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	                && (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}	
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackground(ImageView view, BitmapDrawable ob) {
		int sdk = android.os.Build.VERSION.SDK_INT;
    	try {
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				view.setBackgroundDrawable(ob);
			} else {
				view.setBackground(ob);
			}		
    	} catch ( NullPointerException ne ) {
    		ne.printStackTrace();
    	}
    	
	}
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackground(ImageView view, Drawable ob) {
		int sdk = android.os.Build.VERSION.SDK_INT;
    	try {
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				view.setBackgroundDrawable(ob);
			} else {
				view.setBackground(ob);
			}
    	} catch ( NullPointerException ne ) {
    		ne.printStackTrace();
    	}
    	
	}
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackgroundAtOnce(ImageView view, Drawable ob) {
		setBackground(view, ob);
    	try {
    		view.invalidate();;
    	} catch ( NullPointerException ne ) {
    		ne.printStackTrace();
    	}
    	
	}
		
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackground(Button view, Drawable ob) {
		int sdk = android.os.Build.VERSION.SDK_INT;
    	try {
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				view.setBackgroundDrawable(ob);
			} else {
				view.setBackground(ob);
			}		
    	} catch ( NullPointerException ne ) {
    		ne.printStackTrace();
    	}
    	
	}
		
	
	public static void setImageSrc(ImageView view, BitmapDrawable ob) {
    	view.setImageDrawable(ob);
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackground(LinearLayout ll, BitmapDrawable ob) {
		int sdk = android.os.Build.VERSION.SDK_INT;
    	try {
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				ll.setBackgroundDrawable(ob);
			} else {
				ll.setBackground(ob);
			}		
    	} catch ( NullPointerException ne ) {
    		ne.printStackTrace();
    	}
	}
	
	
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void setBackground(Button view, BitmapDrawable ob) {
		int sdk = android.os.Build.VERSION.SDK_INT;
    	try {
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				view.setBackgroundDrawable(ob);
			} else {
				view.setBackground(ob);
			}		
    	} catch ( NullPointerException ne ) {
    		ne.printStackTrace();
    	}
	}
	
		
	public static void setButtonImg(Context context, Button btn, int drawId, int desiredWidth, int desiredHeight) { 
		Drawable d = context.getResources().getDrawable(drawId);
		d.setBounds(0, 0, (int)(desiredWidth),
				(int)(desiredHeight));
		ScaleDrawable sd = new ScaleDrawable(d, 0, 1.0f, 1.0f);
		//btn.setBackgroundResource(d);
		//btn.setCompoundDrawables(sd.getDrawable(), null, null, null);
		setBackground(btn, sd.getDrawable());
	} 
	
	public static void setImageViewImg(Context context, ImageView image, int drawId, int desiredWidth, int desiredHeight) { 

		Drawable d = context.getResources().getDrawable(drawId);
		d.setBounds(0, 0, (int)(desiredWidth), (int)(desiredHeight));
		ScaleDrawable sd = new ScaleDrawable(d, 0, 1.0f, 1.0f);
		setBackground(image, sd.getDrawable());
		
	} 
	
	
	
	public static ScaleDrawable getImageViewScaled(Context context, int drawId, int desiredWidth, int desiredHeight) { 

		Drawable d = context.getResources().getDrawable(drawId);
		d.setBounds(0, 0, (int)(desiredWidth), (int)(desiredHeight));
		ScaleDrawable sd = new ScaleDrawable(d, 0, 1.0f, 1.0f);
		
		return sd;
	} 	

	
	public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }
	
}
