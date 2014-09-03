package com.sat.ballistics;

import java.util.Vector;
import android.util.Log;

/* http://guns.allzip.org/topic/13/801182.html 
 * http://ballistik.3dn.ru/ 
 * http://www.barnaulpatron.ru/production/sportshuntingcartridgescalibre/76251.html 
 * http://www.shooterscalculator.com/ballistic-trajectory-chart.php
 * http://www.bfxyz.nl/docs/bfxaboutdragfunctions.shtml - data for round bullet also
 * http://vk.bstu.ru/book52/list.htm - data for encyclopedia
 * 
 * 
 * http://www.shooterscalculator.com/ballistic-trajectory-chart.php?pl=%5BMY%5D&presets=%5BMY%5D~~G1~0.4~140.4312~2624~0~1.5~0~0~0~false~0~59~29.92~50~true~1000~100&df=G1&bc=0.4&bw=140.4312&vi=2624&zr=100&sh=1.5&sa=0&ws=0&wa=0&ssb=on&cr=1202.97&ss=100&chartColumns=Range~m%3BElevation~cm%3BWindage~cm%3BEnergy~J%3BTime~s%3BVel%5Bx%2By%5D~m%2Fs%3BVel%5Bx%5D~m%2Fs%3BVel%5By%5D~m%2Fs&lbl=&submitst=+Create+Chart+
 * This is statistics of separate test data 
 * G1 = 0.4       Angle = 0 
 * W  = 140.4312
 * V  = 2624
 * yard = 1202.97 = 1000 meters
 * yard = 109.36  = 100 meters  
 */


/*TODO: Вынести константы из баллистики.... наверное*/

public class Ballistics
{
   private static String LOG = "Ballistics";
   public final double  e    = (2.71828182845904523536);

   private final static double constAngularVelocity    = (7.29212);                  // Кинетическая вязкость воздуха в нормальных условиях
   private final static double kinematicAirViscosity   = (0.0000146);                // m^2/c
   public static double WearthSpeed = constAngularVelocity * Math.pow(10,-5);       // рад/с.
   /* --------------------------------------------------------------------------------*/
   private final static int X = (0);
   private final static int Y = (1);
   private final static int Z = (2);
   /* --------------------------------------------------------------------------------*/
   //private int   degree             = (360);
   //private int   circle_parts       = (60);
   //private int   full_circle        = (degree * circle_parts);             // 21600
   //private float degree_const       = (float)(6.2832);                     // radian/sec - 1 turn/sec
   //private float elevation_degree   = (float)(degree_const / full_circle); // 0.002908
   
   final static double airPresureOnSeaLevel    = (101.325);    // kPa
   private double K0 = airPresureOnSeaLevel * Math.pow(constSoundSpeed,2); // кг/м3 (11816749.48125)
   /* --------------------------------------------------------------------------------*/
   private double      oneMillToMOA          = (double)(3.438);  // 1 mil equals 3.438 MOA
   private double      oneMOAonHundredMeters = (double)(2.9089); // cm
   
   private final float yardConstant     = (float) (27.778); 
   private final float meterConstant    = (float) (25.4);
   private final float oneInchPerMeter  = (float) (0.0254);
   private final float oneInchesInMeter = (float) (39.37);
   
   /* --------------------------------------------------------------------------------*/
   final static double constSoundSpeed          = (341.5);
   final static double T0                       = (273.15);
   final static double R_constantDryAir         = (287.05);    // J/(kg.K)
   final static double constGravityAcceleration = (9.80665);   // m/sec^2
   final static double molarAirMass             = (0.028096);  // kg/mol
   final static double universalGasConstant     = (8.3143);    // Djoul/ mol * K
   final static double speedOfTemperatureLow    = (0.0065);    // K/m
   final static double soundSpeedCalcConst      = (331.5024);  // K/m
  
   /* --------------------------------------------------------------------------------*/
   final double adiotabConst_20  = (1.400);
   final double adiotabConst_100 = (1.402);
   final double adiotabConst_0   = (1.403);
   final double constMinMaxAngle = (90.0);
   /* --------------------------------------------------------------------------------*/
   public final int roundConst      = (10); 
   public final int roundSmallConst = (2); 
   final public double init         = (0.00);
   /* --------------------------------------------------------------------------------*/   
   /*============== Table 1 =================================
    Meters / Feet / Density / Pressure / Temp C / SoundSpeed /  
    ============== Table 2 =================================
    Distance of flight / Vertical deviation / bullet speed 
    =======================================================*/
   
   /*=============================================================================*/
   private BulletInfo bullet = null;
   /*=============================================================================*/
   public class bulletFlightParams
   {
      /* Started parameters */                                  // parsed params now are duplicate
      private double mass                 = (init);             // parsed params TODO: must be in kg ???
      private double balistic_koef        = (init);             // parsed params   
      private  double bulletStartSpeed    = (init);             // parsed params
      /* Deviation parameters */
      private double W1Const              = (1120.4068);        // parsed params 
      /* Weapon parameter */
      private double bullet_turn_speed    = (init);             // parsed params
      public double barrel_rifling        = (init);   //0.24    // parsed params //0.02313; 
      public double Deviation_K           = (init); // calculated             
      /* Additional bullet parameters*/
      public double sectional_bullets     = (init); // may be if bullet spec?
      private double targetDistance       = (init);
      private double maxDistance          = (init);
      
      // TODO: К=J/(Нцтцд*m)
      private double bullet_K_param       = (init);            // parsed params
      // J=момент инерции пули относительно продольной оси
      // Нцтцд = расстояние "центр тяжести-центр давления"
      // m = масса пули
      // K = J / (Нцтцд * m)
      
      /* Iteration changed parameter */
      public double EnergyInDjoule       = (init);// calculated   
      public double speedNearTarget      = (init);// calculated   
      public double koef_wind_resistance = (init);// calculated   
      public double MAXSoundSpeed        = (init);// calculated   
      public double R_function           = (init);// calculated   
      public double dt                   = 0.001; 
      public double mainVelocity         = (init);// calculated   
      public double flightTime           = (init);// calculated   
      public double acceleration         = (init);// calculated   
      public int    stepNumber           = (0);

      /* Geographical parameters */
      public double latitude    = (init); 
      public double azimuth     = (init);
      public double coordAngle  = (init);
      
      /* Calculated vectors */
      private Vector<Double> Rv        = new Vector<Double>(); // start coordinate of bullet (x,y,z)
      private Vector<Double> Rv_target = new Vector<Double>(); // start coordinate of bullet (x,y,z)
      /* Velocity in space */
      private Vector<Double> Vb = new Vector<Double>(); // start velocity   of bullet (Vx,Vy,Vz)
      /* Velocity of wind in space */
      private Vector<Double> Vwind  = new Vector<Double>(); // vector of wind influence (Vwx,Vwy,Vwz)
      /* Velocity with wind influence */
      private Vector<Double> Vp0 = new Vector<Double>(); // vector of previous speed of the flow
      private Vector<Double> Vp  = new Vector<Double>(); // vector of speed of the flow (VPx,VPy,VPz)
      /* Acceleration in space*/
      private Vector<Double> A_b = new Vector<Double>(); // vector of bullet acceleration depending on atmosphere (Ax,Ay,Az)  
      private Vector<Double> G   = new Vector<Double>(); // acceleration of gravity
      /* Deviation */
      private Vector<Double> Vd  = new Vector<Double>(); // vector of deviation
      /* Earth spin */
      private Vector<Double> Wearth = new Vector<Double>(); // this is for earth movement
   }
   public bulletFlightParams BulletFlight = null;
   
   /*=============================================================================*/
   public enum VECTOR_TYPE
   {
      VECTOR_TYPE_RV,         //0
      VECTOR_TYPE_VB,         //1
      VECTOR_TYPE_WIND,       //2
      VECTOR_TYPE_VP0,        //3
      VECTOR_TYPE_VP,         //4
      VECTOR_TYPE_Ab,         //5
      VECTOR_TYPE_G,          //6
      VECTOR_TYPE_VD,         //7
      VECTOR_TYPE_WEARTH,     //8
      VECTOR_TYPE_RV_TARGERT, //9
      
      VECTOR_TYPE_UNKNOWN     //10
   }
   /*=============================================================================*/
   public enum WIND_DIRECTIONS 
   {
      NORTH_NORTH("NORTH_NORTH", 0, 0),
      NORTH_EAST ("NORTH_EAST",  1, 45),
      EAST_EAST  ("EAST_EAST",   2, 90),
      SOUTH_EAST ("SOUTH_EAST",  3, 135),
      SOUTH_SOUTH("SOUTH_SOUTH", 4, 180),
      SOUTH_WEST ("SOUTH_WEST",  5, 224),
      WEST_WEST  ("WEST_WEST",   6, 270),
      NORTH_WEST ("NORTH_WEST",  7, 325);
      
      private String stringName;
      private int intNumber;
      private int intAngle;
      
      private WIND_DIRECTIONS(String toString, int value, int angle) 
      {
         this.stringName = toString;
         this.intNumber = value;
         this.intAngle = angle;
      }

      public int getNumber()
      {
         return this.intNumber;
      }
      
      public int getAngle()
      {
         return this.intAngle;
      }
      
      //TODO: dirty trick
      public void initDirectionByNumber(int val)
      {
         if(val == NORTH_NORTH.getNumber())
         {
            this.intNumber  = val;
            this.stringName = NORTH_NORTH.stringName; 
            this.intAngle   = NORTH_NORTH.intAngle;
         }
         else if(val == NORTH_EAST.getNumber())
         {
            this.intNumber  = val;
            this.stringName = NORTH_EAST.stringName; 
            this.intAngle   = NORTH_EAST.intAngle;
         }
         else if(val == EAST_EAST.getNumber())
         {
            this.intNumber  = val;
            this.stringName = EAST_EAST.stringName; 
            this.intAngle   = EAST_EAST.intAngle;
         }
         else if(val == SOUTH_EAST.getNumber())
         {
            this.intNumber  = val;
            this.stringName = SOUTH_EAST.stringName; 
            this.intAngle   = SOUTH_EAST.intAngle;
         }
         else if(val == SOUTH_SOUTH.getNumber())
         {
            this.intNumber  = val;
            this.stringName = SOUTH_SOUTH.stringName; 
            this.intAngle   = SOUTH_SOUTH.intAngle;
         }
         else if(val == SOUTH_WEST.getNumber())
         {
            this.intNumber  = val;
            this.stringName = SOUTH_WEST.stringName; 
            this.intAngle   = SOUTH_WEST.intAngle;
         }
         else if(val == WEST_WEST.getNumber())
         {
            this.intNumber  = val;
            this.stringName = WEST_WEST.stringName; 
            this.intAngle   = WEST_WEST.intAngle;
         }
         else if(val == NORTH_WEST.getNumber())
         {
            this.intNumber  = val;
            this.stringName = NORTH_WEST.stringName; 
            this.intAngle   = NORTH_WEST.intAngle;
         }
         else
         {
            Log.e(LOG,"Saved params of wind direction is error");
         }
      }
      
