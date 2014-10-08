package com.sat.shootpro;

import java.util.Random;
import java.util.Vector;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
/**
 * http://www.wikihow.com/Calculate-Distances-With-a-Mil-Dot-Rifle-Scope
 * */

public class Target
{
   private String LOG = "Target";
   private PointF realMetersGabarites         = null;
   private static double targetDistance       = (0.0);
   private float  MilsAndMeterKoeff           = (0);      // mils size on set distance
   /* Hit coordinate list */
   private Vector<Point> bulletShotsList      = null;
   private int shotsCounter                   = (0);
   /* Small target data */
   private Point smallTargetCenterCoordinates = null;
   private Point smallTargetLeftTop           = null;
   private Point smallTargetRightBottom       = null;
   private Point smallTargetPixelSize         = null;
   private PointF smallTargetMilsSize         = null;
   private int    smallTargetZoomKoef         = (0);
   private double OnePixEqualsMeters          = (0.0);
   /* Large target data */
   private Point largeTargetGabarites         = null;
   private double OnePixEqualsOnLargeTaret    = (0.0);
   private int shotsForLargeCounters          = (0);
   private Vector<Integer>  holeImages        = null;

   
   /******************************************************
    * Constructor of class BULLET_SHOT_STRUCTURE
    ******************************************************/
   public class BULLET_SHOT_STRUCTURE
   {
      private Integer  imageBulletHoleId;
      private Integer  iShotCounter;
      private Point    pCoordinatesOfTheHole; 
      
      private BULLET_SHOT_STRUCTURE(Integer ImageId, Integer Index, Point shot) 
      {
         this.imageBulletHoleId     = ImageId;
         this.iShotCounter          = Index;
         this.pCoordinatesOfTheHole = shot;
      }
      
      public int getImageBulletHoleId()
      {
         return this.imageBulletHoleId;
      }
      
      public int getiShotCounter()
      {
         return this.iShotCounter;
      }
      
      public Point getpCoordinatesOfTheHole()
      {
         return this.pCoordinatesOfTheHole;
      }
   }
   private Vector<BULLET_SHOT_STRUCTURE>  bulletLargeTargetShotsList  = null;
   
   /******************************************************
    * Constructor of class Target
    ******************************************************/
   public Target()
   {
      realMetersGabarites           = new PointF();
      bulletShotsList               = new Vector<Point>();
      holeImages                    = new Vector<Integer>();
      smallTargetLeftTop            = new Point();
      smallTargetRightBottom        = new Point();
      smallTargetCenterCoordinates  = new Point();
      smallTargetPixelSize          = new Point();
      smallTargetMilsSize           = new PointF();
      largeTargetGabarites          = new Point();
      shotsCounter                  = 0;
      smallTargetZoomKoef           = 0;
      shotsForLargeCounters         = 0;
      bulletLargeTargetShotsList    = new Vector<BULLET_SHOT_STRUCTURE>();
      
      addBulletHolesList();
   }
   
   /***************************************************************************
    *  setRealTargetSize() - Only meters parameters
    *  @param width   -- double
    *  @param  height -- double
    *  @return None.
   **************************************************************************/
   public void setRealTargetSize(double width, double height)
   {
      realMetersGabarites.x = (float) width;
      realMetersGabarites.y = (float) height;

      MilsAndMeterKoeff = (float) getMils(realMetersGabarites.y, true);
   }
   
   /***************************************************************************
    *  getMilsAndMeterKoeff() - Only meters parameters
    *  @return MilsAndMeterKoeff.
   **************************************************************************/
   public float getMilsAndMeterKoeff()
   {
      if(0 == MilsAndMeterKoeff)
      {
         Log.e(LOG,"F:[getMilsAndMeterKoeff] >> Was not initialized");
      }
      return MilsAndMeterKoeff;
   }
   
   /***************************************************************************
    *  getRealTargetSize() - Only meters parameters
    *  @param isHeightToReturn   -- boolean
    *  @return size double.
   **************************************************************************/
   public double getRealTargetSize(boolean isHeightToReturn)
   {
      double dRet = 0.0;
      if(true == isHeightToReturn)
      {
         dRet = realMetersGabarites.y;
      }
      else
      {
         dRet = realMetersGabarites.x;
      }
      return dRet;
   }
   
   
   /***************************************************************************
    *  getTargetDistance() - get information about target distance
    *  @return double targetDistance.
   **************************************************************************/
   public double getTargetDistance()
   {
      return targetDistance;
   }
   
   /***************************************************************************
    *  getTargetDistance() - set information about target distance
    *  @param distance - double
    *  @return None.
   **************************************************************************/
   public void setTargetDistance(double distance)
   {
      targetDistance = distance; 
   }
   
