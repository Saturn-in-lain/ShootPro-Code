package test;
import java.util.Vector;
import junit.framework.TestCase;
import org.junit.Test;

import com.sat.ballistics.Ballistics;
//import com.sat.ballistics.BulletInfo;
//import com.sat.ballistics.BulletSpecifications;

public class JUnitBallistics extends TestCase
{
   private final static int X = (0);
   private final static int Y = (1);
   private final static int Z = (2);
  
   /*****************************************************************************************/
//   @Test
//   public void testCaclulateMcCoy() 
//   {
//      Ballistics tB = new Ballistics(); 
//      
//      double MAX = 0;  //Cx = 0,342349055954777
//      
//      //double must_be = 0.3423491;             // as it is in example
//      double must_be = 0.3119086138138161;
//      double res = tB.calculationOfForceOfHeadResistance(MAX,
//                                            800,
//                                            340.28);
//      
//      assertEquals("McCoy: ",must_be, res);
//   }
   
   
   /*****************************************************************************************/
   @Test
   public void testCaclulateDeviationkoef() 
   {
      Ballistics testBallistics = new Ballistics(); 
      
      //testBallistics.BulletFlight.bullet_K_param    = 0.005; 
      //testBallistics.BulletFlight.bullet_turn_speed = 3504.166;
      
      double Vp = 840.19913960157373; 
      double Vp0= 841.01486312668692;
      
      double must_be = 0.00002479527;
      
      assertEquals("Deviation: ",must_be,testBallistics.roundTo(
                        testBallistics.calculateDeviationkoef(Vp, Vp0),11));
   }
   
   /*****************************************************************************************/
   @Test
   public void testEarthSpeedVector() 
   {   
      Ballistics tB = new Ballistics(); 
      Vector<Double> testVector = new Vector<Double>();
      testVector.add(X,0.0);
      testVector.add(Y,0.0);
      testVector.add(Z,0.0);
      
      double latitude   = (50.0);
      double azimuth    = (60.0);
      double coordAngle = (-30.0);

      double must_wx = (0.00004822698);
      double must_wy = (0.00003665873);
      double must_wz = (-0.00004059307);

      //(4.63E-5,3.52E-5,-3.90E-5) with w earth=0.00007
      //tB.WearthSpeed = (7.29212)* Math.pow(10,-5);
      tB.calculateEarthSpeedVector(latitude, 
                                      azimuth, 
                                      coordAngle,
                                      testVector);
            
      assertEquals("wx: ",tB.roundTo(must_wx,tB.roundConst), 
                   tB.roundTo(testVector.get(X).doubleValue(),tB.roundConst));
      assertEquals("wy: ",tB.roundTo(must_wy,tB.roundConst), 
                   tB.roundTo(testVector.get(Y).doubleValue(),tB.roundConst));
      assertEquals("wz: ",tB.roundTo(must_wz,tB.roundConst), 
                   tB.roundTo(testVector.get(Z).doubleValue(),tB.roundConst));
      
            
      latitude   = (45.0);
      azimuth    = (135.0);
      coordAngle = (-60.0);

      tB.calculateEarthSpeedVector(latitude, 
            azimuth, 
            coordAngle,
            testVector);

      must_wx = ( 0.000025366070498705605);
      must_wy = (0.000055059626473984514);
      must_wz = (-0.000035000000000000004);

      //assertEquals("wx: ",must_wx, testVector.get(X).doubleValue());
      //assertEquals("wy: ",must_wy, testVector.get(Y).doubleValue());
      //assertEquals("wz: ",must_wz, testVector.get(Z).doubleValue());
      //=====================================================================================
      latitude   = (45.0);
      azimuth    = (135.0);
      coordAngle = (60.0);

      tB.calculateEarthSpeedVector(latitude, 
            azimuth, 
            coordAngle,
            testVector);

      must_wx = ( -0.000060366070498705612);
      must_wy = ( -0.0000055621517909261811);
      must_wz = (-0.000035000000000000004);

      //assertEquals("wx: ",must_wx, testVector.get(X).doubleValue());
      //assertEquals("wy: ",must_wy, testVector.get(Y).doubleValue());
      //assertEquals("wz: ",must_wz, testVector.get(Z).doubleValue());

      
   }
   /*****************************************************************************************/
   @Test
   public void testGravitationTurnVector() 
   { 
      Ballistics testBallistics = new Ballistics(); 
      
      Vector<Double> testVectorG = new Vector<Double>();
      testVectorG.add(X, 0.0);
      testVectorG.add(Y, 0.0);
      testVectorG.add(Z, 0.0);
      
      double angle = -60.0; // clock wise
      
      double must_gx = (-8.49);
      double must_gy = (-4.90);
      double must_gz = (0.0);
      
      testBallistics.Parameters.gravityAcceleration = 9.8;
      /* In function it will be set to *=-1 */
      
      testBallistics.calculateGravitationTurnVector(angle,
                                           testVectorG);
      
      assertEquals("gx: ",must_gx, testBallistics.roundTo(testVectorG.get(X).doubleValue(),2));
      assertEquals("gy: ",must_gy, testBallistics.roundTo(testVectorG.get(Y).doubleValue(),2));
      assertEquals("gz: ",must_gz, testBallistics.roundTo(testVectorG.get(Z).doubleValue(),2));
   }
   /*****************************************************************************************/
   @Test
   public void testgetLocalG() 
   {
      double latitude = 0.0;
      double altitude = 0.0;
      
      Ballistics testBallistics = new Ballistics(); 
      double must_G = 9.7803; 
      
      assertEquals("Local G: ",must_G, testBallistics.roundTo(
                                             testBallistics.calculateLocalG(latitude, altitude),4));
   
      latitude = 45.05;
      altitude = 500.0;
      must_G =  9.8047;
      
      assertEquals("Local G: ",must_G, testBallistics.roundTo(
                     testBallistics.calculateLocalG(latitude, altitude),4));
   }
   /*****************************************************************************************/
   @Test
   public void testCaclulateBulletEnergy() 
   {
      Ballistics testBallistics = new Ballistics(); 
      double must_be = 2912.0;
      double result = testBallistics.calculateBulletEnergy(9.10, 800.0);
      assertEquals("Energy: ",must_be, testBallistics.roundTo(result,1));
   }
   
