package com.sat.resourses;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sat.resourses.TouchImageView;
import com.sat.shootpro.R;
import com.sat.shootpro.Target;

public class ImageInDialogView
{
   private Context ctx = null;
   
   private static TouchImageView   img                   = null;
   private static TextView         description           = null;
   private static ScrollView       scrollViewTextDialog  = null;
   private Resources               globalRessources      = null;
   private static String LOG  = "ImageInDialogView";
   /**************************************************************************
    * Function: ImageInDialogView() 
    * @param Context context
    * @param Resources res
    * @return (View) 
    * ***********************************************************************/
   public ImageInDialogView(Context context,Resources res)
   {
      if((null != context) && (null != res))
      {
         this.ctx = context;
         this.globalRessources = res;
      }
      else
      {
         Log.e(LOG,"--- WARNING CONTEXT/RESOURCES == NULL ---");
      }
   }

   /**************************************************************************
    * Function: showPictureDialog() 
    * @param int    ResourceID - Image resource id
    * @param int    TextID     - Text/String from resource id. But 0 = NO text style
    * @param String sTextID    - String from parser to add.
    * @return  None.
    * ***********************************************************************/
   public void showPictureDialog(int ResourceID,
                                 int TextID,
                                 String sTextID)
   {
      final Dialog dialog = new Dialog(this.ctx);
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.setContentView(R.layout.dialog_pic);
      
      img         = (TouchImageView) dialog.findViewById(R.id.imgScaleImgT);
      description = (TextView)       dialog.findViewById(R.id.dialogText);
      
      scrollViewTextDialog = (ScrollView) dialog.findViewById(R.id.scrollViewTextDialog);
      
      if(null != sTextID)
      {
         description.setText(sTextID);
         description.setVisibility(View.VISIBLE);
         scrollViewTextDialog.setVisibility(View.VISIBLE);
      }
      else if (0 != TextID)
      {
         description.setText(globalRessources.getText(TextID));
         description.setVisibility(View.VISIBLE);
         scrollViewTextDialog.setVisibility(View.VISIBLE);
      }
      else
      {
         description.setVisibility(View.GONE);
         scrollViewTextDialog.setVisibility(View.GONE);
      }
      
      if (null != img)
      {

         img.setImageBitmap(BitmapFactory.decodeResource(globalRessources,
                                                         ResourceID));
         img.setMaxZoom(2f);
      }
      else
      {
         Log.e(LOG,"Image was not retrived from resources!!!");
      }
    
      dialog.show();
   }
   
   /**************************************************************************
    * Function: showTargetPictureDialog() 
    * @param ResourceID     - int            - Image resource id
    * @param bulletShotList - Vector<PointF> - List of coordinates of bullet
    * shots.
    * @return  None.
    * ***********************************************************************/
   public void showTargetPictureDialog(int ResourceID,
                                       Target TargetClass)
   {
      final Dialog dialog = new Dialog(this.ctx);
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
      dialog.setContentView(R.layout.dialog_pic);
      
      img         = (TouchImageView) dialog.findViewById(R.id.imgScaleImgT);
      
      description = (TextView)       dialog.findViewById(R.id.dialogText);
      description.setVisibility(View.GONE);
      
      scrollViewTextDialog = (ScrollView) dialog.findViewById(R.id.scrollViewTextDialog);
      scrollViewTextDialog.setVisibility(View.GONE);
      
      if (null != img)
      {
         img.setTargetBitmap(BitmapFactory.decodeResource(globalRessources,ResourceID),
                             TargetClass);
      }
      else
      {
         Log.e(LOG,"Image was not retrived from resources!!!");
      }
      dialog.show();
   }
}
