package com.sat.shootpro;

import com.sat.resourses.BitmapWorkerTask;
import com.sat.resourses.ImageInDialogView;
import com.sat.resourses.TableInDialogView;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
/******************************************************************
 * 
 * @author stas.savinov
 * @Name: MainZoomShoot - Activity for zoomed view of the trainer.
 * @category UI Business Logic
 * @version 1.0.0
 *****************************************************************/
public class MainZoomShoot extends Activity implements InitializerPerClass, OnClickListener
{
   private static String LOG = "MainZoomShoot";
   private Context       ctx = null;
   /** ImageButton ********************************************************/
   private static ImageButton  UpBtn                  = null;
   private static ImageButton  DownBtn                = null;
   private static ImageButton  LeftBtn                = null;
   private static ImageButton  RightBtn               = null;
   private static ImageButton  ShootButton            = null;
   private static ImageButton  DialogTable            = null;
   private static ImageButton  TargetDialog           = null;
   /** ImageView **********************************************************/
   private static ImageView    cloudAnimation         = null;
   private static ImageView    imgMoveBackground      = null;
   private static ImageView    imageMainScopePic      = null;
   private static ImageView    imageZoomedTargetScoped= null;
   private static ImageView    imgCompasArrow         = null;
   private static ImageView    imgCompasGround        = null;
   private static ImageView    imgScopeGrid           = null;
   private        ImageInDialogView imgDialog         = null;
   /** TextView ***********************************************************/
   private static TextView     txtHorizVal            = null;
   private static TextView     txtVertcVal            = null;
   private static TextView     txtWindVal             = null;
   /** LayoutParams *******************************************************/
   RelativeLayout.LayoutParams backgroundLayoutParams = null;
   RelativeLayout.LayoutParams targetLayoutParams     = null;
   RelativeLayout.LayoutParams cloudLayoutParams      = null;
   ScrollView                  VerticalScrollView     = null;
   LinearLayout                VerticalBarLayout      = null;
   HorizontalScrollView        HorizontalScrollView   = null;
   LinearLayout                HorizontalBarLayout    = null; 
   /** BitmapWorkerTask ***************************************************/
   public  static BitmapWorkerTask imageShowTasktask  = null;    //May be needed to reduce to one
   public  static BitmapWorkerTask imageShowTasktask1 = null;
   /** External Class *****************************************************/
   private ShootPerformance         ShootClass        = null;
   public  Target                   TargetClass       = null;
   private TableInDialogView        tableDialog       = null;
   private AnimationDrawable        animation         = null; 
   public  Resources                globalRessources  = null;
   /** Coordinates ********************************************************/
   private Point targetCoordinates                    = new Point(0,0); // target coordinates in  
   private Point windowCenterCoordinates              = new Point(0,0); // center of screen/ scopes center
   private Point scopeClickSetsXY                     = new Point(0,0); // scope click values
   private boolean recalculateShootParams             = true;
   private boolean isReloadDone                       = true;
   private boolean isTargetPresent                    = false;
   /** Loading ***************************************************/
   private static ProgressBar Loading                 = null;
   private        Handler     mHandler                = new Handler();
   /** Clicks ***************************************************/
   String[] clickString = new String[]
    {
       "  -14","  -13", "  -12","  -11", "  -10"," -9",
       " -8"," -7", " -6"," -5", " -4"," -3",
       " -2"," -1", 
       " 0",                                               // 15 
       " 1"," 2", " 3"," 4", 
       " 5"," 6", " 7"," 8", " 9"," 10", " 11"," 12", " 13"," 14"}; // 29 total

   /**************************************************************************
    * Class: AnimationStarter (Animation Starter)
    * @return ()
    ************************************************************************/
   class AnimationStarter implements Runnable
   {
       @Override
       public void run()
       {
           animation.start();
       }
   }
   
   /**************************************************************************
    * Function: InitAllResourcesInClass
    * @param  None.
    * @return None.
    * ***********************************************************************/ 
   @Override
   public void InitAllResourcesInClass(View view)
   {
      animation               = new AnimationDrawable();
      TargetClass             = new Target();
      ShootButton             = (ImageButton)          findViewById(R.id.ImageButtonShoot);
      UpBtn                   = (ImageButton)          findViewById(R.id.ImgUpBtn);
      DownBtn                 = (ImageButton)          findViewById(R.id.ImgDownBtn);
      LeftBtn                 = (ImageButton)          findViewById(R.id.ImgLeftBtn);
      RightBtn                = (ImageButton)          findViewById(R.id.ImgRightBtn);
      DialogTable             = (ImageButton)          findViewById(R.id.imgBtnTable);
      TargetDialog            = (ImageButton)          findViewById(R.id.ImgTargetBtn);
      txtHorizVal             = (TextView)             findViewById(R.id.txtHorizontalParam);
      txtVertcVal             = (TextView)             findViewById(R.id.txtVerticalParam);
      txtWindVal              = (TextView)             findViewById(R.id.txtWindParam);
      cloudAnimation          = (ImageView)            findViewById(R.id.imgViewCloud);
      imgCompasGround         = (ImageView)            findViewById(R.id.imgCompasBackground);
      imgScopeGrid            = (ImageView)            findViewById(R.id.imgScopeMil);
      imageZoomedTargetScoped = (ImageView)            findViewById(R.id.imageZoomedTarget);
      VerticalScrollView      = (ScrollView)           findViewById(R.id.vscrollClickLine);
      HorizontalScrollView    = (HorizontalScrollView) findViewById(R.id.hscrollClickLine);
      windowCenterCoordinates = MainActivity.getWindowsCenter();
      targetLayoutParams      = (RelativeLayout.LayoutParams) 
                                          imageZoomedTargetScoped.getLayoutParams();
   }
   
