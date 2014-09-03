package com.sat.ballistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sat.shootpro.FileManipulations;
import com.sat.shootpro.R;

import android.content.Context;
import android.util.Log;

public class BulletSpecifications
{
    /* TODO: this file mast download and pars txt file which will contain 
           information about bullets. 
           http://www.norma.cc/en/Products/Hunting/308-Winchester/
    */
   
   /* This data from http://guns.allzip.org/topic/91/902146.html
    * Bullet - Berger 7mm 168 grain VLD */
   
   //public static double rtr                = 0.57;   
   //public static double bulletCenterLength = 0;
   
   
   private class JSONBulletTag
   {
      static final String TAG_S_DATA                        = "TAG";
      static final String TAG_A_AMMO                        = "BULLET_LIST";
      
      static final String TAG_S_NAME                        = "bulletName";
      static final String TAG_D_DIAMETER                    = "bulletDiametr";
      static final String TAG_D_LENGTH                      = "bulletTotalLengs"; 
      
      static final String TAG_B_GRAINS                      = "isMassInGrains";
      static final String TAG_D_WEIGHT                      = "bulletWeight";
   
      static final String TAG_D_RADIUS                      =  "bulletRadius";
      static final String TAG_D_RADIUS_TIP                  =  "bulletRadiusOfTip";
      static final String TAG_D_NODE_DIAMETER               =  "bulletNoseDiameter"; 
      static final String TAG_D_DIAMETER_BLUNTTIP           =  "bulletDiameterOfBluntTip";
      static final String TAG_D_HOSE_SHOULDER_DIAMETER      =  "bulletNoseShoulderDiameter";
    
      static final String TAG_D_NOSE_DIAMETER_STEP          = "bulletNoseDiameterOverCircumferentialStep";
   
      static final String TAG_D_DISTANCE_TO_CENTER_OJIV     = "bulletDistanceToCenterOfOgiveCurve";
      static final String TAG_D_NOSE_LENGTH                 = "bulletNoseLength";
      static final String TAG_D_BEVEl_ANGLE_NOSE            = "bulletBevelAngleOfNose"; 
      static final String TAG_D_SECAN_OJIV_ANGLE            = "bulletSecantOgiveCurveAngle";
      static final String TAG_D_BEARING_LENGTH              = "bulletBearingLength";
            
      static final String TAG_D_MAIN_BELT_DIAMETER          = "bulletMainBeltDiameter";

      static final String TAG_D_TAIL_BOAT_LENGTH            = "bulletLengthsOfBoatTail";
      static final String TAG_D_DIAMETER_OF_BOAT            = "bulletDiameterOfBoatTail";
      static final String TAG_D_BEVEL_TAIL_ANGLE            = "bulletBevelBoatTailAngle";
     
      static final String TAG_D_LENGTH_FROM_CANNELUR        = "bulletLengthFromCannelureToBase";
      static final String TAG_D_CANNELUR_WIDTH              = "bulletCannelureWidth";
      static final String TAG_D_DISTANCE_BETWEEN_CANNELURES = "bulletDistanceBetweenTwoCannelures";
      static final String TAG_D_DIAMETER_OF_STEP            = "bulletDiameterOfCircumferentialStep";
      static final String TAG_D_DISTANCE_STEP_AND_BASE      = "bulletDistanceFromCircumStepAndBulletBase";
      static final String TAG_D_ANGLE_STEP                  = "bulletAngleOfCircumferentialStep";
      static final String TAG_D_KNURLING_AND_BASE           = "bulletDistanceFromCircumKnurlingToBulletBase";
      static final String TAG_D_DISTANCR_PUNCHING_BASE      = "bulletDistanceFrompunchingPointToBulletBase"; 
   
      static final String TAG_S_FORM                        = "bulletFormOfBase";
      static final String TAG_S_MATERIALS                   = "bulletMaterials";
      static final String TAG_S_COLOR_MARKING               = "bulletColorMarking";
      
      static final String TAG_D_CALIBRE                     = "bulletCalibre";
      static final String TAG_D_DIF_DIAMETER                = "difDiameter";
      static final String TAG_D_RADIUS_TANG                 = "radiusTang";
      static final String TAG_RADIUS_OJIV                   = "radiusOjivInCalibre";
      static final String TAG_RTR                           = "RtR";
          
