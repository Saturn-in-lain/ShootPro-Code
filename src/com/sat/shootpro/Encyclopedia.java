package com.sat.shootpro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sat.resourses.ImageInDialogView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Encyclopedia extends Activity implements InitializerPerClass, OnClickListener
{
   private static String         LOG       = "Encyclopedia";
   private static Context        ctx       = null;
   
   private static ImageView      pic1      = null;
   private static ImageView      pic2      = null;
   private static ImageView      pic3      = null;
   private static ImageView      pic4      = null;
   private static ImageView      pic5      = null;
   private static ImageView      pic6      = null;
   private static ImageView      pic7      = null;
   
   
   private ImageInDialogView     imgDialog = null;
   
   private TextView encPart1 = null;
   private TextView encPart2 = null;
   private TextView encPart3 = null;
   private TextView encPart4 = null;

   private class JSONTagTemple
   {
      static final String TAG_S_DATA = "TAG";
      static final String TAG_S_CHAPTERS = "CHAPTERS";
      static final String TAG_S_CHAPTER  = "CHAPTER";
   }
   
   /**************************************************************************
    * Function: InitAllResourcesInClass() 
    * @return (View) 
    * ***********************************************************************/
   @Override
   public void InitAllResourcesInClass(View view)
   {
      pic1 = (ImageView) findViewById(R.id.imgPic1);
      pic2 = (ImageView) findViewById(R.id.imgPic2);
      pic3 = (ImageView) findViewById(R.id.imgPic3);
      
      pic4 = (ImageView) findViewById(R.id.imgPic4);
      pic5 = (ImageView) findViewById(R.id.imgPic5);
      pic6 = (ImageView) findViewById(R.id.imgPic6);
      pic7 = (ImageView) findViewById(R.id.imgPic7);
      
      
      encPart1 = (TextView) findViewById(R.id.txtEncyclopedia1);
      encPart2 = (TextView) findViewById(R.id.txtEncyclopedia2);
      encPart3 = (TextView) findViewById(R.id.txtEncyclopedia3);
      encPart4 = (TextView) findViewById(R.id.txtEncyclopedia4);
      
      ctx = this;      
   }
   
   /**************************************************************************
    * Function: onCreate() 
    * @return (View) 
    * ***********************************************************************/
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.encyclopedia);
       InitAllResourcesInClass(null);
 
       /** ============================================================== **/
       pic1.setOnClickListener(this);
       pic2.setOnClickListener(this);
       pic3.setOnClickListener(this);
       pic4.setOnClickListener(this);
       pic5.setOnClickListener(this);
       pic6.setOnClickListener(this);
       pic7.setOnClickListener(this);
       
       /** ============================================================== **/
       readEncyclipedicDataFromLocalFile(ctx);
   }
   
   /**************************************************************************
    * Function: readEncyclipedicDataFromLocalFile - from resource we can only read information.
    *          No resources file can be write in runtime. So this function
    *          will load data for Encyclipedia activity.
    * @param  Context ctx
    * @return 
    * ***********************************************************************/ 
   public void readEncyclipedicDataFromLocalFile(Context ctx)
   {
      if( null != ctx )
      {
         String parserString = null;
         parserString = FileManipulations.readFromLocalFile(ctx, R.raw.database);
         /* Parsing JSON File magic */
         if(null != parserString)
         {
            jsonEncyclopediaParser(parserString);
         }

      }
   }
   /**************************************************************************
    * Function: jsonEncyclopediaParser - Parser of JSON
    * @param  jsonString - String 
    * @return 
    * ***********************************************************************/ 
   private void jsonEncyclopediaParser(String jsonString)
   {
      try
      {
         JSONObject jsonFullResult = new JSONObject(jsonString);
         
         String start = jsonFullResult.getString(JSONTagTemple.TAG_S_DATA);
         if( !start.equals("Encyclopedia"))
         {
            Log.e(LOG,"----WARNING---- JSON Start ERROR");
            return;
         }
         
         JSONArray jsonChapters = jsonFullResult.getJSONArray(JSONTagTemple.TAG_S_CHAPTERS);
         for(int i=0; i<jsonChapters.length(); i++)
         {
            JSONObject chapter = jsonChapters.getJSONObject(i);
            String Description = chapter.getString(JSONTagTemple.TAG_S_CHAPTER);

            switch(i)
            {  
               case 0:
                  encPart1.setText(Description);
                  break;
               case 1:
                  encPart2.setText(Description);
                  break;
               case 2:
                  encPart3.setText(Description);
                  break;
               case 3:
                  encPart4.setText(Description);
                  break;
               default:
                  break;
            }
         }
      }
      catch(JSONException e)
      {
         e.printStackTrace();
      }
   }

   /**************************************************************************
    * Function: onClick() 
    * @param v View
    * @return (View) 
    * ***********************************************************************/
   public void onClick(View v) 
   {
      imgDialog = new ImageInDialogView(ctx, getResources());
      if(null == imgDialog)
      {
         Log.e(LOG,"Error Null pointer to Dialog");
         return;
      }
      switch (v.getId()) 
      {
         case R.id.imgPic1:
            imgDialog.showPictureDialog(R.drawable.card_range1,0,null);
          break;
         case R.id.imgPic2:
            imgDialog.showPictureDialog(R.drawable.card_range2,0,null);
          break;
         case R.id.imgPic3:
            imgDialog.showPictureDialog(R.drawable.traecto1,0,null);
          break;
         case R.id.imgPic4:
            imgDialog.showPictureDialog(R.drawable.mil_dot_01,0,null);
          break;
         case R.id.imgPic5:
            imgDialog.showPictureDialog(R.drawable.mil_dot_02,0,null);
          break;
         case R.id.imgPic6:
            imgDialog.showPictureDialog(R.drawable.mil_dot_03,0,null);
          break;
         case R.id.imgPic7:
            imgDialog.showPictureDialog(R.drawable.mil_dot_04,0,null);
          break;
         default:
            Log.e(LOG,"No id for onClick Listener found!");
          break;
      }
   }
}
