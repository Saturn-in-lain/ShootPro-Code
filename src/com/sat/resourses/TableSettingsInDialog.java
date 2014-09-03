package com.sat.resourses;

import com.sat.shootpro.R;
import com.sat.shootpro.StartMenuActivity;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class TableSettingsInDialog implements OnClickListener
{
   private String  LOG            = "TableSettingsInDialog"; 

   private Context  ctx           = null;
   private Dialog   dialog        = null;
   private View     view          = null;
   private CheckBox show_hide_Vb  = null;
   private CheckBox show_hide_Vd  = null;
   private CheckBox show_hide_Vp  = null;
   private CheckBox show_hide_Ab  = null;
   private CheckBox show_hide_V   = null;
   private CheckBox show_hide_MAX = null;
   private CheckBox show_hide_RF  = null;
   private CheckBox show_hide_En  = null;
   private EditText setTableStep  = null;
   
   private Button   btnSave       = null;
   
   /**************************************************************************
    * Function: InitAllResourcesInClass
    * @param () 
    * @param () 
    * @return () 
    * ***********************************************************************/
   public void InitLocalResources(Dialog view)
   {
      if (null != this.view )
      {
         show_hide_Vb  = (CheckBox) view.findViewById(R.id.cckBxTableVisibility_Velocity);
         show_hide_Vd  = (CheckBox) view.findViewById(R.id.cckBxTableVisibility_Deviation);
         show_hide_Vp  = (CheckBox) view.findViewById(R.id.cckBxTableVisibility_Vp);
         show_hide_Ab  = (CheckBox) view.findViewById(R.id.cckBxTableVisibility_Accelerations);
         show_hide_V   = (CheckBox) view.findViewById(R.id.cckBxTableVisibility_MainVelocity);
         show_hide_MAX = (CheckBox) view.findViewById(R.id.cckBxTableVisibility_MAX);
         show_hide_RF  = (CheckBox) view.findViewById(R.id.cckBxTableVisibility_Rfunction);
         show_hide_En  = (CheckBox) view.findViewById(R.id.cckBxTableVisibility_Energy);
         setTableStep  = (EditText) view.findViewById(R.id.editTxtStepFOrTable);
         
         btnSave = (Button) view.findViewById(R.id.btnSaveAndClose);
         btnSave.setOnClickListener(this);
         
         getCheckBoxSettings();
         
         Integer step = StartMenuActivity.Interf.getTableStepParameter();
         setTableStep.setText(step.toString());
      }
      else
      {
         Log.e(LOG,"--- VIEW == NULL ---"); 
      }
   }
   
   /**************************************************************************
    * Function: setVisibilityForTables - TODO must set in memory information.
    * Must not duplicate partially functionality
    * @param  () None.
    * @return () None. 
    * ***********************************************************************/
   private void getCheckBoxSettings()
   {
      if ( null != StartMenuActivity.Interf )
      {         
         StringBuilder binaryFlagSet = StartMenuActivity.Interf.getTableColumsVisibility();
         if( null != binaryFlagSet )
         {
            show_hide_Vb.setChecked (StartMenuActivity.Interf.isColumInvisible(binaryFlagSet,0));
            show_hide_Vd.setChecked (StartMenuActivity.Interf.isColumInvisible(binaryFlagSet,1));
            show_hide_Vp.setChecked (StartMenuActivity.Interf.isColumInvisible(binaryFlagSet,2)); 
            show_hide_Ab.setChecked (StartMenuActivity.Interf.isColumInvisible(binaryFlagSet,3)); 
            show_hide_V.setChecked  (StartMenuActivity.Interf.isColumInvisible(binaryFlagSet,4));  
            show_hide_MAX.setChecked(StartMenuActivity.Interf.isColumInvisible(binaryFlagSet,5));
            show_hide_RF.setChecked (StartMenuActivity.Interf.isColumInvisible(binaryFlagSet,6)); 
            show_hide_En.setChecked (StartMenuActivity.Interf.isColumInvisible(binaryFlagSet,7)); 
            //Log.e(LOG,"F:[getCheckBoxSettings] binaryFlagSet: " + binaryFlagSet.toString());
         }
      }
   }
   
   /**************************************************************************
    * Function: ImageInDialogView() 
    * @param Context context
    * @param Resources res
    * @return (View) 
    * ***********************************************************************/
   public TableSettingsInDialog(Context context, 
                                View view)
   {
      if((null != context) )
      {
         this.ctx    = context;
         this.view   = view;
         
         dialog   = new Dialog(this.ctx);
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setContentView(R.layout.table_settings_layout);
         InitLocalResources(dialog);
      }
      else
      {
         Log.e(LOG,"--- WARNING CONTEXT/RESOURCES/VIEW == NULL ---");
      }
   }
   
   /**************************************************************************
    * Function: showTableDialog() 
    * @return  None.
    * ***********************************************************************/
   public void showTableSettingsDialog()
   {
      if(null == dialog)
      {
         Log.e(LOG,"showTableDialog Entry parameters error");
         return;
      }
      dialog.show();
   }
   
   /**************************************************************************
    * Function: setVisibilityFlag Simple solution... but don't be lazy and make normal
    * @param box           - CheckBox 
    * @param binaryFlagSet - StringBuilder 
    * @param index         - int 
    * @return () None. 
    * ***********************************************************************/
   private void setVisibilityFlag(CheckBox box,
                                  StringBuilder binaryFlagSet,
                                  int index)
   {
      if(true == box.isChecked())
      {
         binaryFlagSet.setCharAt(index, '1');
      }
      else
      {
         binaryFlagSet.setCharAt(index, '0');
      }
   }
   
   /**************************************************************************
    * Function: setVisibilityForTables - TODO must set in memory information.
    * Must not duplicate partially functionality
    * @param None.
    * @return () None. 
    * ***********************************************************************/
   private StringBuilder setVisibilityForTables()
   {
      StringBuilder binaryFlagSet = new StringBuilder("00000000");

      setVisibilityFlag(show_hide_Vb, binaryFlagSet, 0);
      setVisibilityFlag(show_hide_Vd, binaryFlagSet, 1);
      setVisibilityFlag(show_hide_Vp, binaryFlagSet, 2);
      setVisibilityFlag(show_hide_Ab, binaryFlagSet, 3);
      
      setVisibilityFlag(show_hide_V,  binaryFlagSet, 4);
      setVisibilityFlag(show_hide_MAX,binaryFlagSet, 5);
      setVisibilityFlag(show_hide_RF, binaryFlagSet, 6);
      setVisibilityFlag(show_hide_En, binaryFlagSet, 7);

      if ( null != StartMenuActivity.Interf )
      {         
         StartMenuActivity.Interf.setTableSettingsParam(binaryFlagSet.toString());
      }
      return binaryFlagSet;
   }

   /**************************************************************************
    * Function: onDismiss() 
    * @return  None.
    * ***********************************************************************/
   @Override
   public void onClick(View v)
   {
      setVisibilityForTables();    
      
      Integer step = Integer.valueOf(setTableStep.getText().toString());
      StartMenuActivity.Interf.setTableStepParameter(step);
      dialog.cancel();
   }

   /**************************************************************************
    * Function: onDismiss() 
    * @return  None.
    * ***********************************************************************/
   public void onDismiss(android.content.DialogInterface.OnDismissListener onDismissListener)
   {
      Log.e(LOG,"onDismiss-> take palce");
   }
}
