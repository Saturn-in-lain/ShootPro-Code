package com.sat.shootpro;

import java.util.Locale;
import java.util.Random;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class Options extends Activity implements InitializerPerClass
{
   private static Button      NewGameButton  = null;
   private static CheckBox    CheckSound     = null;
   private static RadioButton langEng        = null;
   private static RadioButton langRus        =  null;
   private static TextView    headerText     = null;
   private static TextView    langTextEng    = null;
   private static TextView    langTextRus    = null;
   private        SaveGame    FileInterf     = null;
   /* This is for language settings */
   private String en = "en";
   private String ru = "ru";
   /**************************************************************************
    * Function: InitAllResourcesInClass
    * @param   view View view
    * @return 
    * ***********************************************************************/
   @Override
   public void InitAllResourcesInClass(View view)
   {
      NewGameButton = (Button) findViewById(R.id.btnNewGameStart);
      CheckSound    = (CheckBox) findViewById(R.id.chkBxSoundSwitch);
      langEng       = (RadioButton) findViewById(R.id.rdB_eng);
      langRus       = (RadioButton) findViewById(R.id.rdB_ru);
      langTextEng   = (TextView) findViewById(R.id.rdB_eng_text);
      langTextRus   = (TextView) findViewById(R.id.rdB_ru_text);
      headerText    = (TextView) findViewById(R.id.headerOptions);
      FileInterf    = new SaveGame(Options.this);

      LocaleLanguage();
   }

   /**************************************************************************
    * Function: onCreate
    * @param  
    * @return 
    * ***********************************************************************/
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.options);
              
       InitAllResourcesInClass(null);
       CheckSound.setChecked(StartMenuActivity.Interf.getSoundFlagSettings());
       /* ********************************************************************** */
       NewGameButton.setOnClickListener(new OnClickListener() 
       {
            @Override
            public void onClick(View v)
            {
               StartMenuActivity.Interf.NewGameParameters(setRandomWindParam(0,15),
                                       (int)setRandomWindParam(0,7));
               FileInterf.deleteSavedGame();
            }
       });
       /* ********************************************************************** */
       CheckSound.setOnCheckedChangeListener(new OnCheckedChangeListener()
       {
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
          {
             StartMenuActivity.Interf.setSoundFlagSettings(isChecked);
          }
       });
       /* ********************************************************************** */  
       langEng.setOnCheckedChangeListener(new OnCheckedChangeListener()
       {
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
          {
             if (buttonView.isChecked()) 
             {
                langRus.setChecked(false);
                updateTexts(en);
             }
          }
       });
       /* ********************************************************************** */  
       langRus.setOnCheckedChangeListener(new OnCheckedChangeListener()
       {
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
          {
             if (buttonView.isChecked()) 
             {
                langEng.setChecked(false);
                updateTexts(ru);
             }
          }
       });
   }
   /**************************************************************************
    * Function: setRandomWindParam - return random number in selected 
    * min and max borders
    * @param min - int
    * @param max - int
    * @return (Double) random number 
    * ***********************************************************************/
   private static double setRandomWindParam(int min,int max)
   {
      double dRet = init;
         Random r = new Random();
         dRet = r.nextInt(max - min + 1) + min;
      return dRet;
   }
   
   /**************************************************************************
    * Function: updateTexts for momently change all text(s)
    * @param lang - String 
    * @return None.
    * ***********************************************************************/
   private void updateTexts(String lang)
   {
      if (lang.equalsIgnoreCase(""))
      {
         return;
      }
      
      StartMenuActivity.Interf.setCurrentLanguage(lang,
                                                  getBaseContext());
      /*-----------------------------------------------------------------------------------*/
      Locale  myLocale = new Locale(lang);
      Locale.setDefault(myLocale);
      android.content.res.Configuration config = new android.content.res.Configuration();
      config.locale = myLocale;
      getBaseContext().getResources().
      updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
      /*-----------------------------------------------------------------------------------*/
      CheckSound.setText(R.string.soundSwitch);
      langTextEng.setText(R.string.English);
      langTextRus.setText(R.string.Russian);
      headerText.setText(R.string.option);
      NewGameButton.setText(R.string.newGame);
      
      if( lang.equals(ru) )
      {
         langRus.setChecked(true);
         langEng.setChecked(false);
      }
      else if( lang.equals(en) )
      {
         langEng.setChecked(true);
         langRus.setChecked(false);
      }
   }
   
   /**************************************************************************
    * Function: LocaleLanguage
    * @param ()
    * @return 
    * ***********************************************************************/
    private void LocaleLanguage()
    {
       String lang = loadLocaleLanguage();
       if(null != lang)
       {
          updateTexts(lang);
       }
       else
       {
          StartMenuActivity.Interf.setCurrentLanguage(en,getBaseContext());
       }       
    }
    
    /**************************************************************************
     * Function: loadLocaleLanguage
     * @param ()
     * @return 
     * ***********************************************************************/
    public String loadLocaleLanguage()
    {
       String lang = "";
       if ( null != StartMenuActivity.Interf )
       {         
          lang = StartMenuActivity.Interf.getCurrentLanguage();
       }
       return lang;
    }
}
