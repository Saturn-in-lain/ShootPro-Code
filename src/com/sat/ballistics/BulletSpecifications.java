package com.sat.ballistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sat.shootpro.FileManipulations;
import com.sat.shootpro.R;

import android.content.Context;
import android.util.Log;

public class BulletSpecifications implements Specification
{
    /* TODO: this file mast download and pars txt file which will contain 
           information about bullets. 
           http://www.norma.cc/en/Products/Hunting/308-Winchester/
    */
// http://sbc-spb.com/index.php?option=com_content&view=article&id=70:uni&catid=21:2011-01-11-13-35-45&Itemid=31&lang=en
   
   /* This data from http://guns.allzip.org/topic/91/902146.html

    * Bullet - Berger 7mm 168 grain VLD */
   
   /*******************************************************************************/
   /*              |--------|
              _ _ _|   |    |_ _ _ _ _ _ _ _ _ _ _ _|_     __________________
            _|     |   |    |          |            | \\_
     _ _  _|       |   |    |          |            |    \\_               |
         |         |   |    |          |            |       \\__           |
     Dbt |         |   |    |          |            |        ===== De      Dn
     - -  -        |   dp   |          D            |      _//   |         |
           |_      |   |    |          |            |   _//      |         |
             |     |   |    |          |            | //         |         |
          |   - - -|   |    |- - - - - - - - - - - -|-           |_________|____
          |        |_ _|_ _ |                       |            | 
          |        |        |                       |            |
              Lbt                                        Xgc
                            |       OAL
                            |                  Lr                |
   */                       
   /*******************************************************************************/

   private static int        databaseID       = 0;
   private String            LOG              = "BulletSpecifications";
   
   /**************************************************************************
    * Main parameters of bullet 
    * ***********************************************************************/
   public String bulletName                                   = "No info"; 
   /** Set parameters **************************************************/
   public double bulletDiametr                                = 0; // D
   public double bulletTotalLengs                             = 0; // OAL overall length
   /** Bullet mass *****************************************************/
   public boolean isInGrains                                  = false;
   public double bulletWeight                                 = 0; // WGT
   /** Geometry of nose ************************************************/
   public double bulletRadius                                 = 0; // R bulletRadius = bulletcalibre * bulletDiametr;
   public double bulletRadiusOfTip                            = 0; // Rrn
   public double bulletNoseDiameter                           = 0; // De  Only one De vs Dmp 
   public double bulletDiameterOfBluntTip                     = 0; // Dmp
   public double bulletNoseShoulderDiameter                   = 0; // Dsh
   public double bulletNoseDiameterOverCircumferentialStep    = 0; // Dn  Some bullets has this params, other Dsh
   
   public double bulletDistanceToCenterOfOgiveCurve           = 0; // Lr may be the same for bullets
   public double bulletNoseLength                             = 0; // Ln
   public double bulletBevelAngleOfNose                       = 0; // B
   public double bulletSecantOgiveCurveAngle                  = 0; // Y
   /** Bearing part length */
   public double bulletBearingLength                          = 0;
   /** Geometry of boat tail ****************************************/
   public double bulletDiameterOfBoatTail                     = 0;   // Dbt ~ Dx
   public double bulletLengthsOfBoatTail                      = 0;   // Lbt
   public double bulletBevelBoatTailAngle                     = 0;   // y
   /** Crimping manner - обжимка ************************************/
   public double bulletMainBeltDiameter                       = 0; // dp
   public double bulletLengthFromCannelureToBase              = 0; // Lc not all has
   public double bulletCannelureWidth                         = 0; // lc
   public double bulletDistanceBetweenTwoCannelures           = 0; // Llc
   public double bulletDiameterOfCircumferentialStep          = 0; // dst
   public double bulletDistanceFromCircumStepAndBulletBase    = 0; // Lst
   public double bulletAngleOfCircumferentialStep             = 0; // Ф
   public double bulletDistanceFromCircumKnurlingToBulletBase = 0; // Lk
   public double bulletDistanceFrompunchingPointToBulletBase  = 0; // Lp
   /*************************************************************************/
   public double bulletDeviationKoefficient                   = 0; //0.005
   public double bulletBallisticsKoefficient                  = 0;
   /*************************************************************************/
   public String bulletFormOfBase                             = null;
   public String bulletMaterials                              = null;
   public String bulletColorMarking                           = null;
   /** Calculate parameter **************************************************/
   public double bulletCalibre                                = 0;
   public double difDiameter                                  = 0;
   public double radiusTang                                   = 0;
   public double radiusOjivInCalibre                          = 0; // R_O
   public double RtR                                          = 0;
   /*************************************************************************/    
   public double ammo_startSpeed                              = 0;
   public double ammo_speedAtXyards                           = 0;
   public double bullet_startDistance                         = 0;
   public double bullet_XyardDistance                         = 0;
   public double ammo_crossSection                            = 0;  // Поперечное сечение d^2 
   public double bullet_W1Const                               = 0;  // 1120.4068 
   public double bullet_turn_speed                            = 0;  //  3504.166 
   public double bullet_K_param                               = 0;
   /*************************************************************************/
   
