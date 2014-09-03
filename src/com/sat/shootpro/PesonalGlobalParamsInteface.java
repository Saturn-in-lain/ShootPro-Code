package com.sat.shootpro;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 
 * @author stas.savinov
 * @description Class wrapper between memory and activity.
 * All that must be get/set from the memory must be set here.  
 * @category 
 * @version 1.1.0
 */

public class PesonalGlobalParamsInteface
{
   private String LOG = "PesonalGlobalParamsInteface";

   public static final String APP_PREFERENCES = "ShootProSettings"; 
   
   private Context                  ctx         = null;
   private SharedPreferences        settings    = null;
   private SharedPreferences.Editor prefsEditor = null; 
   static Locale                    myLocale    = null;
   
   public Object DEF_VAL        = (999);
   public long   DEF_DOUBLE_VAL = (long) (-999.0);
   public double init           = 0.0;
   public String DEF_VAL_STRING = "0";
   
   // In such way we can save all values for save game
   final static Map<String, String> map = new HashMap<String,String>();
   //map.put("STOREDVALUE","save");
 
   /** ************** Types ************************ */
   public enum DATA_TYPE
   {
      DATA_TYPE_INT,       //0
      DATA_TYPE_STRING,    //1
      DATA_TYPE_FLOAT,     //2
      DATA_TYPE_BOOLEAN,   //3
      DATA_TYPE_LONG,      //4
      DATA_TYPE_OBJECT,    //5
      DATA_TYPE_DOUBLE,    //6
      DATA_TYPE_UNKNOWN    //7
   }
   /**************************************************************************/
   /* Name ID                      String Name        Num id      Data type  */
   public enum SAVE_PARAMS_STRUCTURE
   {
      WIND_STRENGTH           ("WIND_STRENGTH",          0,  DATA_TYPE.DATA_TYPE_DOUBLE),
      LANGUAGE                ("LANGUAGE",               1,  DATA_TYPE.DATA_TYPE_STRING),
      DEFAULT                 ("DEFAULT",                2,  DATA_TYPE.DATA_TYPE_DOUBLE),
      SOUND_FLAG              ("SOUND_FLAG",             3,  DATA_TYPE.DATA_TYPE_BOOLEAN),
      TARGET_WIDTH            ("TARGET_WIDTH",           4,  DATA_TYPE.DATA_TYPE_DOUBLE),
      TARGET_HEIGTH           ("TARGET_HEIGTH",          5,  DATA_TYPE.DATA_TYPE_DOUBLE),
      ALTITUDE                ("ALTITUDE",               6,  DATA_TYPE.DATA_TYPE_DOUBLE),
      LATITUDE                ("LATITUDE",               7,  DATA_TYPE.DATA_TYPE_DOUBLE),
      AZIMUT                  ("AZIMUT",                 8,  DATA_TYPE.DATA_TYPE_DOUBLE),
      TEMPERATURE             ("TEMPERATURE",            9,  DATA_TYPE.DATA_TYPE_DOUBLE),
      IS_CELSIUM              ("IS_CELSIUM",             10, DATA_TYPE.DATA_TYPE_BOOLEAN),
      DENSITY                 ("DENSITY",                11, DATA_TYPE.DATA_TYPE_DOUBLE),
      PRESSURE                ("PRESSURE",               12, DATA_TYPE.DATA_TYPE_DOUBLE),
      HUMIDITY                ("HUMIDITY",               13, DATA_TYPE.DATA_TYPE_DOUBLE),
      SOUND_SPEED             ("SOUND_SPEED",            14, DATA_TYPE.DATA_TYPE_DOUBLE),
      WIND_DIRECTION          ("WIND_DIRECTION",         15, DATA_TYPE.DATA_TYPE_INT),
      GRAVIRY                 ("GRAVIRY",                16, DATA_TYPE.DATA_TYPE_DOUBLE),
      SCOPE_CLICK_X           ("SCOPE_CLICK_X",          17, DATA_TYPE.DATA_TYPE_INT),
      SCOPE_CLICK_Y           ("SCOPE_CLICK_Y",          18, DATA_TYPE.DATA_TYPE_INT),
      SCOPE_GRID              ("SCOPE_GRID",             19, DATA_TYPE.DATA_TYPE_INT),
      TABLE_SETTINGS          ("TABLE_SETTINGS",         20, DATA_TYPE.DATA_TYPE_STRING),
      SCOPE_LAST_PARAMETERS   ("SCOPE_LAST_PARAMETERS",  21, DATA_TYPE.DATA_TYPE_INT),
      WEAPON_LAST_PARAMETERS  ("WEAPON_LAST_PARAMETERS", 22, DATA_TYPE.DATA_TYPE_INT),
      ANGLE                   ("ANGLE",                  23, DATA_TYPE.DATA_TYPE_DOUBLE),
      MAX_TARGET_DISTANCE     ("MAX_TARGET_DISTANCE",    24, DATA_TYPE.DATA_TYPE_DOUBLE),
      AMMO_LAST_PARAMETERS    ("AMMO_LAST_PARAMETERS",   25, DATA_TYPE.DATA_TYPE_INT),
      TABLE_STEP_PARAM        ("TABLE_STEP_PARAM",       26, DATA_TYPE.DATA_TYPE_INT);
      