   /***************************************************************************
    *  setLargeTargetSize() - Only meters parameters
    *  @param width   -- double
    *  @param  height -- double
    *  @return None.
   **************************************************************************/
   public void setLargeTargetGabarites(int width, int height)
   {
      largeTargetGabarites.x = width;
      largeTargetGabarites.y = height;
      
      setOnePixEqualsOnLargeTaret();
   }
   
   /***************************************************************************
    *  setBulletShotsList() - Set list
    *  @param List   -- (Vector<PointF>
    *  @return None.
   **************************************************************************/
   public void setBulletShotsList(Vector<Point> List)
   {
      bulletShotsList = List;
   }

   /***************************************************************************
    *  addBulletShotToList()
    *  @param shot   -- PointF
    *  @return None.
   **************************************************************************/
   public void addBulletShotToList(Point shot)
   {
      bulletShotsList.add(shotsCounter, shot);
      shotsCounter++;
   }
   
   /***************************************************************************
    *  clearBulletShotToList()
    *  @param None
    *  @return None.
   **************************************************************************/
   public void clearBulletShotToList()
   {
      bulletShotsList.clear();
      shotsCounter = 0;
   }
   
   /***************************************************************************
    *  addBulletHolesList() - Init image resources
    *  @return None.
   **************************************************************************/
   private void addBulletHolesList()
   {
      if(null != holeImages)
      {
         holeImages.add(R.drawable.hole1);
         holeImages.add(R.drawable.hole2);
      }
   }
   
   /***************************************************************************
    *  getBulletShotsList() - Get list
    *  @return bulletShotsList   -- Vector<Point>
   **************************************************************************/
   public Vector<Integer> getBulletImageHolesList()
   {
      if(null == holeImages)
      {
         Log.e(LOG,"F:[getBulletImageHolesList]-> NULL");
      }
      return holeImages;
   }
   /***************************************************************************
    *  getBulletShotsList() - Get list of BULLET_SHOT_STRUCTURE elements
    *  @param None.
    *  @param None.
    *  @return bulletLargeTargetShotsList   -- Vector<BULLET_SHOT_STRUCTURE>
   **************************************************************************/
   public Vector<BULLET_SHOT_STRUCTURE> getBulletLargeTargetShotsList()
   {
      if (null != bulletLargeTargetShotsList)
      {
         if (!bulletLargeTargetShotsList.isEmpty() )
         {
            return bulletLargeTargetShotsList;
         }
         else
         {
            Log.e(LOG,"F:[getBulletLargeTargetShotsList]>>empty");
         }
      }
      else
      {
         Log.e(LOG,"F:[getBulletLargeTargetShotsList]>>null");
      }
      return bulletLargeTargetShotsList;
   }

   /***************************************************************************
    *  addBulletShotToLargeTargetList()
    *  @param shot   -- Point
    *  @return None.
   **************************************************************************/
   private void addBulletShotToLargeTargetList(Point shot)
   {
      if(null != bulletLargeTargetShotsList)
      {
         if( 1 == getRandom(0,1) )
         {
            shot.x += getRandom(0,MainActivity.UICustom.BULLET_HOLE_RANDOM);
            shot.y += getRandom(0,MainActivity.UICustom.BULLET_HOLE_RANDOM);
         }
         else
         {
            shot.x -= getRandom(0,MainActivity.UICustom.BULLET_HOLE_RANDOM);
            shot.y -= getRandom(0,MainActivity.UICustom.BULLET_HOLE_RANDOM);
         }
         
         bulletLargeTargetShotsList.add( shotsForLargeCounters, 
                                         new BULLET_SHOT_STRUCTURE(returnRandomHole(),
                                                                   shotsForLargeCounters,
                                                                   shot)  );
         shotsForLargeCounters++;
      }
   }
   
   /***************************************************************************
    *  returnRandomHole()
    *  @param None.
    *  @return id - .
   **************************************************************************/
   private Integer returnRandomHole()
   {
      Integer id = 0;
      if(null != holeImages)
      {
         id = holeImages.get(
                              getRandom( (0),
                                         (holeImages.size()-1)
                                        )
                                   );
      }
      return id;
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
         randomNumber = r.nextInt(max - min + 1) + min;
      return randomNumber;
   }
   
   /***************************************************************************
    *  clearBulletShotToLargeTargetList()
    *  @param None.
    *  @return None.
   **************************************************************************/
   private void clearBulletShotToLargeTargetList()
   {
      if(null != bulletLargeTargetShotsList)
      {
         bulletLargeTargetShotsList.clear();
         shotsForLargeCounters = 0;
      }
   }
   