      static final String TAG_D_DEVIATION                   = "bulletDeviationKoefficient"; 
      static final String TAG_D_BALLISTIC_KOEFF             = "bulletBallisticsKoefficient";
     
      static final String TAG_D_START_SPEED                 = "ammo_startSpeed";
      static final String TAG_D_SPEED_AT_X_YARD             = "ammo_speedAtXyards";
      static final String TAG_D_START_DISTANCE              = "bullet_startDistance";
      static final String TAG_D_XYARD_DISTANCE              = "bullet_XyardDistance";
      static final String TAG_D_CROSS_SECTION               = "ammo_crossSection";
      
      static final String TAG_D_W1_CONST                    = "bullet_W1Const";
      static final String TAG_D_TURN_SPEED                  = "bullet_turn_speed";
      static final String TAG_D_K_PARAM                     = "bullet_K_param";
      
      static final String TAG_A_BULLET_TYPE                 = "BULLET_TYPE";
      static final String TAG_A_BULLET_DATA                 = "BULLET";
   }
   
   /**************************************************************************
   * Function: BulletSpecifications - constructor
   * ***********************************************************************/ 
   public BulletSpecifications(Context ctx, 
                               BulletInfo BulletParameters,
                               int LastBulletIndex)
   {
      String parserString = null;
      parserString = FileManipulations.readFromLocalFile(ctx, 
                                                         R.raw.bulletbase);
      BulletSpecifications.LoadBulletDataBase(ctx,
                                             BulletParameters,
                                             LastBulletIndex,
                                             parserString);

   }
   
   
   /**************************************************************************
    * Function: LoadBulletDataBase - 
    * @param  ctx       Context
    * @param  bInfo     BulletInfo
    * @param  bulletTag int
    * @return 
    * ***********************************************************************/ 
   public static void LoadBulletDataBase(Context ctx, BulletInfo bInfo, int bulletTag,String parserString)
   {
      if( null != ctx )
      {
         if(null != parserString)
         {
            jsonBulletParser( parserString,
                              bInfo,
                              bulletTag);
         }
      }
      else
      {
         Log.e("BulletSpecifications","F:[LoadBulletDataBase] >> ERROR Ctx == null");
      }
   }
   
   /**************************************************************************
    * Function: jsonEncyclopediaParser - Parser of JSON
    * @param  jsonString - String - JSON string for parsing 
    * @param  bInfo      - BulletInfo class that will be filled with data
    * @param  bulletTag  - int number of selected ammo 
    * @return None.
    * ***********************************************************************/ 
   private static void jsonBulletParser(String jsonString, BulletInfo bInfo, int bulletTag)
   {
      try
      {
         JSONObject jsonFullResult = new JSONObject(jsonString);
         
         String start = jsonFullResult.getString(JSONBulletTag.TAG_S_DATA);
         if( !start.equals("Bullets"))
         {
            Log.e("BulletParser","ERROR ON START OF PARSER");
            return;
         }
         JSONArray jsonBullets = jsonFullResult.getJSONArray(JSONBulletTag.TAG_A_AMMO);
         JSONObject selectedBullet = jsonBullets.getJSONObject(bulletTag);
         /*========================================================================================================*/
         bInfo.bulletName                 = selectedBullet.getString(JSONBulletTag.TAG_S_NAME);
         bInfo.bulletDiametr              = selectedBullet.getDouble(JSONBulletTag.TAG_D_DIAMETER);
         bInfo.bulletTotalLengs           = selectedBullet.getDouble(JSONBulletTag.TAG_D_LENGTH);
         
         bInfo.isInGrains                 = selectedBullet.getBoolean(JSONBulletTag.TAG_B_GRAINS);
         bInfo.bulletWeight               = selectedBullet.getDouble(JSONBulletTag.TAG_D_WEIGHT);
         
         bInfo.bulletRadius               =  selectedBullet.getDouble(JSONBulletTag.TAG_D_RADIUS);
         bInfo.bulletRadiusOfTip          =  selectedBullet.getDouble(JSONBulletTag.TAG_D_RADIUS_TIP);
         bInfo.bulletNoseDiameter         =  selectedBullet.getDouble(JSONBulletTag.TAG_D_NODE_DIAMETER);
         bInfo.bulletDiameterOfBluntTip   =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DIAMETER_BLUNTTIP);
         bInfo.bulletNoseShoulderDiameter =  selectedBullet.getDouble(JSONBulletTag.TAG_D_HOSE_SHOULDER_DIAMETER);
         
         bInfo.bulletNoseDiameterOverCircumferentialStep =  selectedBullet.getDouble(JSONBulletTag.TAG_D_NOSE_DIAMETER_STEP);
         
         bInfo.bulletDistanceToCenterOfOgiveCurve  =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DISTANCE_TO_CENTER_OJIV);
         bInfo.bulletNoseLength                    =  selectedBullet.getDouble(JSONBulletTag.TAG_D_NOSE_LENGTH);
         bInfo.bulletBevelAngleOfNose              =  selectedBullet.getDouble(JSONBulletTag.TAG_D_BEVEl_ANGLE_NOSE);
         bInfo.bulletSecantOgiveCurveAngle         =  selectedBullet.getDouble(JSONBulletTag.TAG_D_SECAN_OJIV_ANGLE);
         bInfo.bulletBearingLength                 =  selectedBullet.getDouble(JSONBulletTag.TAG_D_BEARING_LENGTH);
         
