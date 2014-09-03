package com.sat.shootpro;

import android.view.View;

public interface InitializerPerClass
{
   /* Constant values */
   final static int DEADBEAF        = (666);
   final static int DEFAULT_INDEX   = (14);
   final static double init         = (0.00);
   
   static String strLocation   = "imageLocation";
   static String strLocation_x = "imageTargerLocation_x";
   static String strLocation_y = "imageTargerLocation_y";
   
   /* Functions */
   /**************************************************************************
    * Function: InitAllResourcesInClass - almost all activity classes
    * have their UI resources. So let's unify this process  
    * ***********************************************************************/
   void InitAllResourcesInClass(View view);
}