   /***************************************************************************
    *  setSmallTargetZoomKoef()
    *  @param data   -- int Zooming koef
    *  @return None.
   **************************************************************************/
   public void setSmallTargetZoomKoef(int data)
   {
     smallTargetZoomKoef = data;
   }
   
   /***************************************************************************
    *  getSmallTargetZoomKoef()
    *  @return int ZoomKoef.
   **************************************************************************/
   public int getSmallTargetZoomKoef()
   {
     return smallTargetZoomKoef;
   }
  
   /***************************************************************************
    *  setSmallTargetCenterCoordinates() Function sets actually left-top
    *  and right-bottom coordinates. Also calculate center coordinate
    *  for target small picture.
    *  @param leftTopCoord - Point 
    *  @return None.
   **************************************************************************/
   public void setSmallTargetCoordinates(Point leftTopCoord) 
   {
      if(null != leftTopCoord)
      {
         smallTargetLeftTop       = leftTopCoord;
         
         if(null != smallTargetPixelSize)
         {
            smallTargetRightBottom.x = smallTargetLeftTop.x + smallTargetPixelSize.x;
            smallTargetRightBottom.y = smallTargetLeftTop.y + smallTargetPixelSize.y;
            
            smallTargetCenterCoordinates.x = smallTargetLeftTop.x + smallTargetPixelSize.x/2;
            smallTargetCenterCoordinates.y = smallTargetLeftTop.y + smallTargetPixelSize.y/2;
         }
         setOnePixEqualsMeters();
      }
      else
      {
         Log.e(LOG,"F:[setSmallTargetCenterCoordinates]>>null");
      }
   }
   
   /***************************************************************************
    *  setTargetPixelSize() -
    *  @param targetGabarites - Point 
    *  @return None.
   **************************************************************************/
   public void setSmallTargetPixelSize(int pixelSizeEvalution)
   {
      
      if(null != smallTargetMilsSize)
      {
         smallTargetPixelSize.x =  (int) (pixelSizeEvalution * smallTargetMilsSize.x);
         smallTargetPixelSize.y =  (int) (pixelSizeEvalution * smallTargetMilsSize.y);

         if(0 != smallTargetZoomKoef)
         {
            smallTargetPixelSize.x *= smallTargetZoomKoef;
            smallTargetPixelSize.y *= smallTargetZoomKoef;
         }
      }
      else
      {
         Log.e(LOG,"F:[setSmallTargetPixelSize]>>null");
      }
   }
   
   /***************************************************************************
    *  getSmallTargetPixelSize() 
    *  @param isHeightToReturn - Flag boolean. True = return height 
    *  @return None.
   **************************************************************************/
   public int getSmallTargetPixelSize(boolean isHeightToReturn)
   {
      int iRet = 0;
      if (null != smallTargetPixelSize)
      {
         if (true == isHeightToReturn)
         {
            iRet = smallTargetPixelSize.y;
//Log.d(LOG,String.format("F:[getSmallTargetPixelSize]>>h[%d]",iRet));
         }
         else
         {
            iRet = smallTargetPixelSize.x;
//Log.d(LOG,String.format("F:[getSmallTargetPixelSize]>>w[%d]",iRet)); 
         }
      }
      else
      {
         Log.e(LOG,"F:[getSmallTargetPixelSize]>>null"); 
      }
      return iRet;
   }
   
   /***************************************************************************
    *  isBulletHitTheTarget() 
    *  @param bulletHit - Point Bullet hit coordinates  
    *  @return None.
   **************************************************************************/
   public boolean isBulletHitTheTarget( Point bulletHit )
   {
      boolean bRet = false;
      
      if( ( bulletHit.x > smallTargetLeftTop.x ) && 
            ( bulletHit.x < smallTargetRightBottom.x ) )
      {
         if( ( bulletHit.y  > smallTargetLeftTop .y ) &&                
               ( bulletHit.y  < smallTargetRightBottom.y ) )
         {  
            addBulletShotToList(bulletHit);
            bRet = true;
         }
      }
      else
      {
         Log.e(LOG,"F:[isBulletHitTheTarget]>> x["+bulletHit.x+"]y["+bulletHit.y+"]");
      }
      return bRet;
   }
   /***************************************************************************
    *  setSmallTargetMilsSize() -
    *  @param double x 
    *  @param double y 
    *  @return None.
   **************************************************************************/
   public void setSmallTargetMilsSize(double x, double y)
   {
      if(0 != MilsAndMeterKoeff)
      {
         smallTargetMilsSize.x =  (float) (x * MilsAndMeterKoeff); 
         smallTargetMilsSize.y =  (float) (y * MilsAndMeterKoeff);
      }
      else
      {
         Log.e(LOG,"F:[setSmallTargetMilsSize] ->> ERROR");
      }
//      Log.e(LOG,String.format("F:[setSmallTargetMilsSize] mils w[%g]y[%g] %g", 
//                              smallTargetMilsSize.x,
//                              smallTargetMilsSize.y,
//                              MilsAndMeterKoeff));
   }

