
package com.sat.ballistics;

// http://sbc-spb.com/index.php?option=com_content&view=article&id=70:uni&catid=21:2011-01-11-13-35-45&Itemid=31&lang=en
 /*******************************************************************************/
/*              |--------|
           _ _ _|   |    |_ _ _ _ _ _ _ _ _ _ _ _|_     __________________
         _|     |   |    |          |            | \\_
  _ _  _|       |   |    |          |            |    \\_               |
      |         |   |    |          |            |       \\__           |
  Dbt |         |   |    |          |            |        ===== De      Dn
  - -  -        |   dp   |          D            |      _//   |         |
        |_      |   |    |          |            |   _//      |         |
          |     |   |    |          |            | //         |         |
       |   - - -|   |    |- - - - - - - - - - - -|-           |_________|____
       |        |_ _|_ _ |                       |            | 
       |        |        |                       |            |
           Lbt                                        Xgc
                         |       OAL
                         |                  Lr                |
*/                       
/*******************************************************************************/
      
public class BulletInfo
{
     public String bulletName        = "No info"; 
   /** Set parameters **************************************************/
     public double bulletDiametr     = 0; // D
     public double bulletTotalLengs  = 0; // OAL overall length
     /** Bullet mass *****************************************************/
     public boolean isInGrains       = false;
     public double bulletWeight      = 0; // WGT
     /** Geometry of nose ************************************************/
     public double bulletRadius               = 0; // R      bulletRadius = bulletcalibre * bulletDiametr;
     public double bulletRadiusOfTip          = 0; // Rrn
     public double bulletNoseDiameter         = 0; // De  Only one De vs Dmp 
     public double bulletDiameterOfBluntTip   = 0; // Dmp
     public double bulletNoseShoulderDiameter = 0; // Dsh
     public double bulletNoseDiameterOverCircumferentialStep = 0; // Dn  Some bullets has this params, other Dsh
     
     public double bulletDistanceToCenterOfOgiveCurve = 0; // Lr may be the same for bullets
     public double bulletNoseLength                   = 0; // Ln
     public double bulletBevelAngleOfNose             = 0; // B
     public double bulletSecantOgiveCurveAngle        = 0; // Y
     /** Bearing part length */
     public double bulletBearingLength = 0;
     /** Geometry of boat tail ****************************************/
     public double bulletDiameterOfBoatTail        = 0;   // Dbt ~ Dx
     public double bulletLengthsOfBoatTail         = 0;   // Lbt
     public double bulletBevelBoatTailAngle        = 0;   // y
     /** Crimping manner - обжимка ************************************/
     public double bulletMainBeltDiameter                       = 0; // dp
     public double bulletLengthFromCannelureToBase              = 0; // Lc not all has
     public double bulletCannelureWidth                         = 0; // lc
     public double bulletDistanceBetweenTwoCannelures           = 0; // Llc
     public double bulletDiameterOfCircumferentialStep          = 0; // dst
     public double bulletDistanceFromCircumStepAndBulletBase    = 0; // Lst
     public double bulletAngleOfCircumferentialStep             = 0; // Ф
     public double bulletDistanceFromCircumKnurlingToBulletBase = 0; // Lk
     public double bulletDistanceFrompunchingPointToBulletBase  = 0; // Lp
     /*************************************************************************/
     public double bulletDeviationKoefficient  = 0; //0.005
     public double bulletBallisticsKoefficient = 0;
     /*************************************************************************/
     public String bulletFormOfBase    = null;
     public String bulletMaterials     = null;
     public String bulletColorMarking  = null;
     /** Calculate parameter **************************************************/
     public double bulletCalibre         = 0;
     public double difDiameter           = 0;
     public double radiusTang            = 0;
     public double radiusOjivInCalibre   = 0; // R_O
     public double RtR                   = 0;
     /*************************************************************************/    
     public double ammo_startSpeed       = 0;
     public double ammo_speedAtXyards    = 0;
     public double bullet_startDistance  = 0;
     public double bullet_XyardDistance  = 0;
     public double ammo_crossSection     = 0;  /* Поперечное сечение d^2 */
     
     public double bullet_W1Const        = 0; /* 1120.4068 */
     public double bullet_turn_speed     = 0; /*  3504.166 */
     public double bullet_K_param        = 0;
     /*************************************************************************/
     
