package com.sat.shootpro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.sat.shootpro.PesonalGlobalParamsInteface.SAVE_PARAMS_STRUCTURE;

public class FragmentTab_Info extends Fragment implements InitializerPerClass,OnClickListener
{
   //private static String LOG = "FragmentTab_Info";

   private static EditText edTxt_altitude     = null;
   private static EditText edTxt_temperature  = null;
   private static EditText edTxt_density      = null;
   private static EditText edTxt_presure      = null;
   private static EditText edTxt_humidity     = null;
   private static EditText edTxt_latitude     = null;
   private static EditText edTxt_azimut       = null;
   private static EditText edTxt_angle        = null;
   
   private static Button   btn_calculate      = null;
      
   private static RadioButton rb_celcium      = null;
   private static RadioButton rb_fahrenheit   = null;
   
   private static TextView txt_calcSoundSpeed = null;
   private static TextView txt_calcGravityG   = null;
   private static TextView txt_calcDryDensity = null;
      
   private static ToggleButton tBtn_ShowHideConstValues = null;
   
   private LinearLayout L = null;
   /********************************Calculations******************************************/
  
   /**************************************************************************
    * Function: InitAllResourcesInClass 
    * @return (View) 
    * ***********************************************************************/
   @Override
   public void InitAllResourcesInClass(View view)
   {
      edTxt_altitude    = (EditText) view.findViewById(R.id.edtTxtParam1);
      edTxt_temperature = (EditText) view.findViewById(R.id.edtTxtParam2);
      edTxt_density     = (EditText) view.findViewById(R.id.edtTxtParam3);
      edTxt_presure     = (EditText) view.findViewById(R.id.edtTxtParam4);
      edTxt_humidity    = (EditText) view.findViewById(R.id.edtTxtParam5);
      edTxt_latitude    = (EditText) view.findViewById(R.id.edtTxtParam6);
      edTxt_azimut      = (EditText) view.findViewById(R.id.edtTxtParam13);
      edTxt_angle       = (EditText) view.findViewById(R.id.edtTxtParam9);
      
      btn_calculate     = (Button) view.findViewById(R.id.btnCalculate);
      
      tBtn_ShowHideConstValues = (ToggleButton) view.findViewById(R.id.tglBtnConstParams);
      
      rb_celcium        = (RadioButton) view.findViewById(R.id.rdBCelcius);
      rb_fahrenheit     = (RadioButton) view.findViewById(R.id.rdFarenheit);
      
      txt_calcSoundSpeed = (TextView)view.findViewById(R.id.txtCalcParam1_1);
      txt_calcGravityG   = (TextView)view.findViewById(R.id.txtCalcParam2_1);
      txt_calcDryDensity = (TextView)view.findViewById(R.id.txtCalcParam3_1);
          
      L = (LinearLayout) view.findViewById(R.id.constValuesLayout);
   }
   
