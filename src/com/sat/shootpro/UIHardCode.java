package com.sat.shootpro;
import android.graphics.Point;
import android.util.Log;

/******************************************************************
 * 
 * @author stas.savinov
 * Class was created with only one purpose - gather all
 * hard code issues in one place for future management. 
 * @category HUCK
 * @version 1.0.0
 *****************************************************************/
public class UIHardCode
{
/*                   MainActivity                                */
   public int backgroundPictureX             = (2500);
   public int backgroundPictureY             = (690);
 //---------------------------------------------------------------//
   public int scopeAdaptMainActX             = (15);
   public int scopeAdaptMainActY             = (30);
//---------------------------------------------------------------//
   public int scopeMarginMainActLeft         = (45);
   public int scopeMarginMainActTop          = (60);
//---------------------------------------------------------------//   
   public int centerCoordMarginMainActLeft   = (45);
   public int centerCoordMarginMainActTop    = (60);
//---------------------------------------------------------------//   
   public int sliceDetectionHightMainAct     = (200);//(75);
   public int sliceDetectionWidtgMainActTop  = (500);//(140);
//-------------------------------------------------------------//   
   public int AmountOfSlicesMainAct          = (5);
//---------------------------------------------------------------//   
   private int MDPI_fingerMoveMainActUp      = (55);
   private int MDPI_fingerMoveMainActDown    = (45);
   private int MDPI_fingerMoveMainActLeft    = (40);
   private int MDPI_fingerMoveMainActRight   = (55);
//---------------------------------------------------------------//   
   private int HDPI_fingerMoveMainActUp      = (55);
   private int HDPI_fingerMoveMainActDown    = (55);
   private int HDPI_fingerMoveMainActLeft    = (40);
   private int HDPI_fingerMoveMainActRight   = (105);
//---------------------------------------------------------------//   
//                    MainZoomShoot            
   public static  int    yVal                = (1);
   public static  int    zVal                = (2);
   public         int    DURATION            = (100);
   public         int    TIME_STEP           = (700);
   public         int    BULLET_HOLE_RANDOM  = (10);
 //---------------------------------------------------------------//
// private double LDPI                       = (0.75);
   private double MDPI                       = (1.0);
// private int MDPI_x                      = (320);
// private int MDPI_y                      = (480);
   private int MDPI_MOVE                     = (2);
   private int MDPI_MG                       = (38);
   private int MDPI_SG                       = (190);
   private int MDPI_pixelSizeEvalution       = (10);    
   private int MDPI_ScrollTextSize           = (15);
//---------------------------------------------------------------//   
// private double HDPI                       = (1.5);
   private int HDPI_MOVE                     = (5);
   private int HDPI_x                        = (480);
   private int HDPI_y                        = (800);
   private int HDPI_MG                       = (55);
   private int HDPI_SG                       = (300);
   private int HDPI_VerticalClickMargin      = (35);
   
   private int  HDPI_cloudSizeZoomAct        = (20);
   private int  HDPI_cloudTopMarginZoomAct   = (45);
   private int  HDPI_cloudDownMarginZoomAct  = (60);
   private int  HDPI_cloudLeftMarginZoomAct  = (-10);
   private int  HDPI_cloudRightMarginZoomAct = (10);
   
   private int HDPI_sliceMoveUpBorder        = (65);
   private int HDPI_sliceMoveDownBorder      = (0);
   private int HDPI_sliceMoveLeftBorder      = (150);
   private int HDPI_sliceMoveRightBorder     = (150);
   private int HDPI_pixelBackgroundMargin    = (30);
//---------------------------------------------------------------//
   private double XHDPI                      = (2.0);
   private int XHDPI_x                       = (800);
   private int XHDPI_y                       = (1280);
   private int XHDPI_MG                      = (-120);
   private int XHDPI_SG                      = (500);
   private int XHDPI_VerticalClickMargin     = (0);
//---------------------------------------------------------------//
   private double XXHDPI                     = (3.0);
   private int XXHDPI_x                      = (2560);
   private int XXHDPI_y                      = (1600);
   private int XXHDPI_MG                     = (55);
   private int XXHDPI_SG                     = (300);
   private int XXHDPI_VerticalClickMargin    = (30);
//---------------------------------------------------------------//
   private String SizeCustomizationName = "MDPI";
   
   public int ScopeSize               = (MDPI_SG);
   public int ScopeGridMargin         = (MDPI_MG);
   public int VerticalClickMargin     = (0);
   public int pixelSizeEvalution      = (int) (MDPI_pixelSizeEvalution * MDPI);
   public int MOVE_STEP               = (MDPI_MOVE);
   public int ScrollTextSize          = (MDPI_ScrollTextSize);
   