      private String    sParameterName;
      private int       iParameterId;
      private DATA_TYPE tParameterType;
      
      private SAVE_PARAMS_STRUCTURE(String toString, int id, DATA_TYPE typeType) 
      {
         this.sParameterName = toString;
         this.iParameterId   = id;
         this.tParameterType = typeType;
      }
      
      public int getParameterId()
      {
         return this.iParameterId;
      }
      
      public DATA_TYPE getParameterType()
      {
         return this.tParameterType;
      }
      
      public String getParameterString()
      {
         return this.sParameterName;
      }
   }
   /**************************************************************************/
      
   /**************************************************************************
    * Function: Constructor for class
    * @param  
    * @return 
    * ***********************************************************************/
   public PesonalGlobalParamsInteface(Context ctx)
   {
      if (ctx == null)
      {
         throw new RuntimeException ("Context is null, what are you doing?");
      }
      this.ctx = ctx;
      this.settings = ctx.getSharedPreferences(APP_PREFERENCES, 0);
   }
   
    /**************************************************************************
     * Function:  setPrefParameters
     * @param  data       SAVE_PARAMS_STRUCTURE
     * @param  value      Object - data for saving
     * @return None.
     * ***********************************************************************/
    public void setPrefParameters(SAVE_PARAMS_STRUCTURE data, Object value)
    {
       boolean bRet     = false;
      
       this.prefsEditor = this.settings.edit();  
       if(null != this.settings)
       {
          switch(data.tParameterType)              
          {
             case DATA_TYPE_INT:
                prefsEditor.putInt(data.sParameterName, 
                                   Integer.valueOf((Integer) value));
                bRet = true;
             break;
             
             case DATA_TYPE_STRING:
                prefsEditor.putString(data.sParameterName, 
                                     (String)value);
                bRet = true;
                break;
                
             case DATA_TYPE_FLOAT:
                prefsEditor.putFloat(data.sParameterName, 
                                     Float.valueOf((Float) value));
                bRet = true;
                break;
             
             case DATA_TYPE_BOOLEAN:
                prefsEditor.putBoolean(data.sParameterName, 
                                       Boolean.valueOf((Boolean) value));
                bRet = true;
                break;
                
             case DATA_TYPE_DOUBLE:
                prefsEditor.putLong(data.sParameterName, 
                                    Double.doubleToRawLongBits((Double) value));
                bRet = true;
                break;
                
             case DATA_TYPE_UNKNOWN:
             default:
                break;
          }
       }
       
       if (false != bRet)
       {
          prefsEditor.commit();
       }
       else
       {
          Log.e(LOG,"Error while saving value");
       }
    }
 