   /**************************************************************************
    * Function: makeAllUIVisible - perform UI turning on. 
    * @param  None.
    * @return None.
    * ***********************************************************************/
   private void makeAllUIVisible()
   {
      ShootButton.setVisibility                 (View.VISIBLE);
      UpBtn.setVisibility                       (View.VISIBLE);
      DownBtn.setVisibility                     (View.VISIBLE);
      LeftBtn.setVisibility                     (View.VISIBLE);
      RightBtn.setVisibility                    (View.VISIBLE);
      DialogTable.setVisibility                 (View.VISIBLE);
      txtHorizVal.setVisibility                 (View.VISIBLE);
      txtVertcVal.setVisibility                 (View.VISIBLE);
      txtWindVal.setVisibility                  (View.VISIBLE);
      VerticalBarLayout.setVisibility           (View.VISIBLE); 
      VerticalScrollView.setVisibility          (View.VISIBLE);
      HorizontalBarLayout.setVisibility         (View.VISIBLE);  
      HorizontalScrollView.setVisibility        (View.VISIBLE);
      imgScopeGrid.setVisibility                (View.VISIBLE);
      imgCompasGround.setVisibility             (View.VISIBLE);
      if(true == isTargetPresent)
      {
         imageZoomedTargetScoped.setVisibility  (View.VISIBLE);
         TargetDialog.setVisibility             (View.VISIBLE);
      }
      /* Strange bug Init here and all works*/
      imgCompasArrow  = (ImageView) findViewById(R.id.imgCompasArrow);
      imgCompasArrow.setVisibility              (View.VISIBLE);
      setWindDirection();
      Loading.setVisibility( View.GONE );
   }
   
   /**************************************************************************
    * Function: showProgressBar  RAT!!!
    * @param progress - int % of progress
    * @return ()
    ************************************************************************/
   private void showProgressBar(int progress) 
   {
      int mProgressStatus = progress;
      Loading.setProgress(mProgressStatus);
      if (0 == progress) 
      {
         Loading.setVisibility(View.VISIBLE);
         Loading.setSecondaryProgress(0);
      } 
      else 
      {
         Loading.setSecondaryProgress(mProgressStatus + 5);
      }
      
      if(mProgressStatus > 100)
      {
         if(Loading.isShown()) 
         {
            Loading.setVisibility( View.GONE );
            makeAllUIVisible();
         }
      }
   }
   
   /**************************************************************************
    * Function: setSavedParameters()
    *    1. Wind string parameter
    *    2. Scope click parameters
    * @param None.
    * @return None.
    *************************************************************************/
    private void setSavedParameters()
    {
       /* Wind settings */
       txtWindVal.setText(String.format("%s %.2g",
                                        getString(R.string.defWindParam),
                                        StartMenuActivity.Engine.Parameters.windStrength));
       /* Scope settings */
       scopeClickSetsXY.x = StartMenuActivity.Interf.getScopeClickX();
       scopeClickSetsXY.y = StartMenuActivity.Interf.getScopeClickY();
       
       
       String verticalClick   = getResources().getString(R.string.defVerticalParam);
       String horizontalClick = getResources().getString(R.string.defHorizontParam);
       
       txtVertcVal.setText(String.format(verticalClick+" %d",    scopeClickSetsXY.y));
       txtHorizVal.setText(String.format(horizontalClick+" %d",  scopeClickSetsXY.x));
    }
    
    /**************************************************************************
     * Function: getParametersFromOtherActivities 
     * @param None.
     * @return getValue - int. Slide Id for zooming
     *************************************************************************/
     private int getParametersFromOtherActivities()
     {
        int getValue  = 0;
        Bundle extras = getIntent().getExtras();
        if( null != extras )
        {
           getValue            = extras.getInt(strLocation);
           targetCoordinates.x = extras.getInt(strLocation_x);
           targetCoordinates.y = extras.getInt(strLocation_y);
        }
        return getValue;
     }