      @Override
      public String toString() 
      {
         return this.stringName;
      }
   }

   /*=============================================================================*/    
      /* Proper way to create API get/set for them. TODO*/
      public class atmosphereParams
      {
         public double altitude     = (0.0);
         public double latitude     = (0.0);
         public double azimut       = (0.0);
         public double angle        = (0.0);
         public double temperature  = (15.0);
         public double t_fahrenheit = (0.0);
         public boolean isCelcium   = true;
         public double density      = (0.0);
         public double preasure     = (0.0);
         public double humidity     = (50.0); //%
         public double soundSpeed   = (0.0);
         public double windStrength = (0.0);
         public double gravityAcceleration = (0.0);

         public WIND_DIRECTIONS windDirection = WIND_DIRECTIONS.EAST_EAST;
      }
      public atmosphereParams Parameters = null;
     
      /* --------------------------------------------------------------------------------*/
                                             /* Wind strength */
      public class windParameters
      {
         int    windStrengthByBofort    = 0;
         String description             = "";
         String windSpeedInMetersPerSec = "";
         String windSpeedInKmPerHours   = "";
         String windSpeedInMilesPerHour = "";
         
         public void setObjectParameters(int bofort, String desc, String meters, String km, String miles )
         {
           this.windStrengthByBofort    = bofort;
           this.description             = desc;
           this.windSpeedInMetersPerSec = meters;
           this.windSpeedInKmPerHours   = km;
           this.windSpeedInMilesPerHour = miles;      
         }
      }
      /* --------------------------------------------------------------------------------*/
      // private List<windParameters> constWindList = new ArrayList<windParameters>();
      //windParameters temp = null;
      //temp.setObjectParameters(0, "None",       "0",   "0",      "<1");constWindList.add(temp);
      //temp.setObjectParameters(1, "Silent",     "1",   "3.6",    "1-3");constWindList.add(temp);
      //temp.setObjectParameters(2, "Low",        "2-3", "5-12",   "4-7");constWindList.add(temp);
      //temp.setObjectParameters(3, "Weak",       "4-5", "13-19",  "8-12");constWindList.add(temp);
      //temp.setObjectParameters(4, "low middle", "6-8", "20-30",  "13-18");constWindList.add(temp);
      //temp.setObjectParameters(5, "Middle",     "9-10", "31-37", "19-24");constWindList.add(temp);
      //temp.setObjectParameters(6, "Strong",     "11-13", "38-48", "25-31");constWindList.add(temp);
      //temp.setObjectParameters(7, "Very strong","14-17", "49-63", "32-38");constWindList.add(temp);
      //temp.setObjectParameters(8, "Heavy",      "18-20", "64-73", "39-46");constWindList.add(temp);
      //temp.setObjectParameters(9, "Storm",      "21-26", "74-94", "47-54");constWindList.add(temp);
      //temp.setObjectParameters(10,"Heavy Storm","27-31", "95-112", "55-63");constWindList.add(temp);
      //temp.setObjectParameters(11,"Angry Storm","32-36", "115-130","64-72");constWindList.add(temp);
      //temp.setObjectParameters(12,"Hurricane",  ">36",   ">130",   "73-82");constWindList.add(temp);
      /* -------------------------------------------------------------------------------------------*/

   /**************************************************************************
    * Constructor: Ballistics class constructor for ordinal job
    *  
    * ***********************************************************************/
   public Ballistics(BulletInfo info)
   {
      BallisticsInit();
      //set BulletInfo parameters
      setBulletInfo(info);
   }
   
   /**************************************************************************
    * Constructor: Ballistics class constructor for JUNIT Tests 
    *  
    * ***********************************************************************/
   public Ballistics()
   {
      BallisticsInit();
   }
   
   /**************************************************************************
    * Function: BallisticsInit  
    * @param None.
    * @return None.
    * ***********************************************************************/
   private void BallisticsInit()
   {
      Parameters   = new atmosphereParams();
      BulletFlight = new bulletFlightParams();
      
      BallisticsVectorsInit();
   }
   
   /**************************************************************************
    * Function: BallisticsVectorsInit  
    * @param None.
    * @return None.
    * ***********************************************************************/
   private void BallisticsVectorsInit()
   {
      BulletFlight.Rv.clear();    BulletFlight.Vb.clear();    BulletFlight.Rv_target.clear();
      BulletFlight.Rv.add(init);  BulletFlight.Vb.add(init);  BulletFlight.Rv_target.add(init);
      BulletFlight.Rv.add(init);  BulletFlight.Vb.add(init);  BulletFlight.Rv_target.add(init);
      BulletFlight.Rv.add(init);  BulletFlight.Vb.add(init);  BulletFlight.Rv_target.add(init);
      
      BulletFlight.A_b.clear();   BulletFlight.Vd.clear();
      BulletFlight.A_b.add(init); BulletFlight.Vd.add(init);
      BulletFlight.A_b.add(init); BulletFlight.Vd.add(init);
      BulletFlight.A_b.add(init); BulletFlight.Vd.add(init);
      
      BulletFlight.Vp0.clear();   BulletFlight.Vp.clear();   BulletFlight.Vwind.clear();
      BulletFlight.Vp0.add(init); BulletFlight.Vp.add(init); BulletFlight.Vwind.add(init);  
      BulletFlight.Vp0.add(init); BulletFlight.Vp.add(init); BulletFlight.Vwind.add(init); 
      BulletFlight.Vp0.add(init); BulletFlight.Vp.add(init); BulletFlight.Vwind.add(init); 
      
      BulletFlight.Wearth.clear();   BulletFlight.G.clear();
      BulletFlight.Wearth.add(init); BulletFlight.G.add(init);
      BulletFlight.Wearth.add(init); BulletFlight.G.add(init);
      BulletFlight.Wearth.add(init); BulletFlight.G.add(init);
      
      BulletFlight.flightTime = (BulletFlight.flightTime - BulletFlight.dt);
   }
   
   /**************************************************************************
    * Function: setBulletInfo  
    * @param info - BulletInfo
    * @return None.
    * ***********************************************************************/
   public void setBulletInfo(BulletInfo info)
   {
      if(null != info)
      {
         bullet = info;
         BulletFlight.mass                 = bullet.bulletWeight * 1000; //(for 9.10)
         BulletFlight.balistic_koef        = bullet.bulletBallisticsKoefficient;  
         BulletFlight.bulletStartSpeed     = bullet.ammo_startSpeed;
         BulletFlight.W1Const              = bullet.bullet_W1Const;
         BulletFlight.bullet_turn_speed    = bullet.bullet_turn_speed;
         BulletFlight.bullet_K_param       = bullet.bullet_K_param;
      }
      else
      {
         Log.e(LOG,"Information was not parsed");
         bullet = null;
      }
   }
   
   /**************************************************************************
    * Function: setBulletFlightPrivates  
    * @param mass             - double in grams/grains TODO
    * @param bulletStartSpeed - double 
    * @param balistic_koef    - double 
    * @param W1Const          - double
    * @param barrel_rifling   - double 
    * @param bullet_K_param   - double 
    * @return None.
    * ***********************************************************************/
   public void setBulletFlightPrivates(double mass,
                                       boolean isInGramms,
                                       double bulletStartSpeed,
                                       double balistic_koef,
                                       double W1Const,
                                       double barrel_rifling,
                                       double bullet_K_param)
   {
      if(null != BulletFlight)
      {
         if(true == isInGramms)
         {
            BulletFlight.mass                 = mass / 1000;
         }
         else
         {
            BulletFlight.mass                 = mass;
         }
         BulletFlight.bulletStartSpeed     = bulletStartSpeed;
         BulletFlight.balistic_koef        = balistic_koef;  
         BulletFlight.W1Const              = W1Const;
         BulletFlight.barrel_rifling       = barrel_rifling;
         BulletFlight.bullet_K_param       = bullet_K_param;
      }
   }
   
   /**************************************************************************
    * Function: getBulletVectorsInfo  
    * @param Type - VECTOR_TYPE enum
    * @param result - Vector<Double>
    * @return None.
    * ***********************************************************************/
   public void getBulletVectorsInfo( VECTOR_TYPE Type, Vector<Double> result)
   {
      if(null == BulletFlight)
      {
         Log.e(LOG,"Some issues with BulletFlight init values == null");
         return;
      }
      
      switch(Type)
      {
         case VECTOR_TYPE_RV:
            copyVectors(BulletFlight.Rv,result);
            break;
            
         case VECTOR_TYPE_VB:
            copyVectors(BulletFlight.Vb,result);
            break;
            
         case VECTOR_TYPE_VD:
            copyVectors(BulletFlight.Vd,result);
            break;
            
         case VECTOR_TYPE_VP:
            copyVectors(BulletFlight.Vp,result);
            break;
            
         case VECTOR_TYPE_Ab:
            copyVectors(BulletFlight.A_b,result);
            break;
            
         case VECTOR_TYPE_RV_TARGERT:
            copyVectors(BulletFlight.Rv_target,result);
            break;
            
         default:
            break;
      }
   }

