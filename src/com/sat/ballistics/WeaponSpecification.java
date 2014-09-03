package com.sat.ballistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sat.shootpro.FileManipulations;
import com.sat.shootpro.R;

import android.content.Context;
import android.util.Log;

public class WeaponSpecification
{
   /**************************************************************************
    * Main parameters of scope 
    * ***********************************************************************/
   public String weaponName              = "UNKNOWN";
   public double weaponCalibre           = (7.62);
   public double weaponCalibreDiameter   = (51.0);
   public double weaponBarrelRiflingStep = (0.24);
   public double weaponNumberOfRifling   = (3.4);
    
    /**************************************************************************
     * Class JSONWeaponTag - For storing parser field data
     * ***********************************************************************/
    private class JSONWeaponTag
    {
       static final String TAG_S_DATA             = "TAG";
       static final String TAG_A_WEAPON           = "WEAPON_LIST";
       static final String TAG_S_NAME             = "weaponName";
       static final String TAG_D_CALIBRE          = "weaponCalibre";
       static final String TAG_D_CALIBRE_DIAMETER = "weaponCalibreDiameter";
       static final String TAG_D_BARREL_LIFLING   = "weaponBarrelRiflingStep";
       static final String TAG_D_BARREL_NUMBERS   = "weaponNumberOfRifling";
       
       static final String TAG_WEAPON_TYPE        = "WEAPONS_TYPE";
       static final String TAG_S_WEAPON           = "WEAPON";
       
       
    }
   /**************************************************************************
    * Constructor: ScopeSpecification() 
    * ***********************************************************************/
    public WeaponSpecification(Context ctx, 
                              int LastUsedScopeParameter)
    {
       String parserString = null;
       parserString = FileManipulations.readFromLocalFile(ctx, 
                                                          R.raw.weapondatabase);
       this.LoadWeaponDataBase(ctx,
                               LastUsedScopeParameter,
                               parserString);
      }
    /**************************************************************************
     * Function: LoadWeaponDataBase - 
     * @param ctx - Contex
     * @param scopeTag - number if JSON list
     * @return None.
     * ***********************************************************************/ 
    public void LoadWeaponDataBase(Context ctx, int weaponTag, String parserString)
    {
       if( null != ctx )
       {
          if(null != parserString)
          {
             jsonWeaponParser( parserString,
                               weaponTag);
          }
       }
    }
   
    /**************************************************************************
     * Function: jsonWeaponParser - Parser of JSON
     * @param  jsonString - String - JSON string for parsing 
     * @param  scopeTag  - int number of selected scope 
     * @return None.
     * ************************************************************************/ 
    private void jsonWeaponParser(String jsonString, int weaponTag)
    {
       try
       {
          JSONObject jsonFullResult = new JSONObject(jsonString);
          String start = jsonFullResult.getString(JSONWeaponTag.TAG_S_DATA);
          if( !start.equals("Weapons"))
          {
             Log.e("WeaponParser","ERROR ON START OF PARSER");
             return;
          }
          JSONArray jsonWeapons     = jsonFullResult.getJSONArray(JSONWeaponTag.TAG_A_WEAPON);
          if(weaponTag <= jsonWeapons.length())
          {
             JSONObject selectedWeapon = jsonWeapons.getJSONObject(weaponTag);
             /*====================================================================================*/
             weaponName              = selectedWeapon.getString(JSONWeaponTag.TAG_S_NAME);
             weaponCalibre           = selectedWeapon.getDouble(JSONWeaponTag.TAG_D_CALIBRE);
             weaponCalibreDiameter   = selectedWeapon.getDouble(JSONWeaponTag.TAG_D_CALIBRE_DIAMETER);
             weaponBarrelRiflingStep = selectedWeapon.getDouble(JSONWeaponTag.TAG_D_BARREL_LIFLING);
             weaponNumberOfRifling   = selectedWeapon.getDouble(JSONWeaponTag.TAG_D_BARREL_NUMBERS);
             /*====================================================================================*/
          }
       }
       catch(JSONException e)
       {
          e.printStackTrace();
       }
       
    }
    /**************************************************************************
     * Function:  - getInfoAboutWeaponFromDataBase
     * @param ctx       - Contex
     * @param weaponTag - int
     * @return None.
     * ***********************************************************************/ 
    public String getInfoAboutWeaponFromDataBase(Context ctx, int weaponTag)
    {
       String sRet    = null;
       String sParsed = null;
       
       sParsed = FileManipulations.readFromLocalFile(ctx, 
                                                     R.raw.weapondatabase);
       sRet    = jsonWeaponsTypeParser(sParsed, 
                                       weaponTag);
       
       return sRet;
    }
    
    /**************************************************************************
     * Function: jsonWeaponParser - Parser of JSON
     * @param  jsonString - String - JSON string for parsing 
     * @param  weaponTag  - int number of selected scope 
     * @return None.
     * ************************************************************************/ 
    private String jsonWeaponsTypeParser(String jsonString, int weaponTag)
    {
       String sRet    = null;
       try
       {
          JSONObject jsonFullResult = new JSONObject(jsonString);
          String start = jsonFullResult.getString(JSONWeaponTag.TAG_S_DATA);
          if( !start.equals("Weapons"))
          {
             Log.e("WeaponParser","ERROR ON START OF PARSER");
             return sRet;
          }
          JSONArray jsonWeapons     = jsonFullResult.getJSONArray(JSONWeaponTag.TAG_WEAPON_TYPE);
          if(weaponTag <= jsonWeapons.length())
          {
             JSONObject selectedWeapon = jsonWeapons.getJSONObject(weaponTag);
             sRet   = selectedWeapon.getString(JSONWeaponTag.TAG_S_WEAPON);
          }
       }
       catch(JSONException e)
       {
          e.printStackTrace();
       }
       return sRet;
    }
}
