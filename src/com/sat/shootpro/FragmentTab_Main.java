package com.sat.shootpro;

import com.sat.resourses.TableSettingsInDialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class FragmentTab_Main extends Fragment implements InitializerPerClass
{
   //private static String LOG = "FragmentTab_Main";
   private static Button      btnSetTableRows   = null;
   private static RadioButton rdbInches         = null;
   private static RadioButton rdbMeters         = null;
   private static EditText    etWidth           = null;
   private static EditText    etHeight          = null;
   
   private static CheckBox    chkBxDistance     = null;
   private static EditText    etDistance        = null;
   private static TextView    tCurrentWeapon    = null;
   private static TextView    tCurrentAmmo      = null;
   private static TextView    tCurrentScope     = null;
 
   private static TableSettingsInDialog TableSetting = null;
   private Context                      ctx          = null;
   
   //TODO: This must be shares parameter, but for testing for now let it be...
   //Tab_Info also include this 
   static double steps          = init; 
   static int    step_increment = 0; 
   static int    pos            = 0; 
   
   /**************************************************************************
    * Function: InitAllResourcesInClass
    * @param () 
    * @param () 
    * @return () 
    * ***********************************************************************/
   @Override
   public void InitAllResourcesInClass(View view)
   {
      this.ctx        = view.getContext();
      btnSetTableRows = (Button)       view.findViewById(R.id.btnSetTableRows);
      rdbInches       = (RadioButton)  view.findViewById(R.id.rdBtnInches);
      rdbMeters       = (RadioButton)  view.findViewById(R.id.rdBtnMeters);
      etWidth         = (EditText)     view.findViewById(R.id.edtTxtTWidth);
      etHeight        = (EditText)     view.findViewById(R.id.edtTxtTHeight);
      chkBxDistance   = (CheckBox) view.findViewById(R.id.chkBxSelectDistance);
      etDistance      = (EditText)view.findViewById(R.id.editTxtDistanceSelection);
      tCurrentWeapon  = (TextView)view.findViewById(R.id.txtCurrentWeapon);
      tCurrentAmmo    = (TextView)view.findViewById(R.id.txtCurrentAmmo);
      tCurrentScope   = (TextView)view.findViewById(R.id.txtCurrentScope);
      
      TableSetting    = new TableSettingsInDialog(this.ctx,view);
      Double width    = StartMenuActivity.Interf.getTargetWidthParameter();
      Double height   = StartMenuActivity.Interf.getTargetHeightParameter();
      
      etWidth.setText( width.toString() );
      etHeight.setText( height.toString() );
      setIncrementStep();
   }
   
   /**************************************************************************
    * Function: onCreateView
    * @param inflater  LayoutInflater
    * @param container ViewGroup
    * @param savedInstanceState Bundle
    * @return None. 
    * ***********************************************************************/
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      if (container == null)
      {
         return null;
      }
      final View view = (ScrollView) inflater.inflate(R.layout.fragment_main,
                                                      container, 
                                                      false);
      InitAllResourcesInClass(view);
      
      /* ********************************************************************** */
      btnSetTableRows.setOnClickListener(new OnClickListener()
      {
         @Override
         public void onClick(View arg0)
         {
            TableSetting.showTableSettingsDialog();
         }
      });
      /* ********************************************************************** */
      rdbInches.setOnCheckedChangeListener(new OnCheckedChangeListener()
      {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            if (buttonView.isChecked()) 
            {
               rdbMeters.setChecked(false);
               convertLengthValue(false);
            }
         }
      });
      /* ********************************************************************** */  
      rdbMeters.setOnCheckedChangeListener(new OnCheckedChangeListener()
      {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            if (buttonView.isChecked()) 
            {
               rdbInches.setChecked(false);
               convertLengthValue(true);
            }
         }
      });
      /* ********************************************************************** */ 
      chkBxDistance.setOnCheckedChangeListener(new OnCheckedChangeListener()
      {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            if (buttonView.isChecked()) 
            {
               Double Distance =  StartMenuActivity.Engine.getTargetDistance();
               etDistance.setText(Distance.toString());
               etDistance.setVisibility(View.VISIBLE);
            }
            else
            {
               etDistance.setVisibility(View.GONE);
            }
         }
      });
      /* ********************************************************************** */
      setCurrentMainInfirmation();
      
      return (view);
   }
   /**************************************************************************
    * Function: setIncrementStep
    * @param None. Will be set from the field
    * @return () None. 
    * ***********************************************************************/
   private void setCurrentMainInfirmation()
   {
      tCurrentWeapon.setText(StartMenuActivity.WeaponInformation.weaponName);
      tCurrentAmmo.setText(StartMenuActivity.BulletInformation.bulletName);
      tCurrentScope.setText(StartMenuActivity.ScopeInformation.scopeName);
   }
   
   /**************************************************************************
    * Function: setIncrementStep
    * @param None. Will be set from the field
    * @return () None. 
    * ***********************************************************************/
   private void setIncrementStep()
   {
      //Integer stepParam = Integer.valueOf(etStep.getText().toString());
      //step_increment = stepParam;
   }
     
   /**************************************************************************
    * Function: convertLengthValue 
    * @param  isMeters boolean
    * @return None. 
    * ***********************************************************************/
   private void convertLengthValue(boolean isMeters)
   {
      Double lenghtW =  Double.valueOf(etWidth.getText().toString());
      Double lenghtH =  Double.valueOf(etHeight.getText().toString());
      
      if (false == isMeters)
      {
         lenghtW = StartMenuActivity.Engine.convertionMetersToInches(lenghtW);
         lenghtH = StartMenuActivity.Engine.convertionMetersToInches(lenghtH);
      }
      else
      {
         lenghtW = StartMenuActivity.Engine.convertionInchesToMeters(lenghtW);
         lenghtH = StartMenuActivity.Engine.convertionInchesToMeters(lenghtH);
         /* Save parameters in memory, but only in meters for now */
         StartMenuActivity.Interf.setTargetParameters( lenghtW, lenghtH );
      }
      etWidth.setText(lenghtW.toString());
      etHeight.setText(lenghtH.toString());
   }
   /**************************************************************************
    * Function: onResume
    * @return () 
    * ***********************************************************************/
   public void onResume() 
   {
      super.onResume();
      setCurrentMainInfirmation();
   }
   /**************************************************************************
    * Function: onDestroy
    * @param () None.
    * @return None.
    * ***********************************************************************/
    public void onDestroy()
    {
       super.onDestroy();
       if(chkBxDistance.isChecked())
       {
          Double dDistance = Double.valueOf(etDistance.getText().toString());
          if (! dDistance.isNaN())
          {
             StartMenuActivity.Engine.setTargetDistance(dDistance);
          }
       }
    }
}