   /**************************************************************************
    * Function: shootingTragectory  http://twtmas.mpei.ac.ru/mas/worksheets/shooting.mcd
    * @Note:  This is main function with will be filled with smaller ones =)  
    * @param Mass of bullet 
    * @param Starting speed of the bullet
    * @return None.
    * ***********************************************************************/
   public void prepareBallisticShoot(double maxDistanceToTarget)
   {
      if ((null == Parameters) || (null == BulletFlight))
      {
         Log.e(LOG,"Atmosphere/BulletFlight parameters == null");
         return;
      }
      
      BallisticsVectorsInit();
      /** First we must initialize parameters */
      BulletFlight.Vb.set(X,BulletFlight.bulletStartSpeed);
      BulletFlight.Vb.set(Y,init);
      BulletFlight.Vb.set(Z,init);
      BulletFlight.maxDistance = maxDistanceToTarget;
      
      /* Calculate bullet_turn_speed or w parameter: [OK] */
      if(0 == BulletFlight.bullet_K_param)
      {
         BulletFlight.bullet_K_param = (0.005);
         Log.e(LOG,"Issue with bullet_K_param!!! Parser error?");
      }
      
      if(0 == BulletFlight.bullet_turn_speed)
      {
         double turn = (BulletFlight.bulletStartSpeed / BulletFlight.barrel_rifling );
         BulletFlight.bullet_turn_speed = roundTo(turn,roundConst);
      }
      /* Pre set of Vp vectors */
      Mlt(BulletFlight.Vb,
          -1, 
          BulletFlight.Vp);
      /* ------------------------------------------------------------------------------------ */
      /* Calculate speed near target [ OK ] */
      BulletFlight.speedNearTarget = getAmmoSpeedNearTarget(BulletFlight.bulletStartSpeed,
                                                            BulletFlight.targetDistance);
      /* Calculate earth speed vector [ OK ] */
      earthSpeedVector(BulletFlight.latitude, 
                       BulletFlight.azimuth, 
                       BulletFlight.coordAngle,
                       BulletFlight.Wearth);
      /* Calculate gravitation vector [ OK ] */
      gravitationTurnVector(BulletFlight.coordAngle,
                            BulletFlight.G);
   }
   
   /**************************************************************************
    * Function: calculateShootStep()
    * @param shootLengthStep - double step of calculation
    * @return () 
    * ***********************************************************************/
   public void calculateShootStep(double shootLengthStep)
   {
      double temp = init;
      Vector<Double> temp_vector = new Vector<Double>();
      temp_vector.add(X,init); 
      temp_vector.add(Y,init); 
      temp_vector.add(Z,init);
                   
      while ( Lng(BulletFlight.Rv) <= shootLengthStep )
      {
         if( Lng(BulletFlight.Rv) < BulletFlight.targetDistance)
         {
            saveVectorParameterNearTarget();
         }
         
         temp = init;
         temp_vector.set(X,init); 
         temp_vector.set(Y,init); 
         temp_vector.set(Z,init);
         /*----------------------------------------------------------------------------------------*/
         /* 1. Set Vp0 = Vp 
          * 1--- сохраняем старый вектор потока
          * 2--- плюсуем ветер */ 
         copyVectors(BulletFlight.Vp,
                     BulletFlight.Vp0);
         
         // TODO: Vv = Find_Wnd(RV); Calculate wind according to coordinates 
         /* 2. Set Vp = Vv - Vb */
         if( 0 !=  Lng(BulletFlight.Vwind))
         {
            vectorDeduction(BulletFlight.Vwind, 
                            BulletFlight.Vb, 
                            BulletFlight.Vp);
         }        
         else
         {
            Mlt(BulletFlight.Vb,
                -1, 
                BulletFlight.Vp);
         }
        
         /*----------------------------------------------------------------------------------------*/
         /* 3. Deviation Calculate Vd = VP(Vp, Vp0); */
         VP(BulletFlight.Vp, 
            BulletFlight.Vp0, 
            BulletFlight.Vd);
         
         /* Calculate Vd_s= K*w/(Lng(Vp)*Lng(Vp0)); Vd = Mlt(Vd, Vd_s);  Tested [ CHECK ] */
         BulletFlight.Deviation_K = caclulateDeviationkoef( Lng(BulletFlight.Vp),
                                                            Lng(BulletFlight.Vp0));
         Mlt(BulletFlight.Vd,                  
             BulletFlight.Deviation_K,
             BulletFlight.Vd);
/*-----------------------------------------------------------------------------------------------------------*/
//Ath=Find_Ath(RV); Recalculate atmosphere params, because shooting from 700m to 0m - parameters can be changed  
/*-----------------------------------------------------------------------------------------------------------*/
         BulletFlight.mainVelocity  = calculateVelocity(BulletFlight.Vp);
         BulletFlight.MAXSoundSpeed = (BulletFlight.mainVelocity / Parameters.soundSpeed);
         BulletFlight.R_function    = getR_Function(BulletFlight.MAXSoundSpeed,
                                                    BulletFlight.W1Const);
         
         BulletFlight.EnergyInDjoule = caclulateBulletEnergy(BulletFlight.mass,
                                                             BulletFlight.mainVelocity); 
         /*-----------------------------------------------------------------------------------------------*/          
         if (BulletFlight.sectional_bullets != 0)
         {
            BulletFlight.acceleration = getG1AccelerationKoef(BulletFlight.sectional_bullets);
         }
         else
         {
            BulletFlight.acceleration = getAmmoAcceleration(BulletFlight.R_function,
                                                            BulletFlight.balistic_koef, 
                                                            Parameters.soundSpeed, 
                                                            Parameters.density);
         }
         /*-----------------------------------------------------------------------------------------------*/
         /* 4. [Вычисление ускорений] A(аэродин) ~ A * (Vp / V) */
            temp = BulletFlight.acceleration * (BulletFlight.Vp.get(X) / BulletFlight.mainVelocity);
         BulletFlight.A_b.set(X,temp);
            temp = BulletFlight.acceleration * (BulletFlight.Vp.get(Y) / BulletFlight.mainVelocity); 
         BulletFlight.A_b.set(Y,temp);
            temp = BulletFlight.acceleration * (BulletFlight.Vp.get(Z) / BulletFlight.mainVelocity);
         BulletFlight.A_b.set(Z,temp);

         /* A = A(аэродин) + A(тяжести) + A(вращения СК) */ 
         /* Calculate additional A_b for with earth [ CHECK ]
          * A_b = (A_b + G); */
         
         additionalAccelerationCalculateion( BulletFlight.Rv,        
                                             BulletFlight.Vb,
                                             BulletFlight.A_b);
         //TODO: A_b = Find_A(Vp, Ath); 
         // For now it is calculated once
         /*-------------------------------------------------------------------------*/
         /* 5. [Приращение координат] RV = RV + Mlt(Vb, dT); */
         Mlt(BulletFlight.Vb,
             BulletFlight.dt,
             temp_vector);
         
         vectorAddition(BulletFlight.Rv,
                        temp_vector,
                        BulletFlight.Rv);
        
         /*---------------------------------------------------------------------*/
         /* 6. [Приращение скоростей] Calculate Vb = Vb + Mlt(A_b, dT) + Vd; vector [ OK? ] */
         
         temp_vector.set(X,(init)); 
         temp_vector.set(Y,(init)); 
         temp_vector.set(Z,(init));
         
         /* Mlt(A_b, dT) */
         Mlt(BulletFlight.A_b,
             BulletFlight.dt,
             temp_vector);
         
         /* Vb + Mlt(A_b, dT) */
         vectorAddition(BulletFlight.Vb, 
                        temp_vector, 
                        BulletFlight.Vb);
         
         /*---------------------------------------------------------------------*/
         /* 7. [Приращение девиации] + Vd */ 
         vectorAddition(BulletFlight.Vb, 
                        BulletFlight.Vd, 
                        BulletFlight.Vb);
         /*--------------------------------------------------------------------*/
         BulletFlight.stepNumber = (BulletFlight.stepNumber + 1);
         BulletFlight.flightTime = (BulletFlight.flightTime + BulletFlight.dt);
         /*--------------------------------------------------------------------*/
      }
      temp_vector.removeAllElements();
      
//TODO:>> testLogShoot();                       /* Calculation Log*/
   }

   /**************************************************************************
    * Function: saveVectorParameterNearTarget()  
    * @return (double)  
    * ***********************************************************************/
   private void saveVectorParameterNearTarget()
   {
      if( (Lng(BulletFlight.Rv)+1) > BulletFlight.targetDistance &&
          (0 == BulletFlight.Rv_target.get(X)) )
      {
         copyVectors(BulletFlight.Rv,
                     BulletFlight.Rv_target); 
         
         Log.e(LOG,">> Save target parameters" + 
               String.format("[%g][%g][%g]", 
               BulletFlight.Rv_target.get(X),
               BulletFlight.Rv_target.get(Y),
               BulletFlight.Rv_target.get(Z)));
      }
   }
   
   
   /**************************************************************************
    * Function: testLogShoot() TODO: RAT Debug 
    * @return (double)  
    * ***********************************************************************/
//   private void testLogShoot()
//   {
//      Log.e("Test",String.format("x:[%g] y:[%g] z:[%g]",
//                                                         BulletFlight.Rv.get(X),
//                                                         BulletFlight.Rv.get(Y),
//                                                         BulletFlight.Rv.get(Z)));
//      /* [ Скорость пули ] */
//      Log.e("Test",String.format("Vx:[%g] Vy:[%g] Vz:[%g]",
//                                                         BulletFlight.Vb.get(X),
//                                                         BulletFlight.Vb.get(Y),
//                                                         BulletFlight.Vb.get(Z)));
//      /* [ Скорость пули + скорость ветра ] */
//      Log.e("Test",String.format("VPx:[%g] VPy:[%g] VPz:[%g]",
//                                                         BulletFlight.Vp.get(X),
//                                                         BulletFlight.Vp.get(Y),
//                                                         BulletFlight.Vp.get(Z)));
//      /* [ Ускорение ] */
//      Log.e("Test",String.format("Ax:[%g] Ay:[%g] Az:[%g]",
//                                                         BulletFlight.A_b.get(X),
//                                                         BulletFlight.A_b.get(Y),
//                                                         BulletFlight.A_b.get(Z)));
//      /* [ Deviation ] */
//      Log.e("Test",String.format("Vd_x:[%g] Vd_y:[%g] Vd_z:[%g]",
//                                                         BulletFlight.Vd.get(X),
//                                                         BulletFlight.Vd.get(Y),
//                                                         BulletFlight.Vd.get(Z)));
//
//      Log.e("Test",String.format("Velocity:[%g]  Enerdy[%g]  Time:[%g]  ",
//                                                         BulletFlight.mainVelocity,
//                                                         BulletFlight.EnergyInDjoule,
//                                                         BulletFlight.flightTime));
//
//      Log.e("Test",String.format("R_function:[%g] Accelereation:[%g] MAX:[%g] ",
//                                                         BulletFlight.R_function,
//                                                         BulletFlight.acceleration,
//                                                         BulletFlight.MAXSoundSpeed));
//      
//      Log.e("Test",String.format("dencity[%g] preasure[%g] bullet_turn_speed[%g] \n soundSpeed:[%g]" +
//                                 "mass[%g] koef[%g] deviation[%g] \niterations[%d]",
//                                                         Parameters.density,
//                                                         Parameters.preasure,
//                                                         BulletFlight.bullet_turn_speed,
//                                                         Parameters.soundSpeed,
//                                                         BulletFlight.mass,
//                                                         BulletFlight.balistic_koef,
//                                                         BulletFlight.Deviation_K,
//                                                         BulletFlight.stepNumber));
//      Log.e("Test","===============================================");
//   }
   