   /***************************************************************************
    *  calculateLargeTargetHits() - perform convention of small bullet hits list
    *  for scaled target. Magic use proportion from small target size and
    *  real target meters size 
    *  Lets use only width parameter for conversion
    *  @param None.
    *  @return None.
   **************************************************************************/
   public void calculateLargeTargetHits()
   {
      int stepForRecalculation = 0;
      
      if( (null != largeTargetGabarites) && 
                                    (null != bulletShotsList) )
      {
         stepForRecalculation = (int) (largeTargetGabarites.x / smallTargetPixelSize.x);

         
         if(bulletShotsList.size() > bulletLargeTargetShotsList.size())
         {
            for(int i = bulletLargeTargetShotsList.size(); i < bulletShotsList.size(); i++)
            {
               Point temp = new Point();
               temp.x = ( bulletShotsList.get(i).x - smallTargetLeftTop.x)*stepForRecalculation;
               temp.y = ( bulletShotsList.get(i).y - smallTargetLeftTop.y)*stepForRecalculation;
               addBulletShotToLargeTargetList(temp);
            }
         }
         else if(bulletShotsList.size() < bulletLargeTargetShotsList.size())
         {
            clearBulletShotToLargeTargetList();
            for(Point element : bulletShotsList )
            {
               Point temp = new Point();
               temp.x = (element.x - smallTargetLeftTop.x)*stepForRecalculation;
               temp.y = (element.y - smallTargetLeftTop.y)*stepForRecalculation;
               addBulletShotToLargeTargetList(temp);
            }   
         }
         else if(bulletShotsList.size() == 0)
         {
            clearBulletShotToLargeTargetList();
         }
      }
      else
      {
         Log.e(LOG,"F:[calculateLargeTargetHits]>>null"); 
      }
   }
   
   /***************************************************************************
    *  setOnePixEqualsOnLargeTaret()
    *  @param None. 
    *  @return None.
   **************************************************************************/
   public void setOnePixEqualsOnLargeTaret()
   {
      if( (null != realMetersGabarites) && (null != largeTargetGabarites)) 
      {
         OnePixEqualsOnLargeTaret = ( (1.0 * realMetersGabarites.x) /  (double)largeTargetGabarites.x);
      }
      else
      {
         OnePixEqualsOnLargeTaret = 0.0;
         Log.e(LOG,"F:[calculateMetersPerPixOnLargeTraget]>>null");
      }
   }
   /***************************************************************************
    *  getOnePixEqualsOnLargeTaret()
    *  @param None. 
    *  @return None.
   **************************************************************************/
   public double getOnePixEqualsOnLargeTaret()
   {
      return OnePixEqualsOnLargeTaret;
   }

   /***************************************************************************
    *  setOnePixEqualsMeters()
    *  @param None. 
    *  @return None.
   **************************************************************************/
   public void setOnePixEqualsMeters()
   {
      if( (null != realMetersGabarites) && (null != smallTargetPixelSize)) 
      {
         OnePixEqualsMeters = ( (1.0 * realMetersGabarites.x) / (double)smallTargetPixelSize.x);
      }
      else
      {
         OnePixEqualsMeters = 0.0;
         Log.e(LOG,"F:[calculateMetersPerPixOnSmallTraget]>>null");
      }
   }

   /***************************************************************************
    *  getOnePixEqualsMeters()
    *  @param None. 
    *  @return None.
   **************************************************************************/
   public double getOnePixEqualsMeters()
   {
      return OnePixEqualsMeters;
   }
  
   /**************************************************************************
    * Function: getMils 
    * @param  targetSize      double
    * @param  distanceInMeter boolean true - in meters, false - yards  
    * @return dRet - mils. 
    * ***********************************************************************/
   public static double getMils(double targetSize, boolean distanceInMeter)
   {
      double dRet = StartMenuActivity.Engine.init;
      
      if( true == distanceInMeter )
      {
         dRet = StartMenuActivity.Engine.getMilsFromMetersDistance(targetSize,
                                                                   targetDistance);
      }
      else
      {
         dRet = StartMenuActivity.Engine.getMilsFromYardDistance(targetSize,
                                                                 targetDistance);
      }
      return dRet;
   }
}