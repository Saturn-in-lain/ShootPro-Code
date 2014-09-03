package com.sat.resourses;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class HorizontalLockableScrollView extends HorizontalScrollView
{
   private boolean scrollable = false;
   
   private OnScrollStoppedListener onScrollStoppedListener = null;
   private Runnable                scrollerTask            = null;
   
   private int initialPosition   = 0;
   private int newCheck          = 100;
   
   private int lastPoint       = 0;
   /*************************************************************************
    * Function: HorizontalLockableScrollView
    * @param context Context
    * @param attrs   AttributeSet
    * @return None. 
    * ***********************************************************************/
   public HorizontalLockableScrollView(Context context, AttributeSet attrs) 
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
             HorizontalLockableScrollView.this.postDelayed(scrollerTask, newCheck);
           }
         }
       };
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
       //Log.i("HorizontalLockableScrollView", "X from ["+oldl+"] to ["+l+"]");
       super.onScrollChanged(l, t, oldl, oldt);
       lastPoint = l;
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
    * @param scrollable - boolean
    * @return None. 
    * ***********************************************************************/
   public void setScrollable(boolean scrollable) 
   {
       this.scrollable = scrollable;
   }
   
   /*************************************************************************
    * Function: onTouchEvent
    * @param event - MotionEvent
    * @return None. 
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
    * @param event - MotionEvent
    * @return None. 
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
    * @param event - MotionEvent
    * @return None. 
    * ***********************************************************************/
   public void setOnScrollStoppedListener(
               HorizontalLockableScrollView.OnScrollStoppedListener listener) 
   {
     onScrollStoppedListener = listener;
   }
   
   /*************************************************************************
    * Function: startScrollerTask
    * @return None. 
    * ***********************************************************************/
   public void startScrollerTask() 
   {
     initialPosition = getScrollY();
     HorizontalLockableScrollView.this.postDelayed(scrollerTask, newCheck);
   }
   
   /*************************************************************************
    * Function: OnScrollStoppedListener
    * @return None. 
    * ***********************************************************************/
   public interface OnScrollStoppedListener 
   {
     void onScrollStopped();
   }
}