   /**************************************************************************
    * Function: additionalAccelerationCalculateion 
    * @param Vector<Double> coord
    * @param Vector<Double> velocity
    * @param Vector<Double> result - return upgraded A_b vector
    * @return None. 
    * ***********************************************************************/
   public void additionalAccelerationCalculateion(Vector<Double> coord,
                                                  Vector<Double> velocity,
                                                  Vector<Double> result)
   {
      double x1 = (init), AcbX0 = (init); // THIS is for future update Ацб = w*w*R
      double y1 = (init), AcbY0 = (init);
      double z1 = (init), AcbZ0 = (init);
      
      Vector<Double> Acb = new Vector<Double>(); 
      Acb.add(init); Acb.add(init); Acb.add(init);
     
      /* Ax(вращения СК) = 2*wz*Vy - 2*wy*Vz + X*(wz*wz + wy*wy) + AцбX0  [ ~OK ] */
      x1 = 2 * BulletFlight.Wearth.get(Z) * velocity.get(Y) - 
           2 * BulletFlight.Wearth.get(Y) * velocity.get(Z) + 
           coord.get(X) * ( Math.pow( BulletFlight.Wearth.get(Z),2)  +
                            Math.pow( BulletFlight.Wearth.get(Y),2)) + AcbX0;
      y1 = 2 * BulletFlight.Wearth.get(Z) * velocity.get(X) - 
           2 * BulletFlight.Wearth.get(X) * velocity.get(Z) + 
           coord.get(Y) * ( Math.pow( BulletFlight.Wearth.get(Z),2)  + 
                            Math.pow( BulletFlight.Wearth.get(X),2)) + AcbY0;
      z1 = (2 * BulletFlight.Wearth.get(Y) * velocity.get(X)) - 
           (2 * BulletFlight.Wearth.get(X) * velocity.get(Y)) + 
           coord.get(Z) * ( Math.pow( BulletFlight.Wearth.get(Y),2)  + 
                            Math.pow( BulletFlight.Wearth.get(X),2)) + AcbZ0;
    
      Acb.set(X,x1); // + BulletFlight.G.get(X) in such way Z value correct 
      Acb.set(Y,y1);
      Acb.set(Z,z1);
      
      vectorAddition(Acb, 
                     BulletFlight.G, 
                     Acb);
     
      vectorAddition(result, 
                     Acb, 
                     result);
      
      Acb.removeAllElements();
   }
   
   /**************************************************************************
    * Function: earthSpeedVector() - Calculate earth speed once 
    * @param double latitude
    * @param double azimuth
    * @param double coordAngle
    * @param Vector<Double> result 
@Note: Tested - by JUnit
    * @return None. 
    * ***********************************************************************/
   public void earthSpeedVector(double latitude, 
                                double azimuth, 
                                double coordAngle,
                                Vector<Double> result )
   {
      double Z0 = (init); double Z1  = (init); 
      double X0 = (init); double X1  = (init); 
      double Y0 = (init); double Y1  = (init); 
      //Широта = 50 градусов северной(+), азимут цели = 60, угол места = +30
      latitude   = Math.toRadians(latitude);
      azimuth    = Math.toRadians(azimuth);
      coordAngle = Math.toRadians(coordAngle);
      
        X0 = WearthSpeed;
        Z1 = Z0 = init;
        X1 = X0 * Math.cos(latitude) - Y0 * Math.sin(latitude); //4.50E-5
        Y1 = X0 * Math.sin(latitude) + Y0 * Math.cos(latitude); //5.36E-5
        //Теперь, после поворота СК, Wземли = (4.50E-5, 5.36E-5, 0)
        X0 = X1;
        Z0 = Z1;
        Z1 = Z0 * Math.cos(azimuth) - X0 * Math.sin(azimuth); // -3.90E-5
        X1 = Z0 * Math.sin(azimuth) + X0 * Math.cos(azimuth); //2.25E-5
        //Теперь, после поворота СК, Wземли = (2.25E-5,5.36E-5,-3.90E-5)
        Y0 = Y1;
        X0 = X1;
        X1 = X0 * Math.cos(coordAngle) - Y0 * Math.sin(coordAngle); //4.63E-5
        Y1 = X0 * Math.sin(coordAngle) + Y0 * Math.cos(coordAngle);//3.52E-5

        //Результат: Wземли = (4.63E-5,3.52E-5,-3.90E-5)
        result.set(X, X1);
        result.set(Y, Y1);
        result.set(Z, Z1);
   }
   /**************************************************************************
    * Function: gravitationTurnVector() 
    * @param double coordAngle
@Note: Tested - by JUnit
    * @return None. Set global BulletFlight.G
    * ***********************************************************************/
   public void gravitationTurnVector(double coordAngle, Vector<Double> G)
   {
      double x1 = init;  double y1 = init;

      if(Parameters.gravityAcceleration > 0)
      {
         // Пример как не надо делать :)
         Parameters.gravityAcceleration = Parameters.gravityAcceleration * -1;
      }
      
      // Если не учитывать угла местности       [CHECK NOT WORKING]
      if( (coordAngle > constMinMaxAngle) ) // -constMinMaxAngle
      {
         y1 = Parameters.gravityAcceleration;
      }
      else
      {
         /* A(тяжести) после учета угла местности ~поворота земли  [ OK ] */
         
         coordAngle = Math.toRadians(coordAngle); 
         G.set(Y,Parameters.gravityAcceleration);
         
         x1 = (G.get(X) * Math.cos(coordAngle)) -
                            (G.get(Y) * Math.sin(coordAngle));
        
         y1 = (G.get(X) * Math.sin(coordAngle)) + 
                            (G.get(Y) * Math.cos(coordAngle));
         
//        x1 = Parameters.gravityAcceleration * Math.sin(coordAngle);
//        y1 = Parameters.gravityAcceleration * Math.cos(coordAngle);
         
      }
      G.set(X,x1);
      G.set(Y,y1);
      G.set(Z,init);
   }
   
   /**************************************************************************
    * Function: calculateVelocity
    * @param Vector<Double> velocity
@Note:  Tested - by JUnit 
    * @return Double - Velocity.
    * ***********************************************************************/
   public double calculateVelocity(Vector<Double> velocity)
   {
      double V = init;
      V = Math.pow(velocity.get(X),2) +
          Math.pow(velocity.get(Y),2) +
          Math.pow(velocity.get(Z),2);
      V = Math.sqrt(V);
      return V;//roundTo(V,roundConst);
   }
   
   /**************************************************************************
    * Function: getLocalG - Set precise gravitation acceleration parameter
    * @param double latitude
    * @param double altitude
@Note:  Tested - by JUnit 
    * @return Double - Local gravit.acceleration (+).
    * ***********************************************************************/
   public double getLocalG (double latitude, double altitude)
   {
      double G = init;
      latitude  = Math.toRadians(latitude);
      double temp = 0.000003086 * altitude;
      double Sin1 = 0.0053024 * (roundTo(Math.sin(latitude) * Math.sin(latitude),7));
      double Sin2 = 0.0000058 * (roundTo(Math.sin(2*latitude) * Math.sin(2*latitude),7));
         G = 9.780327 *( 1 + Sin1 - Sin2) - temp ;
      return G;
   }

   /**************************************************************************
    * Function: caclulateDeviationkoef - Set precise gravitation acceleration parameter
    * @param Lng_Vp  double   Lng_Vp(Vp)
    * @param Lng_Vp0 double   Lng_Vp0(Vp0)
@Note:  Tested - by JUnit 
    * @return None.
    * ***********************************************************************/
   public double caclulateDeviationkoef(double Lng_Vp, double Lng_Vp0)
   {
      double koef = init;
      koef = (BulletFlight.bullet_K_param * BulletFlight.bullet_turn_speed) / (Lng_Vp * Lng_Vp0);
      return koef;
   }
   /**************************************************************************
    * Function: copyVectors  
    * @param fromVector - Vector<Double> what to copy
    * @param toVector   - Vector<Double> where to copy
    * @return None.
    * ***********************************************************************/
   private void copyVectors(Vector<Double> fromVector, Vector<Double> toVector)
   {
      if( (null != fromVector) && (null != toVector))
      {
         if(fromVector.size() == toVector.size())
         {
            for(int i = 0; i < toVector.size();i++)
            {
               toVector.set(i, fromVector.get(i));
            }
         }
      }
   }
   