   public int pixelBackgroundMargin   = (HDPI_pixelBackgroundMargin);
   public int cloudSizeZoomAct        = (HDPI_cloudSizeZoomAct);
   public int cloudTopMarginZoomAct   = (HDPI_cloudTopMarginZoomAct);
   public int cloudDownMarginZoomAct  = (HDPI_cloudDownMarginZoomAct);
   public int cloudLeftMarginZoomAct  = (HDPI_cloudLeftMarginZoomAct);
   public int cloudRightMarginZoomAct = (HDPI_cloudRightMarginZoomAct);
   public int sliceMoveUpBorder       = (HDPI_sliceMoveUpBorder);
   public int sliceMoveDownBorder     = (HDPI_sliceMoveDownBorder);
   public int sliceMoveLeftBorder     = (HDPI_sliceMoveLeftBorder);
   public int sliceMoveRightBorder    = (HDPI_sliceMoveRightBorder);
   
   public int fingerMoveMainActUp     = (MDPI_fingerMoveMainActUp);
   public int fingerMoveMainActDown   = (MDPI_fingerMoveMainActDown);
   public int fingerMoveMainActLeft   = (MDPI_fingerMoveMainActLeft);
   public int fingerMoveMainActRight  = (MDPI_fingerMoveMainActRight);
   
   /***************************************************************************************
     Class construct 
     @param Point - size of main window 
   ***************************************************************************************/
   public UIHardCode(Point size)
   {
      if( (size.y == HDPI_x) && 
                  (size.x == HDPI_y) )
      {
         this.SizeCustomizationName   = "HDPI";
         this.MOVE_STEP               = (HDPI_MOVE);
         
         this.ScopeSize               = (HDPI_SG);
         this.ScopeGridMargin         = (HDPI_MG);
         this.VerticalClickMargin     = (HDPI_VerticalClickMargin);
         this.pixelSizeEvalution      = (int) (pixelSizeEvalution * MDPI);
         this.pixelBackgroundMargin   = (HDPI_pixelBackgroundMargin);
         
         this.cloudSizeZoomAct        = (HDPI_cloudSizeZoomAct);
         this.cloudTopMarginZoomAct   = (HDPI_cloudTopMarginZoomAct);
         this.cloudDownMarginZoomAct  = (HDPI_cloudDownMarginZoomAct);
         this.cloudLeftMarginZoomAct  = (HDPI_cloudLeftMarginZoomAct);
         this.cloudRightMarginZoomAct = (HDPI_cloudRightMarginZoomAct);
         
         this.sliceMoveUpBorder       = (HDPI_sliceMoveUpBorder);
         this.sliceMoveDownBorder     = (HDPI_sliceMoveDownBorder);
         this.sliceMoveLeftBorder     = (HDPI_sliceMoveLeftBorder);
         this.sliceMoveRightBorder    = (HDPI_sliceMoveRightBorder);
         
         this.fingerMoveMainActUp     = (HDPI_fingerMoveMainActUp);
         this.fingerMoveMainActDown   = (HDPI_fingerMoveMainActDown);
         this.fingerMoveMainActLeft   = (HDPI_fingerMoveMainActLeft);
         this.fingerMoveMainActRight  = (HDPI_fingerMoveMainActRight);
         //Log.e("[UIHardCode]","[ HDPI ]");
      } 
      else if ((size.y == XHDPI_x) && 
                        (size.x == XHDPI_y) ) // Nexus 7
      {
         this.SizeCustomizationName = "XHDPI";
         this.ScopeSize             = XHDPI_SG;
         this.ScopeGridMargin       = XHDPI_MG;
         this.VerticalClickMargin   = XHDPI_VerticalClickMargin;
         this.pixelSizeEvalution    = (int) (pixelSizeEvalution  * XHDPI);
         this.ScrollTextSize        = (int) (MDPI_ScrollTextSize * XHDPI);
         //Log.e("[UIHardCode]","[ XHDPI ]");

      } 
      else if ((size.y == XXHDPI_x) && 
                        (size.x == XXHDPI_y) )
      {
         this.SizeCustomizationName = "XXHDPI";
         this.ScopeSize             = XXHDPI_SG;
         this.ScopeGridMargin       = XXHDPI_MG;
         this.VerticalClickMargin   = XXHDPI_VerticalClickMargin;
         this.pixelSizeEvalution    = (int) (pixelSizeEvalution * XXHDPI);
         //Log.e("[UIHardCode]","[ XXHDPI ]");
      } 
      else
      {
         // By default we have MDPI
         Log.e("[UIHardCode]","[ MDPI ] >> x:" + size.x + " y:" + size.y);
      }
   }
   
   /**************************************************************************
    * Function: toString() -> return size customization name 
    * @return  String.
    * ***********************************************************************/
   public String toString()
   {
      return this.SizeCustomizationName;
   }
}