         bInfo.bulletMainBeltDiameter              =  selectedBullet.getDouble(JSONBulletTag.TAG_D_MAIN_BELT_DIAMETER);
         bInfo.bulletLengthsOfBoatTail             =  selectedBullet.getDouble(JSONBulletTag.TAG_D_TAIL_BOAT_LENGTH);
         bInfo.bulletDiameterOfBoatTail            =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DIAMETER_OF_BOAT);
         bInfo.bulletBevelBoatTailAngle            =  selectedBullet.getDouble(JSONBulletTag.TAG_D_BEVEL_TAIL_ANGLE);
         
         bInfo.bulletLengthFromCannelureToBase     =  selectedBullet.getDouble(JSONBulletTag.TAG_D_LENGTH_FROM_CANNELUR);
         bInfo.bulletCannelureWidth                =  selectedBullet.getDouble(JSONBulletTag.TAG_D_CANNELUR_WIDTH);
         bInfo.bulletDistanceBetweenTwoCannelures  =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DISTANCE_BETWEEN_CANNELURES);
         bInfo.bulletDiameterOfCircumferentialStep =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DIAMETER_OF_STEP);
         bInfo.bulletDistanceFromCircumStepAndBulletBase    =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DISTANCE_STEP_AND_BASE);
         bInfo.bulletAngleOfCircumferentialStep             =  selectedBullet.getDouble(JSONBulletTag.TAG_D_ANGLE_STEP);
         bInfo.bulletDistanceFromCircumKnurlingToBulletBase =  selectedBullet.getDouble(JSONBulletTag.TAG_D_KNURLING_AND_BASE);
         bInfo.bulletDistanceFrompunchingPointToBulletBase  =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DISTANCR_PUNCHING_BASE);
         
         bInfo.bulletFormOfBase     =  selectedBullet.getString(JSONBulletTag.TAG_S_FORM);
         bInfo.bulletMaterials      =  selectedBullet.getString(JSONBulletTag.TAG_S_MATERIALS);
         bInfo.bulletColorMarking   =  selectedBullet.getString(JSONBulletTag.TAG_S_COLOR_MARKING);

         bInfo.bulletCalibre        = selectedBullet.getDouble(JSONBulletTag.TAG_D_CALIBRE);
         bInfo.difDiameter          =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DIF_DIAMETER);
         bInfo.radiusTang           =  selectedBullet.getDouble(JSONBulletTag.TAG_D_RADIUS_TANG);
         bInfo.radiusOjivInCalibre  =  selectedBullet.getDouble(JSONBulletTag.TAG_RADIUS_OJIV);
         bInfo.RtR                  =  selectedBullet.getDouble(JSONBulletTag.TAG_RTR);

         bInfo.bulletDeviationKoefficient   = selectedBullet.getDouble(JSONBulletTag.TAG_D_DEVIATION);
         bInfo.bulletBallisticsKoefficient  = selectedBullet.getDouble(JSONBulletTag.TAG_D_BALLISTIC_KOEFF);
         
         bInfo.ammo_startSpeed      =  selectedBullet.getDouble(JSONBulletTag.TAG_D_START_SPEED);
         bInfo.ammo_speedAtXyards   =  selectedBullet.getDouble(JSONBulletTag.TAG_D_SPEED_AT_X_YARD);
         bInfo.bullet_startDistance =  selectedBullet.getDouble(JSONBulletTag.TAG_D_START_DISTANCE);
         bInfo.bullet_XyardDistance =  selectedBullet.getDouble(JSONBulletTag.TAG_D_XYARD_DISTANCE);
         bInfo.ammo_crossSection    =  selectedBullet.getDouble(JSONBulletTag.TAG_D_CROSS_SECTION);
         
         bInfo.bullet_W1Const       =  selectedBullet.getDouble(JSONBulletTag.TAG_D_W1_CONST);
         bInfo.bullet_turn_speed    =  selectedBullet.getDouble(JSONBulletTag.TAG_D_TURN_SPEED);
         bInfo.bullet_K_param       =  selectedBullet.getDouble(JSONBulletTag.TAG_D_K_PARAM);
         
         /*========================================================================================================*/
      }
      catch(JSONException e)
      {
         e.printStackTrace();
      }
   }
   
   /**************************************************************************
    * Function:  getInfoAboutScopeFromDataBase
    * @param ctx      - Contex
    * @param scopeTag - int
    * @return None.
    * ***********************************************************************/ 
   public String getInfoAboutBulletFromDataBase(Context ctx, int scopeTag)
   {
      String sRet    = null;
      String sParsed = null;
      
      sParsed = FileManipulations.readFromLocalFile(ctx, 
                                                    R.raw.bulletbase);
      sRet    = jsonBulletTypeParser(sParsed, 
                                    scopeTag);
      
      return sRet;
   }
   
   /**************************************************************************
    * Function: jsonBulletTypeParser - Parser of JSON
    * @param  jsonString - String - JSON string for parsing 
    * @param  bulletTag  - int number of selected bullet 
    * @return None.
    * ************************************************************************/ 
   private String jsonBulletTypeParser(String jsonString, int bulletTag)
   {
      String sRet    = null;
      try
      {
         JSONObject jsonFullResult = new JSONObject(jsonString);
         String start = jsonFullResult.getString(JSONBulletTag.TAG_S_DATA);
         if( !start.equals("Bullets"))
         {
            Log.e("BulletSpecifications","F:[jsonBulletTypeParser] ERROR ON START OF PARSER");
            return sRet;
         }
         JSONArray jsonScopes     = jsonFullResult.getJSONArray(JSONBulletTag.TAG_A_BULLET_TYPE);
         if(bulletTag <= jsonScopes.length())
         {
            JSONObject selectedScope = jsonScopes.getJSONObject(bulletTag);
            sRet                 = selectedScope.getString(JSONBulletTag.TAG_A_BULLET_DATA);
         }
      }
      catch(JSONException e)
      {
         e.printStackTrace();
      }
      return sRet;
   }
   
   /*************************************************************************************/
//   public static double bulletDiametr          = 0.00785; // D meters
//   public static double bulletNoseDiameter     = 0.00145; // Dg
//   public static double bulletMainBeltDiameter = 0.00785; // Dp
//   public static double bulletTailDiameter     = 0.00785; // Dx
//
//   public static double bulletTotalLengs       = 0.0286;  // Xk
//   public static double bulletNoseLength       = 0.0184;  // Xgc
//   public static double bulletTailLength       = 0.0000;  // Xxvc
//   public static double bulletRadiusOjivalo    = 6.835;   // R_O  (R_O - радиус оживала в калибрах) 
//   
//   public static double bulletcalibre      = 0.00762;   //? 
//   public static double bulletWeight       = 0.0096;  // gm 
//   public static double bulletVelocity     = 950 ;   // meters/sec
//   
//   public static double bulletBalisticKoef         = 0.197; 
//   public static double bulletDeviationKoefficient = 0.005;
   /*************************************************************************************/
}
