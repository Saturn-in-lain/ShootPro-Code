package com.sat.resourses;

import java.util.Vector;
import com.sat.shootpro.Target;
import com.sat.shootpro.Target.BULLET_SHOT_STRUCTURE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class TouchImageView extends ImageView 
{
   Matrix matrix = new Matrix();
   static String LOG = "TouchImageView";
   
   Bitmap imageBitMap          = null;
   /*--------------------------------------------------*/
   private Paint  currentPaint = null;
   public float   paint_left   = 0;
   public float   paint_top    = 0;
   public float   paint_right  = 0;
   public float   paint_bottom = 0;
   /*--------------------------------------------------*/
   
   // We can be in one of these 3 states
   static final int NONE = 0;
   static final int DRAG = 1;
   static final int ZOOM = 2;
  
   int mode = NONE;

   // Remember some things for zooming
   PointF last  = new PointF();
   PointF start = new PointF();
   float minScale = 1f;
   float maxScale = 4f;
   float[] m;

   float redundantXSpace, 
         redundantYSpace, 
         origRedundantXSpace,
         origRedundantYSpace;

   float width, height;
   
   /**------------------------------------------**/
   static boolean fZoom = false;
   static double  counter = 0;
   /**------------------------------------------**/
   
   static final int   CLICK      = 3;
   static final float SAVE_SCALE = 1f;
   
   float saveScale = SAVE_SCALE;

   float right, 
         bottom, 
         origWidth, 
         origHeight, 
         bmWidth, 
         bmHeight, 
         origScale,
         origBottom, 
         origRight;

   ScaleGestureDetector mScaleDetector     = null;
   GestureDetector      mGestureDetector   = null;
   Context              context            = null;
 
   /**************************************************************************
    * Function: TouchImageView() 
    * @return (View) 
    * ***********************************************************************/
@SuppressLint("WrongCall")
public TouchImageView(Context context, AttributeSet attrs)
   {
      super(context,attrs);
      super.setClickable(true);
      this.context = context;
      mScaleDetector = new ScaleGestureDetector(context, new ScaleListener(this));
            
      m = new float[9];
      matrix.setTranslate(minScale, minScale);
      setImageMatrix(matrix);
      setScaleType(ScaleType.MATRIX);

      /*---------------------------------------------------------*/
      currentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
      currentPaint.setDither(true);
      currentPaint.setColor(0xFF00CC00);  // alpha.r.g.b
      currentPaint.setStyle(Paint.Style.STROKE);
      currentPaint.setStrokeJoin(Paint.Join.ROUND);
      currentPaint.setStrokeCap(Paint.Cap.ROUND);
      currentPaint.setStrokeWidth(2);
      currentPaint.setColor(Color.RED);
      /*---------------------------------------------------------*/
      this.onMeasure((int)width, (int)height);
      
      setOnTouchListener(new OnTouchListener()
      {
         @Override
         public boolean onTouch(View v, MotionEvent event)
         {
            boolean onDoubleTapEvent = mGestureDetector.onTouchEvent(event);
            
            if (true == onDoubleTapEvent)
            {
               // Reset Image to original scale values
               mode      = NONE;
               saveScale = SAVE_SCALE;
               bottom    = origBottom;
               right     = origRight;
               last      = new PointF();
               start     = new PointF();
               m         = new float[9];
               matrix    = new Matrix();
               /*------------------------------------------------------------------------*/
               counter++;
               if( ( fZoom == true ) && ( counter%2 == 0) )
               {
                  matrix.setScale( origScale, origScale ); 
                  matrix.postTranslate(origRedundantXSpace, origRedundantYSpace);

                  matrix.setTranslate(maxScale, maxScale);
                  setScaleType(ScaleType.MATRIX);
                  fZoom = false;
                  Log.e(LOG,"onDoubleTap MAX: origScale=" + origScale);
                  counter = 0;
               }
               else if( (fZoom == false) && (counter%2 == 0) )
               {

                  matrix.setScale( origScale/2, origScale/2 ); 
                  matrix.postTranslate(origRedundantXSpace/2, origRedundantYSpace/2);

                  matrix.setScale(origScale, origScale); 
                  matrix.setTranslate(minScale, minScale);
                  setScaleType(ScaleType.MATRIX);
                  fZoom = true;
               }
               //           TODO: this is for test
               /*-------------------------------------------------*/
                  //setImageMatrix(matrix);
                  invalidate();
               /*-------------------------------------------------*/
               /*-------------------------------------------------*/
               return true;
            }

            mScaleDetector.onTouchEvent(event);

            matrix.getValues(m);
            float x = m[Matrix.MTRANS_X];
            float y = m[Matrix.MTRANS_Y];
            PointF curr = new PointF(event.getX(), event.getY());

            switch (event.getAction())
            {
               case MotionEvent.ACTION_DOWN:
                  last.set(event.getX(), event.getY());
                  start.set(last);
                  mode = DRAG;
                  break;
                  
               case MotionEvent.ACTION_MOVE:
                  if (mode == DRAG)
                  {
                     float deltaX = curr.x - last.x;
                     float deltaY = curr.y - last.y;
                     float scaleWidth = Math.round(origWidth * saveScale);
                     float scaleHeight = Math.round(origHeight * saveScale);
                     if (scaleWidth < width)
                     {
                        deltaX = 0;
                        if (y + deltaY > 0)
                           deltaY = -y;
                        else if (y + deltaY < -bottom)
                           deltaY = -(y + bottom);
                     } 
                     else if (scaleHeight < height)
                     {
                        deltaY = 0;
                        if (x + deltaX > 0)
                           deltaX = -x;
                        else if (x + deltaX < -right)
                           deltaX = -(x + right);
                     } 
                     else
                     {
                        if (x + deltaX > 0)
                           deltaX = -x;
                        else if (x + deltaX < -right)
                           deltaX = -(x + right);
   
                        if (y + deltaY > 0)
                           deltaY = -y;
                        else if (y + deltaY < -bottom)
                           deltaY = -(y + bottom);
                     }
                     matrix.postTranslate(deltaX, deltaY);
                     last.set(curr.x, curr.y);
                  }
                  break;
   
               case MotionEvent.ACTION_UP:
                  mode = NONE;
                  int xDiff = (int) Math.abs(curr.x - start.x);
                  int yDiff = (int) Math.abs(curr.y - start.y);
                  if (xDiff < CLICK && yDiff < CLICK)
                     performClick();
                  break;
   
               case MotionEvent.ACTION_POINTER_UP:
                  mode = NONE;
                  break;
            }
            setImageMatrix(matrix);
            invalidate();
            return true; // indicate event was handled
         }
      });
      /** ==================================================== **/
      mGestureDetector = new GestureDetector(context,
         new GestureDetector.SimpleOnGestureListener()
         {
            @Override
            public boolean onDoubleTapEvent(MotionEvent e)
            {
               return true;
            }
         });
   }
   
   /**************************************************************************
    * Function: onDraw() 
    * @param canvas - Canvas
    * @return (View) 
    * ***********************************************************************/
   @Override
   protected void onDraw(Canvas canvas) 
   {
       super.onDraw(canvas);
   }

   /**************************************************************************
    * Function: ProcessingBitmap() - main task for this function is to
    * draw bullet holes in the target.
    * @param bm1         - Bitmap
    * @param listOfShots -  Vector<BULLET_SHOT_STRUCTURE>
    * @return newBitmap - (Bitmap) 
    * ***********************************************************************/
   public Bitmap ProcessingBitmap(Bitmap bm1, 
                                  Vector<BULLET_SHOT_STRUCTURE>    listOfShots)
   {
       Bitmap newBitmap  = null;
       Config config     = bm1.getConfig();

       if(null == config)
       {
          config = Bitmap.Config.ARGB_8888;
       }
       
       newBitmap = Bitmap.createBitmap(bm1.getWidth(), 
                                       bm1.getHeight(), 
                                       config);
       
       Canvas newCanvas = new Canvas(newBitmap);
       newCanvas.drawBitmap(bm1, 0, 0, null);

       
       for(BULLET_SHOT_STRUCTURE element : listOfShots )
       {
          /* placeRedDotAtCoordinates(element, newCanvas); */
          placeImageAtCoordinates(element.getpCoordinatesOfTheHole(), 
                                  newCanvas, 
                                  element.getImageBulletHoleId());
       }
       return newBitmap;
    }
   
   /**************************************************************************
    * Function: placeImageAtCoordinates() 
    * @param element   - Point
    * @param newCanvas - Canvas
    * @param id        - Integer
    * @return (None). 
    * ***********************************************************************/
   private void placeImageAtCoordinates(Point element,  
                                        Canvas newCanvas, 
                                        Integer id)
   {
      Paint p  = new Paint();
      Bitmap b = BitmapFactory.decodeResource(getResources(), id);
      p.setColor(Color.RED);
      
      int w = b.getWidth();
      int h = b.getHeight();
      
      // Here is random coord part.
      w = (w/2) /*+ getRandom(0,w)*/; 
      h = (h/2) /*+ getRandom(0,h)*/;
      
      newCanvas.drawBitmap(b, 
                          (element.x - w), 
                          (element.y - h),
                           p);
   }
   
   /**************************************************************************
    * Function: placeRedDotAtCoordinates() - May be needed in future testings
    * @param element   - Point
    * @param newCanvas - Canvas
    * @return (None) 
    * ***********************************************************************/
//   @SuppressWarnings("unused")
//   private void placeRedDotAtCoordinates(Point element,  Canvas newCanvas)
//   {
//      newCanvas.drawCircle( element.x, 
//                            element.y, 
//                            2, 
//                            currentPaint);
//   }
   
   /**************************************************************************
    * Function: setImageBitmap() 
    * @return (View) 
    * ***********************************************************************/
   @Override
   public void setImageBitmap(Bitmap bm)
   {
      bmWidth     = bm.getWidth();
      bmHeight    = bm.getHeight();
      super.setImageBitmap(bm);
   }

   /**************************************************************************
    * Function: setTargetBitmap() 
    * @param  bm          -  Bitmap
    * @param  listOfShots -  Vector<PointF> coordinates of bullet shots on 
    * target for marking.
    * @return (View) 
    * ***********************************************************************/
   public void setTargetBitmap(Bitmap bm, Target TargetClass)
   {
      bmWidth     = bm.getWidth();
      bmHeight    = bm.getHeight();
      
      TargetClass.setLargeTargetGabarites( bm.getWidth(), bm.getHeight());
      TargetClass.calculateLargeTargetHits();
      
      imageBitMap = ProcessingBitmap(bm, 
                                     TargetClass.getBulletLargeTargetShotsList());
      super.setImageBitmap(imageBitMap);
   }
   
   
   /**************************************************************************
    * Function: setMaxZoom() 
    * @return (View) 
    * ***********************************************************************/
   public void setMaxZoom(float x)
   {
      maxScale = x;
   }

   /**************************************************************************
    * Function: onMeasure() 
    * @return (View) 
    * ***********************************************************************/
   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
   {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
      int  w = (int) bmWidth;
      int  h = (int) bmHeight;
      width = resolveSize(w, widthMeasureSpec);  
      height = resolveSize(h, heightMeasureSpec);
      // Fit to screen.
      float scale = 0;
      float scaleX = (float) width / (float) bmWidth;
      float scaleY = (float) height / (float) bmHeight;
      
      scale = Math.min(scaleX, scaleY);
      matrix.setScale(scale, scale);
      setImageMatrix(matrix);
      saveScale = SAVE_SCALE;
      origScale = scale;
      
      // Center the image
      redundantYSpace = (float) height - (scale * (float) bmHeight);
      redundantXSpace = (float) width - (scale * (float) bmWidth);
      redundantYSpace /= (float) 2;
      redundantXSpace /= (float) 2;
      
      origRedundantXSpace = redundantXSpace;
      origRedundantYSpace = redundantYSpace;
      
      matrix.postTranslate(redundantXSpace, redundantYSpace);
      
      origWidth  = width - (2 * redundantXSpace);
      origHeight = height - (2 * redundantYSpace);
      right = (width * saveScale) - width - (2 * redundantXSpace * saveScale);
      bottom = (height * saveScale) - height - (2 * redundantYSpace * saveScale);
      origRight = right;
      origBottom = bottom;
      setImageMatrix(matrix);
   }
}