   /**************************************************************************
    * Function: onCreateView 
    * @param inflater            LayoutInflater
    * @param container           ViewGroup
    * @param savedInstanceState  Bundle
    * @return (View) 
    * ***********************************************************************/
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
   {
      if (null == container)
      {
         return null;
      }
      final View view = (ScrollView) inflater.inflate(R.layout.fragment_info,
                                                      container, 
                                                      false);
      /* ********************************************************************** */
      InitAllResourcesInClass(view);
      /* ********************************************************************** */
      initTextFieldsFromMemory();
      /* ********************************************************************** */
      rb_celcium.setOnCheckedChangeListener(new OnCheckedChangeListener()
      {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            if (buttonView.isChecked()) 
            {
               rb_fahrenheit.setChecked(false);
               convertTemperature(true);
            }
         }
      });
      /* ********************************************************************** */  
      rb_fahrenheit.setOnCheckedChangeListener(new OnCheckedChangeListener()
      {
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
         {
            if (buttonView.isChecked()) 
            {
               rb_celcium.setChecked(false);
               convertTemperature(false);
            }
         }
      });
      /* ********************************************************************** */
      btn_calculate.setOnClickListener(this);
      tBtn_ShowHideConstValues.setOnClickListener(this);
      /* ********************************************************************** */
      return (view);
   }
   
   /**************************************************************************
    * Function:  initTextFieldsFromMemory()
    * @return (None) 
    * ***********************************************************************/
   private void initTextFieldsFromMemory()
   {
      StartMenuActivity.Engine.Parameters.altitude =  
         StartMenuActivity.Interf.getAltitudeParameter();
      edTxt_altitude.setText(String.valueOf(StartMenuActivity.Engine.Parameters.altitude));
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.latitude =  
         StartMenuActivity.Interf.getLatitudeParameter();
      edTxt_latitude.setText(String.valueOf(StartMenuActivity.Engine.Parameters.latitude));
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.azimut =  
         StartMenuActivity.Interf.getAzimutParameter();
      edTxt_azimut.setText(String.valueOf(StartMenuActivity.Engine.Parameters.azimut));
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.angle =
         StartMenuActivity.Interf.getAngleParameter();
      edTxt_angle.setText(String.valueOf(StartMenuActivity.Engine.Parameters.angle));
      
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.temperature =  
         StartMenuActivity.Interf.getTemperatureParameter();
      edTxt_temperature.setText(String.valueOf(StartMenuActivity.Engine.Parameters.temperature));
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.isCelcium = 
         StartMenuActivity.Interf.getTemperatureTypeParameter();
      if(true == StartMenuActivity.Engine.Parameters.isCelcium)
      {
         rb_fahrenheit.setChecked(false);
         rb_celcium.setChecked(true);
      }
      else
      {
         rb_fahrenheit.setChecked(true);
         rb_celcium.setChecked(false);
      }
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.density = 
         StartMenuActivity.Interf.getDensityParameter();
      edTxt_density.setText(String.valueOf(StartMenuActivity.Engine.Parameters.density));
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.preasure = 
         StartMenuActivity.Interf.getPreasureParameter();
      edTxt_presure.setText(String.valueOf(StartMenuActivity.Engine.Parameters.preasure));
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.humidity = 
         StartMenuActivity.Interf.getHumidityParameter();
      edTxt_humidity.setText(String.valueOf(StartMenuActivity.Engine.Parameters.humidity));
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.soundSpeed = 
         StartMenuActivity.Interf.getSoundSpeedParameter();
      txt_calcSoundSpeed.setText(String.valueOf(StartMenuActivity.Engine.Parameters.soundSpeed));
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.gravityAcceleration = 
         StartMenuActivity.Interf.getGravityAccelerationParameter();
      txt_calcGravityG.setText(String.valueOf(StartMenuActivity.Engine.Parameters.gravityAcceleration));
      /*--------------------------------------------------------------*/
      StartMenuActivity.Engine.Parameters.windStrength = 
         StartMenuActivity.Interf.getWindStrengthParameter();
      /*--------------------------------------------------------------*/
      int direction = (Integer) StartMenuActivity.Interf.getWindDirectionParameter();
      StartMenuActivity.Engine.Parameters.windDirection.initDirectionByNumber(direction); 
   }
   
   /*************************************************************************
    * Function: onClick()
    * @param View view 
    * @return None. 
    * ***********************************************************************/
   public void onClick(View view)
   {
      switch (view.getId()) 
      {
         case R.id.btnCalculate:
            calculatePreasure();
            calculateDensityInfo();
            calculateSoundSpeedInfo();
            calculateGravityInfo();

            saveChangesInMemory();
            break;
            
         case R.id.tglBtnConstParams:
            if(null != L)
            {
               if (tBtn_ShowHideConstValues.isChecked())
               {
                  L.setVisibility(View.GONE);
               }
               else
               {
                  L.setVisibility(View.VISIBLE);
               }
            }
            else
            {
               Log.e("FragmentInfo","LinearLayout -> null");
            }
            break;
            
         default:
            break;
      }
   }
   /**************************************************************************
    * Function: saveChangesInMemory  - atmosphere parameters must be saved
    * @param  None. 
    * @return None. 
    * ***********************************************************************/
   private void saveChangesInMemory()
   {
      Double value = init;
      /*--------------------------------------------------------------*/
      value = Double.valueOf(edTxt_latitude.getText().toString());
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.LATITUDE, value);
      /*--------------------------------------------------------------*/
      value = Double.valueOf(edTxt_altitude.getText().toString());
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.ALTITUDE,value);
      /*--------------------------------------------------------------*/
      value = Double.valueOf(edTxt_azimut.getText().toString());
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.AZIMUT,value);
      /*--------------------------------------------------------------*/
      value = Double.valueOf(edTxt_temperature.getText().toString());
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.TEMPERATURE,value);
      /*--------------------------------------------------------------*/
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.IS_CELSIUM,rb_celcium.isChecked());
      /*--------------------------------------------------------------*/
      value = Double.valueOf(edTxt_density.getText().toString());
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.DENSITY,value);
      /*--------------------------------------------------------------*/
      value = Double.valueOf(edTxt_presure.getText().toString());
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.PRESSURE,value);
      /*--------------------------------------------------------------*/
      value = Double.valueOf(edTxt_humidity.getText().toString());
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.HUMIDITY,value);
      /*--------------------------------------------------------------*/
      value = Double.valueOf(txt_calcSoundSpeed.getText().toString());
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.SOUND_SPEED,value);
      /*--------------------------------------------------------------*/
      value = Double.valueOf(txt_calcGravityG.getText().toString());
      StartMenuActivity.Interf.setPrefParameters(SAVE_PARAMS_STRUCTURE.GRAVIRY, value);
   }
   
   /**************************************************************************
    * Function: convertTemperature - from Celcium to Farenheit, or vs.
    * @param  isCelcium - boolean 
    * @return None.  
    * ***********************************************************************/
   private void convertTemperature(boolean isCelcium)
   {
      Double temperature =  Double.valueOf(edTxt_temperature.getText().toString());
      if (false == isCelcium)
      {
         StartMenuActivity.Engine.Parameters.t_fahrenheit = 
            StartMenuActivity.Engine.convertionCelciumToFarenheit(temperature);
         temperature = StartMenuActivity.Engine.Parameters.t_fahrenheit;
      }
      else
      {
         StartMenuActivity.Engine.Parameters.temperature = 
            StartMenuActivity.Engine.convertionFarenheitToCelcium(temperature);
         temperature = StartMenuActivity.Engine.Parameters.temperature;
      }
      edTxt_temperature.setText(temperature.toString());
   }
   
   /**************************************************************************
    * Function:  calculatePreasure
    * @return None.  
    * ***********************************************************************/
   private void calculatePreasure()
   {
      StartMenuActivity.Engine.Parameters.preasure =  
         StartMenuActivity.Engine.calculatePressure(  StartMenuActivity.Engine.Parameters.altitude);
      StartMenuActivity.Engine.Parameters.preasure /= 1000; // must be in kPa TODO:??? wtf in calculation we *1000 twice
      edTxt_presure.setText(String.valueOf(StartMenuActivity.Engine.Parameters.preasure));
   }
   
   /**************************************************************************
    * Function:  calculateDensityInfo
    * @return None.  
    * ***********************************************************************/
   private void calculateDensityInfo()
   {
      StartMenuActivity.Engine.Parameters.altitude = Double.valueOf(edTxt_altitude.getText().toString());
      StartMenuActivity.Engine.Parameters.density =  Math.abs(StartMenuActivity.Engine.calculateDensity( 
                                                            StartMenuActivity.Engine.Parameters.preasure, 
                                                            StartMenuActivity.Engine.Parameters.temperature,
                                                            StartMenuActivity.Engine.Parameters.humidity));
      edTxt_density.setText(String.valueOf(StartMenuActivity.Engine.Parameters.density));
      
      double dryDensity = StartMenuActivity.Engine.calculateDensityDryAir(
                                                   StartMenuActivity.Engine.Parameters.preasure,
                                                   StartMenuActivity.Engine.Parameters.temperature);
      
      txt_calcDryDensity.setText(String.valueOf(dryDensity));
      
   }
   
   /**************************************************************************
    * Function:  calculateSoundSpeedInfo
    * @return None.  
    * ***********************************************************************/
   private void calculateSoundSpeedInfo()
   {
      StartMenuActivity.Engine.Parameters.preasure   = Double.valueOf(edTxt_presure.getText().toString());
      StartMenuActivity.Engine.Parameters.altitude   = Double.valueOf(edTxt_altitude.getText().toString());
      StartMenuActivity.Engine.Parameters.humidity   = Double.valueOf(edTxt_humidity.getText().toString());
      
      StartMenuActivity.Engine.Parameters.soundSpeed =  
         Math.abs(StartMenuActivity.Engine.getSoundSpeedInAir( 
                                                            StartMenuActivity.Engine.Parameters.temperature, 
                                                            StartMenuActivity.Engine.Parameters.preasure,
                                                            StartMenuActivity.Engine.Parameters.humidity));
      
      txt_calcSoundSpeed.setText(String.valueOf( StartMenuActivity.Engine.Parameters.soundSpeed));
      
   }
   /**************************************************************************
    * Function:  calculateGravityInfo
    * @return None.  
    * ***********************************************************************/
   private void calculateGravityInfo()
   {
      //TODO - This params mast be in Atmosphere parameters, not in bullet
      StartMenuActivity.Engine.BulletFlight.azimuth = Double.valueOf(edTxt_azimut.getText().toString());
      StartMenuActivity.Engine.Parameters.latitude  = Double.valueOf(edTxt_latitude.getText().toString());
      
      StartMenuActivity.Engine.BulletFlight.coordAngle = Double.valueOf(edTxt_angle.getText().toString());
      StartMenuActivity.Engine.Parameters.gravityAcceleration =  Math.abs(StartMenuActivity.Engine.getLocalG( 
                                                                  StartMenuActivity.Engine.Parameters.latitude, 
                                                                  StartMenuActivity.Engine.Parameters.altitude));
      
      txt_calcGravityG.setText(String.valueOf(StartMenuActivity.Engine.roundTo(
                  StartMenuActivity.Engine.Parameters.gravityAcceleration,4)));
   }
}