   /**************************************************************************
    * Class JSONBulletTag - For storing parser field data
    * ***********************************************************************/
   private class JSONBulletTag
   {
      static final String TAG_S_DATA                        = "TAG";
      static final String TAG_A_AMMO                        = "BULLET_LIST";
      static final String TAG_S_NAME                        = "bulletName";
      static final String TAG_D_DIAMETER                    = "bulletDiametr";
      static final String TAG_D_LENGTH                      = "bulletTotalLengs"; 
      static final String TAG_B_GRAINS                      = "isMassInGrains";
      static final String TAG_D_WEIGHT                      = "bulletWeight";
      static final String TAG_D_RADIUS                      = "bulletRadius";
      static final String TAG_D_RADIUS_TIP                  = "bulletRadiusOfTip";
      static final String TAG_D_NODE_DIAMETER               = "bulletNoseDiameter"; 
      static final String TAG_D_DIAMETER_BLUNTTIP           = "bulletDiameterOfBluntTip";
      static final String TAG_D_HOSE_SHOULDER_DIAMETER      = "bulletNoseShoulderDiameter";
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
                               int LastBulletIndex)
   {
      String parserString   = null;
      databaseID            = R.raw.bulletbase;
      parserString          = FileManipulations.readFromLocalFile(ctx,databaseID);
      
      LoadDataBase(ctx, LastBulletIndex, parserString);
   }
      
  
   /**************************************************************************
    * Function: jsonParser - Parser of JSON
    * @param  jsonString - String - JSON string for parsing 
    * @param  bulletTag  - int number of selected ammo 
    * @return None.
    * ***********************************************************************/ 
   @Override
   public void jsonParser(String jsonString, int Tag)
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
         JSONObject selectedBullet = jsonBullets.getJSONObject(Tag);
         /*========================================================================================================*/
         this.bulletName                 = selectedBullet.getString(JSONBulletTag.TAG_S_NAME);
         this.bulletDiametr              = selectedBullet.getDouble(JSONBulletTag.TAG_D_DIAMETER);
         this.bulletTotalLengs           = selectedBullet.getDouble(JSONBulletTag.TAG_D_LENGTH);
         
         this.isInGrains                 = selectedBullet.getBoolean(JSONBulletTag.TAG_B_GRAINS);
         this.bulletWeight               = selectedBullet.getDouble(JSONBulletTag.TAG_D_WEIGHT);
         