  /**************************************************************************
   * Function: VP  - vector multiplicate with vector
   * @param vect1 Double 1 - Vp
   * @param vect2 Double 2 - Vb
   * @param result Double result
@Test: Tested - by JUnit
   * @return None.
   * ***********************************************************************/
   public void VP (Vector<Double> vect1, Vector<Double> vect2 ,Vector<Double> result)
   {
      if ((vect1.size() == 3) && (vect2.size() == 3))
      {
         for(int i=0; i < result.size(); i++)
         {
            double temp = 0;
            if (i == X)
               { temp = ( (vect1.get(Y) * vect2.get(Z)) - (vect1.get(Z) * vect2.get(Y))); } 
            else if (i == Y)
               { temp = ( (vect1.get(Z) * vect2.get(X)) - (vect1.get(X) * vect2.get(Z))); } 
            else if (i == Z)
               { temp = ( (vect1.get(X) * vect2.get(Y)) - (vect1.get(Y) * vect2.get(X))); }
            else
               { Log.e(LOG,"F:[VP] -> ERROR on vectors size"); }
            result.set(i, temp);
         } 
      }
      else
      {
         Log.e(LOG,"F:[VP] -> vectors has wrong length");
      }
   }
  /**************************************************************************
   * Function: vectorAngle  - calculate angle between vectors
   * @param Vector<Double> vect1
   * @param Vector<Double> vect2
   * @return double Angle in radians.
   * @Note: 1 оb/sec=6.28 rad/sec also see const value oneTurnInRad
   * ***********************************************************************/
   @SuppressWarnings("unused")
   private static double vectorAngle (Vector<Double> vect1, Vector<Double> vect2)
   {
      double sin_angle = 0; double AB   = 0;
      double angle     = 0; double modA = 0;
      double temp      = 0; double modB = 0;
      
      if ((vect1.size() == 3) && (vect2.size() == 3))
      {
         AB = (vect1.get(0) * vect2.get(0)) + (vect1.get(1) * vect2.get(1)) + vect1.get(2) * vect2.get(2);
            temp = Math.pow(vect1.get(0), 2) + Math.pow(vect1.get(1), 2) + Math.pow(vect1.get(2), 2);
         modA = Math.sqrt(temp); 
            temp = 0;
            temp = Math.pow(vect2.get(0), 2) + Math.pow(vect2.get(1), 2) + Math.pow(vect2.get(2), 2);
         modB = Math.sqrt(temp);
   
         angle = AB / (modA * modB);                     // cos(fi)
         sin_angle = Math.sqrt(1 - Math.pow(angle, 2));  // sin(fi)
      }
      else
      {
         Log.e(LOG,"F:[vectorAngle] -> vectors has wrong length");
      }
      return sin_angle;
   }
   
   /**************************************************************************
    * Function: vectorDeduction  - vector deduction of vectors (-)
    * @param Vector<Double> vect1
    * @param Vector<Double> vect2
    * @param Vector<Double> result
    * @return None.
    * ***********************************************************************/
   public void vectorDeduction (Vector<Double> vect1, Vector<Double> vect2, Vector<Double> result)
   {
      if ((vect1.size() == 3) && (vect2.size() == 3))
      {
         for(int i=0; i < result.size(); i++)
         {
            double temp = init;
            temp = ( vect1.get(i) -  vect2.get(i) ); 
            result.set(i, roundTo(temp,roundConst));
         } 
      }
      else
      {
         Log.e(LOG,"F:[VP] -> vectors has wrong length");
      }
   }
   
   /**************************************************************************
    * Function: vectorAddition  - vector addition of vectors (+)
    * @param Vector<Double> vect1
    * @param Vector<Double> vect2
    * @param Vector<Double> result
    * @return None.
    * ***********************************************************************/
   public void vectorAddition (Vector<Double> vect1, Vector<Double> vect2 ,Vector<Double> result)
   {
      if ((vect1.size() == 3) && (vect2.size() == 3))
      {
         for(int i=0; i < result.size(); i++)
         {
            double temp = init;
            temp = ( vect1.get(i) +  vect2.get(i) ); 
            result.set(i, roundTo(temp,roundConst));
         } 
      }
      else
      {
         Log.e(LOG,"F:[VP] -> vectors has wrong length");
      }
   }
   
   /**************************************************************************
    * Function: Lng  - calculate vectors...
    * @param Vector<Double> vect
@Note: Tested JUnit
    * @return (double)  
    * ***********************************************************************/
   public double Lng(Vector<Double> vect)
   {
      double result  = init;
      double temp    = init;  
      for(int i=0; i < vect.size(); i++)
      {
         temp = init;
         temp = vect.get(i);
         result += Math.pow(temp, 2);
      }
      result = Math.sqrt(result);
      return roundTo(result,11);
   }
   
   /**************************************************************************
    * Function: Mlt  - multiply vector on some value
    * @param vect   - Vector<Double> 
    * @param value  - double 
    * @param result - Vector<Double> 
@Note: Tested - JUnit
    * @return (double)  None.
    * ***********************************************************************/
   public void  Mlt(Vector<Double> vect, double value ,Vector<Double> result)
   {
      if (vect.size() == result.size())
      {
         for(int i=0; i < vect.size(); i++)
         {
            Double temp = vect.get(i);
            temp = temp * value;
            result.set(i, roundTo(temp,roundConst));
         }
      }
      else
      {
         Log.e(LOG,"F:[Mlt] -> Both vectors must be initialized");
      }
   }

   /**************************************************************************
    * Function: setWindInfluence - use to set wind influence vector. For now
    *    algorithm will be quit simle. North-north, south-south directions are
    *    ignored. East-east and west-west - full strength. All others - only
    *    half of strength. Also multiplied by -1, if dirrection is reversed. 
    * @param (None).
    * @return (None).
    * ***********************************************************************/
   public void setWindInfluence()
   {
      /*
            50%        0%      50%
                  325  0  45       
                     \ | /        
           100%  270 - * - 90 100%
                     / | \ 
                 225  180 135
            50%        0%      50%
           
      */
      double y_result = 0;
      switch(Parameters.windDirection)
      {
         case EAST_EAST:
            y_result = Parameters.windStrength;
            break;
         case WEST_WEST:
            y_result = (-1) * Parameters.windStrength;
            break;
            
         case NORTH_EAST:
         case SOUTH_EAST:
            y_result = Parameters.windStrength/2;
            break;
            
         case NORTH_WEST:
         case SOUTH_WEST:
            y_result = Parameters.windStrength/2 * (-1);
            break;
            
         case NORTH_NORTH:
         case SOUTH_SOUTH:
         default:
            y_result = 0;
            break;
      }
      
      BulletFlight.Vwind.set(X,init); 
      BulletFlight.Vwind.set(Y,y_result);
      BulletFlight.Vwind.set(Z,init);
   }
                                    /* MAIN CALCULATION FUNCTIONS */
   /**************************************************************************
    * Function: windInfluence
    * @param bulletFlightTime   (double )
    * @param lenghtOfTrajectory (double ) 
    * @param bulletSpeed        (double )  
    * @param windSpeed          (double ) 
    * @return result (double)  
    * ***********************************************************************/
   public double windInfluence(double bulletFlightTime, 
                               double lenghtOfTrajectory, 
                               double bulletSpeed, 
                               double windSpeed)
   {
      double result = init;
        result = (bulletFlightTime - lenghtOfTrajectory / bulletSpeed) * windSpeed;
      return result;
   }
   
   /**************************************************************************
    * Function: caclulateBulletEnergy
    * @param (double ) BulletMass
    * @param (double ) MainVelocity 
      @Note: Tested - JUnit
    * @return (double) Energy = (m*v^2)/2 
    * ***********************************************************************/
   public double caclulateBulletEnergy(double bulletMass, double mainVelocity)
   {
      double energy = init;
         energy = (bulletMass * Math.pow(mainVelocity, 2))/2000;
      return roundTo(energy,roundConst);
   }
   
   /**************************************************************************
    * Function: roundTo
    * @param (double )- value that must be round
    * @param (int ) x - number of after dot digits
      @Note: Tested - [ OK ]
    * @return (double)- value that was rounded 
    * ***********************************************************************/
   public double roundTo(double value , int x)
   {
      double result = init;
         result = (Math.round(value* Math.pow(10,x)) / Math.pow(10,x));
      return result;
   }
   
   /***********************************************************************************************
    * Function: getSoundSpeedInAir
    * @param (double ) currentTemperature - Celsius tempetature 
    * @param (double ) pressure (kPa)
    * @param (double ) humidity (0-100%)
    * @return (double) 
    * @Note:  range 0 to 30 ° C (273.15 - 303.15 K) and for the pressure range 75 - 102 kPa
    *         http://resource.npl.co.uk/acoustics/techguides/speedair/
    *         view-source:http://resource.npl.co.uk/acoustics/techguides/speedair/
    * ********************************************************************************************/
   public double getSoundSpeedInAir(double currentTemperature, double pressure, double humidity)
   {
      double soundSpeed = init;
      double T = 0;
      if ( (humidity > 100.0) || (humidity < 0.0) )
      {
         Log.e(LOG,"Himidity param has error! It will be set to 50%");
         humidity = 50.0;
      }
      T = currentTemperature + T0;
      
      // 101.33 * 1000.0 -> must be
      pressure = pressure * 1000.0 * 1000.0;  // this is future bug :)
                           //Molecular concentration of water vapor calculated from pressure
                           //using Giacomos method by Davis (1991) as implemented in DTU report 11b-1997
      double ENH = (3.14) * Math.pow(10,-8) * pressure + (1.00062) + Math.pow(currentTemperature,2) * (5.6) * Math.pow(10,-7);
                        //These commented lines correspond to values used in Cramer (Appendix)
      double PSV1 = Math.pow(T,2) * (1.2378847) * Math.pow(10,-5) -(1.9121316) * Math.pow(10,-2) * T;  
                                                               //PSV1 = Math.pow(T,2) * 1.2811805 * Math.pow(10,-5) - 1.9509874*Math.pow(10,-2) * T;
      double PSV2 = (33.93711047) - (6.3431645) * Math.pow(10,3) / T;                                      
                                                               //PSV2 = 34.04926034 - 6.3536311 * Math.pow(10,3) / T;
      double PSV  = Math.pow(e,PSV1) * Math.pow(e,PSV2);
      double H    = humidity * ENH * PSV/pressure;
      double Xw   = H / 100.0;
      double Xc   = (400.0) * Math.pow(10,-6); 
                                                               //Xc = 314.0 * Math.pow(10,-6);
                                                               //Speed calculated using the method of Cramer from
                                                               //JASA vol 93 pg 2510
      double sqrT = Math.pow(currentTemperature,2); 
      double C1 = (0.603055) * currentTemperature + soundSpeedCalcConst -
                   sqrT * (5.28) * Math.pow(10,-4) + ((0.1495874) * currentTemperature + 
                   (51.471935) - sqrT * (7.82) * Math.pow(10,-4)) * Xw;
      
      double C2 = ((-1.82) * Math.pow(10,-7) + (3.73) * Math.pow(10,-8) * 
                  currentTemperature - sqrT * (2.93) * Math.pow(10,-10)) * 
                  pressure +((-85.20931) - (0.228525) * currentTemperature + 
                  sqrT * (5.91) * Math.pow(10,-5)) * Xc;
      
      double C3 = Math.pow(Xw,2) * (2.835149) + Math.pow(pressure,2)* (2.15) * 
                  Math.pow(10,-13) - Math.pow(Xc,2) * (29.179762) - (4.86) * 
                  Math.pow(10,-4) * Xw * pressure * Xc;
      
      soundSpeed = C1 + C2 - C3;
      soundSpeed = roundTo(soundSpeed,roundSmallConst);
      return soundSpeed;
   }
   