    /**************************************************************************
     * Function:  getPrefParameters
     * @param  data SAVE_PARAMS_STRUCTURE
     * @return Object value
     * ***********************************************************************/
     public Object getPrefParameters(SAVE_PARAMS_STRUCTURE data)
     {
        Object retValue = null;
         
        try
        {
           if(null != this.settings)
           {
              switch(data.tParameterType)
              {
                  case DATA_TYPE_INT:
                     Object retValue1 =  Integer.valueOf(this.settings.getInt(data.sParameterName, 
                                                                              (Integer) DEF_VAL));
                     retValue = retValue1;
                  break;
                  
                  case DATA_TYPE_STRING:
                     Object retValue2 = String.valueOf(this.settings.getString(data.sParameterName, 
                                                                                    DEF_VAL_STRING));
                     retValue = retValue2;
                     break;
                     
                  case DATA_TYPE_FLOAT:
                     Object retValue3 = Float.valueOf(this.settings.getFloat(data.sParameterName, 0));//(Float)
                     retValue = retValue3;
                     break;
                  
                  case DATA_TYPE_BOOLEAN:
                     Object retValue4 = Boolean.valueOf(this.settings.getBoolean(data.sParameterName, 
                                                                                 (Boolean) true));
                     retValue = retValue4;
                     break;
                  
                  case DATA_TYPE_DOUBLE:
                     Object retValue5 = Double.longBitsToDouble(this.settings.getLong(data.sParameterName, 
                                                                                     DEF_DOUBLE_VAL));
                     retValue = retValue5;
                     break;
                     
                  case DATA_TYPE_UNKNOWN:
                  default:
                     break;
              }
           }
        }
        catch(ClassCastException e)
        {
           Log.e(LOG,"Class cast exception was detected" + e.toString());
        }
        
        return retValue;
     }
      
     /**************************************************************************
      * Function: deleteAllPreferences()
      * @param  
      * @return 
      * ***********************************************************************/
     public void deleteAllPreferences()
     {
        if (null != this.prefsEditor)
        {
           this.prefsEditor.clear();
           this.prefsEditor.commit();
        }
     }  
   
