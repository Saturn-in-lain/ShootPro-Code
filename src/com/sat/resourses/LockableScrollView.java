package com.sat.resourses;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView
{
   private boolean scrollable;
   
   /*
    * I'm adding stuff about onScrollStoppedListener to try and fix the 
    * fling problem when you're looking at an indexed table. Which I think 
    * means has a frozen column. You end up getting out of sync b/c the 
    * main data and the index data. Using the example at the url below I was
    * trying to get some sort of fix to this. It is currently a work in
    * progress.
    * 
    * http://stackoverflow.com/questions/8181828/android-detect-when-scrollview-stops-scrolling
    */
   private OnScrollStoppedListener onScrollStoppedListener;
   private int initialPosition;
   private Runnable scrollerTask;
   
   private int newCheck  = 100;
   private int lastPoint = 0;
   
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
             LockableScrollView.this.postDelayed(scrollerTask, newCheck);
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
     LockableScrollView.this.postDelayed(scrollerTask, newCheck);
   }
   
   /*************************************************************************
    * Interface: OnScrollStoppedListener
    * ***********************************************************************/
   public interface OnScrollStoppedListener 
   {
     void onScrollStopped();
   }
}
