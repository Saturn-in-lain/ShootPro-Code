package com.sat.ballistics;

import android.content.Context;

/******************************************************************
 * @author:     stas.savinov
 * @Name:       Specification
 * @Desciption: Specification interface for weapon, bullet and scope data
 * @category:  
 * @version:    0.0.1
 *****************************************************************/
public interface Specification
{
   
   /**************************************************************************
    * Function:  - Parser of JSON
    * @param  jsonString - String JSON string for parsing 
    * @param  Tag        - int number of selected scope 
    * @return None.
    * ************************************************************************/ 
   void jsonParser(String jsonString, int Tag);
   
   /**************************************************************************
    * Function:  - Parser of JSON
    * @param  jsonString - String - JSON string for parsing 
    * @param  Tag  - int number of selected scope 
    * @return None.
    * ************************************************************************/ 
   String jsonTypeParser(String jsonString, int Tag);
   
   /**************************************************************************
    * Function: LoadDataBase 
    * @param   ctx          - Contex
    * @param   scopeTag     - number if JSON list
    * @param   parserString - String
    * @return None.
    * ***********************************************************************/ 
   public void LoadDataBase(Context ctx, int Tag, String parserString);
   
   /**************************************************************************
    * Function:  - getInfoAboutScopeFromDataBase
    * @param ctx - Context
    * @return None.
    * ***********************************************************************/ 
   public String getInfoAboutFromDataBase(Context ctx, int Tag);

   
}