     /* Способность пули сохранять скорость вопреки сопротивлению воздуха. Существует 
      * (G1.1, G5.1, G6.1, G7.1, GS, RA4, GL, GI) большое колличество мат моделей. Могут попадаться
      * расширенные модели. Однако за основу взяли G1 */
     
     /* Для конической части пули - R стремится в бесконечность*/
     
    /**************************************************************************
     * Constructor: setBulletInfoParams() 
     * ***********************************************************************/
     public BulletInfo()
     {
        //TODO: must provide correct initialization
     }
     
     /**************************************************************************
       * Function: calculatePreParametersOfMcCoy() 
       * 
       * @return (void) 
       * @Note:
       * ***********************************************************************/
//        this.bulletDiametr           = bulletDiametr;            //D
//        this.bulletNoseDiameter      = bulletNoseDiameter;       //Dg
//        this.bulletMainBeltDiameter  = bulletMainBeltDiameter;   //Dp
//        this.bulletTotalLengs        = bulletTotalLengs;         //Xk
//        this.bulletNoseLength        = bulletNoseLength;         //Xgc
//        this.bulletLengthsOfBoatTail = bulletTailLength;         //Xxvx
//        this.radiusOjivInCalibre     = bulletRadius;             //R_O
//        this.bulletDiameterOfBoatTail = Dx;                     //Dx Диаметрт дна пули
//        
     public void calculatePreParametersOfMcCoy()
     {
        /** Some data sheets has no parameters... It's not an error. Conversion must be done */
        if (0 == this.bulletLengthsOfBoatTail)
        {
           this.bulletDiameterOfBoatTail = this.bulletDiametr;
        }
        else if (this.bulletNoseDiameter == this.bulletDiametr)
        {
           this.bulletLengthsOfBoatTail = 0; 
        }

        /* Вариант вычисления радиуса*/
        if(0 == this.bulletRadius)
        {
           if( (0 != this.radiusOjivInCalibre) && 
               (0 != this.bulletDiametr)           )
           {
              this.bulletRadius = (this.radiusOjivInCalibre * this.bulletDiametr);
           }
        }
        
        /* Вычисляем радиус тангенциального оживала */
        if (this.RtR == 0)
        {
           double DifD     = (this.bulletDiametr - this.bulletNoseDiameter);
           this.radiusTang = ( Math.pow((this.bulletNoseLength / DifD),2) + 0.25) * this.bulletDiametr;
           this.RtR        = this.radiusTang / this.bulletRadius; //'Для конической головной части равно нулю
        }
     }
        
     /**************************************************************************
      * Function: calculateBKfromSpeed() 
      * @param double speed_point_1 for example 100m
      * @param double speed_point_2             400m
      * @param double distance_point_1          850m/s
      * @param double distance_point_2          750m/s
      * @return (double) result - ballistic koeff
      * @Note:
      * ***********************************************************************/
      public double calculateBKfromSpeed(double speed_point_1, double speed_point_2,
                                         double distance_point_1, double distance_point_2)
      {
         double result = 0;
         double koeff  = (0.0052834); 
         
         double Distance       = distance_point_2 - distance_point_1;
         double sqrtSpeedParam = Math.sqrt(speed_point_1) - Math.sqrt(speed_point_2);
            result = Math.round( ((Distance * koeff) / (sqrtSpeedParam * 1000 )) / 1000 );
         return result;
      }
      
      /**************************************************************************
       * Function: aeroDinamicKoeff_Cd() 
       * @param double speed_point_1 for example 100m
       * @param double speed_point_2             400m
       * @param double distance_point_1          850m/s
       * @param double distance_point_2          750m/s
       * @param double mass                      10 kg
       * @param double ammo_crossSection         0.024 m2 
       * @return (double) result - // аэродинамический безразмерный коэффициент
       * @Note:
       * ***********************************************************************/
       public double aeroDinamicKoeff_Cd(double speed_point_1, double speed_point_2,
                                          double distance_point_1, double distance_point_2,
                                          double mass, double ammo_crossSection)
       {
          double result = 0;
          double ro_air_density  = (1.225); 
          double Distance = (distance_point_2 - distance_point_1);
          
          double r1 = (-Math.log(1-speed_point_1))/speed_point_1;
          double r2 = (-Math.log(1-speed_point_2))/speed_point_2;
             result = ( (2 * (r1 - r2)) / (Distance *  ro_air_density) )  * ( mass / ammo_crossSection);  
          return result;
       }
}