   /**************************************************************************
    * Function: getSoundSpeedInDryAir
    * @param (double ) currentTemperature - Celsius tempetature 
    * @return (double) speed of sound in dry air 
    * @Note 
    * ***********************************************************************/
   public double getSoundSpeedInDryAir(double currentTemperature)
   {
      double soundSpeed = init;
         soundSpeed = soundSpeedCalcConst * Math.sqrt( 1 + (currentTemperature / T0));
      return soundSpeed;
   }
   
   /**************************************************************************
    * Function: calculatePressure
    * @param altitude   - (double) in meters   
    * @return  preasure -(double) in Pa (?) -> may be kPa would be better
    * ***********************************************************************/
   public double calculatePressure(double altitude)
   {
      double pressure = init;
      double temp2    = init;
      double temp1 = 1 - ( (speedOfTemperatureLow * altitude) / T0 );
         if(Parameters.gravityAcceleration == 0)
         {
            Parameters.gravityAcceleration = constGravityAcceleration;
         }
         temp2 = (Parameters.gravityAcceleration  * molarAirMass) / (universalGasConstant * speedOfTemperatureLow);
         pressure = airPresureOnSeaLevel * Math.pow(temp1,temp2);
         pressure = roundTo(pressure,roundSmallConst); 
      return pressure;
   }
   
   /**************************************************************************
    * Function: calculateDensity - make calculations for dry air 
    * @param (double) pressure (hPa)
    * @param (double) altitude  
    * @return (double) density (kg/m^3)
    * ***********************************************************************/
   public double calculateDensityDryAir(double pressure, double temperature)
   {
      double density = init;
         pressure = roundTo(pressure * 1000,2); // must be in pa
         density  = pressure / (R_constantDryAir * (T0 + temperature)); 
         density = density * 100; // return in kg/m^3
         density  = roundTo(density,4);
      return density;
   }
   
   /**************************************************************************
    * Function: calculateDensity - make calculations for moist air, not dry
       double T = T0 - (speedOfTemperatureLow * altitude);
       density = (pressure * molarAirMass) / (universalGasConstant * T);
       density = roundTo((density * 100),3); // return in kg/m^3
       pressure = (pressure*1000); // must be in pa
    * @param (double) pressure (hPa)
    * @param (double) altitude  
    * @return (double) density (kg/m^3)
    * ***********************************************************************/
   public double calculateDensity(double pressure, double temperature, double humidity)
   {
      double density = init;
      double Psat = (6.0178 * Math.pow(10, (7.5 * (temperature + T0) - 2048.625) /
                                                      (temperature + T0 - 35.85)));
      double Pv = (humidity /100 * Psat);
      double Pd = ((pressure - Pv)/(287.058 * ( temperature + T0)));
         
         Pv = (Pv/(461.495 *(temperature + T0)));
         density = (Pd + Pv); 
         density *= 100.0;
         density = roundTo(density,roundSmallConst);
      return density;
   }
  
   /**************************************************************************
    * Function: convertionInchesToMeters
    * @param (double) inch
    * @return (double) meters
    * ***********************************************************************/
   public double convertionInchesToMeters(double inch)
   {
      double meters = init;
         meters = (inch * oneInchPerMeter);
         meters = roundTo(meters,roundSmallConst);
      return meters;
   }
   
   /**************************************************************************
    * Function: convertionInchesToMeters
    * @param (double) inch
    * @return (double) meters
    * ***********************************************************************/
   public double convertionMetersToInches(double meter)
   {
      double inch = init;
         inch = (meter / oneInchesInMeter); 
         inch = roundTo(inch,roundSmallConst);
      return inch;
   }
   
   
   /**************************************************************************
    * Function: convertionBarometerToPasal
    * @param (double) mm in barometer
    * @return (double)  kPa
    * ***********************************************************************/
   public double convertionBarometerToPasal(double barometer)
   {
      double kPa = init;
      double koef = 133.3;
         kPa = barometer * koef;
         kPa = roundTo(kPa,roundSmallConst);
      return kPa;
   }

   /**************************************************************************
    * Function: convertionCelciumTOFarenhait
    * @param (double) F
@Note: Tested - [ OK ]
    * @return (double)  temperature
    * ***********************************************************************/
   public double convertionFarenheitToCelcium(double F)
   {
      double temperature = init;
      double koef = 1.8000;
         temperature = (F - 32.0)/koef;
         temperature = roundTo(temperature,roundSmallConst);
      return temperature;
   }

   /**************************************************************************
    * Function: convertionCelciumTOFarenhait
    * @param (double) C
@Note: Tested - [ OK ]
    * @return (double)  temperature
    * ***********************************************************************/
   public double convertionCelciumToFarenheit(double C)
   {
      double temperature = init;
      double koef = 1.8000;
         temperature = (Math.abs(C) * koef) + 32.0;
         temperature = roundTo(temperature,roundSmallConst);
      return temperature;
   }
   
   /**************************************************************************
    * Function: convertionFpsToMetersPerSec
    * @param (double) fps
    * @return (double) Meters per sec
    * ***********************************************************************/
   public double convertionFpsToMetersPerSec(double fps)
   {
      double metersPerSec = init;
      double oneMeterPerSecInFPS = 3.28;
         metersPerSec = (fps/oneMeterPerSecInFPS);
      return metersPerSec;
   }
   
   /**************************************************************************
    * Function: convertionGrainsToGrams
    * @param (double) grain
@Note: Tested - [ OK ]
    * @return (double) gramm
    * ***********************************************************************/
   public double convertionGrainsToGrams(double grain)
   {
      double gramm = init;
      double oneGrainToMilligram = 15.432;      
         gramm = (grain * oneGrainToMilligram); 
         gramm = roundTo(gramm,4);
      return gramm;
   }
   /**************************************************************************
    * Function: convertionGramsToGrains
    * @param (double) grams
@Note: Tested - [ OK ]
    * @return (double) grans
    * ***********************************************************************/
   public double convertionGramsToGrains(double grams)
   {
      double grains = init;
      double oneGrainToMilligram = 15.432;
         grains = (grams / oneGrainToMilligram); 
         grains = roundTo(grains,4);
      return grains;
   }
   
   /**************************************************************************
    * Function: getRangeInYards
    * @param (double) targetSize in inches
    * @param (double) sizeInMiles
    * @return (double) Range in yards
    * ***********************************************************************/
   public double getRangeInYards(double targetSize, double sizeInMiles)
   {
      double range = init;
         range = (targetSize * yardConstant) / sizeInMiles;
         range = roundTo(range,roundSmallConst);
      return range;
   }
   
   /**************************************************************************
    * Function: getMilsFromYardDistance - Question for what scope magnification? 
    * @param targetSize  - (double)in inches
    * @param rangeInYard - (double) yards 
    * @return (double) sizeInMiles in yards
    * ***********************************************************************/
   public double getMilsFromYardDistance(double targetSize, double rangeInYard)
   {
      double sizeInMiles = init;
         sizeInMiles = (targetSize * yardConstant) / rangeInYard;
         sizeInMiles = roundTo(sizeInMiles,roundSmallConst);
      return sizeInMiles;
   }
   
   /**************************************************************************
    * Function: getRangeInMeters
    * @param (double) targetSize in meters
    * @param (double) sizeInMiles
    * @return (double) Range in yards
    * ***********************************************************************/
   public double getRangeInMeters(double targetSize, double sizeInMiles)
   {
      double range = init;
         range = (targetSize * meterConstant) / sizeInMiles;
         if(range < 0.001)
         {
            range = init;
         }
         else
         {
            range = roundTo(range,roundSmallConst);
         }
      return range;
   }
   
   /**************************************************************************
   * Function: getMilsFromMetersDistance
   * @param  targetSize    - (double) in meters
   * @param  rangeInMeters - (double) sizeInMiles
   * @return (double) mils
   * ***********************************************************************/
  public double getMilsFromMetersDistance(double targetSize, double rangeInMeters)
  {
     Double sizeInMiles = init;
        sizeInMiles = (targetSize * 1000) / rangeInMeters;
       
        if(sizeInMiles < 0.0001 || sizeInMiles.isInfinite() || sizeInMiles.isNaN() || sizeInMiles > 50)
        {
           sizeInMiles = init;
        }
        else
        {
           sizeInMiles = roundTo(sizeInMiles,roundSmallConst);
        }
     return sizeInMiles;
  }
   
  /**************************************************************************
   * Function: getSantimetersDeviationPerClickSet - TODO: getSantimetersDeviationPerClickSet
   * @param rangeInMeters (double) 
   * @param clickOnScope  (int)
   * @param isNegative    (boolean) 
   * @param clickPrice    (double) 
   * @return sizeInSantimeters (double)
   * ***********************************************************************/
  public double getMetricDeviationPerClickSet(double  rangeInMeters, 
                                              int     clickOnScope,
                                              double  clickPrice,
                                              boolean returnInMeters)
  {
     double sizeInSantimeters = init;
                    //            4            0.25               2.9089   
        sizeInSantimeters = (clickOnScope * clickPrice) * oneMOAonHundredMeters;
        if (false == returnInMeters)
        {
           sizeInSantimeters *= (rangeInMeters/100);
        }
     return sizeInSantimeters;
  }
  
