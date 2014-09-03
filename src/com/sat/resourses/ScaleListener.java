package com.sat.resourses;

import android.graphics.Matrix;
import android.util.Log;
import android.view.ScaleGestureDetector;

/**************************************************************************
 * Class: ScaleListener() 
 * @return (View) 
 * ***********************************************************************/
class ScaleListener extends
      ScaleGestureDetector.SimpleOnScaleGestureListener
{
   /**
    * 
    */
   private final TouchImageView touchImageView;

   /**
    * @param touchImageView
    */
   ScaleListener(TouchImageView touchImageView)
   {
      this.touchImageView = touchImageView;
   }

   /**************************************************************************
    * Function: onScaleBegin() 
    * @return (View) 
    * ***********************************************************************/
   @Override
   public boolean onScaleBegin(ScaleGestureDetector detector)
   {
      this.touchImageView.mode = TouchImageView.ZOOM;
      Log.e(TouchImageView.LOG,"onScaleBegin....");
      return true;
   }
   
   /**************************************************************************
    * Function: onScale() 
    * @param detector - ScaleGestureDetector
    * @return (boolean) always true. 
    * ***********************************************************************/
   @Override
   public boolean onScale(ScaleGestureDetector detector)
   {
      float mScaleFactor = (float) Math.min(Math.max(.95f, detector.getScaleFactor()), 1.05);
      float origScale = this.touchImageView.saveScale;
      this.touchImageView.saveScale *= mScaleFactor;

      Log.e(TouchImageView.LOG,"onScale");
      
      if (this.touchImageView.saveScale > this.touchImageView.maxScale)
      {
         this.touchImageView.saveScale = this.touchImageView.maxScale;
         mScaleFactor = this.touchImageView.maxScale / origScale;
      } 
      else if (this.touchImageView.saveScale < this.touchImageView.minScale)
      {
         this.touchImageView.saveScale = this.touchImageView.minScale;
         mScaleFactor = this.touchImageView.minScale / origScale;
      }
      this.touchImageView.right = (this.touchImageView.width * this.touchImageView.saveScale) - this.touchImageView.width - (2 * this.touchImageView.redundantXSpace * this.touchImageView.saveScale);
      this.touchImageView.bottom = (this.touchImageView.height * this.touchImageView.saveScale) - this.touchImageView.height
                                    - (2 * this.touchImageView.redundantYSpace * this.touchImageView.saveScale);
      if (this.touchImageView.origWidth * this.touchImageView.saveScale <= this.touchImageView.width || this.touchImageView.origHeight * this.touchImageView.saveScale <= this.touchImageView.height)
      {
         this.touchImageView.matrix.postScale(mScaleFactor, mScaleFactor, this.touchImageView.width / 2, this.touchImageView.height / 2);
         if (mScaleFactor < 1)
         {
            this.touchImageView.matrix.getValues(this.touchImageView.m);
            float x = this.touchImageView.m[Matrix.MTRANS_X];
            float y = this.touchImageView.m[Matrix.MTRANS_Y];
            if (mScaleFactor < 1)
            {
               if (Math.round(this.touchImageView.origWidth * this.touchImageView.saveScale) < this.touchImageView.width)
               {
                  if (y < -this.touchImageView.bottom)
                     this.touchImageView.matrix.postTranslate(0, -(y + this.touchImageView.bottom));
                  else if (y > 0)
                     this.touchImageView.matrix.postTranslate(0, -y);
               } 
               else
               {
                  if (x < -this.touchImageView.right)
                     this.touchImageView.matrix.postTranslate(-(x + this.touchImageView.right), 0);
                  else if (x > 0)
                     this.touchImageView.matrix.postTranslate(-x, 0);
               }
            }
         }
      } 
      else
      {
         this.touchImageView.matrix.postScale(mScaleFactor, 
                           mScaleFactor, 
                           detector.getFocusX(),
                           detector.getFocusY());
         this.touchImageView.matrix.getValues(this.touchImageView.m);
         float x = this.touchImageView.m[Matrix.MTRANS_X];
         float y = this.touchImageView.m[Matrix.MTRANS_Y];
         if (mScaleFactor < 1)
         {
            if (x < (-this.touchImageView.right))
               this.touchImageView.matrix.postTranslate(-(x + this.touchImageView.right), 0);
            else if (x > 0)
               this.touchImageView.matrix.postTranslate(-x, 0);
            if (y < (-this.touchImageView.bottom))
               this.touchImageView.matrix.postTranslate(0, -(y + this.touchImageView.bottom));
            else if (y > 0)
               this.touchImageView.matrix.postTranslate(0, -y);
         }
      }
      return true;
   }
}