   /*****************************************************************************************/
   @Test
   public void test_Math_VP() 
   {
      Ballistics testBallistics = new Ballistics(); 
      
      Vector<Double> testVectorVp = new Vector<Double>();
      testVectorVp.add(X, -800.00);
      testVectorVp.add(Y, 0.0);
      testVectorVp.add(Z, 0.0);
      
      Vector<Double> testVectorVb = new Vector<Double>();
      testVectorVb.add(X, -800.0);
      testVectorVb.add(Y, 0.0);
      testVectorVb.add(Z, -5.0);
      
      Vector<Double> testResult = new Vector<Double>();
      testResult.add(X, 0.0);
      testResult.add(Y, 0.0);
      testResult.add(Z, 0.0);
      
      double must_Vdx = (0.0);
      double must_Vdy = (-4000.0);
      double must_Vdz = (0.0);
      
      testBallistics.VP(testVectorVp, testVectorVb ,testResult);
      
      assertEquals("Vdx: ",must_Vdx, testBallistics.roundTo(testResult.get(X).doubleValue(),5));
      assertEquals("Vdy: ",must_Vdy, testBallistics.roundTo(testResult.get(Y).doubleValue(),5));
      assertEquals("Vdz: ",must_Vdz, testBallistics.roundTo(testResult.get(Z).doubleValue(),5));
   
   }
   /*****************************************************************************************/
   @Test
   public void test_calculateVelocity() 
   {
      Ballistics testBallistics = new Ballistics(); 
      Vector<Double> testVectorA = new Vector<Double>();
         testVectorA.add(X, 800.00);
         testVectorA.add(Y, 10.0);
         testVectorA.add(Z, 0.0);
      double must_be = 800.0624975588;
      must_be = testBallistics.roundTo(must_be,testBallistics.roundConst);
      assertEquals("Lng: ",
            must_be, 
            testBallistics.roundTo(testBallistics.calculateVelocity(testVectorA),
            testBallistics.roundConst));
   }
   