  /**************************************************************************
   * Function: getMOAFromMils
   * @param mils  - double 
   * @return dRet - double
   * ***********************************************************************/
  public double getMOAFromMils(double  mils)
  {
     double dRet = init;
            dRet = ( mils * oneMillToMOA );
            if(50 > dRet)
            {
               dRet = init;
            }
     return dRet;
  }
   /**************************************************************************
    * Function: getAmmoAcceleration -
    * @param R_function used MAX speed
    * @param ballistikKoeff - [ballistik koeff.]
    * @param soundSpeed     - meters per seconds 
    * @param dencity        - плотность воздуха 
    * @return (double) Meters per sec
    * @ Note: table R for Max values in feets // K0 = ~142859.6748596518
    * ***********************************************************************/
   public double getAmmoAcceleration(double R_function, 
                                     double ballistikKoeff, 
                                     double soundSpeed, 
                                     double dencity)
   {
      double ammoAcceleration = init, K1 = init;
       //[ ~OK  ] dencity & airPresureOnSeaLevel должна быть одной размерность  
        ammoAcceleration = R_function / ballistikKoeff;
            K1 = dencity * Math.pow(soundSpeed,2);                       // current
      //double C  = (K1)/(K0);
         ammoAcceleration *= (K1)/(K0);
         ammoAcceleration = roundTo(ammoAcceleration,roundConst);
      return ammoAcceleration;
   }
   // K1 = 1209.00 * (341.5 * 341.5) = 140996300.25
   /**************************************************************************
   * Function: getR_Function - R_function used MAX speed
   * @param MAX    - [bulletSpeed / soundSpeed]
   * @return (double) R koef.
   * @ Note: table R for Max values in feets
   * ***********************************************************************/
   public double getR_Function(double MAX, double W1Const)
   {
      double R = init;
                                          // this data for speed in MAX
      double temp = MAX * W1Const;
      if (MAX < 0.74973) 
         { R = 74.422 / Math.pow(10, 8) *  Math.pow(temp, 1.6);   }
      else if ((MAX >= 0.74973) && (MAX < 0.92823))
         { R = 59.939 / Math.pow(10, 12) * Math.pow(temp, 3);     }
      else if ((MAX >= 0.92823) && (MAX < 1.06211))
         { R = 23.385 / Math.pow(10, 22) * Math.pow(temp, 6.45);  }
      else if ((MAX >= 1.06211) && (MAX < 1.30310)) 
         { R = 95.408 / Math.pow(10, 12) * Math.pow(temp, 3);     }
      else if ((MAX >= 1.30310) && (MAX < 1.78507)) 
         { R = 59.814 / Math.pow(10, 8) *  Math.pow(temp, 1.8);   }
      else if ((MAX >= 1.78507) && (MAX < 2.32059)) 
         { R = 58.495 / Math.pow(10, 7) *  Math.pow(temp, 1.5);   }
      else if ((MAX >= 2.32059) && (MAX < 3.57013)) 
         { R = 15.366 / Math.pow(10, 7) *  Math.pow(temp, 1.67);  }
      
      if(R == 0)
      {
         Log.e(LOG,"Warning! R function returns 0!");
      }
      else
      { //Фут/с - это 0.3048 м/с
         R = R * 304.8;// переворачиваем футы в метры
         R = roundTo(R,roundConst);
      }
      return R;
   }
   /**************************************************************************
    * Function: getG1AccelerationKoef  - other varian of getAmmoAcceleration function,
    * but this one can be used if no ballistik koef. is available
    * @param double currentDencity
    * @return double - bullet acceleration
    * @Note may not be needed 
    * ***********************************************************************/
// G1 - понятно что, это ускорение по соотв. мод. 
// Cx(M) - коэффициент лобового сопротивления; в скобках М - значит, зависит он от числа Маха (скорость пули делить на скорость звука при даной температуре). Зависимость Cx(M) стабильна, не меняется при любых плотностях воздуха и скоростях звука.
// S-площадь поперечного сечения пули (и ствола).
// P - плотность воздуха
// а - скорость звука при даной температуре.
// V - скорость пули в м/с
// М - скорость пули в Махах, М=V/а
// m - масса пули
// G1=(0,5*P*S*Cx(M)*V*V)/m
// V = (V/a)*a = M*a; (V^2) = (M^2)*(a^2)
// G1=[(P*S*a*a)/(2*m)]*Cx(M)*(M^2)
   public double getG1AccelerationKoef(double sectional_bullets)
   {
      double A_WithkoefG1 = init;
      
      double S  = sectional_bullets;          // площадь поперечного сечения пули (и ствола).
      double P  = Parameters.density;         // плотность воздуха 
      double a  = Parameters.soundSpeed;      // скорость звука при даной температуре.
      double V  = BulletFlight.mainVelocity;  // скорость пули в м/с
      double M  = BulletFlight.MAXSoundSpeed; // скорость пули в Махах, М=V/а
      double m  = BulletFlight.mass;          // масса пули
      double Cx = init;                          // Cx(M) - коэффициент лобового сопротивления;
      
      double K0 = (airPresureOnSeaLevel * Math.pow(constSoundSpeed,2)); 
      double K1 = (P * Math.pow(a,2));                     // current
      double C  = (K1/K0);
      
         Cx = forceOfHeadResistance(M,V,a);
         M = V/a;
         A_WithkoefG1 = ( P * S * Math.pow(a,2) / (2*m) ) * Cx * Math.pow(M,2);
         A_WithkoefG1 = A_WithkoefG1 * C; // ускорение пули для данной атмосферы и скорости пули.
         //A = (К1/К0)*R_funktion(V/С)/bk;
      return A_WithkoefG1;
   }
   
/*************************************************************************************************
    * Function: forceOfHeadResistance() - McCoy function for getG1AccelerationKoef() function
    * @param double ammoSpeed
    * @param double speedOfSound
    * @return (double) Cx
    * @Note: this function is for precise calculations
    * http://www.jbmballistics.com/cgi-bin/jbmdrag-5.1.cgi - this is for test 
***** *******************************************************************************************/
   public double forceOfHeadResistance(double MAX, double ammoSpeed, double speedOfSound)
   {
      double CD0  = init;
      double Max  = init;
            
      if(0 == MAX)
      {   
         Max = ( ammoSpeed / speedOfSound );
      }
      else
      {
         Max = MAX;
      }
      double M2  = Math.pow(Max,2);
      double M21 = (M2 - 1);
      double M4  = Math.pow(Max,4);
      
      /* NP - носовой поток может быть Ламинарный (L) или Турбулентный (T) 
         Если Dиаметр < 20mm, то использовать Tурбублентный */

     
//     
//      bullet.setBulletInfoParams(BulletSpecifications.bulletDiametr, 
//                                 BulletSpecifications.bulletNoseDiameter, 
//                                 BulletSpecifications.bulletMainBeltDiameter, 
//                                 BulletSpecifications.bulletLengthsOfBoatTail, 
//                                 BulletSpecifications.bulletNoseLength, 
//                                 BulletSpecifications.bulletTailLength, 
//                                 BulletSpecifications.bulletRadiusOjivalo,
//                                 BulletSpecifications.bulletTailDiameter);
      
      bullet.calculatePreParametersOfMcCoy();
      /*************************************** Example *****************************/
      
//            800 М/С Cx = 0,342349055954777 
//            700 М/С Cx = 0,368590977950188 
//            600 М/С Cx = 0,39683917914464 
//            500 М/С Cx = 0,427074153647378 
//            400 М/С Cx = 0,452374337771593 
//            300 М/С Cx = 0,234259807166892 
//            200 М/С Cx = 0,228856858123614

      /** Koef. settings *****************************************************/

      /* FALSE if T air flow d>20mm, else if L = TRUE */
      boolean NP = (bullet.bulletDiametr > 20 ? false : true); 
      
      double Lmx = bullet.bulletLengthsOfBoatTail  / bullet.bulletDiametr;
      double Lmk = bullet.bulletTotalLengs         / bullet.bulletDiametr;
      double Lmg = bullet.bulletNoseLength         /  bullet.bulletDiametr;
      double DgD = bullet.bulletNoseDiameter       / bullet.bulletDiametr;
      double DxD = bullet.bulletDiameterOfBoatTail / bullet.bulletDiametr; // диаметр поской носовой части пули
      double DpD = bullet.bulletMainBeltDiameter   / bullet.bulletDiametr;
      double tay = (bullet.bulletDiametr - bullet.bulletNoseDiameter ) / bullet.bulletNoseLength;
      
      //Радиус тангенциального оживала
       double RtR  = bullet.RtR;
      
      /***************************************************************/
      /** Rem. Koef. air resistance of bullet tail *******************/
      double PB4  = init;
      double PB2  = init;
      double PBP1 = init;
      double CDB  = init;
      
      PB4 = (1 + 0.09 * (M2) * (1 - Math.exp(Lmg - Lmk))) * (1 + 0.25 * (M2) * (1 - DxD));
      if ( Max < 1 )
      {
         PB2 = 1 / (1 + 0.1875 * (M2) + 0.0531 * (M4));
      }
      else
      {
         PB2 = 1 / (1 + 0.2477 * (M2) + 0.0345 * (M4));
      }
      PBP1 = PB2 * PB4;
      CDB = 1.4286 * (1 - PBP1) * DxD * DxD / (M2);
      
      /***************************************************************/
      /** Rem. Koef. air resistance rubbing **************************/
      double Re = speedOfSound * Max * bullet.bulletTotalLengs / kinematicAirViscosity;
      double Ret = 0.4343 * Math.log(Re);
      double DUM = 1 + (0.3333 + 0.02 * (1/(Lmg*Lmg))) * RtR;
      double SWCyl = 3.1416 * (Lmk - Lmg);
      double SWN = 1.5708 * Lmg * DUM * (1 + 0.125 * (1/(Lmg*Lmg)));
      
      double Cfl = 1.328 * (1 + 0.12 * Math.pow( (M2),(-0.12))) / Math.sqrt(Re);
      double Cft = 0.455 * (1 + 0.21 * Math.pow( (M2),(-0.32))) / Math.pow(Ret,(2.58));
      
      double Cdsfn = init; 
      if (true == NP)  // L
         { Cdsfn = Cfl * SWN; }
      else            // T
         { Cdsfn = Cft * SWN; }
      double CDSF = (Cdsfn + Cft * SWCyl) * 1.2732;
      /***************************************************************/
      /** Rem. Koef. air resistance ojivala **************************/
      double CDHT = 0;
      double SM21 = 0;
      
      if ( Max >= 1) 
         { SM21 = Math.sqrt(M21); }
      
      double Mn1 = 1 / Math.sqrt(1 + 0.552 * (Math.pow(tay,(0.8))));
      double Mn2 = 1 + 0.368 * (Math.pow(tay,(1.85)));
      double CHI = M21 / (2.4 * M2);
      double C1 = 0.7156 - 0.5313 * RtR + 0.595 * RtR * RtR;
      double C2 = 0.0796 + 0.0779 * RtR;
      double C3 = 1.587 + 0.049 * RtR;
      double C4 = 0.1122 + 0.1658 * RtR;
      double Kfc1 = C1 - C2 * tay * tay;
      double Kfc2 = C3 + C4 * tay;
      
      if (Max < Mn1) 
         { CDHT = 0; }
      
      if ((Max > Mn1) && (Max <= 1)) 
         {  CDHT = 0.368 * Math.pow(tay,1.8) + 1.6 * tay * CHI; }
      
      if( (Max > 1) && (Max < Mn2) )
      {
         double temp = tay * Math.sqrt(Mn2 * Mn2 - 1);
         CDHT = Kfc1 * (Math.pow(temp,Kfc2)) / (Mn2 * Mn2 - 1);
      }
      
      if ( Max >= Mn2)
         { CDHT = Kfc1 * Math.pow((tay * SM21),Kfc2) / M21; }
      
      /***************************************************************/
      /** Rem. Koef. air resistance of flat nose part ****************/
      double PTP  = init;
      double CDHM = init;
      if (Max <= 1)
      {
         PTP = Math.pow((1 + 0.2 * M2),(3.5));
      }
      else
      {
         PTP = Math.pow((1.2 * M2),(3.5)) * (6 / Math.pow( (7 * M2 - 1), (2.5) ) );   
      }
      
      double Cmep = 1.122 * (PTP - 1) * DgD * DgD / M2;
      if (Max <= 0.91)
      { 
         CDHM = 0;
      }
      if ( (Max > 0.91) && (Max <= 1.41)) 
      {
         CDHM = (0.254 + 2.28 * CHI) * Cmep;
      }
      if (Max >= 1.41) 
      { 
         CDHM = 0.85 * Cmep; 
      }
      /***************************************************************/
      /** Rem. Koef. air resistance of belt **************************/
      double CDBND = init;
      if (Max < 0.95) 
      {
         CDBND = Math.pow(Max,(12.5)) * (DpD - 1);
      }
      else
      {
         CDBND = (0.21 + 0.28 / M2) * (DpD - 1);
      }
      /***************************************************************/
      /** Rem. Koef. air resistance of conical tail part *************/
      double CDBT   = init;
      double TB     = init;
      double TB23   = init;
      double BBT    = init;
      
      if (Lmx == 0) 
      {
         CDBT = 0;
      }
      
      if (Lmx > 0)
      {
         TB = (1 - DxD) / (2 * Lmx);
         TB23 = 2 * TB * TB + TB * TB * TB;
         BBT = 1 - Math.exp(-2 * Lmx) + 2 * TB * ((Math.exp(-2 * Lmx)) * (Lmx + 0.5) - 0.5);
         
         if ( Max <= 0.85 ) 
         {
            CDBT = 0;
         }
         if ((Max > 0.85) && (Max <= 1)) 
         { 
            CDBT = 2 * TB23 * BBT / (0.564 + 1250 * CHI * CHI);   
         }
         if ((Max > 1) && (Max <= 1.1))
         {
            CDBT = 2 * TB23 * BBT * (1.774 - 9.3 * CHI);
         }
         if (Max > 1.1)
         {
            double AA2 = 5 * tay / (6 * SM21) + 0.25 * tay * tay - 0.7435 * (Math.pow(tay,1.6)) / Math.pow(Max,0.4);
            double AA1 = (1 - 0.6 * RtR / Max) * AA2;
            double EXL = Math.exp(-1.1952 * (Lmk - Lmg - Lmx) / Max);
            double XXM = (1.2 * M4 - 2 * M21) * TB * TB / (M21 * M21);
            double AA = AA1 * EXL - XXM + 2 * TB / SM21;
            double RB = SM21 / 0.85;
            double EXBT = Math.exp(-0.85 * Lmx / SM21);
            double AAB = 1 - EXBT + (2 * TB * (EXBT * (Lmx + RB) - RB));
            CDBT = 4 * AA * TB * AAB * RB;      
         }
      }
      /** Rem. force Of head resistance!******************************/
      if (CDHT  < 0 )  {CDHT  = 0;}
      if (CDHM  < 0 )  {CDHM  = 0;}
      if (CDSF  < 0 )  {CDSF  = 0;}
      if (CDBND < 0 )  {CDBND = 0;}
      if (CDBT  < 0 )  {CDBT  = 0;}
      if (CDB   < 0 )  {CDB   = 0;}

      CD0 = CDHT + CDHM + CDSF + CDB + CDBND + CDBT;
      //Log.e(LOG,"CDHT:" + CDHT + " CDHM:" + CDHM + " CDSF:" + CDSF + " CDB:" + CDB + " CDBND:" + CDBND + " CDBT:" + CDBT);
      //Log.e(LOG,"CX koef: " + koef + " at ammoSpeed: " + ammoSpeed);
      
      return CD0;
   }