     /**************************************************************************
      * Function: getInitAtmospereParams()
      * @param  
      * @return 
      * ***********************************************************************/
     public void getInitAtmospereParams()
     {
        if(null != StartMenuActivity.Engine)
        {
           StartMenuActivity.Engine.Parameters.altitude = 
                 (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.ALTITUDE);
           
           if(DEF_DOUBLE_VAL == StartMenuActivity.Engine.Parameters.altitude || 
                 Double.NaN == StartMenuActivity.Engine.Parameters.altitude)
           {
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.ALTITUDE,
                                1.0);
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.LATITUDE,
                                49.9935000);
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.AZIMUT,
                                init);
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.TEMPERATURE,
                                15.0);
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.IS_CELSIUM,
                                true);
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.DENSITY,
                                init);
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.PRESSURE,
                                init);
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.HUMIDITY,
                                50.0);
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.SOUND_SPEED,
                                340.29);
              /*--------------------------------------------------------------*/
              setPrefParameters(SAVE_PARAMS_STRUCTURE.GRAVIRY,
                                9.8);
              /*--------------------------------------------------------------*/
           }
           setWindParameters();
        }
     }
   /**************************************************************************
    * Function: getLastUsedScopeParameter
    * @param  None.
    * @return Ret - integer; Saved value of bullet parameter in table 
    * ***********************************************************************/
   public int getLastUsedScopeParameter()
   {
      Integer Ret = 0;
  
      Ret = (Integer) getPrefParameters(SAVE_PARAMS_STRUCTURE.SCOPE_LAST_PARAMETERS);
      
      if(0 == Ret.compareTo( (Integer) DEF_VAL))
      {
         Ret = 0;
         setPrefParameters(SAVE_PARAMS_STRUCTURE.SCOPE_LAST_PARAMETERS,
                           Ret);
      }
      return Ret;
   }
   
   /**************************************************************************
    * Function: setLastUsedScopeParameter
    * @param  id Int.
    * @return None
    * ***********************************************************************/
   public void setLastUsedScopeParameter(int id)
   {
      setPrefParameters(SAVE_PARAMS_STRUCTURE.SCOPE_LAST_PARAMETERS, id);
   }
   
   /**************************************************************************
    * Function: getLastUsedWeaponParameter
    * @param  None.
    * @return Ret - integer; Saved value of bullet parameter in table 
    * ***********************************************************************/
   public int getLastUsedWeaponParameter()
   {
      Integer Ret = 0;

      Ret = (Integer) getPrefParameters(SAVE_PARAMS_STRUCTURE.WEAPON_LAST_PARAMETERS);
      if(0 == Ret.compareTo( (Integer) DEF_VAL))
      {
         Ret = 0;
         setPrefParameters(SAVE_PARAMS_STRUCTURE.WEAPON_LAST_PARAMETERS, Ret);
      }
      return Ret;
   }
   
   /**************************************************************************
    * Function: setTableStepParameter
    * @param  step - int.
    * @return None. 
    * ***********************************************************************/
   public void setTableStepParameter(int step)
   {
      setPrefParameters(SAVE_PARAMS_STRUCTURE.TABLE_STEP_PARAM, step);
   }
   
   /**************************************************************************
    * Function: getTableStepParameter
    * @param  None.
    * @return Ret - integer;
    * ***********************************************************************/
   public int getTableStepParameter()
   {
      Integer Ret = 0;
      Ret = (Integer) getPrefParameters(SAVE_PARAMS_STRUCTURE.TABLE_STEP_PARAM);
      if(0 == Ret.compareTo( (Integer) DEF_VAL))
      {
         Ret = 50;
         setPrefParameters(SAVE_PARAMS_STRUCTURE.TABLE_STEP_PARAM, Ret);
      }
      return Ret;
   }
   
   
   
   /**************************************************************************
    * Function: setLastUsedWeaponParameter
    * @param  id Int.
    * @return None
    * ***********************************************************************/
   public void setLastUsedWeaponParameter(int id)
   {
      setPrefParameters(SAVE_PARAMS_STRUCTURE.WEAPON_LAST_PARAMETERS, id);
   }

   
   /**************************************************************************
    * Function: setLastUsedWeaponParameter
    * @param  id Int.
    * @return None
    * ***********************************************************************/
   public void setSoundFlagSettings(boolean bFlag)
   {
      setPrefParameters(SAVE_PARAMS_STRUCTURE.SOUND_FLAG, bFlag);
   }
    
   /**************************************************************************
    * Function: getLastUsedWeaponParameter
    * @param  id Int.
    * @return None
    * ***********************************************************************/
   public boolean getSoundFlagSettings()
   {
      Boolean bRet = (Boolean) getPrefParameters(SAVE_PARAMS_STRUCTURE.SOUND_FLAG);
      return bRet;
   }
  
   /**************************************************************************
    * Function: getAltitudeParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getAltitudeParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.ALTITUDE);
      
      if( (DEF_DOUBLE_VAL == dResult) || (dResult.isNaN()) )
      {
         dResult = init;
      }
      return dResult;
   }
   /**************************************************************************
    * Function: getLatitudeParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getLatitudeParameter()
   {
      Double dResult = init;
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.LATITUDE);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() ) )
      {
         //Log.e(LOG," Some errors have been found with saved LATITUDE parameter");
         dResult = 49.9999;
      }
      return dResult;
   }
   /**************************************************************************
    * Function:  getAzimutParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getAzimutParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.AZIMUT);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() ) )
      {
         dResult = init;
      }
      return dResult;
   }
   /**************************************************************************
    * Function:  getAngleParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getAngleParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.ANGLE);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() ) )
      {
         dResult = 60.0;
      }
      return dResult;
   }
   /**************************************************************************
    * Function:  getTemperatureParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getTemperatureParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.TEMPERATURE);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() )  )
      {
         dResult = 15.0;
      }
      return dResult;
   }
   /**************************************************************************
    * Function:  getTemperatureTypeParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public boolean getTemperatureTypeParameter()
   {
      boolean bResult = true;
      bResult = (Boolean) getPrefParameters(SAVE_PARAMS_STRUCTURE.IS_CELSIUM);
      return bResult;
   }
   /**************************************************************************
    * Function:  getDensityParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getDensityParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.DENSITY);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() )  )
      {
         dResult = 120.900;
      }
      return dResult;
   }
   /**************************************************************************
    * Function:  getPreasureParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getPreasureParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.PRESSURE);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() )  )
      {
         dResult = 0.0;
      }
      return dResult;
   }
   /**************************************************************************
    * Function:  getHumidityParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getHumidityParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.HUMIDITY);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() )  )
      {
         dResult = 50.0;
      }
      return dResult;
   }
   /**************************************************************************
    * Function:  getHumidityParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getSoundSpeedParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.SOUND_SPEED);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() )  )
      {
         dResult = 340.29; //341.500
      }
      return dResult;
   }
   /**************************************************************************
    * Function:  getGravityAccelerationParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getGravityAccelerationParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.GRAVIRY);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() )  )
      {
         dResult = 9.81;
      }
      return dResult;
   }
   /**************************************************************************
    * Function:  getWindStrengthParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public double getWindStrengthParameter()
   {
      Double dResult = init;
      
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.WIND_STRENGTH);
      
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() )  )
      {
         dResult = 5.0;
      }
      return dResult;
   }   
   /**************************************************************************
    * Function:  getWindDirectionParameter
    * @param  None.
    * @return Ret 
    * ***********************************************************************/
   public int getWindDirectionParameter()
   {
      Integer dResult = 0;
      
      dResult = (Integer) getPrefParameters(SAVE_PARAMS_STRUCTURE.WIND_DIRECTION);
      
      if( (DEF_VAL == dResult))
      {
         dResult = 2;
      }
      return dResult;
   }  
   /**************************************************************************
    * Function: getTargetHeightParameter - return saved parameter or 
    * return default 1.2 meters value
    * @param  None.
    * @return dResult - double; 
    * ***********************************************************************/
   public double getTargetHeightParameter()
   {
      Double dResult = init;
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.TARGET_HEIGTH);
      
      if( (DEF_DOUBLE_VAL == dResult) || dResult.isNaN() ||  (0.0 == dResult))
      {
         setTargetParameters(0.5,1.2);
         dResult = 1.2; 
      }
      return dResult;
   }
   /**************************************************************************
    * Function: getTargetWidthParameter return saved parameter or 
    * return default 0.5 meters value
    * @param  None.
    * @return dResult - double; 
    * ***********************************************************************/
   public double getTargetWidthParameter()
   {
      Double dResult = init;
      dResult = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.TARGET_WIDTH);
      if( (DEF_DOUBLE_VAL == dResult) || ( dResult.isNaN() )  || (0.0 == dResult) )
      {
         setTargetParameters(0.5,1.2);
         dResult = 0.5;
      }
      return dResult;
   }
   
   /**************************************************************************
    * Function: setTargetParameters
    * @param  width   double
    * @param  height  double
    * @return None.
    * ***********************************************************************/
   public void setTargetParameters(double width,double height)
   {
      if (null == ctx)
      {
         throw new RuntimeException ("Context is null, what are you doing?");
      }       
      
      setPrefParameters(SAVE_PARAMS_STRUCTURE.TARGET_WIDTH,  width);
      setPrefParameters(SAVE_PARAMS_STRUCTURE.TARGET_HEIGTH, height);
   }
   /**************************************************************************
    * Function: getWindParameters 
    * @param  None. 
    * @return None.
    * ***********************************************************************/
   public void setWindParameters()
   {
      Double windStrength = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.WIND_STRENGTH);
      if( (DEF_DOUBLE_VAL == windStrength) || 
            windStrength.isNaN())
      {
         setPrefParameters(SAVE_PARAMS_STRUCTURE.WIND_STRENGTH, 5.0);
      }
      else
      {
         StartMenuActivity.Engine.Parameters.windStrength = windStrength;
      }
      
      int windDirection = (Integer) getPrefParameters(SAVE_PARAMS_STRUCTURE.WIND_DIRECTION);
      if(DEF_VAL.equals(windDirection))
      {
         setPrefParameters(SAVE_PARAMS_STRUCTURE.WIND_DIRECTION, 2); //EAST_EAST = (2)
      }
      else
      {
         StartMenuActivity.Engine.Parameters.windDirection.initDirectionByNumber(windDirection); 
      }
   }
   /**************************************************************************
    * Function: NewGameParameters 
    * @param  windStrength  - double 
    * @param  windDirection - double
    * @return 
    * ***********************************************************************/
   public void NewGameParameters(double windStrength, int windDirection)
   {
      setPrefParameters(SAVE_PARAMS_STRUCTURE.WIND_STRENGTH, windStrength);
      
      StartMenuActivity.Engine.Parameters.windStrength = windStrength;
      
      setPrefParameters(SAVE_PARAMS_STRUCTURE.WIND_DIRECTION,windDirection);
      
      StartMenuActivity.Engine.Parameters.windDirection.initDirectionByNumber(windDirection);

      setTargetParameters( (0.5),
                           (1.2) );
      
      setPrefParameters(SAVE_PARAMS_STRUCTURE.MAX_TARGET_DISTANCE, 1000.0);
      
      setLastUsedAmmoParameter(0);
      setLastUsedScopeParameter(0);
      setLastUsedWeaponParameter(0);
      
   }
   
   /**************************************************************************
    * Function: getImageLeftTopCoordinates - 
    * @param img ImageView.
    * @return Point x,y
    * ***********************************************************************/
   public Point getImageLeftTopCoordinates(ImageView img)
   {
     Point current = new Point(0,0);
   
     RelativeLayout.LayoutParams tgtPrms = (RelativeLayout.LayoutParams) img.getLayoutParams();
     current.x = tgtPrms.leftMargin;      
     current.y = tgtPrms.topMargin;
        
     return current;
   }
   
   /**************************************************************************
    * Function: getImageCenterCoordinates - get coordinates of image center
    * @param img ImageView.
    * @param targetGabarites Point. - may be [null]
    * @return Point x,y
    * ***********************************************************************/
   public Point getImageCenterCoordinates(ImageView img,Point targetGabarites)
   {
     Point current = new Point(0,0);
   
     RelativeLayout.LayoutParams tgtPrms = (RelativeLayout.LayoutParams) img.getLayoutParams();
     current.x = tgtPrms.leftMargin;      
     current.y = tgtPrms.topMargin;
        
     if(null == targetGabarites)
     {
        Point targetLength = new Point(0,0);
        targetLength = getImageGabaritesParams(img);
        current.x += (targetLength.x / 2);  
        current.y += (targetLength.y / 2);
     }
     else
     {
        current.x += (targetGabarites.x / 2);  
        current.y += (targetGabarites.y / 2);
     }
     return current;
   }
   
  /**************************************************************************
   * Function: getImageGabaritesParams - get image height/width
   * @param img - ImageView
   * @return Point x,y (width, height)
   * ***********************************************************************/
    public Point getImageGabaritesParams(ImageView img)
    {
       Point current = new Point(0,0);
          RelativeLayout.LayoutParams tgtPrms = (RelativeLayout.LayoutParams) img.getLayoutParams();
          current.x = tgtPrms.width;      
          current.y = tgtPrms.height;
       return current;
    }
    
    /**************************************************************************
     * Function: getScopeClickX 
     * @param  None. 
     * @return None.
     * ***********************************************************************/
    public int getScopeClickX()
    {
       Integer iResult = 0; 
       iResult = (Integer) getPrefParameters(SAVE_PARAMS_STRUCTURE.SCOPE_CLICK_X);
       if( iResult.equals(DEF_VAL) )
       {
          iResult = 0;
       }
       return iResult;
    }
    
    /**************************************************************************
     * Function: setScopeClickX 
     * @param  X int. 
     * @return None.
     * ***********************************************************************/
    public void setScopeClickX(int X)
    {
       setPrefParameters(SAVE_PARAMS_STRUCTURE.SCOPE_CLICK_X, X);
    }
    
    /**************************************************************************
     * Function: getScopeClickY 
     * @param  None. 
     * @return None.
     * ***********************************************************************/
    public int getScopeClickY()
    {
       Integer iResult = 0; 
       iResult = (Integer) getPrefParameters(SAVE_PARAMS_STRUCTURE.SCOPE_CLICK_Y);
       if(  iResult.equals(DEF_VAL) )
       {
          iResult = 0;
       }
       return iResult;
    }
    /**************************************************************************
     * Function: getScopeClickY 
     * @param  Y int. 
     * @return None.
     * ***********************************************************************/
    public void setScopeClickY(int Y)
    {
       setPrefParameters(SAVE_PARAMS_STRUCTURE.SCOPE_CLICK_Y, Y);
    }
    
    /**************************************************************************
     * Function: getScopeGridSavedParam
     * @param  None. 
     * @return None.
     * ***********************************************************************/
    public int getScopeGridSavedParam()
    {
       Integer iResult = 0; 
       iResult = (Integer) getPrefParameters(SAVE_PARAMS_STRUCTURE.SCOPE_GRID);
       
       if( DEF_VAL == iResult )
       {
          iResult = 0;
       }
       return iResult;
    }
    /**************************************************************************
     * Function: setScopeGridSavedParam
     * @param  gridIndex int. 
     * @return None.
     * ***********************************************************************/
    public void setScopeGridSavedParam(int gridIndex)
    {
       setPrefParameters(SAVE_PARAMS_STRUCTURE.SCOPE_GRID, gridIndex);
    }
    
    /**************************************************************************
     * Function: getScopeGridSavedParam
     * @param  None. 
     * @return None.
     * ***********************************************************************/
    public int getLastUsedAmmoParameter()
    {
       Integer iResult = 0; 
       iResult = (Integer) getPrefParameters(SAVE_PARAMS_STRUCTURE.AMMO_LAST_PARAMETERS);
       
       if( iResult.equals(DEF_VAL) )
       {
          iResult = 0;
       }
       return iResult;
    }
    /**************************************************************************
     * Function: setScopeGridSavedParam
     * @param  gridIndex int. 
     * @return None.
     * ***********************************************************************/
    public void setLastUsedAmmoParameter(int id)
    {
       setPrefParameters(SAVE_PARAMS_STRUCTURE.AMMO_LAST_PARAMETERS, id);
    }
    
    /**************************************************************************
     * Function: setMaxTargetDistance
     * @param  dDistance - Double. Max target distance 
     * @return None.
     * ***********************************************************************/
    public void setMaxTargetDistance(Double dDistance)
    {
       if ( (!dDistance.isNaN()) && 
                                  (dDistance >= 0) )
       {
          setPrefParameters(SAVE_PARAMS_STRUCTURE.MAX_TARGET_DISTANCE, dDistance);
          StartMenuActivity.Engine.setMaxTargetDistance(dDistance);
       }
       else
       {
         Log.e(LOG,"F:[setMaxTargetDistance] > NaN");  
       }
    }
    /**************************************************************************
     * Function: getMaxTargetDistance
     * @param  None. 
     * @return dDistance Double - MAX_TARGET_DISTANCE
     * ***********************************************************************/
    public Double getMaxTargetDistance()
    {
       Double dDistance = 0.0;
       dDistance = (Double) getPrefParameters(SAVE_PARAMS_STRUCTURE.MAX_TARGET_DISTANCE);
       if( (DEF_DOUBLE_VAL == dDistance) || 
                                          (dDistance.isNaN()))
       {
          dDistance = 1000.0;
          setPrefParameters(SAVE_PARAMS_STRUCTURE.MAX_TARGET_DISTANCE, dDistance);
       }
       return dDistance;
    }
    
    /**************************************************************************
     * Function: changeLang()
     * @param String lang
     * @param Context ctx
     * @return 
     * ***********************************************************************/
    public void setCurrentLanguage(String sLanguage, Context ctx)
    {
       if ( (null != sLanguage) && 
                               (!sLanguage.isEmpty()) )
       {
          setPrefParameters(SAVE_PARAMS_STRUCTURE.LANGUAGE, sLanguage);
       }
    }
    
    /**************************************************************************
     * @param  None. 
     * @return None.
     * ***********************************************************************/
    public Locale getLocalParameter()
    {
       if(null == myLocale) 
       {
          myLocale = new Locale(getCurrentLanguage());
       }
       Locale.setDefault(myLocale);
       return myLocale;
    }

    /**************************************************************************
     * Function: getCurrentLanguage
     * @param  None. 
     * @return None.
     * ***********************************************************************/
    public String getCurrentLanguage()
    {
       String sLanguage = ""; 
       sLanguage = (String) getPrefParameters(SAVE_PARAMS_STRUCTURE.LANGUAGE);
       return sLanguage;
    }

    /**************************************************************************
     * Function: getTableSettingsParam
     * @param  flagSet String. 
     * @return None.
     * ***********************************************************************/
    public void setTableSettingsParam(String flagSet)
    {
      if( (null != flagSet) && (!flagSet.isEmpty()) )
      {
         setPrefParameters(SAVE_PARAMS_STRUCTURE.TABLE_SETTINGS,flagSet);
      }
      else
      {
         Log.e(LOG,"F:[setTableSettingsParam] > null");
      }
    }
    /**************************************************************************
     * Function: getTableColumsVisibility
     * @param None.
     * @return String
     * ***********************************************************************/
    public StringBuilder getTableColumsVisibility()
    {
       StringBuilder binaryFlagSet = new StringBuilder("00000000");

       String res = (String) getPrefParameters(SAVE_PARAMS_STRUCTURE.TABLE_SETTINGS);
       
       if( (null != res) && (DEF_VAL_STRING != res))
       {
          binaryFlagSet.setLength(0);
          binaryFlagSet.append(res);
       }
       return binaryFlagSet;
    }
    /**************************************************************************
     * Function: isColumVisible check if flag was set to 0 or 1
     * @param  flags - StringBuilder
     * @param  index - int
     * @return boolean
     * ***********************************************************************/
    public boolean isColumInvisible(StringBuilder flags, int index)
    {
       boolean fRet = false;
       if(flags.length() > index)
       {
          if (Character.toString(flags.charAt(index)).equals("1"))
          {
             fRet = true;
          }
       }
       return fRet;
    }
   /**************************************************************************
    * Function: downloadGame -> TODO Is this needed?
    * @description: Must save all values for current game
    * @param  
    * @return 
    * ***********************************************************************/
   public void downloadGame()
   {
      this.settings = this.ctx.getSharedPreferences(APP_PREFERENCES, 0);  
   }
   /**************************************************************************
    * Function: toast // TODO Only for test
    * @param () String msg
    * @return () none
    * ***********************************************************************/
    public void showToastMsg (String msg, Context ctx)
    {
        Toast.makeText (ctx, msg, Toast.LENGTH_LONG).show();
    }
}