   /**************************************************************************
    * Function: onCreate
    * @param savedInstanceState - Bundle
    * @return () None.
    ************************************************************************/
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main_shoot);

      setVolumeControlStream(AudioManager.STREAM_MUSIC);    // RAT: if won't work
      
      if(null == this.ctx)
      {
         this.ctx = MainZoomShoot.this;
      }
      
      Loading  = (ProgressBar) findViewById(R.id.prgrsBar);
      Loading.setVisibility(View.VISIBLE);
      
      InitAllResourcesInClass(null);                        /** << Init. objects **/
      imageActivityInitializer();                           /** << Init. all large images **/
      setSavedParameters();                                 /** << Set all saved parameters **/
      addClickTextLayout(this.ctx);                         /** << Click scope parameters **/ 
      setCloudMargins(0,0);                                 /** << Cloud margin **/ 
      
      if ( ( 0 != targetCoordinates.x ) || 
                     ( 0 != targetCoordinates.y ) )
      {
         setTargetParameters(0);                            /** << Target manipulation data */ 
         ShootClass = new ShootPerformance(TargetClass);    /** << Shoot manipulation data */
         
         /** Initialize dialog with table data**/
         tableDialog = new TableInDialogView(this);
         isTargetPresent = true;
         calculateNewShootInDialog();
      }
      else
      {
         isTargetPresent        = false;
         recalculateShootParams = false;
         makeAllUIVisible();                                /** << Note: Even if target is not present - load all UI */
      }
      setScopeGrid();                                       /** << Set UI Margins */
      /** Set UI buttons Listeners *********/
      ShootButton.setOnClickListener  (this);
      UpBtn.setOnClickListener        (this);
      DownBtn.setOnClickListener      (this);
      LeftBtn.setOnClickListener      (this);
      RightBtn.setOnClickListener     (this);
      DialogTable.setOnClickListener  (this);
      TargetDialog.setOnClickListener (this);
   }
   
   /**************************************************************************
    * Function: onBackPressed() RAT
    * @param   None.
    * @return  None.
    * ***********************************************************************/
   @Override
   public void onBackPressed() 
   {
      targetCoordinates.x = 0;     //recalculateShootParams = true;
      targetCoordinates.y = 0;
      this.finish();
      return;
   }   
   
   /**************************************************************************
    * Function: setTargetZoomChange - prepare target for UI representation 
    * and with MilDot corresponding. Also this function set target parameters  
    * @param   zoomValue - int.
    * @return  None. 
    * ***********************************************************************/
   private void setTargetParameters(int zoomValue) 
   {
      TargetClass.setTargetDistance( StartMenuActivity.Engine.getTargetDistance() );
      
      TargetClass.setRealTargetSize( StartMenuActivity.Interf.getTargetWidthParameter(), 
                                     StartMenuActivity.Interf.getTargetHeightParameter() );
      
      TargetClass.setSmallTargetMilsSize( StartMenuActivity.Interf.getTargetWidthParameter(), 
                                          StartMenuActivity.Interf.getTargetHeightParameter() );
      
      TargetClass.setSmallTargetZoomKoef(zoomValue);
      TargetClass.setSmallTargetPixelSize(MainActivity.UICustom.pixelSizeEvalution);
      
      imageZoomedTargetScoped.getLayoutParams().height = TargetClass.getSmallTargetPixelSize(true);
      imageZoomedTargetScoped.getLayoutParams().width  = TargetClass.getSmallTargetPixelSize(false);
   }   
   
   /**************************************************************************
    * Function: showTargetShotResults() - Function for showing
    * target with bullet hits on it. Please notice, that current implementation
    * does not accurate as much, as I want. One px on 600 meters = 61 px 
    * on resized large target picture. This will be reworked on next sprint.   
    * @return  None. 
    * ***********************************************************************/   
   private void showTargetShotResults()
   {
      if( null != TargetClass )
      {
         imgDialog = new ImageInDialogView(ctx, getResources());
         imgDialog.showTargetPictureDialog(R.drawable.target_main,
                                           TargetClass);
      }
      else
      {
         StartMenuActivity.Interf.showToastMsg("No target!", ctx);
      }
   }
   
   /**************************************************************************
    * Function: setScopeGrid() - choose correct scope grid and set
    * needed margins for scope-grid image 
    * Notes: Remove hard code values
    * @return  None.
    * ***********************************************************************/
   private void setScopeGrid() 
   {
      int imageIndex = 0;
      int imageId    = 0;
      
      imageId    = StartMenuActivity.Interf.getScopeGridSavedParam();
      imageIndex = StartMenuActivity.ScopeInformation.getScopeResourceIndex(imageId);
      
      MainActivity.loadBitmap(imageShowTasktask1,
                              imageIndex, 
                              imgScopeGrid, 
                              globalRessources,
                              false);
      setUIOffsetsByWindowSize();
   }
   
   /**************************************************************************
    * Function: setCloudMargins() 
    * @param x_deviation double margin step value
    * @param y_deviation double
    * @return  None.
    * ***********************************************************************/
   private void setCloudMargins(double x_deviation, double y_deviation) 
   {
      cloudLayoutParams              = (RelativeLayout.LayoutParams) cloudAnimation.getLayoutParams();
      cloudLayoutParams.topMargin    = (windowCenterCoordinates.y + MainActivity.UICustom.cloudTopMarginZoomAct);
      cloudLayoutParams.leftMargin   = (windowCenterCoordinates.x + MainActivity.UICustom.cloudLeftMarginZoomAct);
      
      if( (0 != x_deviation) && (0 != y_deviation) )
      {
         int sizeX = (int) (x_deviation * MainActivity.UICustom.pixelSizeEvalution);
         int sizeY = (int) (y_deviation * MainActivity.UICustom.pixelSizeEvalution);
         cloudLayoutParams.topMargin    += sizeY;
         cloudLayoutParams.leftMargin   += sizeX;
      }
      
      cloudLayoutParams.height       = MainActivity.UICustom.cloudSizeZoomAct;
      cloudLayoutParams.width        = MainActivity.UICustom.cloudSizeZoomAct;
      cloudAnimation.setLayoutParams(cloudLayoutParams);
      
      /*????? RAT ???? TODO*/
      //cloudLayoutParams              = (RelativeLayout.LayoutParams) cloudAnimation.getLayoutParams();
      //cloudLayoutParams.topMargin    = (windowCenterCoordinates.y + MainActivity.UICustom.cloudTopMarginZoomAct);
      //cloudLayoutParams.leftMargin   = (windowCenterCoordinates.x + MainActivity.UICustom.cloudLeftMarginZoomAct);
      //cloudAnimation.setLayoutParams(cloudLayoutParams);
   }
   
   /**************************************************************************
    * Function: resetCloudToCenter() 
    * @return  None.
    * ***********************************************************************/
   private void resetCloudToCenter() 
   {
      cloudLayoutParams              = (RelativeLayout.LayoutParams) cloudAnimation.getLayoutParams();
      cloudLayoutParams.topMargin    = (windowCenterCoordinates.y + MainActivity.UICustom.cloudTopMarginZoomAct);
      cloudLayoutParams.leftMargin   = (windowCenterCoordinates.x + MainActivity.UICustom.cloudLeftMarginZoomAct);
      cloudAnimation.setLayoutParams(cloudLayoutParams);
   }
   /**************************************************************************
    * Function: setWindDirection() - Rotate arrow in correct direction. If
    * wind strength equals 0 m\s, then there is no need in directions.
    * @return  None.
    * ***********************************************************************/
   private void setWindDirection() 
   {
      int angle = 0;
      if(0 != StartMenuActivity.Engine.Parameters.windStrength )
      {
         angle = StartMenuActivity.Engine.Parameters.windDirection.getAngle();
      }
      rotateWindArrow(angle);
   }
   
   /**************************************************************************
    * Function: rotateWindArrow() - set direction for wind arrow
    * @param  degree - float.
    * @return  None.
    * ***********************************************************************/
   private void rotateWindArrow(float degree) 
   {
      final RotateAnimation rotateAnim = new RotateAnimation(0.0f, 
                             degree,
                             RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                             RotateAnimation.RELATIVE_TO_SELF, 0.5f);
      rotateAnim.setDuration(0);
      rotateAnim.setFillAfter(true);
      imgCompasArrow.startAnimation(rotateAnim);
  }
   
   /**************************************************************************
    * Function: parallelImageLoading()  Funny, because in separate thread I 
    * launch two AsyncTask's :) This is experiment
    * @param   None.
    * @return  None.
    * ***********************************************************************/
   public void parallelImageLoading() 
   {
      new Thread(new Runnable() 
      {
         public void run() 
         {
            mHandler.post(new Runnable() 
            {
                public void run() 
                {
                   imageActivityInitializer();
                }
            });
         }
     }).start(); 
     return;
   } 
   
   /**************************************************************************
    * Function: imageActivityInitializer() - Initialize background image with 
    * scope image. 
    * @param  None.
    * @return  None.
    * ***********************************************************************/
   private void imageActivityInitializer()
   {
      /** Initialize front screen image **/
      imgMoveBackground = (ImageView) findViewById( R.id.imageMainShootPic );
      MainActivity.loadBitmap(imageShowTasktask1,
                              getImageResourceID(), 
                              imgMoveBackground, 
                              globalRessources,
                              false);
      
      /** Initialize front screen image **/
      imageMainScopePic = (ImageView) findViewById( R.id.imageMainScopePic);
      MainActivity.loadBitmap(imageShowTasktask,
                              R.drawable.background_shot_activity_800x480, 
                              imageMainScopePic, 
                              globalRessources,
                              /*true*/false);
        
      backgroundLayoutParams              = (RelativeLayout.LayoutParams) imgMoveBackground.getLayoutParams();
      backgroundLayoutParams.topMargin    = (backgroundLayoutParams.topMargin    + MainActivity.UICustom.pixelBackgroundMargin);
      backgroundLayoutParams.bottomMargin = (backgroundLayoutParams.bottomMargin - MainActivity.UICustom.pixelBackgroundMargin);
      imgMoveBackground.setLayoutParams(backgroundLayoutParams);
   }
   
   /**************************************************************************
    * Function: getImageResourceID() - how to make this simpler?
    * @param  None.
    * @return  None.
    * ***********************************************************************/
   private int getImageResourceID()
   {
      int iRet = 0;
      globalRessources = getResources();
      if (null == globalRessources)
      {
         Log.e(LOG,"Resource issue was NOT found");
      }

      iRet = getParametersFromOtherActivities();
      switch(iRet)
      {
         case 1:
            iRet = R.drawable.poligon_1;
            break;
         case 2:
            iRet = R.drawable.poligon_2;
            break;
         case 3:
            iRet = R.drawable.poligon_3;
            break;
         case 4:
            iRet = R.drawable.poligon_4;
            break;
         case 5:
            iRet = R.drawable.poligon_5;
            break;
         case 6:
            iRet = R.drawable.poligon_6;
            break;
         case 7:
            iRet = R.drawable.poligon_7;
            break;
         case 8:
            iRet = R.drawable.poligon_8;
            break;
         case 9:
            iRet = R.drawable.poligon_9;
            break;
         case 10:
            iRet = R.drawable.poligon_10;
            break;
         case 11:
            iRet = R.drawable.poligon_11;
            break;
         case 12:
            iRet = R.drawable.poligon_12;
            break;
         case 13:
            iRet = R.drawable.poligon_13;
            break;
         case 14:
            iRet = R.drawable.poligon_14;
            break;
         case 15:
            iRet = R.drawable.poligon_15;
            break;
         default:
            iRet = R.drawable.poligon_7;
            break;
      }
      return iRet;
   }
   
   /**************************************************************************
    * Function: onClick() 
    * @return (View) 
    * ***********************************************************************/
   public void onClick(View v) 
   {
      backgroundLayoutParams = (RelativeLayout.LayoutParams) imgMoveBackground.getLayoutParams();

      switch (v.getId()) 
      {
         case R.id.ImgUpBtn:
            moveBackImageUp(); //setTargetZoomedSize(4);
            break;
            
         case R.id.ImgDownBtn:
            moveBackImageDown(); //setTargetZoomedSize(0);
            break;
            
         case R.id.ImgLeftBtn:
            moveBackImageLeft();
            break;
            
         case R.id.ImgRightBtn:
            moveBackImageRight();
            break;
         
         case R.id.ImgTargetBtn:
            showTargetShotResults();
            break;
            
         case R.id.imgBtnTable:
            if(null != tableDialog)
            {
               tableDialog.showTableDialog();
            }
            else
            {
               StartMenuActivity.Interf.showToastMsg("No target!", ctx);
            }
            break;
            
         case R.id.ImageButtonShoot:
            if(true == isReloadDone)
            {
               gunShotSoundPlay();
               isReloadDone = false;                     // strange sync sets
               performShootResultCalculation();
            }
            else
            {
               Log.e(LOG,"Error of reload (isReloadDone->true?)"); 
            }
            break;

         default:
            break;
      }
   }
   
   /**************************************************************************
    * Function: moveBackImageUp()
    * @param  None.
    * @return  None.
    *************************************************************************/
   private void moveBackImageUp()
   {
      if ( ( backgroundLayoutParams.topMargin + MainActivity.UICustom.MOVE_STEP) < MainActivity.UICustom.sliceMoveUpBorder ) 
      {
         backgroundLayoutParams.topMargin    = (backgroundLayoutParams.topMargin    + MainActivity.UICustom.MOVE_STEP);
         backgroundLayoutParams.bottomMargin = (backgroundLayoutParams.bottomMargin - MainActivity.UICustom.MOVE_STEP);
         imgMoveBackground.setLayoutParams(backgroundLayoutParams);

         targetLayoutParams = (RelativeLayout.LayoutParams) imageZoomedTargetScoped.getLayoutParams();
         targetLayoutParams.topMargin    = (targetLayoutParams.topMargin    + MainActivity.UICustom.MOVE_STEP);
         targetLayoutParams.bottomMargin = (targetLayoutParams.bottomMargin - MainActivity.UICustom.MOVE_STEP);
         imageZoomedTargetScoped.setLayoutParams(targetLayoutParams);
      }
   }
   
   /**************************************************************************
    * Function: moveBackImageDown()
    * @param  None.
    * @return  None. 
    *************************************************************************/
   private void moveBackImageDown()
   {
      if ( ( backgroundLayoutParams.bottomMargin + MainActivity.UICustom.MOVE_STEP ) < MainActivity.UICustom.sliceMoveDownBorder )
      {
         backgroundLayoutParams.topMargin    = (backgroundLayoutParams.topMargin    - MainActivity.UICustom.MOVE_STEP);
         backgroundLayoutParams.bottomMargin = (backgroundLayoutParams.bottomMargin + MainActivity.UICustom.MOVE_STEP);
         imgMoveBackground.setLayoutParams(backgroundLayoutParams);
         
         targetLayoutParams = (RelativeLayout.LayoutParams) imageZoomedTargetScoped.getLayoutParams();
         targetLayoutParams.topMargin    = (targetLayoutParams.topMargin    - MainActivity.UICustom.MOVE_STEP);
         targetLayoutParams.bottomMargin = (targetLayoutParams.bottomMargin + MainActivity.UICustom.MOVE_STEP);
         imageZoomedTargetScoped.setLayoutParams(targetLayoutParams);
      }
   }
   
   /**************************************************************************
    * Function:  moveBackImageLeft()
    * @param  None.
    * @return  None.
    *************************************************************************/
   private void moveBackImageLeft()
   {
      if ( ( backgroundLayoutParams.leftMargin + MainActivity.UICustom.MOVE_STEP ) < MainActivity.UICustom.sliceMoveLeftBorder )
      {
         backgroundLayoutParams.leftMargin   = (backgroundLayoutParams.leftMargin  + MainActivity.UICustom.MOVE_STEP);
         backgroundLayoutParams.rightMargin  = (backgroundLayoutParams.rightMargin - MainActivity.UICustom.MOVE_STEP);
         imgMoveBackground.setLayoutParams(backgroundLayoutParams);       

         targetLayoutParams = (RelativeLayout.LayoutParams) imageZoomedTargetScoped.getLayoutParams();
         targetLayoutParams.leftMargin   = (targetLayoutParams.leftMargin  + MainActivity.UICustom.MOVE_STEP);
         targetLayoutParams.rightMargin  = (targetLayoutParams.rightMargin - MainActivity.UICustom.MOVE_STEP);
         imageZoomedTargetScoped.setLayoutParams(targetLayoutParams);
      }
   }
   
   /**************************************************************************
    * Function: moveBackImageRight()
    * @param  None.
    * @return  None.
    *************************************************************************/
   private void moveBackImageRight()
   {
      if ( ( backgroundLayoutParams.rightMargin + MainActivity.UICustom.MOVE_STEP ) < MainActivity.UICustom.sliceMoveRightBorder )
      {
         backgroundLayoutParams.leftMargin   = (backgroundLayoutParams.leftMargin  - MainActivity.UICustom.MOVE_STEP);
         backgroundLayoutParams.rightMargin  = (backgroundLayoutParams.rightMargin + MainActivity.UICustom.MOVE_STEP);
         imgMoveBackground.setLayoutParams(backgroundLayoutParams);
         
         targetLayoutParams = (RelativeLayout.LayoutParams) imageZoomedTargetScoped.getLayoutParams();
         targetLayoutParams.leftMargin  = (targetLayoutParams.leftMargin  - MainActivity.UICustom.MOVE_STEP);
         targetLayoutParams.rightMargin = (targetLayoutParams.rightMargin + MainActivity.UICustom.MOVE_STEP);
         imageZoomedTargetScoped.setLayoutParams(targetLayoutParams);
      }
   }
   
   /**************************************************************************
    * Function: startAnimation - animation of the cloud after shoot
    * @param  None.
    * @return None.
    ************************************************************************/
     private void startAnimation()
     {
        cloudAnimation.setVisibility(View.VISIBLE);
        cloudAnimation.setBackgroundDrawable(null);

        animation.addFrame(globalRessources.getDrawable(R.drawable.cloud_0), MainActivity.UICustom.DURATION); // 100
        animation.addFrame(globalRessources.getDrawable(R.drawable.cloud_1), MainActivity.UICustom.DURATION); // 200
        animation.addFrame(globalRessources.getDrawable(R.drawable.cloud_2), MainActivity.UICustom.DURATION); // 300
        animation.addFrame(globalRessources.getDrawable(R.drawable.cloud_1), MainActivity.UICustom.DURATION); // 400
        animation.addFrame(globalRessources.getDrawable(R.drawable.cloud_0), MainActivity.UICustom.DURATION); // 500
        animation.setOneShot(true);
        
        cloudAnimation.setImageDrawable(animation);
        cloudAnimation.post(new AnimationStarter());

        new Handler().postDelayed(new Runnable()          //You can batter...
        {
           @Override
           public void run() 
           {
              cloudAnimation.setVisibility(View.INVISIBLE);
              animation.stop();
            }
        },MainActivity.UICustom.TIME_STEP);
        //Runtime.getRuntime().gc();
     }

   /**************************************************************************
    * Function: calculateNewShootInDialog() -
    * Create separate thread for shoot calculations and to minimize UI freeze.
    * This will be done once. Recalculate must be done only after changes
    * of some   main parameters. Distance, wind, etc
    * @param   None.
    * @return  None.
    * ***********************************************************************/
    public void calculateNewShootInDialog() 
    {
       new Handler().postDelayed(new Runnable()          // You can batter...
       {
          @Override
          public void run() 
          {
             StartMenuActivity.Engine.setMaxTargetDistance(StartMenuActivity.Interf.getMaxTargetDistance());
             ShootClass.initBallisticEngineParameters();
             calculateNewShoot();
          }
       },MainActivity.UICustom.TIME_STEP);
       return;
    } 
    
   /**************************************************************************
   * Function: calculateNewShoot() Perform iteration calculation of bullet shot
   * with all parameters, which were settled. 
   * @param  None.
   * @return None.
   * @throws InterruptedException 
   * ***********************************************************************/
   private void calculateNewShoot()
   {
     int step       = (0); 
     int increment  = (0);
           
     recalculateShootParams = false;
     ShootClass.setWindParameters();
     showProgressBar(0);
     
     step = StartMenuActivity.Interf.getTableStepParameter();
     
     while( increment <= StartMenuActivity.Interf.getMaxTargetDistance() )
     {
        StartMenuActivity.Engine.calculateShootStep(increment);
        tableDialog.addTableRow(StartMenuActivity.Engine,
                                TargetClass,  
                                increment,
                                StartMenuActivity.Interf.getTableColumsVisibility());
        increment += step;
        showProgressBar( increment/10 );
     }
     ShootClass.shootOnTargetDistance();
   }
     
  /**************************************************************************
   * Function: performShootResultCalculation() - knowing coordinated of window 
   * center and coordinates of target, we can calculate result. Also scope sets 
   * must be taken into account. 
   * Algorithm:
   * 1. Fired deviations parameters
   * 2. Calculate sets of scope on the distance of target
   * 3. Get shoot coordinated with all sets on target
   * 4. isItTargeted hit by bullet
   * 5. Target was hit
   * 6. Target was not hit
   * @param  None.
   * @return None.
   ************************************************************************/
   private void performShootResultCalculation()
   {
      double scopeDeviationSet_X = init;
      double scopeDeviationSet_Y = init;
      
      if(true == isTargetPresent)
      {
         if (true == recalculateShootParams)                                        /**1**/
         {                                                                          
            ShootClass.shootOnTargetDistance();
         }                                              
                                                                                    /**2**/ 
         scopeDeviationSet_X = ShootClass.shootTogetherWithScopeSets(scopeClickSetsXY.x,true); 
         scopeDeviationSet_Y = ShootClass.shootTogetherWithScopeSets(scopeClickSetsXY.y,true); 
/*-----------------------------------------------------------------------------------------------------------------*/         
//Log.e(LOG,String.format("Clicks equals meters x,y[%g][%g]",
//      scopeDeviationSet_X, 
//      scopeDeviationSet_Y));
/*-----------------------------------------------------------------------------------------------------------------*/                                                                                    /**3**/
         scopeDeviationSet_X += ( ShootClass.deviationShot.get(UIHardCode.zVal) );
         scopeDeviationSet_Y += ( ShootClass.deviationShot.get(UIHardCode.yVal) );
/*-----------------------------------------------------------------------------------------------------------------*/
//Log.e(LOG,String.format("Clicks meters + deviation meters [%g][%g]", 
//      scopeDeviationSet_X, 
//      scopeDeviationSet_Y));
/*-----------------------------------------------------------------------------------------------------------------*/
         if( true == isTargetWasHit(windowCenterCoordinates, 
                                    scopeDeviationSet_X, 
                                    scopeDeviationSet_Y) )                          /**4**/
         {
            isReloadDone = true;                                                    /**5**/
            StartMenuActivity.Interf.showToastMsg("HIT", ctx);
         }
         else
         {
            setCloudMargins(scopeDeviationSet_X,scopeDeviationSet_Y);               /**6**/
            targetMissed(true);
            resetCloudToCenter();
         }
      }
      else
      {
         targetMissed(false);
      }
   }
 
   /**************************************************************************
    * Function: isTargetWasHit - Define if the target was hit with all parameters  
    * @param center Point (y,z) - coordinates of bullet on distance of target
    * @param xVal int - value of bullet deviation X axis
    * @param yVal int - value of bullet deviation Y axis
    * @return true if Hit, else false
    * ***********************************************************************/
    private boolean isTargetWasHit(Point center, double xVal, double  yVal)
    {
       boolean fRet          = false;
       Point scopeCenter     = new Point(0,0);
       Point bulletHit       = new Point(0,0);
          
       scopeCenter = MainActivity.getImageCenterInScreenContent(imgScopeGrid);

       TargetClass.setSmallTargetCoordinates(StartMenuActivity.Interf.getImageLeftTopCoordinates(imageZoomedTargetScoped)); 
       xVal =  (double)TargetClass.getMilsAndMeterKoeff() * xVal;     
       yVal =  (double)TargetClass.getMilsAndMeterKoeff() * yVal; 

       int sizeX = (int) (xVal * MainActivity.UICustom.pixelSizeEvalution);
       int sizeY = (int) (yVal * MainActivity.UICustom.pixelSizeEvalution);
       
       bulletHit.x = ( scopeCenter.x + sizeX );
       bulletHit.y = ( scopeCenter.y + sizeY );
       
       fRet = TargetClass.isBulletHitTheTarget(bulletHit);
       return fRet;
    }

   /**************************************************************************
   * Function: targetMissed() - perform all tasks related to that event.
   * @param  isTargetPresent - boolean.
   * @return None.
   * ***********************************************************************/
    private void targetMissed(boolean isTargetPresent)
    {
       startAnimation();
       isReloadDone = true;
       if(true == isTargetPresent)
       {
          StartMenuActivity.Interf.showToastMsg("MISS", ctx);
       }
       else
       {
          StartMenuActivity.Interf.showToastMsg("NO TARGET", ctx);
       }
    }
    
    /**************************************************************************
     * Function: setUIOffsetsBeWindowSize() - TODO
     * @return  None.
     * ***********************************************************************/
    private void setUIOffsetsByWindowSize()
    {
       
       Point windowCenter            = MainActivity.getWindowsCenter();
       RelativeLayout.LayoutParams L = (RelativeLayout.LayoutParams) imgScopeGrid.getLayoutParams();
       
       L.topMargin    = windowCenter.y - MainActivity.UICustom.ScopeSize/2 + MainActivity.UICustom.ScopeGridMargin;
       L.bottomMargin = windowCenter.y + MainActivity.UICustom.ScopeSize   + MainActivity.UICustom.ScopeSize/2 - MainActivity.UICustom.ScopeGridMargin;
       
       L.leftMargin   = windowCenter.x - MainActivity.UICustom.ScopeSize/2;
       L.rightMargin  = windowCenter.x - MainActivity.UICustom.ScopeSize + MainActivity.UICustom.ScopeSize/2;

       L.height = (MainActivity.UICustom.ScopeSize);
       L.width  = (MainActivity.UICustom.ScopeSize);
       
       imgScopeGrid.setLayoutParams(L);
    }
    
    /**************************************************************************
     * Function: setScopeClickMargin() This is dirty huck in order
     * not to create screen per HDPI resolution on this screen.
     * @param   (VL) ScrollView 
     * @return  None.
     * ***********************************************************************/
    private void setScopeClickMargin( ScrollView VL ) 
    {
       RelativeLayout.LayoutParams LP  = (android.widget.RelativeLayout.LayoutParams) VL.getLayoutParams();
       LP.rightMargin   = (LP.rightMargin + MainActivity.UICustom.VerticalClickMargin);
       VL.setLayoutParams(LP);
    }
    
    /***************************************************************************
     *  addClickTextLayout() - Function will create vertical and horizontal 
     *                         list of buttons with text on them.
     *  @param  ctx  - (Context) 
     *  @return None.
    **************************************************************************/
     private void addClickTextLayout(Context ctx)
     {
        VerticalBarLayout   = (LinearLayout) this.findViewById(R.id.listViewClickVertical);
        HorizontalBarLayout = (LinearLayout) this.findViewById(R.id.listViewClickHorizontal);
        
        setScopeClickMargin(VerticalScrollView);

        for( int increment = 0; increment < clickString.length; increment++ )
        {
           addVerticalItem(ctx,
                           increment,
                           VerticalBarLayout);
           
           addHorizontalItem(ctx,
                             increment,
                             HorizontalBarLayout);
        }
        VerticalBarLayout.setScrollbarFadingEnabled(true);
        HorizontalBarLayout.setScrollbarFadingEnabled(true);
     }
     
     /***************************************************************************
      *  addHorizontalItem() - add button in list horizontally 
      *  @param ctx                 - (Context) 
      *  @param index               - (int)
      *  @param HorizontalBarLayout - LinearLayout
      *  @return None.
      **************************************************************************/
     private void addHorizontalItem(Context ctx, int index, LinearLayout HorizontalBarLayout)
     {
        Button item  = (Button) new Button(ctx); 
        item         = addScopeClickItemButton(item, 
                                               index, 
                                               clickString[index],
                                               false);
        HorizontalBarLayout.addView(item);
     } 
     
     /***************************************************************************
      *  addVerticalItem()        - add button in list vertically
      *  @param ctx               - (Context) 
      *  @param index             - (int)
      *  @param VerticalBarLayout - LinearLayout
      *  @return None.
      **************************************************************************/
     private void addVerticalItem(Context ctx, int index,LinearLayout VerticalBarLayout)
     {
        Button item = (Button) new Button(ctx); 
               item = addScopeClickItemButton(item, 
                                              index, 
                                              clickString[index],
                                              true);
        VerticalBarLayout.addView(item);
     }
     
    /***************************************************************************
     *  addScopeClickItemButton() - add button value for scope clicks
     *  @param btn                - (Button) button view
     *  @param index              - (int)   position in list
     *  @param text               - (String) show text  
     *  @param isVertical         - (boolean) added button will be for vertical(true)
     *                              horizontal(false)
     *  @return (Button) Created button         
     **************************************************************************/
     private Button addScopeClickItemButton(Button btn, 
                                            final int index, 
                                            String text, 
                                            final boolean isVertical)
     {
        btn.setEllipsize(TextUtils.TruncateAt.END);
        btn.setHorizontallyScrolling(false);
        btn.setSingleLine();
        btn.setGravity(Gravity.RIGHT);
        btn.setText(text);
        btn.setTextSize(MainActivity.UICustom.ScrollTextSize);
        btn.setTextColor(Color.WHITE);
        btn.setBackgroundColor(Color.TRANSPARENT);
        
        btn.setOnClickListener(new View.OnClickListener()
        {
           @Override
           public void onClick(final View view)
           {
              if(true == isVertical)
              {
                 scopeClickSetsXY.y = (index - DEFAULT_INDEX);
                 txtVertcVal.setText(String.format("Vertical Click: %d", scopeClickSetsXY.y));
                 StartMenuActivity.Interf.setScopeClickY(scopeClickSetsXY.y);
              }
              else
              {
                 scopeClickSetsXY.x = (index - DEFAULT_INDEX);
                 txtHorizVal.setText(String.format("Horizontal Click: %d",scopeClickSetsXY.x));
                 StartMenuActivity.Interf.setScopeClickY(scopeClickSetsXY.x);
              }
           }
        });
        return btn;
     }
   /**************************************************************************
    * Function: onClose()
    * @param  None.
    * @return None.
    * ***********************************************************************/
    protected void onClose()
    {
       recalculateShootParams = true;
       Log.d(LOG,"Close");
    }
    
   /**************************************************************************
    * Function: onDestroy()
    * @param  None.
    * @return None.
    * ***********************************************************************/
    protected void onDestroy()
    {
       super.onDestroy();
       if((null != imageShowTasktask) && 
             (null != imageShowTasktask1))
       {
          imageShowTasktask.cancel(true);
          imageShowTasktask = null;
          
          imageShowTasktask1.cancel(true);
          imageShowTasktask1 = null;
          
          recalculateShootParams = true;
       }
    }
    
    /**************************************************************************
     * Function: gunShotSoundPlay() - reproduce sound of fire and reload.
     * @param  None.
     * @return  None.
     *************************************************************************/
    private void gunShotSoundPlay()
    {
       MediaPlayer  soundOfRifleShot  = null;
       
       if(true == StartMenuActivity.Interf.getSoundFlagSettings())
       {
          try
          {
             soundOfRifleShot = MediaPlayer.create( getApplicationContext(), 
                                                    R.raw.fire_and_reload );
             soundOfRifleShot.setLooping(false);
             soundOfRifleShot.start();
          } 
          catch (IllegalStateException e)
          {
             e.printStackTrace();
          } 
          /******************************************************************************/
          soundOfRifleShot.setOnPreparedListener(new MediaPlayer.OnPreparedListener() 
          {
             @Override
             public void onPrepared(MediaPlayer mp) 
             {
                mp.start();
             } 
          });
          /******************************************************************************/
          soundOfRifleShot.setOnCompletionListener(new OnCompletionListener() 
          {
             public void onCompletion(MediaPlayer mp) 
             {
                if(null != mp)
                {
                   mp.release();
                   mp = null;
                }
             };
          });
          /******************************************************************************/
       }
       else
       {
          Log.d(LOG,"Sound was switch off");
       }
    }
}