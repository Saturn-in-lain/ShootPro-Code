package com.sat.shootpro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;

public class FileManipulations
{
   /**************************************************************************
   * Constructor: ScopeSpecification() 
   * ***********************************************************************/
   public FileManipulations()
   {
      //[STAB]
   }
   
   /**************************************************************************
    * Function: readFromLocalFile
    * @param  ctx - Context
    * @param  rawResFile - id in R.raw.
    * @return sRet - String with JSON for parsing
    * ***********************************************************************/ 
   public static String readFromLocalFile (Context ctx, int rawResFile)
   {
      String sRet = null;
      if( null != ctx )
      {
         InputStream inputStream = ctx.getResources().openRawResource(rawResFile);
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         
         try 
         {
            int i = 0;
            i = inputStream.read();
            while (i != -1)
            {
               byteArrayOutputStream.write(i);
               i = inputStream.read();
            }
            inputStream.close();
         } 
         catch (IOException e) 
         {
            e.printStackTrace();
         }
         
         sRet = byteArrayOutputStream.toString();
      }
      return sRet;
   }
   
}