         this.bulletRadius               =  selectedBullet.getDouble(JSONBulletTag.TAG_D_RADIUS);
         this.bulletRadiusOfTip          =  selectedBullet.getDouble(JSONBulletTag.TAG_D_RADIUS_TIP);
         this.bulletNoseDiameter         =  selectedBullet.getDouble(JSONBulletTag.TAG_D_NODE_DIAMETER);
         this.bulletDiameterOfBluntTip   =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DIAMETER_BLUNTTIP);
         this.bulletNoseShoulderDiameter =  selectedBullet.getDouble(JSONBulletTag.TAG_D_HOSE_SHOULDER_DIAMETER);
         
         this.bulletNoseDiameterOverCircumferentialStep =  selectedBullet.getDouble(JSONBulletTag.TAG_D_NOSE_DIAMETER_STEP);
         
         this.bulletDistanceToCenterOfOgiveCurve  =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DISTANCE_TO_CENTER_OJIV);
         this.bulletNoseLength                    =  selectedBullet.getDouble(JSONBulletTag.TAG_D_NOSE_LENGTH);
         this.bulletBevelAngleOfNose              =  selectedBullet.getDouble(JSONBulletTag.TAG_D_BEVEl_ANGLE_NOSE);
         this.bulletSecantOgiveCurveAngle         =  selectedBullet.getDouble(JSONBulletTag.TAG_D_SECAN_OJIV_ANGLE);
         this.bulletBearingLength                 =  selectedBullet.getDouble(JSONBulletTag.TAG_D_BEARING_LENGTH);
         
         this.bulletMainBeltDiameter              =  selectedBullet.getDouble(JSONBulletTag.TAG_D_MAIN_BELT_DIAMETER);
         this.bulletLengthsOfBoatTail             =  selectedBullet.getDouble(JSONBulletTag.TAG_D_TAIL_BOAT_LENGTH);
         this.bulletDiameterOfBoatTail            =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DIAMETER_OF_BOAT);
         this.bulletBevelBoatTailAngle            =  selectedBullet.getDouble(JSONBulletTag.TAG_D_BEVEL_TAIL_ANGLE);
         
         this.bulletLengthFromCannelureToBase     =  selectedBullet.getDouble(JSONBulletTag.TAG_D_LENGTH_FROM_CANNELUR);
         this.bulletCannelureWidth                =  selectedBullet.getDouble(JSONBulletTag.TAG_D_CANNELUR_WIDTH);
         this.bulletDistanceBetweenTwoCannelures  =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DISTANCE_BETWEEN_CANNELURES);
         this.bulletDiameterOfCircumferentialStep =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DIAMETER_OF_STEP);
         this.bulletDistanceFromCircumStepAndBulletBase    =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DISTANCE_STEP_AND_BASE);
         this.bulletAngleOfCircumferentialStep             =  selectedBullet.getDouble(JSONBulletTag.TAG_D_ANGLE_STEP);
         this.bulletDistanceFromCircumKnurlingToBulletBase =  selectedBullet.getDouble(JSONBulletTag.TAG_D_KNURLING_AND_BASE);
         this.bulletDistanceFrompunchingPointToBulletBase  =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DISTANCR_PUNCHING_BASE);
         
         this.bulletFormOfBase     =  selectedBullet.getString(JSONBulletTag.TAG_S_FORM);
         this.bulletMaterials      =  selectedBullet.getString(JSONBulletTag.TAG_S_MATERIALS);
         this.bulletColorMarking   =  selectedBullet.getString(JSONBulletTag.TAG_S_COLOR_MARKING);

         this.bulletCalibre        = selectedBullet.getDouble(JSONBulletTag.TAG_D_CALIBRE);
         this.difDiameter          =  selectedBullet.getDouble(JSONBulletTag.TAG_D_DIF_DIAMETER);
         this.radiusTang           =  selectedBullet.getDouble(JSONBulletTag.TAG_D_RADIUS_TANG);
         this.radiusOjivInCalibre  =  selectedBullet.getDouble(JSONBulletTag.TAG_RADIUS_OJIV);
         this.RtR                  =  selectedBullet.getDouble(JSONBulletTag.TAG_RTR);

         this.bulletDeviationKoefficient   = selectedBullet.getDouble(JSONBulletTag.TAG_D_DEVIATION);
         this.bulletBallisticsKoefficient  = selectedBullet.getDouble(JSONBulletTag.TAG_D_BALLISTIC_KOEFF);
         
         this.ammo_startSpeed      =  selectedBullet.getDouble(JSONBulletTag.TAG_D_START_SPEED);
         this.ammo_speedAtXyards   =  selectedBullet.getDouble(JSONBulletTag.TAG_D_SPEED_AT_X_YARD);
         this.bullet_startDistance =  selectedBullet.getDouble(JSONBulletTag.TAG_D_START_DISTANCE);
         this.bullet_XyardDistance =  selectedBullet.getDouble(JSONBulletTag.TAG_D_XYARD_DISTANCE);
         this.ammo_crossSection    =  selectedBullet.getDouble(JSONBulletTag.TAG_D_CROSS_SECTION);
         
         this.bullet_W1Const       =  selectedBullet.getDouble(JSONBulletTag.TAG_D_W1_CONST);
         this.bullet_turn_speed    =  selectedBullet.getDouble(JSONBulletTag.TAG_D_TURN_SPEED);
         this.bullet_K_param       =  selectedBullet.getDouble(JSONBulletTag.TAG_D_K_PARAM);
         
         /*========================================================================================================*/
      }
      catch(JSONException e)
      {
         e.printStackTrace();
      }
      
   }
  
   /**************************************************************************
    * Function: jsonTypeParser - Parser of JSON
    * @param  jsonString - String - JSON string for parsing 
    * @param  bulletTag  - int number of selected bullet 
    * @return None.
    * ************************************************************************/ 
   @Override
   public String jsonTypeParser(String jsonString, int Tag)
   {
      String sRet    = null;
      try
      {
         JSONObject jsonFullResult = new JSONObject(jsonString);
         String start = jsonFullResult.getString(JSONBulletTag.TAG_S_DATA);
         if( !start.equals("Bullets"))
         {
            Log.e(LOG,"F:[jsonBulletTypeParser] ERROR ON START OF PARSER");
            return sRet;
         }
         JSONArray jsonScopes     = jsonFullResult.getJSONArray(JSONBulletTag.TAG_A_BULLET_TYPE);
         if(Tag <= jsonScopes.length())
         {
            JSONObject selectedScope = jsonScopes.getJSONObject(Tag);
            sRet                 = selectedScope.getString(JSONBulletTag.TAG_A_BULLET_DATA);
         }
      }
      catch(JSONException e)
      {
         e.printStackTrace();
      }
      return sRet;
   }


   /**************************************************************************
    * Function: LoadDataBase 
    * @param  ctx       Context
    * @param  bInfo     BulletInfo
    * @param  bulletTag int
    * @return 
    * ***********************************************************************/ 
   @Override
   public void LoadDataBase(Context ctx, int Tag, String parserString)
   {
      if( null != ctx )
      {
         if(null != parserString)
         {
            jsonParser( parserString,Tag);
         }
      }
      else
      {
         Log.e("BulletSpecifications","F:[LoadBulletDataBase] >> ERROR Ctx == null");
      }
   }

   /**************************************************************************
   * Function:  getInfoAboutFromDataBase
   * @param ctx      - Contex
   * @param scopeTag - int
   * @return None.
   * ***********************************************************************/ 
   @Override
   public String getInfoAboutFromDataBase(Context ctx, int Tag)
   {
      String sRet    = null;
      String sParsed = null;
             sParsed = FileManipulations.readFromLocalFile(ctx, databaseID);
             sRet    = jsonTypeParser(sParsed, Tag);
      return sRet;
   }
}
