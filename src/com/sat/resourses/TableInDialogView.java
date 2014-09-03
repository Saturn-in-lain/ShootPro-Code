package com.sat.resourses;

import java.util.Vector;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import com.sat.ballistics.Ballistics;
import com.sat.ballistics.Ballistics.VECTOR_TYPE;
import com.sat.shootpro.R;
import com.sat.shootpro.StartMenuActivity;
import com.sat.shootpro.Target;

public class TableInDialogView
{
   private Context     ctx                = null;
   private TableLayout questionContainer  = null;
   private Dialog      dialog             = null; 
   private String      LOG                = "TableInDialogView"; 
   
   /**************************************************************************
    * Function: ImageInDialogView() 
    * @param Context context
    * @param Resources res
    * @return (View) 
    * ***********************************************************************/
   public TableInDialogView(Context context)
   {
      if((null != context) )
      {
         this.ctx = context;
         dialog   = new Dialog(this.ctx);
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setContentView(R.layout.table_layout);
         questionContainer = (TableLayout) dialog.findViewById(R.id.tablelayout);
      }
      else
      {
         Log.e(LOG,"--- WARNING CONTEXT/RESOURCES/VIEW == NULL ---");
      }
   }
   /**************************************************************************
    * Function: showTableDialog() 
    * @return  None.
    * ***********************************************************************/
   public void showTableDialog()
   {
      if((null == dialog) && 
            (null == this.questionContainer))
      {
         Log.e(LOG,"showTableDialog Entry parameters error");
         return;
      }
      dialog.show();
   }
   
   /**************************************************************************
    * Function: deleteTableDialog() 
    * @return  None.
    * ***********************************************************************/
   public void deleteTableDialog()
   {
      if( null != this.questionContainer )
      {
         this.questionContainer.removeAllViews();
         this.questionContainer.destroyDrawingCache();
      }
   }
   
   
   /**************************************************************************
    * Function: showTableDialog() 
    * @param int ResourceID - Image resource id
    * @param int TextID     - Text/String resource id
    * @return  None.
    * ***********************************************************************/
   public void addTableRow(Ballistics Engine, 
                           Target TargetClass,
                           int rowNumber,
                           StringBuilder flags)
   {
      if((null == Engine.BulletFlight) && 
            (null == this.questionContainer))
      {
         Log.e(LOG,"showTableDialog Entry parameters error");
         return;
      }
      
      /*-----------------------------------------------------------------------*/
      /* TODO: RAT */
      Vector<Double> template = new Vector<Double>();
      template.add(0,0.0); 
      template.add(1,0.0); 
      template.add(1,0.0);
      
      Engine.getBulletVectorsInfo(VECTOR_TYPE.VECTOR_TYPE_RV,
                                 template);
      /*-----------------------------------------------------------------------*/
      LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(
                                       Context.LAYOUT_INFLATER_SERVICE);
      View question = inflater.inflate(R.layout.ballistics_data_row, null);
      question.setId(rowNumber);
      
      setTableValue(question,
                    Engine,
                    TargetClass.getRealTargetSize(true),
                    flags);
      /*-----------------------------------------------------------------------*/
      TableRow tr = (TableRow) question;
      TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
                                               TableLayout.LayoutParams.WRAP_CONTENT,
                                               TableLayout.LayoutParams.WRAP_CONTENT);
                                            
