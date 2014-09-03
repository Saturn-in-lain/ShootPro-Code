package com.sat.shootpro;

import com.sat.resourses.ImageInDialogView;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class FragmentTab_Weapon extends Fragment implements InitializerPerClass,
                                             OnCheckedChangeListener, OnClickListener
{
   //private static String LOG = "FragmentTab_Weapon";
   /* Rifles */
   private static CheckBox SVD         = null;
   private static CheckBox L96         = null;
   private static TextView txtSVD      = null;
   private static TextView txtL96      = null;
   private ImageView imgViewSVD        = null;
   private ImageView imgViewL96        = null;
   /* Scopes */
   private static CheckBox PSO         = null;
   private static CheckBox Bender      = null;
   private static TextView txtPSO      = null;
   private static TextView txtBender   = null;
   private ImageView imgViewBENDER     = null;
   private ImageView imgViewPSO        = null;
   /* Main stuff */
   private ImageInDialogView imgDialog = null;
   private static Context    ctx       = null;
   
   /**************************************************************************
    * Function: InitAllResourcesInClass 
    * @return (View) 
    * ***********************************************************************/
   @Override
   public void InitAllResourcesInClass(View view)
   {
      SVD           = (CheckBox) view.findViewById(R.id.chkBoxWpnSVD);
      L96           = (CheckBox) view.findViewById(R.id.chkBoxWpnL96);
      txtSVD        = (TextView) view.findViewById(R.id.txtViewSVD);
      txtL96        = (TextView) view.findViewById(R.id.txtViewL96);
      PSO           = (CheckBox) view.findViewById(R.id.chkBoxPSO);
      Bender        = (CheckBox) view.findViewById(R.id.chkBoxBender);
      txtPSO        = (TextView) view.findViewById(R.id.txtViewScopePSO);
      txtBender     = (TextView) view.findViewById(R.id.txtViewScopeBender);
      imgViewSVD    = (ImageView) view.findViewById(R.id.imgViewSVD);
      imgViewL96    = (ImageView) view.findViewById(R.id.imgViewL96);
      imgViewBENDER = (ImageView) view.findViewById(R.id.imgViewBender);
      imgViewPSO    = (ImageView) view.findViewById(R.id.imgViewPSO);
      
      ctx = view.getContext();
   }
   
   /**************************************************************************
    * Function: onCreateView 
    * @return (View) 
    * ***********************************************************************/
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      if (container == null)
      {
         return null;
      }
      final View view = (ScrollView) inflater.inflate(R.layout.fragment_weapon,container, false);
      
      InitAllResourcesInClass(view);
      /** ----------------------------------- */
      SVD.setOnCheckedChangeListener(this);
      L96.setOnCheckedChangeListener(this);
      PSO.setOnCheckedChangeListener(this);
      Bender.setOnCheckedChangeListener(this);
      /** ----------------------------------- */
      imgViewSVD.setOnClickListener(this);
      imgViewL96.setOnClickListener(this);
      imgViewBENDER.setOnClickListener(this);
      imgViewPSO.setOnClickListener(this);
      /** ----------------------------------- */
      setCurrentWeaponChecker();
      setCurrentScopeChecker();
      /** ----------------------------------- */
      return (view);
   }
   /**************************************************************************
    * Function: setCurrentWeaponChecker() 
    * @param None
    * @return (None) 
    * ***********************************************************************/
   private void setCurrentWeaponChecker()
   {
      switch(StartMenuActivity.Interf.getLastUsedWeaponParameter())
      {
         case 1:
            SVD.setChecked(true);
            txtSVD.setVisibility(View.VISIBLE);
            L96.setChecked(false);
            txtL96.setVisibility(View.GONE);
            break;
         case 0:
         default:
            SVD.setChecked(false);
            txtSVD.setVisibility(View.GONE);
            L96.setChecked(true);
            txtL96.setVisibility(View.VISIBLE);
            break;
      }
   }
   /**************************************************************************
    * Function: setCurrentScopeChecker() 
    * @param None
    * @return (None) 
    * ***********************************************************************/
   private void setCurrentScopeChecker()
   {
      switch(StartMenuActivity.Interf.getLastUsedScopeParameter())
      {
         case 1:
            PSO.setChecked(true);
            Bender.setChecked(false);
            txtPSO.setVisibility(View.VISIBLE);
            break;
         case 0:
         default:
            Bender.setChecked(true);
            PSO.setChecked(false);
            txtBender.setVisibility(View.VISIBLE);
            break;
      }
   }
   
   /**************************************************************************
    * Function: onCheckedChanged() 
    * @param CompoundButton buttonView
    * @param boolean isChecked
    * @return (View) 
    * ***********************************************************************/
   @Override
   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
   {
      switch (buttonView.getId()) 
      {
         case R.id.chkBoxWpnSVD:
            if (isChecked) 
            {
               //TODO: may be parse text and set here?
               txtSVD.setVisibility(View.VISIBLE);
               txtL96.setVisibility(View.GONE);
               L96.setChecked(false);
               
               StartMenuActivity.Interf.setLastUsedWeaponParameter(1);
               StartMenuActivity.updateWeaponClass(getActivity().getBaseContext());
            }
            else
            {
               txtSVD.setVisibility(View.GONE);
            }
            break;
         case R.id.chkBoxWpnL96:
            if (isChecked) 
            {
               txtL96.setVisibility(View.VISIBLE);
               txtSVD.setVisibility(View.GONE);
               SVD.setChecked(false);
               
               StartMenuActivity.Interf.setLastUsedWeaponParameter(0);
               StartMenuActivity.updateWeaponClass(getActivity().getBaseContext());
            }
            else
            {
               txtL96.setVisibility(View.GONE);
            }
            break;
         case R.id.chkBoxPSO:
            if (isChecked) 
            {
               txtPSO.setVisibility(View.VISIBLE);
               txtBender.setVisibility(View.GONE);
               Bender.setChecked(false);
               
               StartMenuActivity.Interf.setScopeGridSavedParam(1);
               StartMenuActivity.Interf.setLastUsedScopeParameter(1);
               
               StartMenuActivity.updateScopeClass(getActivity().getBaseContext());
            }
            else
            {
               txtPSO.setVisibility(View.GONE);
            }
            break;
         case R.id.chkBoxBender:
            if (isChecked) 
            {
               txtBender.setVisibility(View.VISIBLE);
               txtPSO.setVisibility(View.GONE);
               PSO.setChecked(false);
               
               StartMenuActivity.Interf.setScopeGridSavedParam(0);
               StartMenuActivity.Interf.setLastUsedScopeParameter(0);
               StartMenuActivity.updateScopeClass(getActivity().getBaseContext());
            }
            else
            {
               txtBender.setVisibility(View.GONE);
            }
            break;
         default:
            break;
      }
      
   }
   /*************************************************************************
    * Function: onClick()
    * @param View view 
    * @return None. 
    * ***********************************************************************/
      public void onClick(View view)
   {
      imgDialog = new ImageInDialogView(ctx, getResources());
      if(null != imgDialog)
      {
         switch (view.getId()) 
         {
            case R.id.imgViewSVD:
               imgDialog.showPictureDialog(R.drawable.svd,
                                           0,
                     StartMenuActivity.WeaponInformation.getInfoAboutWeaponFromDataBase(ctx,1));
               break;
            case R.id.imgViewL96:
               imgDialog.showPictureDialog(R.drawable.l96,
                                           0,
                     StartMenuActivity.WeaponInformation.getInfoAboutWeaponFromDataBase(ctx,0));
               break;
            case R.id.imgViewBender:
               imgDialog.showPictureDialog(R.drawable.schmidt_bender_scope,
                                           0,
                     StartMenuActivity.ScopeInformation.getInfoAboutScopeFromDataBase(ctx,1));
               break;
            case R.id.imgViewPSO:
               imgDialog.showPictureDialog(R.drawable.pso_scope,
                                           0,
                    StartMenuActivity.ScopeInformation.getInfoAboutScopeFromDataBase(ctx,0));
               break;
            default:
               break;
         }
      }
   }
}