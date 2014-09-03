package com.sat.resourses;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> 
{
   private final WeakReference<ImageView> imageViewReference;
   private int       data     = 0;
   private int       screen_x = 0;
   private int       screen_y = 0;
   private Resources res      = null;
   private boolean   setBackground = false;
   
   /**************************************************************************
    * Function: BitmapWorkerTask()
    * @param ()
    * ***********************************************************************/
   public BitmapWorkerTask(ImageView imageView, 
                           Resources resource, 
                           int maxX, 
                           int maxY,
                           boolean setOnBackground) 
   {
       // Use a WeakReference to ensure the ImageView can be garbage collected
       imageViewReference = new WeakReference<ImageView>(imageView);
       this.res           = resource;
       this.screen_x      = maxX;
       this.screen_y      = maxY;
       this.setBackground = setOnBackground;
   }

   /**************************************************************************
    * Function: doInBackground() -Decode image in background.
    * @param ()
    * ***********************************************************************/
   @Override
   protected Bitmap doInBackground(Integer... params) 
   {
      Bitmap ret = null; 
      data = params[0];
      ret = decodeSampledBitmapFromResource(this.res, 
                                        data, 
                                        this.screen_x, 
                                        this.screen_y);
      if(null == ret)
      {
         Log.e("BitmapWorkerTask","F:[doInBackground] --> bitmap equals null");
      }
      return ret;
   }

   /**************************************************************************
    * Function: onPostExecute() - Once complete, see if ImageView is still 
    * around and set bitmap.
    * @param bitmap - Bitmap
    * @return None.
    * ***********************************************************************/
   @Override
   protected void onPostExecute(Bitmap bitmap) 
   {
       if ( (imageViewReference != null) && (bitmap != null)) 
       {
           final ImageView imageView = imageViewReference.get();
           if (imageView != null) 
           {
               if(false == this.setBackground)
               {
                  imageView.setImageBitmap(bitmap);
               }
               else
               {
                  // This is alternative for background.
                  Drawable d = new BitmapDrawable(res,bitmap);
                  imageView.setBackgroundDrawable(d);
               }
           }
           else
           {
              Log.e("BitmapWorkerTask","F:[BitmapWorkerTask] --> image equals null");
           }
       }
       if(null == bitmap)
       {
          Log.e("BitmapWorkerTask","F:[BitmapWorkerTask] --> bitmap equals null");
       }
       if(null == imageViewReference)
       {
          Log.e("BitmapWorkerTask","F:[BitmapWorkerTask] --> imageViewReference equals null");
       }
       /********************/
       cleanUp(bitmap);
       /********************/
   }

   /**************************************************************************
    * Function: cleanUp()
    * @param ()
    * @return 
    * ***********************************************************************/
   private void cleanUp(Bitmap bitmap)
   {
      if(null != bitmap)
      {
         //bitmap.recycle();   
         bitmap = null;
      }
      System.gc();
      //Runtime.getRuntime().gc();
   }
   
   /**************************************************************************
    * Function: calculateInSampleSize()
    * @param ()
    * @return 
    * ***********************************************************************/
   public static int calculateInSampleSize( BitmapFactory.Options options, 
                                                int reqWidth, int reqHeight) 
   {
      // Raw height and width of image
      final int height = options.outHeight;
      final int width  = options.outWidth;
      int inSampleSize = 1;
  
      if ( (height > reqHeight) || (width > reqWidth) ) 
      {
           // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio  = Math.round((float) width / (float) reqWidth);
           // Choose the smallest ratio as inSampleSize value, this will guarantee
           // a final image with both dimensions larger than or equal to the
           // requested height and width.
        inSampleSize = (heightRatio < widthRatio) ? heightRatio : widthRatio;
      }
      return inSampleSize;
   }
   
   /**************************************************************************
    * Function: decodeSampledBitmapFromResource()
    * @param  None.
    * @return None. 
    * ***********************************************************************/
   public static Bitmap decodeSampledBitmapFromResource(Resources res, 
                                                         int resId,
                                                         int reqWidth, 
                                                         int reqHeight) 
   {
      final BitmapFactory.Options options = new BitmapFactory.Options();
      options.inDither           = false;    
      options.inPurgeable        = true;      //Tell to gcc that whether it needs free memory, the Bitmap can be cleared
      options.inInputShareable   = true;      //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
      options.inTempStorage      = new byte[5 * 1024]; 
      options.inScaled           = false;
      if (null == BitmapFactory.decodeResource(res, resId, options))
      {
         // Calculate inSampleSize
         options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
         // Decode bitmap with inSampleSize set
         options.inJustDecodeBounds = false;
      }
      return BitmapFactory.decodeResource(res, resId, options);
   }
}