      trParams.setMargins(0,1,1,0);
      tr.setLayoutParams(trParams);
     /*-----------------------------------------------------------------------*/
      try
      {
         questionContainer.addView(tr); 
      }
      catch(NullPointerException e)
      {
         Log.e(LOG,"Exception table row NULL");
      }
   }
    
   /**************************************************************************
    * Function: setTableValue 
    * @param question - View 
    * @param Engine   - Ballistics 
    * @param flags    - StringBuilder 
    * @return (None) 
    * ***********************************************************************/
   private void setTableValue(View          question, 
                              Ballistics    Engine, 
                              double        targetHeight, 
                              StringBuilder flags)
   {
      ShapeDrawable border = new ShapeDrawable(new RectShape());
      border.getPaint().setStyle(Style.STROKE);
      border.getPaint().setColor(Color.BLUE);
      
      int X=0,Y=1,Z=2;
            
      TextView rowX = (TextView) question.findViewById(R.id.rowX);
      rowX.setBackgroundDrawable(border);
      
      TextView rowY = (TextView) question.findViewById(R.id.rowY);
      rowY.setBackgroundDrawable(border);
      /**************************************************************************/
      TextView rowMOA_Y    = (TextView) question.findViewById(R.id.rowMOAY);
      rowMOA_Y.setBackgroundDrawable(border);
      TextView rowMils_Y   = (TextView) question.findViewById(R.id.rowMilsY);
      rowMils_Y.setBackgroundDrawable(border);
      TextView rowInches_Y = (TextView) question.findViewById(R.id.rowInchesY);
      rowInches_Y.setBackgroundDrawable(border);
      /**************************************************************************/
      TextView rowZ = (TextView) question.findViewById(R.id.rowZ);
      rowZ.setBackgroundDrawable(border);
      /**************************************************************************/
      TextView rowMOA_Z    = (TextView) question.findViewById(R.id.rowMOAZ);
      rowMOA_Z.setBackgroundDrawable(border);
      TextView rowMils_Z   = (TextView) question.findViewById(R.id.rowMilsZ);
      rowMils_Z.setBackgroundDrawable(border);
      TextView rowInches_Z = (TextView) question.findViewById(R.id.rowInchesZ);
      rowInches_Z.setBackgroundDrawable(border);
       /**************************************************************************/
      
      TextView rowVx = (TextView) question.findViewById(R.id.rowVx);
      rowVx.setBackgroundDrawable(border);
      TextView rowVy = (TextView) question.findViewById(R.id.rowVy);
      rowVy.setBackgroundDrawable(border);
      TextView rowVz = (TextView) question.findViewById(R.id.rowVz);
      rowVz.setBackgroundDrawable(border);
      
      TextView rowVwx = (TextView) question.findViewById(R.id.rowVwx);
      rowVwx.setBackgroundDrawable(border);
      TextView rowVwy = (TextView) question.findViewById(R.id.rowVwy);
      rowVwy.setBackgroundDrawable(border);
      TextView rowVwz = (TextView) question.findViewById(R.id.rowVwz);
      rowVwz.setBackgroundDrawable(border);
      
      TextView rowVPx = (TextView) question.findViewById(R.id.rowVPx);
      rowVPx.setBackgroundDrawable(border);
      TextView rowVPy = (TextView) question.findViewById(R.id.rowVPy);
      rowVPy.setBackgroundDrawable(border);
      TextView rowVPz = (TextView) question.findViewById(R.id.rowVPz);
      rowVPz.setBackgroundDrawable(border);
      
      TextView rowAx = (TextView) question.findViewById(R.id.rowAx);
      rowAx.setBackgroundDrawable(border);
      TextView rowAy = (TextView) question.findViewById(R.id.rowAy);
      rowAy.setBackgroundDrawable(border);
      TextView rowAz = (TextView) question.findViewById(R.id.rowAz);
      rowAz.setBackgroundDrawable(border);
      
      TextView rowMainVelocity   = (TextView) question.findViewById(R.id.rowMainVelocity);
      rowMainVelocity.setBackgroundDrawable(border);
      
      TextView rowMAX            = (TextView) question.findViewById(R.id.rowMAX);
      rowMAX.setBackgroundDrawable(border);
      
      TextView rowRfunction      = (TextView) question.findViewById(R.id.rowRfunction);
      rowRfunction.setBackgroundDrawable(border);
      
      TextView rowAcceleration   = (TextView) question.findViewById(R.id.rowAcceleration);
      rowAcceleration.setBackgroundDrawable(border);
      
      TextView rowTime           = (TextView) question.findViewById(R.id.rowTime);
      rowTime.setBackgroundDrawable(border);

      TextView rowEnergy         = (TextView) question.findViewById(R.id.rowEnergy);
      rowEnergy.setBackgroundDrawable(border);

      if(null != Engine)
      {
         /**--------------------------------------------------------------------------------*/
         Vector<Double> template = new Vector<Double>();
         template.add(X,0.0); 
         template.add(Y,0.0); 
         template.add(Z,0.0);
         
         Engine.getBulletVectorsInfo(VECTOR_TYPE.VECTOR_TYPE_RV, template);
         rowX.setText(String.format("x:[%.3g]",template.get(X))); 
         /**--------------------------------------------------------------------------------*/
         rowY.setText(String.format(       "(m):[%.3g]",template.get(Y)));          
         
         double Mils = Engine.getMilsFromMetersDistance(targetHeight, 
                                                        template.get(X) );
         
         rowMils_Y.setText(String.format("Mils:[%.2g]", Mils ));                   
         
         rowMOA_Y.setText(String.format("MOA:[%.3g]",Engine.getMOAFromMils(Mils)));
         
         rowInches_Y.setText(String.format("(Inch):[%.2g]",
                             Engine.convertionMetersToInches(template.get(Y))));                   
         /**--------------------------------------------------------------------------------*/
         rowZ.setText(String.format(       "(m):[%.3g]",template.get(Z)));
         
         Mils = Engine.getMilsFromMetersDistance(targetHeight, 
                                                 template.get(X));
         rowMils_Z.setText(String.format("Mils:[%.3g]",
                                         Mils));
         rowMOA_Z.setText(String.format("MOA:[%.3g]",
                                         Engine.getMOAFromMils(Mils)));
         rowInches_Z.setText(String.format("(Inch):[%.3g]",
                              Engine.convertionMetersToInches(template.get(Z))));
         /**--------------------------------------------------------------------------------*/
         if(true == StartMenuActivity.Interf.isColumInvisible(flags,0))
         {
            rowVx.setVisibility(View.GONE);
            rowVy.setVisibility(View.GONE);
            rowVz.setVisibility(View.GONE);
         }
         else
         {
            Engine.getBulletVectorsInfo(VECTOR_TYPE.VECTOR_TYPE_VB,template);
            rowVx.setText(String.format("Vx[%.5g]",template.get(X))); 
            rowVy.setText(String.format("Vy[%.5g]",template.get(Y))); 
            rowVz.setText(String.format("Vz[%.5g]",template.get(Z))); 
         }
         /**--------------------------------------------------------------------------------*/
         if(true == StartMenuActivity.Interf.isColumInvisible(flags,1))
         {
            rowVwx.setVisibility(View.GONE);
            rowVwy.setVisibility(View.GONE);
            rowVwz.setVisibility(View.GONE);
         }
         else
         {
            Engine.getBulletVectorsInfo(VECTOR_TYPE.VECTOR_TYPE_VD,template);
            rowVwx.setText(String.format("Vdx[%.5g]",template.get(X))); 
            rowVwy.setText(String.format("Vdy[%.5g]",template.get(Y)));  
            rowVwz.setText(String.format("Vdz[%.5g]",template.get(Z)));  
         }
         /**--------------------------------------------------------------------------------*/
         if(true == StartMenuActivity.Interf.isColumInvisible(flags,2))
         {
            rowVPx.setVisibility(View.GONE);
            rowVPy.setVisibility(View.GONE);
            rowVPz.setVisibility(View.GONE);
         }
         else
         {
            Engine.getBulletVectorsInfo(VECTOR_TYPE.VECTOR_TYPE_VP,template);
            rowVPx.setText(String.format("VPx:[%.5g]",template.get(X))); 
            rowVPy.setText(String.format("VPy:[%.5g]",template.get(Y))); 
            rowVPz.setText(String.format("VPz:[%.5g]",template.get(Z))); 
         }
         /**--------------------------------------------------------------------------------*/
         if(true == StartMenuActivity.Interf.isColumInvisible(flags,3))
         {
            rowAx.setVisibility(View.GONE);
            rowAy.setVisibility(View.GONE);
            rowAz.setVisibility(View.GONE);
         }
         else
         {
            Engine.getBulletVectorsInfo(VECTOR_TYPE.VECTOR_TYPE_Ab,template);
            rowAx.setText(String.format("Ax:[%.5g]",template.get(X))); 
            rowAy.setText(String.format("Ay:[%.5g]",template.get(Y)));
            rowAz.setText(String.format("Az:[%.5g]",template.get(Z)));
         }
         /**--------------------------------------------------------------------------------*/
         if(true == StartMenuActivity.Interf.isColumInvisible(flags,4))
         {
            rowMainVelocity.setVisibility(View.GONE);
         }
         else
         {
            rowMainVelocity.setText(String.format("V[%.5g]", Engine.BulletFlight.mainVelocity));
         }
         /**--------------------------------------------------------------------------------*/
         if(true == StartMenuActivity.Interf.isColumInvisible(flags,5))
         {
            rowMAX.setVisibility(View.GONE);
         }
         else
         {
            rowMAX.setText(String.format("MAX:[%.5g]", Engine.BulletFlight.MAXSoundSpeed));
         }
         /**--------------------------------------------------------------------------------*/
         if(true == StartMenuActivity.Interf.isColumInvisible(flags,6))
         {
            rowRfunction.setVisibility(View.GONE);
         }
         else
         {
            rowRfunction.setText(String.format("R:[%.5g]", Engine.BulletFlight.R_function));
         }
         /**--------------------------------------------------------------------------------*/
         if(true == StartMenuActivity.Interf.isColumInvisible(flags,7))
         {
            rowEnergy.setVisibility(View.GONE);
         }
         else
         {
            rowEnergy.setText(String.format("E:[%.5g]", Engine.BulletFlight.EnergyInDjoule)); 
         }
         /**--------------------------------------------------------------------------------*/
         rowAcceleration.setText(String.format("A:[%.5g]",  Engine.BulletFlight.acceleration));
         rowTime.setText(String.format("Time:[%.5g]",       Engine.BulletFlight.flightTime));
      }
      else
      {
         Log.e(LOG,"setTableValue>> Engine row issue");
      }
   }
}
