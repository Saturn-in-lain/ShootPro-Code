package com.sat.shootpro;

import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sat.resourses.BitmapWorkerTask;
import com.sat.resourses.HorizontalLockableScrollView;
import com.sat.resourses.LockableScrollView;
/******************************************************************
 * @author:     stas.savinov
 * @Name:       MainActivity
 * @Desciption: Activity for Not zoomed view of the trainer.
 * @category:   UI Business Logic of the view
 * @version:    1.1.0
 *****************************************************************/
public class MainActivity extends Activity implements InitializerPerClass,OnClickListener
{
   private static String      LOG                  = "MainActivity";
   private static boolean     dragStatus           = false;
   private        boolean     scroolActivated      = true;
   private static ImageButton sniperNotebookButton = null;
   private static ImageButton shootButton          = null;
   private static TextView    txtWindSpeed         = null;
   private static ImageView   sniperScope          = null;
   private static ImageView   compasView           = null;
   private static ImageView   imageTargetScoped    = null;
   private static ImageView   backgroundView       = null;

   private static LockableScrollView            scrollMainVerticalImage   = null;
   private static HorizontalLockableScrollView  scrollMainHorizontalImage = null;
   
   /** Target data */
   public static Point windowGabarites     = new Point(0,0);  // Gabarites only within  windows size
   public static Point fullwindowGabarites = new Point(0,0);  // Gabarites of full image scrolling size
   Point               targetCoord         = new Point(0,0);  // Real coordinates
   Point               scopeCoord          = new Point(0,0);  // Real coordinates
   Point               moveCoord           = new Point(0,0);  // While moving scroll windows
   Point               centerCoord         = new Point(0,0);  // Coordinates only within 800x480 windows size 
   
   /** Background polygon picture */
   public  static BitmapWorkerTask     task     = null;
   public  static UIHardCode           UICustom = null;
   
   /**************************************************************************
    * Function: InitAllResourcesInClass
    * @param () None.
    * @return None.
    * ***********************************************************************/ 
   @Override
   public void InitAllResourcesInClass(View view)
   {
      compasView                = (ImageView)          findViewById(R.id.imgViewWind);
      scrollMainVerticalImage   = (LockableScrollView )findViewById(R.id.scrollMainImage);
      scrollMainHorizontalImage = (HorizontalLockableScrollView)
                                             findViewById(R.id.scrollMainHorizontalImage);
      sniperScope               = (ImageView)   findViewById(R.id.sniperScope);
      sniperNotebookButton      = (ImageButton) findViewById(R.id.btn_sniper_notebook);
      shootButton               = (ImageButton) findViewById(R.id.btn_shoot);
      txtWindSpeed              = (TextView)    findViewById(R.id.txtWindSpeed); 
      imageTargetScoped         = (ImageView)   findViewById(R.id.imageTarget);
      scrollMainVerticalImage.setVerticalScrollBarEnabled(false);
      scrollMainHorizontalImage.setHorizontalScrollBarEnabled(false);
      
      windowGabarites           = setImageCenterCoordinates();
      UICustom                  = new UIHardCode(windowGabarites);
   } 
   
   /**************************************************************************
    * Function: setSavedParameters
    * @param    None.
    * @return   None. 
    *************************************************************************/
    private void setSavedParameters()
    {
       if(null != StartMenuActivity.Engine.Parameters)
       {
          txtWindSpeed.setText(String.format("%.2g",
                               StartMenuActivity.Engine.Parameters.windStrength));
       }
       else
       {
          txtWindSpeed.setText("none");  
       }
    }
    
   /**************************************************************************
    * Function: onCreate
    * @param () Bundle savedInstanceState.
    * @return None.
    * ***********************************************************************/ 
   @Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
    
      /** Initialize objects ************/
		InitAllResourcesInClass(null);
		
		/** Set saved parameters **********/
		setSavedParameters();
		
