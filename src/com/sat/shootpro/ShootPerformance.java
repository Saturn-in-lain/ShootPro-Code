package com.sat.shootpro;
import java.util.Vector;
import com.sat.ballistics.Ballistics.VECTOR_TYPE;

public class ShootPerformance
{
   public  Vector<Double>  deviationShot         = null;

   /**************************************************************************
    * Constructor: ShootPerformance class constructor for ordinal job
    * This class is an wrapper for connecting all parameters of the game
    * with functionality in Ballistic engine classes.
    * ***********************************************************************/
   public ShootPerformance(Target T)
   {
      deviationShot = new Vector<Double>();
      deviationShot.add(0, StartMenuActivity.Engine.init); 
      deviationShot.add(1, StartMenuActivity.Engine.init); 
      deviationShot.add(2, StartMenuActivity.Engine.init);
      
      preSetAtmosphereParams();
      preSetBallisticParams();
   }
   
   /**************************************************************************
    * Function: preSetBallisticParams - take from proper place 
    * Note() Stab function 
    * @return () 
    * ***********************************************************************/
   private void preSetBallisticParams()
   {
      StartMenuActivity.Engine.BulletFlight.latitude   = StartMenuActivity.Interf.getLatitudeParameter();
      StartMenuActivity.Engine.BulletFlight.azimuth    = StartMenuActivity.Interf.getAzimutParameter();
      StartMenuActivity.Engine.BulletFlight.coordAngle = StartMenuActivity.Interf.getAngleParameter();
      StartMenuActivity.Engine.setWindInfluence();
   }
    /**************************************************************************
    * Function: preSetAtmosphereParams - take from proper place
    * tab, or parse from saved memory 
    * Note() Stab function
    * @return () 
    * ***********************************************************************/
    private void  preSetAtmosphereParams()
    {
      StartMenuActivity.Engine.Parameters.temperature = StartMenuActivity.Interf.getTemperatureParameter();
      StartMenuActivity.Engine.Parameters.altitude    = StartMenuActivity.Interf.getAltitudeParameter();
      StartMenuActivity.Engine.Parameters.humidity    = StartMenuActivity.Interf.getHumidityParameter();
      StartMenuActivity.Engine.Parameters.latitude    = StartMenuActivity.Interf.getLatitudeParameter();
             
       StartMenuActivity.Engine.Parameters.t_fahrenheit = 
                      StartMenuActivity.Engine.convertionCelciumToFarenheit(
                      StartMenuActivity.Engine.Parameters.temperature);
       
       StartMenuActivity.Engine.Parameters.preasure = 
                      StartMenuActivity.Engine.calculatePressure(
                      StartMenuActivity.Engine.Parameters.altitude);
       
       StartMenuActivity.Engine.Parameters.preasure = StartMenuActivity.Interf.getPreasureParameter();

       StartMenuActivity.Engine.Parameters.density =  
                         Math.abs(StartMenuActivity.Engine.calculateDensity( 
                            StartMenuActivity.Engine.Parameters.preasure, 
                            StartMenuActivity.Engine.Parameters.temperature,
                            StartMenuActivity.Engine.Parameters.humidity));

       StartMenuActivity.Engine.Parameters.soundSpeed =  
                         Math.abs(StartMenuActivity.Engine.getSoundSpeedInAir( 
                            StartMenuActivity.Engine.Parameters.temperature, 
                            StartMenuActivity.Engine.Parameters.preasure,
                            StartMenuActivity.Engine.Parameters.humidity));
       
       StartMenuActivity.Engine.Parameters.gravityAcceleration =  
                         Math.abs(StartMenuActivity.Engine.getLocalG( 
                            StartMenuActivity.Engine.Parameters.latitude, 
                            StartMenuActivity.Engine.Parameters.altitude));
       
      StartMenuActivity.Engine.Parameters.density             = StartMenuActivity.Interf.getDensityParameter();            
      StartMenuActivity.Engine.Parameters.gravityAcceleration = StartMenuActivity.Interf.getGravityAccelerationParameter();
      StartMenuActivity.Engine.Parameters.soundSpeed          = StartMenuActivity.Interf.getSoundSpeedParameter();
    }
    
   /**************************************************************************
   * Function: initBallisticEngineParameters
   * @param  None. 
   * @return None. 
   * ***********************************************************************/
   public void initBallisticEngineParameters(double maxDistance)
   {
      StartMenuActivity.Engine.prepareBallisticShoot(maxDistance);
   }
    
   /**************************************************************************
    * Function: shootOnTargetDistance - where deviationShot(x, y, z) vector
    * filled with coordinates of bullet in target distance range
    * @param  None. 
    * @return None. 
    * ***********************************************************************/
   public void shootOnTargetDistance()
   {
      StartMenuActivity.Engine.setWindInfluence();
      StartMenuActivity.Engine.getBulletVectorsInfo(VECTOR_TYPE.VECTOR_TYPE_RV_TARGERT,
                                                    deviationShot);
   }
   
   /**************************************************************************
    * Function: setWindParameters
    * @param  None. 
    * @return None. 
    * ***********************************************************************/
   public void setWindParameters()
   {
      StartMenuActivity.Engine.setWindInfluence();
   }
   
   /**************************************************************************
    * Function: shootTogetherWithScopeSets - return value
    * of scope clicks settings. 
    * @param  scope_X_or_Y_click - (int) value of clicks
    * @param  returnInMeters     - (double) true if meters, false centimeters 
    * @return metricDeviation    - double. 
    * ***********************************************************************/
   public double shootTogetherWithScopeSets(int scope_X_or_Y_click, boolean returnInMeters)
   {
      double  metricDeviation = StartMenuActivity.Engine.init;
      metricDeviation = StartMenuActivity.Engine.getMetricDeviationPerClickSet(
                              StartMenuActivity.Engine.getTargetDistance(),
                              scope_X_or_Y_click,
                              StartMenuActivity.ScopeInformation.scopeClickValue,
                              returnInMeters);
      return metricDeviation;
   }
}
