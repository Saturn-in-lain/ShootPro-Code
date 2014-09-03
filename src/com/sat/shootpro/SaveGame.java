package com.sat.shootpro;

import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;

/*
      Step 1. Initialize new file, if game was start at the first time
      Step 2. Provide all parameters for saving in this file
      Step 3. Provide parser to get all needed parameters from file
      Step 4. Provide flexible data representation for storing all possible data in one file
      Step 5.  
*/

public class SaveGame
{
   final private String LOG = "SaveGame";
   // In such way we can save all values for save game

   //final static Map<String, String> map = new HashMap<String,String>();
   //map.put("STOREDVALUE","save"); 
    private File saveGameParamFile = null;
   /**************************************************************************/

   
   /**************************************************************************
   * Function: Constructor for class
   * @param  
   * @return 
   * ***********************************************************************/
   public SaveGame(Context ctx)
   {
     //TODO - Initialization on new file. 
      //File sdcard = Environment.getExternalStorageDirectory();
      //sdcard.canRead();
      
      String filePath = ctx.getString(R.string.saveFilePath);
      
      saveGameParamFile = ctx.getFileStreamPath(filePath);
      if(!saveGameParamFile.exists())
      {
         saveGameParamFile = new File(filePath);
         Log.e(LOG," File didn't existed! I've created it :) ");
      }
   }
   
   /**************************************************************************
   * Function: isExternalStorageWritable
   * @param  
   * @return 
   * ***********************************************************************/
//   boolean isExternalStorageWritable() 
//   {
//      boolean bRet = false;
//      String state = Environment.getExternalStorageState();
//      if (Environment.MEDIA_MOUNTED.equals(state)) 
//      {
//         bRet = true;
//      }
//      return bRet;
//   }
   
  /**************************************************************************
  * Function: 
  * @param  
  * @return 
  * ***********************************************************************/ 
   public void writeToLocalFile()
  {
      JSONObject object = new JSONObject();
      try 
      {
         object.put("name",      "Start");
         object.put("score",     Integer.valueOf((int) Math.random()));
         object.put("current",   Double.valueOf((double) Math.random()));
         object.put("nickname",  "Android Tutor");
      }
      catch (JSONException e) 
      {
         e.printStackTrace();
      }
      BufferedWriter bufferedWriter = null;
      try
      {
         bufferedWriter = new BufferedWriter(new FileWriter(saveGameParamFile));
         bufferedWriter.write(object.toString());
         bufferedWriter.flush();
         bufferedWriter.close();
         Log.e("write_in_file",object.toString());
      } 
      catch (IOException e)
      {
         e.printStackTrace();
      }
  }
  
   /**************************************************************************
    * Function: 
    * @param  
    * @return 
    * ***********************************************************************/ 
     public void readFromlFile()
    {
        StringBuilder text = new StringBuilder();
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader(saveGameParamFile));  
            String line;   
            while ((line = br.readLine()) != null)
            {
                text.append(line);
                text.append('\n');
            } 
            Log.e("read_from_file",text.toString());
        }
        catch (IOException e) 
        {
           e.printStackTrace();
        }
        
     }
   /**************************************************************************
    * Function: deleteSavedGame - wipe all data from file. For now for debug
    * @param  None.
    * @return None.
    * ***********************************************************************/ 
   void deleteSavedGame()
   {
      if (null != saveGameParamFile)
      {
         saveGameParamFile.delete();
      }
   }
}