	   /** Initialize image font **************************************************************/
		backgroundView = (ImageView) findViewById(R.id.imagePolygon);
		loadBitmap(task,
		           R.drawable.poligon_small, 
		           backgroundView,
		           getResources(),
		           false);
		
		
		/** Sniper scope ***********************************************************************/
		sniperScope.setOnTouchListener(new OnTouchListener()
		{
		   @Override
            public boolean onTouch(View v, MotionEvent event)
            {
		       switch( event.getAction() )
               {
                  case MotionEvent.ACTION_DOWN:
                     dragStatus = true;
                     break;
                     
                  case MotionEvent.ACTION_MOVE:
                     break;
                     
                  case MotionEvent.ACTION_UP:
                     dragStatus = false;
                     break;
               }
               return false;
            }
		});
		/** Shoot or fire button *******************************************************/
		shootButton.setOnClickListener(this);
		/** Sniper note book **********************************************************/
		sniperNotebookButton.setOnClickListener(this); 
		/** Compass - scope movement disable ****************************************************/
		compasView.setOnClickListener(this); 
		/** Target ************************/
		targetCoord = generateRandomTargetCoordinated();
	}

   /*************************************************************************
    * Function: onClick()
    * @param View view 
    * @return None. 
    * ***********************************************************************/
   public void onClick(View view)
   {
      Intent myIntent = null;
      
      switch (view.getId()) 
      {
         case R.id.btn_shoot:
            startMainZoomShootActivity(myIntent);
            break;
            
         case R.id.btn_sniper_notebook:
            myIntent = new Intent(view.getContext(), FragmentTab_Activity.class);
            startActivityForResult(myIntent, 0);
            break;
            
         case R.id.imgViewWind:
            setScopeMovementRights(scroolActivated);
            break;
            
         default:
            break;
      }
   }
   
   /**************************************************************************
    * Function: startMainZoomShootActivity - compare coordinates of target
    * and scope. Send this coordinates to Zoomed View.  And start Zoomed view.
    * @param myIntent - Intent
    * @return None.
    *************************************************************************/
   private void startMainZoomShootActivity(Intent myIntent)
   {
      int   selectedImageSliceId = 0;
      Point detectCoordiates     = new Point(0,0);

      myIntent             = new Intent(getApplicationContext(), 
                                        MainZoomShoot.class);
      
      detectCoordiates     = calculateTargetCoordinatesForDetection();
      selectedImageSliceId = detectImageSliceIdDependence(detectCoordiates);
      
      myIntent.putExtra("imageLocation", selectedImageSliceId );
      myIntent.putExtra(strLocation_x,  detectCoordiates.x);
      myIntent.putExtra(strLocation_y,  detectCoordiates.y);
     
      startActivity(myIntent);
      beforeDestroy();
   }

   /**************************************************************************
    * Function: calculateTargetCoordinatesForDetection - compare coordinates of target
    * and scope. If they are one under another, then return true.
    * @param None.
    * @return detectCoordiates - Point.
    *************************************************************************/
   private Point calculateTargetCoordinatesForDetection()
   {
      Point detectCoordiates  = new Point(0,0);
      
      if( (0 == centerCoord.x) && (0 == centerCoord.y) )
      {
         centerCoord = setScopeCenterCoordinate();
      }
      moveCoord.x    = scrollMainHorizontalImage.getScrollCoordinates();
      moveCoord.y    = scrollMainVerticalImage.getScrollCoordinates();
      
      targetCoord    =  StartMenuActivity.Interf.getImageCenterCoordinates(imageTargetScoped,null);
      targetCoord.x  =  targetCoord.x - (moveCoord.x + UICustom.scopeAdaptMainActX); 
      targetCoord.y  =  targetCoord.y - (moveCoord.y + UICustom.scopeAdaptMainActY); 

      if(true == isTargetAndScopeOverlap(centerCoord,targetCoord) )
      {
         detectCoordiates.x =  (targetCoord.x  + moveCoord.x + UICustom.scopeAdaptMainActX);
         detectCoordiates.y =  (targetCoord.x  + moveCoord.x + UICustom.scopeAdaptMainActX);
      }
      
      return detectCoordiates;
   }
   
   /**************************************************************************
    * Function: setScopeMovementRights - give rights for Scope to move, or move 
    * background image.
    * @param fCanScroll - boolean
    * @return None.
    *************************************************************************/
   private void setScopeMovementRights(boolean fCanScroll)
   {
      scroolActivated = !fCanScroll;
      scrollMainVerticalImage.setScrollable(scroolActivated);
      scrollMainHorizontalImage.setScrollable(scroolActivated);
   }
   
   /**************************************************************************
    * Function: generateRandomTargetCoordinated TODO
    * @param None.
    * @Note. Size of background picture currently can't be calculated in this
    * moment.
    * @return pRet Point.
    *************************************************************************/
   private Point generateRandomTargetCoordinated()
   {
      Point pRet = new Point(10,10);
      
      pRet = StartMenuActivity.Interf.getImageCenterCoordinates(imageTargetScoped,null);
      
      RelativeLayout.LayoutParams params = 
         (RelativeLayout.LayoutParams) imageTargetScoped.getLayoutParams();
      
      Log.e(LOG,"Current target coordinates:[" + pRet.x + "][" + pRet.y + "]" );
            
      pRet.x =  getRandom(50,UICustom.backgroundPictureX - 200);
      pRet.y =  getRandom(50,UICustom.backgroundPictureY - 100 );

      params.topMargin = pRet.y; 
      params.leftMargin = pRet.x;
      
      imageTargetScoped.setLayoutParams(params);
            
      Log.e(LOG,"Upgrade target coordinates:[" + pRet.x + "][" + pRet.y + "]" );
      
      return pRet; 
   }
   
   /**************************************************************************
    * Function: returnRandom() - TODO: Remove doubling
    * @param min - int
    * @param max - int
    * @return (None) 
    * ***********************************************************************/
   private int getRandom(int min, int max)
   {
      int randomNumber = 0;
      Random r = new Random();
      if(min > max)
      {
         min = 0;
         max = 100;
         Log.e(LOG,"F:[getRandom]>>Errors min/max");
      }
      randomNumber = r.nextInt(max - min + 1) + min;
      return randomNumber;
   }
   
	/**************************************************************************
	* Function: onTouchEvent
	* @param () MotionEvent event
	* @Note Image scope now equals 100x100dp 
	* @return () onTouchEvent
	*************************************************************************/
	@Override
   public boolean onTouchEvent(MotionEvent event) 
	{
	   Point MoveStart = new Point(0,0);
	   Point MoveStop  = new Point(0,0);
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) sniperScope.getLayoutParams();
        
      if( true == dragStatus)
      {
          switch( event.getAction() )
          {
              case MotionEvent.ACTION_DOWN:          // Last touched coordinates
                 MoveStart.x = (int) Math.abs(event.getRawX() - params.leftMargin);
                 MoveStart.y = (int) Math.abs(event.getRawY() - params.topMargin);
                 break;
                 
              case MotionEvent.ACTION_MOVE:         
                 MoveStop.x  = (int) event.getRawX(); 
                 MoveStop.y  = (int) event.getRawY();
               
                 // Scope image size and margin for scope cross 
                 if ( ( MoveStop.x + UICustom.fingerMoveMainActRight ) >= windowGabarites.x )
                    break;
                 else if ( (MoveStop.x - UICustom.fingerMoveMainActLeft ) <= 0 )
                    break;
                 if ( ( MoveStop.y + UICustom.fingerMoveMainActDown ) >= windowGabarites.y )
                    break;
                 else if ( ( MoveStop.y - UICustom.fingerMoveMainActUp ) <= 0 )
                    break;
                 
                 params.leftMargin = Math.abs( (MoveStop.x - MoveStart.x)) 
                                            - UICustom.scopeMarginMainActLeft;
                 params.topMargin  = Math.abs( (MoveStop.y - MoveStart.y)) 
                                            - UICustom.scopeMarginMainActTop;
                 sniperScope.setLayoutParams(params);
                 centerCoord = setScopeCenterCoordinate();
                 break;

              case MotionEvent.ACTION_UP:
              default:
                 break;
          } 
       }
       return super.onTouchEvent(event);
    }
	/**************************************************************************
    * Function: setScopeCenterCoordinate
    * @param () None. 
    * @return Point x,y 
    * ***********************************************************************/
	private Point setScopeCenterCoordinate()
	{
	   Point retCoord = new Point(0,0);
	   RelativeLayout.LayoutParams params = 
	              (RelativeLayout.LayoutParams) sniperScope.getLayoutParams();
	   retCoord.x = params.leftMargin + UICustom.centerCoordMarginMainActLeft;
	   retCoord.y = params.topMargin  + UICustom.centerCoordMarginMainActTop;
	   return retCoord;
	}
	
   /**************************************************************************
    * Function: getImageCenterCoordinates
    * @param () None. 
    * @return Point x,y (width, height)
    * ***********************************************************************/
    public Point setImageCenterCoordinates()
    {
       Point current     = new Point(0,0);
           Display mdisp = getWindowManager().getDefaultDisplay();
           current.x     = mdisp.getWidth();
           current.y     = mdisp.getHeight();
       return current;
    }
   /**************************************************************************
   * Function: getWindowsSize
   * @param () None.
   * @return None.
   * ***********************************************************************/
   public static Point getWindowsSize()
   {
      return windowGabarites;
   }
   /**************************************************************************
    * Function: getWindowsCenter return center coordinates of activity window
    * @param None.
    * @return None.
    * ***********************************************************************/
    public static Point getWindowsCenter()
    {
       Point current = new Point(0,0);
          current.x  = windowGabarites.x/2;
          current.y  = windowGabarites.y/2;
       return current;
    }
    /**************************************************************************
     * Function: getImageCenter - return image center coordinates
     * with screen related dependency. 
     * @param image - ImageView.
     * @return None.
     * ***********************************************************************/
     public static Point getImageCenterInScreenContent(ImageView  image)
     {
        Point imageCenter   = new Point(0,0);
        
        int Top               = image.getTop();
        int Left              = image.getLeft();
        int ImageHeightCenter = image.getHeight()/2;
        int ImageWidthCenter  = image.getWidth()/2;
        imageCenter.y         = (Top + ImageHeightCenter);
        imageCenter.x         = (Left + ImageWidthCenter);

        return imageCenter;
     }
	/**************************************************************************
	* Function: 
	* @param imgTask       - BitmapWorkerTask task link 
	* @param resId         - int R.id. recourse
	* @param imageView     - ImageView where we place image
	* @param res           - Resources - link
	* @param setBackground - boolean - switch for representation variants
	* @return None.
	*************************************************************************/
	public static void loadBitmap(BitmapWorkerTask imgTask, 
	                             int resId, 
	                             ImageView imageView,
	                             Resources res,
	                             boolean setBackground) 
	{
	   imageView.destroyDrawingCache();
	   imgTask = new BitmapWorkerTask( imageView, 
   	                                res, 
   	                                windowGabarites.x, 
   	                                windowGabarites.y,
   	                                setBackground );
	   imgTask.execute( resId );
	   //Log.d(LOG, "Free memory left>>:    " + getFreeMemmory() + " bytes");
	}

	/**************************************************************************
    * Function: onDestroy
    * @param () None.
    * @return None.
    * ***********************************************************************/
    protected void onDestroy()
    {
       super.onDestroy();
       beforeDestroy();
       scrollMainHorizontalImage.onDestroy();
       scrollMainVerticalImage.onDestroy();
    }
    
    /**************************************************************************
     * Function: beforeDestroy
     * @param () None.
     * @return None.
     * ***********************************************************************/
     private void beforeDestroy()
     {
        if(null != task)
        {
           task.cancel(true);
           task = null;
        }
     }
     
    /**************************************************************************
     * Function: detectTargetAndScopeOverlap() - return true if target
     * placed within scope  
     * @param scope  - Point coordinates of scope
     * @param target - Point coordinated of target
     * @return bRet - true if target and scope in one place, otherwise false.
     * ***********************************************************************/
    private boolean isTargetAndScopeOverlap(Point scopeCenter, Point targetCenter)
    {
       boolean bRet = false;

       if( (targetCenter.x >= (scopeCenter.x - sniperScope.getWidth()/2))   && 
           (targetCenter.y >= (scopeCenter.y - sniperScope.getWidth()/2))   &&
             (targetCenter.x <= (scopeCenter.x + sniperScope.getWidth()/2)) && 
             (targetCenter.y <= (scopeCenter.y + sniperScope.getWidth()/2))  )
       {
          bRet = true;
       }
       return bRet;
    }
    
    /**************************************************************************
     * Function: detectImageTargetId - TODO
     * @param () x,y. - this is stupid and it must be reworked. Only to prove general concept!
     * @return None.
     * ***********************************************************************/
    public int detectImageSliceIdDependence(Point detectCoordinate)
    {
       int result     = DEADBEAF;
       int step_hight = UICustom.sliceDetectionHightMainAct;   // size of slice
       int step_width = UICustom.sliceDetectionWidtgMainActTop;
       int start_x    = imageTargetScoped.getWidth()/2; 
       int start_y    = imageTargetScoped.getHeight()/2;
       
//       int end_x      = windowGabarites.x - imageTargetScoped.getWidth()/2;  
//       int end_y      = windowGabarites.y - imageTargetScoped.getHeight()/2;
       
       fullwindowGabarites.x = backgroundView.getWidth();
       fullwindowGabarites.y = backgroundView.getHeight();
       
       int end_x      = fullwindowGabarites.x - imageTargetScoped.getWidth()/2;  
       int end_y      = fullwindowGabarites.y - imageTargetScoped.getHeight()/2; 
       
       if ((detectCoordinate.x >= start_x) && 
           (detectCoordinate.y >= start_y) && 
           (detectCoordinate.x <= end_x)   && 
           (detectCoordinate.y <= end_y))
       {
          int x_counter   = 1;   
          int y_counter   = 0;
          int cell_x_step = (start_x + step_width);
          int cell_y_step = (start_y + step_hight);
          
          while (detectCoordinate.x >= cell_x_step)
          {
             x_counter++;
             cell_x_step += step_width;
          }
          
          while (detectCoordinate.y >= cell_y_step)
          {
             y_counter++;
             cell_y_step += step_hight;
          }
          result = (y_counter * UICustom.AmountOfSlicesMainAct) + (x_counter);
          
Log.e( LOG, "F:[detectImageSliceIdDependence]>> Params: [" + detectCoordinate.x + "][" + detectCoordinate.y + "]" );
Log.e( LOG, "F:[detectImageSliceIdDependence]>> result: " + result);       
       }
       else
       {
Log.e( LOG, "F:[detectImageSliceIdDependence] Error: [" + detectCoordinate.x + "][" + detectCoordinate.y + "] result = 666" );
Log.e( LOG, "F:[start_x,y]:[" + start_x + "][" + start_y + "]" );
Log.e( LOG, "F:[end_x,y]:[" + end_x + "][" + end_y + "]" );
         detectCoordinate.x = 0;
         detectCoordinate.y = 0;
       }
       return result;
    }
    
    /**************************************************************************
     * Function: getFreeMemmory - better to move this function to proper place 
     * @param () None.
     * @return (long) Free memory left
     * ***********************************************************************/
    public static long getFreeMemmory()
    {
       Runtime rt = Runtime.getRuntime();
       long freeMem = rt.freeMemory() + (rt.maxMemory() - rt.totalMemory());
       //Log.d(LOG, "Heap allocated size: " + android.os.Debug.getNativeHeapAllocatedSize() + " bytes");
       return freeMem;
    }
     
	/**************************************************************************
    * Function: onClose
    * @param () None.
    * @return None.
    * ***********************************************************************/
    protected void onClose()
    {
       return;
    }

	/**************************************************************************
	 * Function: onCreateOptionsMenu
	 * @param () Menu menu
	 * @return () true 
	 * ***********************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	   getMenuInflater().inflate(R.menu.activity_main, menu);
	   return true;
	}
}
 