   /**************************************************************************
    * Function: calculateBalisticKoef
    * @param BulletInfo bullet
    * @param double Cb - коэффициента сопротивления аэродинамических сил пули 
    * @param double Cg - коэффициента сопротивления аэродинамических сил стандартной пули (G1)
    * @return (double) Range in yards
    * ***********************************************************************/
    public double calculateBalisticKoef(BulletInfo bullet, double Cb, double Cg)
    {
       double koef = init;
       double SD = bullet.bulletWeight / bullet.ammo_crossSection;
       double i_form_factor = Cb / Cg;  // from G1 or G7
          koef = SD / i_form_factor;
       return koef;
    }
    
    /**************************************************************************
     * Function: calculateBalisticKoef1
     * @param BulletInfo bullet
     * @param double calibre
     * @param double i_form_factor
     * @return (double) Range in yards
     * @Note:  i_form_factor take from G1 or G7
     * ***********************************************************************/
     public double calculateBalisticKoef1(BulletInfo bullet, double calibre, double i_form_factor)
     {
        double koef = init;
        double SD = bullet.bulletWeight / bullet.ammo_crossSection;
           koef = SD / i_form_factor * Math.pow(calibre,2);
        return koef;
     }
       
   /**************************************************************************
    * Function: getAmmoEnergy
    * @param (double) mass of bullet in gram
    * @param (double) speed of ammo in m\sec
    * @return (double) AmmoStrengs energy ammo in J (Joule)
    * @Note: Tested [ OK ]
    * ***********************************************************************/
   public double getAmmoEnergy(double ammoWeight, double ammoSpeed)
   {
      double AmmoEnergy = init;  
         ammoWeight /= 1000; // because must be it Kg 
         AmmoEnergy = ( ammoWeight * Math.pow(ammoSpeed,2) ) / 2;
         AmmoEnergy = roundTo(AmmoEnergy,roundConst);
      return AmmoEnergy;
   }
   
   /**************************************************************************
    * Function: getAmmoStrength
    * @param (double) mass of bullet in kg
    * @param (double) energy ammo in J (Joule)
    * @param (double) speed of ammo in m\sec
    * @return (double) AmmoStrengs
    * ***********************************************************************/
   public double getAmmoStrength(double ammoWeight, double ammoEnergy, double ammoSpeed)
   {
      double AmmoStrengs = init;
         AmmoStrengs = ( ammoWeight * (ammoEnergy * ammoSpeed) ) / 2; //1.96????
      return AmmoStrengs;
   }
   
   /**************************************************************************
    * Function: getAmmoSpeedNearTarget
    * @param (double) ammoStartSpeed - m/sec
    * @param (double) targetDistance - meters (yards?)
    * @return (double) AmmoSpeedNearTarget
    * ***********************************************************************/
   public double getAmmoSpeedNearTarget(double ammoStartSpeed, double targetDistance)
   {
      double AmmoSpeedNearTarget = init;
      if (targetDistance == 0)
      {
         targetDistance = 0.0001;
      }
      AmmoSpeedNearTarget = ( ammoStartSpeed / e * 0.012 * targetDistance);
         //AmmoSpeedNearTarget = ( ammoStartSpeed / 0.012 * targetDistance);
      return AmmoSpeedNearTarget;
   }
   
   /**************************************************************************
    * Function: getAngleGunJumpDuringShot - this will be very positive 
    * update for functionality. But for now it's just 'future' improve.  
    * @param None.
    * @return None. 
    * ***********************************************************************/
   public double getAngleGunJumpDuringShot()
   {
      double Angle = init;
      
      double gunPowderWeight  = init;   //масса пороха
      double bulletWeight     = init;   //масса пули
      double gunWeight        = init;   //масса винтовки
      double gubBarelLenght    = init;  //длина ствола
      double angleH = init; // расстояние "по вертикали" (т.е. вбок на 90 градусов от направления ствола) 
                         //от середины приклада 
                         //(в том месте, где в плечо упирают, конечно же) до центра ствола, 
      double К = init;      //какой-то коэффициент... >> ??? 
         Angle = К * angleH * gubBarelLenght *( (bulletWeight + gunPowderWeight/2 ) / gunWeight);
      return Angle;
   }
   
   /**************************************************************************
    * Function: setTargetDistance
    * @param  None.
    * @return None. 
    * ***********************************************************************/
   public void setTargetDistance(Double dDistance)
   {
      if( null != BulletFlight)
         BulletFlight.targetDistance = dDistance;
   }
   
   /**************************************************************************
    * Function: getTargetDistance
    * @param  None.
    * @return None. 
    * ***********************************************************************/
   public double getTargetDistance()
   {
      double dRet = (600.0); // TODO: Random! 
      if( null != BulletFlight)
      {
         if(0 !=  BulletFlight.targetDistance)
         {
            dRet = BulletFlight.targetDistance;
         }
      }
      return dRet;
   }
   
   /**************************************************************************
    * Function: setMaxTargetDistance - 
    * @param dmaxDistance - Double.
    * @return None. 
    * ***********************************************************************/
   public void setMaxTargetDistance(Double dmaxDistance)
   {
      if( null != BulletFlight)
         BulletFlight.maxDistance = dmaxDistance;
      else
         Log.e(LOG,"[setTargetDistance]");
   }
   
}