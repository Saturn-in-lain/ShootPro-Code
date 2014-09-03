package com.sat.ballistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sat.shootpro.FileManipulations;
import com.sat.shootpro.R;

import android.content.Context;
import android.util.Log;

public class ScopeSpecification
{
   /**************************************************************************
    * Main parameters of scope 
    * ***********************************************************************/
    public String          scopeName                  = "UNKNOWN";
    public static int      scopeMagnificationLowest   = 4;
    public static int      scopeMagnificationHighest  = 4;
    public        double   scopeClickValue            = 0.25;
    public static double   scopeHightAboveBarrel      = 0;
    public static boolean  scopeIsMetric              = true;
    public int             scopeIndex                 = 0;
    /**************************************************************************
     * Class JSONScopeTag - For storing parser field data
     * ***********************************************************************/
    private class JSONScopeTag
    {
       static final String TAG_S_DATA               = "TAG";
       static final String TAG_A_SCOPE              = "SCOPE_LIST";
       
       static final String TAG_A_SCOPE_TYPE         = "SCOPES_TYPE";
       static final String TAG_A_SCOPE_DATA         = "SCOPE";

       static final String TAG_S_NAME               = "scopeName";
       static final String TAG_I_MAGNIFICATION_LOW  = "scopeMagnificationLowest";
       static final String TAG_I_MAGNIFICATION_HIGH = "scopeMagnificationHighest";
       static final String TAG_D_CLICK_VAL          = "scopeClickValue";
       static final String TAG_D_HIGHT_BARREL       = "scopeHightAboveBarrel";
       static final String TAG_B_IS_METRIC          = "scopeIsMetric";
       static final String TAG_I_TYPE_INDEX         = "scopeIndex";
    }
    
    /**************************************************************************
     * Constructor: ScopeSpecification() 
     * ***********************************************************************/
     public ScopeSpecification(Context ctx, 
                               int LastUsedScopeParameter)
     {
        String parserString = null;
        parserString = FileManipulations.readFromLocalFile(ctx, 
                                                           R.raw.scopedatabase);
        this.LoadScopeDataBase(ctx,
                               LastUsedScopeParameter,
                               parserString);
       
     }
     
    /**************************************************************************
     * ENUM main scope id list SCOPE_ID
     * ***********************************************************************/
    private enum  Scope_ID_ENUM
    {
       SCOPE_ID_MILDOT  (R.drawable.scope_mil), // 0
       SCOPE_ID_PSO     (R.drawable.scope_1);   // 1

       private int indexImageId;
 
       private Scope_ID_ENUM(int index) 
       {
          this.indexImageId = index;
       }
    };
  
    /**************************************************************************
     * Function: jsonScopeParser - Parser of JSON
     * @param  jsonString - String - JSON string for parsing 
     * @param  scopeTag  - int number of selected scope 
     * @return None.
     * ***********************************************************************/ 
    private void jsonScopeParser(String jsonString, int scopeTag)
    {
       try
       {
          JSONObject jsonFullResult = new JSONObject(jsonString);
          String start = jsonFullResult.getString(JSONScopeTag.TAG_S_DATA);
          if( !start.equals("Scopes"))
          {
             Log.e("ScopeParser","ERROR ON START OF PARSER");
             return;
          }
          JSONArray jsonScopes     = jsonFullResult.getJSONArray(JSONScopeTag.TAG_A_SCOPE);
          if(scopeTag <= jsonScopes.length())
          {
             JSONObject selectedScope = jsonScopes.getJSONObject(scopeTag);
             /*====================================================================================*/
             scopeName                 = selectedScope.getString(JSONScopeTag.TAG_S_NAME);
             scopeMagnificationLowest  = selectedScope.getInt(JSONScopeTag.TAG_I_MAGNIFICATION_LOW);
             scopeMagnificationHighest = selectedScope.getInt(JSONScopeTag.TAG_I_MAGNIFICATION_HIGH);
             scopeClickValue           = selectedScope.getDouble(JSONScopeTag.TAG_D_CLICK_VAL);
             scopeHightAboveBarrel     = selectedScope.getDouble(JSONScopeTag.TAG_D_HIGHT_BARREL);
             scopeIsMetric             = selectedScope.getBoolean(JSONScopeTag.TAG_B_IS_METRIC);
             scopeIndex                = selectedScope.getInt(JSONScopeTag.TAG_I_TYPE_INDEX);
             /*====================================================================================*/
          }
       }
       catch(JSONException e)
       {
          e.printStackTrace();
       }
       
    }
    
    /**************************************************************************
     * Function: jsonScopeTypeParser - Parser of JSON
     * @param  jsonString - String - JSON string for parsing 
     * @param  scopeTag  - int number of selected scope 
     * @return None.
     * ************************************************************************/ 
    private String jsonScopeTypeParser(String jsonString, int scopeTag)
    {
       String sRet    = null;
       try
       {
          JSONObject jsonFullResult = new JSONObject(jsonString);
          String start = jsonFullResult.getString(JSONScopeTag.TAG_S_DATA);
          if( !start.equals("Scopes"))
          {
             Log.e("ScopeParser","ERROR ON START OF PARSER");
             return sRet;
          }
          JSONArray jsonScopes     = jsonFullResult.getJSONArray(JSONScopeTag.TAG_A_SCOPE_TYPE);
          if(scopeTag <= jsonScopes.length())
          {
             JSONObject selectedScope = jsonScopes.getJSONObject(scopeTag);
             sRet                 = selectedScope.getString(JSONScopeTag.TAG_A_SCOPE_DATA);
          }
       }
       catch(JSONException e)
       {
          e.printStackTrace();
       }
       return sRet;
    }
    
    /**************************************************************************
     * Function:  - getInfoAboutScopeFromDataBase
     * @param ctx - Context
     * @return None.
     * ***********************************************************************/ 
    public String getInfoAboutScopeFromDataBase(Context ctx, int scopeTag)
    {
       String sRet    = null;
       String sParsed = null;
       
       sParsed = FileManipulations.readFromLocalFile(ctx, 
                                                     R.raw.scopedatabase);
       sRet    = jsonScopeTypeParser(sParsed, 
                                     scopeTag);
       
       return sRet;
    }
    
    /**************************************************************************
     * Function: getScopeResourceIndex 
     * @param  imageId int 
     * @return 
     * ***********************************************************************/ 
    public int getScopeResourceIndex(int imageId)
    {
       int id = 0;
       switch(imageId)
       {
          case 0:
             id = Scope_ID_ENUM.SCOPE_ID_MILDOT.indexImageId;
             break;
          case 1:
             id = Scope_ID_ENUM.SCOPE_ID_PSO.indexImageId;
             break;
          default:
             id = Scope_ID_ENUM.SCOPE_ID_MILDOT.indexImageId;
             break;
       }
       return id;
    }
    /**************************************************************************
     * Function: LoadScopeDataBase - 
     * @param  ctx Context 
     * @param  scopeTag int
     * @return 
     * ***********************************************************************/ 
    public void LoadScopeDataBase(Context ctx, int scopeTag,String parserString)
    {
       if( null != ctx )
       {
          if(null != parserString)
          {
             jsonScopeParser( parserString,
                              scopeTag);
          }
       }
    }
    
    
}
