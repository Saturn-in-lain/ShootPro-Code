package com.sat.shootpro;

import com.sat.resourses.ImageInDialogView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentTab_Ammo extends Fragment implements InitializerPerClass,
                                                   OnClickListener,OnCheckedChangeListener
{
   private static CheckBox Berger168         = null;
   private static CheckBox Berger180         = null;
   private static CheckBox PersonalChkBx     = null;
   private static CheckBox DefaultChkBx      = null;
   private static LinearLayout PersonalBulletParameters = null; 
   private static TextView txtBerger168Information      = null;
   private static TextView txtBerger180Information      = null;
   private ImageView imgViewBulletTips       = null;
   private ImageView imgViewBulletBerger168  = null;
   private ImageView imgViewBulletBerger180  = null;
   private static RadioButton  rdbGrams      = null;
   private static RadioButton  rdbGrans      = null;
   private static Button       btnSave       = null;
   private static EditText  etWeight         = null;
   private static EditText  etSpeed          = null;
   private static EditText  etBallistikKoef  = null;
   private static EditText  etW1Const        = null;
   private static EditText  etBarrelStep     = null;
   private static EditText  etBulletK_param  = null;
   private static EditText  etMaxDistance    = null;
   private ImageInDialogView imgDialog       = null;
   /**************************************************************************/
   private static Context    ctx           = null;
   /**************************************************************************
    * Function: InitAllResourcesInClass 
    * @return (View) 
    * ***********************************************************************/
   @Override
   public void InitAllResourcesInClass(View view)
   {
      /*CheckBoxes*/
      Berger168     = (CheckBox) view.findViewById(R.id.chkBoxBulletBerger168);
      Berger180     = (CheckBox) view.findViewById(R.id.chkBoxBulletBerger180);
      PersonalChkBx = (CheckBox) view.findViewById(R.id.chkBoxBulletPersonal);
      DefaultChkBx  = (CheckBox) view.findViewById(R.id.chkBoxBulletDefault);
      
      /*Texts*/
      txtBerger168Information = (TextView) view.findViewById(R.id.txtBerger168Information);
      txtBerger180Information = (TextView) view.findViewById(R.id.txtBerger180Information);
      
      /*Images*/
      imgViewBulletBerger168 = (ImageView) view.findViewById(R.id.imgViewBerger168);
      imgViewBulletBerger180 = (ImageView) view.findViewById(R.id.imgViewBerger180);
      imgViewBulletTips = (ImageView) view.findViewById(R.id.imgViewBulletTips);
      
      /*RadioButtons*/
      rdbGrams = (RadioButton) view.findViewById(R.id.rdBtnGramms);
      rdbGrans = (RadioButton) view.findViewById(R.id.rdBtnGrains);

      /*Button*/
      btnSave =  (Button) view.findViewById(R.id.btnSaveParameters);
         
      /*EditText*/
      etWeight = (EditText) view.findViewById(R.id.edtTxtParam28);
      etSpeed  = (EditText) view.findViewById(R.id.edtTxtParam29);       
      etBallistikKoef = (EditText) view.findViewById(R.id.edtTxtParam30);
      etW1Const       = (EditText) view.findViewById(R.id.edtTxtParam31);
      etBarrelStep    = (EditText) view.findViewById(R.id.edtTxtParam32);
      etBulletK_param = (EditText) view.findViewById(R.id.edtTxtParam33);
      etMaxDistance      = (EditText) view.findViewById(R.id.edtTxtParam34);

      PersonalBulletParameters = (LinearLayout)  view.findViewById(R.id.LLPersonal);
      PersonalBulletParameters.setVisibility(View.GONE);
      ctx = view.getContext();
   }
   /**************************************************************************
    * Function: onCreateView() 
    * TODO can this code be encapsulated more efficient? 
    * @return (View) 
    * ***********************************************************************/
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      if (container == null)
      {
         return null;
      }
      final View view = (ScrollView) inflater.inflate(R.layout.fragment_ammo,
                                                      container, 
                                                      false);
      InitAllResourcesInClass(view);
      getFieldParamsFromMemory();
      /*------------------------------------------------------------------------------------------*/
      imgViewBulletTips.setOnClickListener(this);
      imgViewBulletBerger168.setOnClickListener(this);
      imgViewBulletBerger180.setOnClickListener(this);
      /*------------------------------------------------------------------------------------------*/
      DefaultChkBx.setOnCheckedChangeListener(this);
      PersonalChkBx.setOnCheckedChangeListener(this);
      Berger168.setOnCheckedChangeListener(this);
      Berger180.setOnCheckedChangeListener(this);
      /*------------------------------------------------------------------------------------------*/
     
      rdbGrams.setOnCheckedChangeListener(new OnCheckedChangeListener()
      {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            if (buttonView.isChecked()) 
            {
               rdbGrans.setChecked(false);
               convertWeightValue(true);
            }
         }
      });
      /*------------------------------------------------------------------------------------------*/
      rdbGrans.setOnCheckedChangeListener(new OnCheckedChangeListener()
      {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            if (buttonView.isChecked()) 
            {
               rdbGrams.setChecked(false);
               convertWeightValue(false);
            }
         }
      });
      /*------------------------------------------------------------------------------------------*/
      btnSave.setOnClickListener(new OnClickListener()
      {
         public void onClick(View v)
         {
            StartMenuActivity.Engine.setBulletFlightPrivates( 
                              Double.valueOf(etWeight.getText().toString()),
                              Boolean.valueOf(rdbGrams.isChecked()),
                              Double.valueOf(etSpeed.getText().toString()),
                              Double.valueOf(etBallistikKoef.getText().toString()),
                              Double.valueOf(etW1Const.getText().toString()),
                              Double.valueOf(etBarrelStep.getText().toString()),
                              Double.valueOf(etBulletK_param.getText().toString()));
            
            StartMenuActivity.Interf.setMaxTargetDistance(
                              Double.valueOf(etMaxDistance.getText().toString()));
            
         }
      });
      /*------------------------------------------------------------------------------------------*/
      setCurrentAmmoChecker();
      /*------------------------------------------------------------------------------------------*/
      return (view);
   }
   
   /**************************************************************************
    * Function: setCurrentWeaponChecker() 
    * @param None
    * @return (None) 
    * ***********************************************************************/
   private void setCurrentAmmoChecker()
   {
      switch(StartMenuActivity.Interf.getLastUsedAmmoParameter())
      {
         case 1:
            DefaultChkBx.setChecked(false);
            Berger168.setChecked(true);
            Berger180.setChecked(false);
            break;
         case 2:
            DefaultChkBx.setChecked(false);
            Berger168.setChecked(false);
            Berger180.setChecked(true);
            break;
         case 0:
         default:
            DefaultChkBx.setChecked(true);
            Berger180.setChecked(false);
            Berger168.setChecked(false);
            break;
      }
   }
   
   /**************************************************************************
    * Function: getFieldParamsFromMemory
    * @param  (None)
    * @return (None) 
    * ***********************************************************************/
   private void getFieldParamsFromMemory()
   {
      Double weight = StartMenuActivity.BulletInformation.bulletWeight;
      etWeight.setText(weight.toString());
      
      Double speed = StartMenuActivity.BulletInformation.ammo_startSpeed;
      etSpeed.setText(speed.toString());
      
      Double BallistikKoef = StartMenuActivity.BulletInformation.bulletBallisticsKoefficient;
      etBallistikKoef.setText(BallistikKoef.toString());
      
      Double W1Const = StartMenuActivity.BulletInformation.bullet_W1Const;
      etW1Const.setText(W1Const.toString());
     
      Double BarrelKparam = StartMenuActivity.BulletInformation.bullet_K_param;
      etBulletK_param.setText(BarrelKparam.toString());      
      
      Double BarrelRifling = StartMenuActivity.WeaponInformation.weaponBarrelRiflingStep;
      etBarrelStep.setText(BarrelRifling.toString());   
      
      Double MaxDistance =  StartMenuActivity.Interf.getMaxTargetDistance();
      etMaxDistance.setText(MaxDistance.toString());
      
   }
   
   /**************************************************************************
    * Function: convertTemperature
    * @param  boolean isGrams
    * @return (None) 
    * ***********************************************************************/
   private void convertWeightValue(boolean isGrams)
   {
      Double weight =  Double.valueOf(etWeight.getText().toString());
      
      if (false == isGrams)
      {
         weight = StartMenuActivity.Engine.convertionGramsToGrains(weight);
      }
      else
      {
         weight = StartMenuActivity.Engine.convertionGrainsToGrams(weight);
      }
      etWeight.setText(weight.toString());
   }
   
   /*************************************************************************
    * Function: onClick() - for image clicks
    * @param View view 
    * @return None. 
    * ***********************************************************************/
   @Override
   public void onClick(View view)
   {
      imgDialog = new ImageInDialogView(ctx, getResources());
      if(null != imgDialog)
      {
         switch (view.getId()) 
         {
            case R.id.imgViewBerger168:
               imgDialog.showPictureDialog(R.drawable.berger7mm168grain, 0,
               StartMenuActivity.BulletInformation.getInfoAboutFromDataBase(ctx,0));
               break;
            case R.id.imgViewBerger180:
               imgDialog.showPictureDialog(R.drawable.berger7mm180gran, 0,
               StartMenuActivity.BulletInformation.getInfoAboutFromDataBase(ctx,1));
               break;
            case R.id.imgViewBulletTips:
               imgDialog.showPictureDialog(R.drawable.schema,0,null);
               break;
            default:
               break;
         }
      }
   }
   /*************************************************************************
    * Function: onCheckedChanged() - for checkbox 
    * @param CompoundButton buttonView
    * @param boolean isChecked
    * @return None. 
    * ***********************************************************************/
   @Override
   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
   {
      switch (buttonView.getId()) 
      {
         case R.id.chkBoxBulletDefault:
            if (isChecked) 
            {
               txtBerger168Information.setVisibility(View.GONE);
               txtBerger180Information.setVisibility(View.GONE);
               
               Berger180.setChecked(false);
               Berger168.setChecked(false);
               PersonalChkBx.setChecked(false);
            }
            break;
         
         case R.id.chkBoxBulletBerger168:
            if (isChecked) 
            {
               txtBerger168Information.setVisibility(View.VISIBLE);
               txtBerger180Information.setVisibility(View.GONE);
               
               Berger180.setChecked(false);
               PersonalChkBx.setChecked(false);
               DefaultChkBx.setChecked(false);
            }
            else
            {
               txtBerger168Information.setVisibility(View.GONE);
            }
         break;
         
         case R.id.chkBoxBulletBerger180:
            if (isChecked) 
            {
               txtBerger180Information.setVisibility(View.VISIBLE);
               txtBerger168Information.setVisibility(View.GONE);
               
               Berger168.setChecked(false);
               DefaultChkBx.setChecked(false);
               PersonalChkBx.setChecked(false);
            }
            else
            {
               txtBerger180Information.setVisibility(View.GONE);
            }
         break;
         
         case R.id.chkBoxBulletPersonal:
            if (isChecked) 
            {
               PersonalBulletParameters.setVisibility(View.VISIBLE);
               txtBerger180Information.setVisibility(View.GONE);
               txtBerger168Information.setVisibility(View.GONE);
               
               Berger168.setChecked(false);
               Berger180.setChecked(false);
               DefaultChkBx.setChecked(false);
            }
            else
            {
               PersonalBulletParameters.setVisibility(View.GONE);
            }
         break;
         
         default:
            break;
      }
   }
}