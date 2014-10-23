package com.sat.resourses;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView
{
   /*
   * http://stackoverflow.com/questions/8181828/android-detect-when-scrollview-stops-scrolling
   */
   private boolean                 scrollable              = false;
   private OnScrollStoppedListener onScrollStoppedListener = null;
   private Runnable                scrollerTask            = null;
   private int                     initialPosition         = 0;
   private int                     lastPoint               = 0;
   private int                     newCheckDelay           = 100;
   private static Handler          mHandler                = new Handler();
   
   /*************************************************************************
    * Function: LockableScrollView
    * @param context Context
    * @param attrs   AttributeSet
    * @return None. 
    * ***********************************************************************/
   public LockableScrollView(Context context, AttributeSet attrs) 
   {
       super(context, attrs);
       scrollable = true;
       
       scrollerTask = new Runnable() 
       {
         public void run() 
         {
           int newPosition = getScrollY();
           if (initialPosition - newPosition == 0) 
           {
             //scroll stopped
              if (onScrollStoppedListener != null) 
              {
                onScrollStoppedListener.onScrollStopped();
              }
           } 
           else 
           {
             initialPosition = getScrollY();
             //LockableScrollView.this.postDelayed(scrollerTask, newCheck);
             mHandler.postDelayed(scrollerTask, newCheckDelay);         
           }
         }
       };
   }
   
   /*************************************************************************
    * Function: onDestroy for memory safe
    * @param None.
    * @Note: https://ss-proxy.appspot.com/habrahabr.ru/company/badoo/blog/240479/
    * @return None. 
    * ***********************************************************************/
   public void onDestroy()
   {
      mHandler.removeCallbacksAndMessages(null);
   }
   
   /*************************************************************************
    * Function: onScrollChanged
    * @param l int
    * @param t int
    * @param oldl int
    * @param oldt int
    * @return None. 
    * ***********************************************************************/
   @Override
   protected void onScrollChanged(int l, int t, int oldl, int oldt) 
   {
       super.onScrollChanged(l, t, oldl, oldt);
       lastPoint = t;
   }
   /*************************************************************************
    * Function: getScrollCoordinates
    * @param  None.
    * @return result Point. 
    * ***********************************************************************/
   public int getScrollCoordinates()
   {
      return lastPoint;
   }
   
   /*************************************************************************
    * Function: setScrollable
    * @param  boolean  - scrollable
    * @return None.
    * ***********************************************************************/
   public void setScrollable(boolean scrollable) 
   {
       this.scrollable = scrollable;
   }
   
   /*************************************************************************
    * Function: onTouchEvent
    * @param  event  - MotionEvent
    * @return boolean.
    * ***********************************************************************/
   @Override
   public boolean onTouchEvent(MotionEvent event) 
   {
       if ((event.getAction() == MotionEvent.ACTION_DOWN) && !scrollable) 
       {
           return false;
       } 
       else 
       {
           return super.onTouchEvent(event);
       }
   }
   
   /*************************************************************************
    * Function: onInterceptTouchEvent
    * @param  event  - MotionEvent
    * @return boolean.
    * ***********************************************************************/
   @Override
   public boolean onInterceptTouchEvent(MotionEvent event) 
   {
       if (!scrollable) 
       {
           return false;
       } 
       else 
       {
           return super.onInterceptTouchEvent(event);
       }
   }
   
   /*************************************************************************
    * Function: setOnScrollStoppedListener
    * @param  OnScrollStoppedListener  - listener
    * @return boolean.
    * ***********************************************************************/
   public void setOnScrollStoppedListener(
                                 LockableScrollView.OnScrollStoppedListener listener) 
   {
     onScrollStoppedListener = listener;
   }
   
   /*************************************************************************
    * Function: onInterceptTouchEvent
    * @param  None.
    * @return boolean.
    * ***********************************************************************/
   public void onInterceptTouchEvent() 
   {
     initialPosition = getScrollY();
     //LockableScrollView.this.postDelayed(scrollerTask, newCheckDelay);
     mHandler.postDelayed(scrollerTask, newCheckDelay);
   }
   
   /*************************************************************************
    * Interface: OnScrollStoppedListener
    * ***********************************************************************/
   public interface OnScrollStoppedListener 
   {
     void onScrollStopped();
   }
}