   /*****************************************************************************************/
   @Test
   public void test_Math_Lng() 
   {
      Ballistics testBallistics = new Ballistics(); 
      Vector<Double> testVectorA = new Vector<Double>();
         testVectorA.add(X, 500.00);
         testVectorA.add(Y, 50.0);
         testVectorA.add(Z, 0.0);
      double must_be = 502.49;
      assertEquals("Lng: ",must_be, testBallistics.roundTo(testBallistics.Lng(testVectorA),2));
   }
   /*****************************************************************************************/
   @Test
   public void test_Math_Mlt() 
   {
      Ballistics testBallistics = new Ballistics(); 
      
      double value = -1;
      
      Vector<Double> testVectorA = new Vector<Double>();
         testVectorA.add(X, 841.0);
         testVectorA.add(Y, 0.0);
         testVectorA.add(Z, 0.0);
         
      Vector<Double> result = new Vector<Double>();
         result.add(X, 0.0);
         result.add(Y, 0.0);
         result.add(Z, 0.0);
            
      testBallistics.Mlt(testVectorA, value , result);
      
      assertEquals("Mlt: ",-841.00, testBallistics.roundTo(result.get(X).doubleValue(),2));
      assertEquals("Mlt: ",0.0,     testBallistics.roundTo(result.get(Y).doubleValue(),2));
      assertEquals("Mlt: ",0.0,     testBallistics.roundTo(result.get(Z).doubleValue(),2));
      
   }
   /*****************************************************************************************/
   @Test
   public void test_Math_VectorDeduction() 
   {
      Ballistics testBallistics = new Ballistics();
      Vector<Double> A = new Vector<Double>();
      A.add(X, 800.0);
      A.add(Y, 10.0);
      A.add(Z, 0.0);
      
      Vector<Double> B = new Vector<Double>();
      B.add(X, 0.0);
      B.add(Y, 0.0);
      B.add(Z, 5.0);
      
      testBallistics.vectorDeduction(B, 
                                     A, 
                                     A);
      
      assertEquals("VectorDeduction_X: ", -800.0, testBallistics.roundTo(A.get(X).doubleValue(),2));
      assertEquals("VectorDeduction_Y: ",  -10.0, testBallistics.roundTo(A.get(Y).doubleValue(),2));
      assertEquals("VectorDeduction_Z: ",    5.0, testBallistics.roundTo(A.get(Z).doubleValue(),2));
      
   }
   /*****************************************************************************************/
   @Test
   public void test_Math_VectorAddition() 
   {
      Ballistics testBallistics = new Ballistics();
      Vector<Double> A = new Vector<Double>();
      A.add(X, 799.0);
      A.add(Y, -0.01);
      A.add(Z, 0.0);
      
      Vector<Double> B = new Vector<Double>();
      B.add(X, 1.0);
      B.add(Y, 0.10);
      B.add(Z, 1.0);
      
      testBallistics.vectorAddition(B, 
                                    A, 
                                    A);
      
      assertEquals("VectorDeduction_X: ", 800.0, testBallistics.roundTo(A.get(X).doubleValue(),2));
      assertEquals("VectorDeduction_Y: ",  0.09, testBallistics.roundTo(A.get(Y).doubleValue(),2));
      assertEquals("VectorDeduction_Z: ",   1.0, testBallistics.roundTo(A.get(Z).doubleValue(),2));
      
      testBallistics.vectorAddition(B, 
                                    A, 
                                    A);
      assertEquals("VectorDeduction_X: ", 801.0, testBallistics.roundTo(A.get(X).doubleValue(),2));
      assertEquals("VectorDeduction_Y: ",  0.19, testBallistics.roundTo(A.get(Y).doubleValue(),2));
      assertEquals("VectorDeduction_Z: ",   2.0, testBallistics.roundTo(A.get(Z).doubleValue(),2));
      
      
   }
   /*****************************************************************************************/
}

