package com.sat.shootpro;

import java.util.Locale;

import com.sat.ballistics.Ballistics;
import com.sat.ballistics.BulletSpecifications;
import com.sat.ballistics.ScopeSpecification;
import com.sat.ballistics.WeaponSpecification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

//import android.util.Log;

public class StartMenuActivity extends Activity implements InitializerPerClass,OnClickListener
{
   //private static String LOG = "StartMenu";
   
   private static Button                    startButton  = null;
   private static Button                    optionButton = null;
   private static Button                    informButton = null;
   private static Button                    exitButton   = null;
   private static TextView              startButtonText  = null;
   private static TextView              optionButtonText = null;
   private static TextView              informButtonText = null;
   private static TextView              exitButtonText   = null;
   /** Global class instances which will be used all over the place */
   public static Ballistics            Engine            = null;
   public static AllSettingsManager Interf      = null;
   
   public static ScopeSpecification   ScopeInformation   = null;
   public static WeaponSpecification  WeaponInformation  = null;
   public static BulletSpecifications BulletInformation  = null;
   
   private Context ctx = null;
   
   /**************************************************************************
    * Function: InitAllResourcesInClass
    * @param  
    * @return 
    * ***********************************************************************/
   @Override
   public void InitAllResourcesInClass(View view)
   {
      ctx = StartMenuActivity.this;
      
      /** Initialize main buttons for this activity */
      startButton       = (Button)findViewById(R.id.btnStart);
      optionButton      = (Button)findViewById(R.id.btnOptions);
      informButton      = (Button)findViewById(R.id.btnInformation);
      exitButton        = (Button)findViewById(R.id.btnExit);
      startButtonText   = (TextView)findViewById(R.id.txtStart);
      optionButtonText  = (TextView)findViewById(R.id.txtInfo);
      informButtonText  = (TextView)findViewById(R.id.txtOption);
      exitButtonText    = (TextView)findViewById(R.id.txtExit);
      
      Interf            = new AllSettingsManager(ctx);
      
      /** Initialize main ballistics classes */
      updateBulletClass(this);
      updateScopeClass(this);
      updateWeaponClass(this);
      
      Engine = new Ballistics(BulletInformation);
      Interf.getInitAtmospereParams();
      
      updateTexts();
   }
   
   /**************************************************************************
    * Function: onCreate
    * @param  Bundle savedInstanceState
    * @return 
    * ***********************************************************************/
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_start_menu);
       
       /** Get saved program data and user settings */
       InitAllResourcesInClass(null);

       /** Set onClick functions for buttons */
       startButton.setOnClickListener(this);
       optionButton.setOnClickListener(this); 
       informButton.setOnClickListener(this); 
       exitButton.setOnClickListener(this); 
   }
   
   /*************************************************************************
    * Function: updateTexts() -- TODO -> is this needed
    * @param  None.
    * @return None. 
    * ***********************************************************************/
   private void updateTexts()
   {
      Locale  myLocale = new Locale(Interf.getCurrentLanguage());
      Locale.setDefault(myLocale);
      android.content.res.Configuration config = new android.content.res.Configuration();
      config.locale = myLocale;
      getBaseContext().getResources().
      updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
      
      Interf.setCurrentLanguage(null, ctx);
      startButtonText.setText(R.string.start);
      optionButtonText.setText(R.string.option);
      informButtonText.setText(R.string.encyclopedia);
      exitButtonText.setText(R.string.exit);
   }
   
   /*************************************************************************
    * Function: onClick()
    * @param View view 
    * @return None. 
    * ***********************************************************************/
   public void onClick(View v)
   {
      Intent myIntent = null;
      switch (v.getId()) 
      {
         case R.id.btnStart:
            myIntent = new Intent(v.getContext(), MainActivity.class);
            startActivityForResult(myIntent, 0);
            break;
            
         case R.id.btnOptions:
            myIntent = new Intent(v.getContext(), Options.class);
            startActivityForResult(myIntent, 0);
            break;
            
         case R.id.btnInformation:
            myIntent = new Intent(v.getContext(), Encyclopedia.class);
            startActivityForResult(myIntent, 0); 
            break;
            
         case R.id.btnExit:
            finish();
            break;
            
         default:
            break;
      }
   }
   
   /**************************************************************************
    * Function: updateWeaponClass
    * @param  ctx Context. 
    * @return None.
    * ***********************************************************************/
   public static void updateWeaponClass(Context ctx)
   {
      WeaponInformation   = new WeaponSpecification(ctx, 
                                 Interf.getLastUsedWeaponParameter());
   }

   /**************************************************************************
    * Function: updateScopeClass
    * @param  ctx Context. 
    * @return None.
    * ***********************************************************************/
   public static void updateScopeClass(Context ctx)
   {
      ScopeInformation = new ScopeSpecification(ctx, 
                                 Interf.getLastUsedScopeParameter());
   }
   
   /**************************************************************************
    * Function: updateBulletClass
    * @param  ctx Context. 
    * @return None.
    * ***********************************************************************/
   public static void updateBulletClass(Context ctx)
   {
      BulletInformation   = new BulletSpecifications(ctx,
                                   Interf.getLastUsedAmmoParameter());
   }
   
   /**************************************************************************
    * Function: onResume
    * @param  
    * @return 
    * ***********************************************************************/
   @Override
   public void onResume() 
   {
      super.onResume();
      updateTexts();
   }
   /**************************************************************************
    * Function: onDestroy
    * @param  
    * @return 
    * ***********************************************************************/
   @Override
   public void onDestroy() 
   {
      super.onDestroy();
      finish();
   }
   /**************************************************************************
    * Function: onStop
    * @param  
    * @return 
    * ***********************************************************************/
   @Override
   protected void onStop()
   {
      super.onStop();
   }
}