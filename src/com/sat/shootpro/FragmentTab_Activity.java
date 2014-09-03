package com.sat.shootpro;

import java.util.HashMap;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentTab_Activity extends    FragmentActivity 
                                    implements TabHost.OnTabChangeListener
{
   private TabHost mTabHost                      = null;
   private HashMap<String, TabInfo> mapTabInfo   = new HashMap<String, TabInfo>();
   private TabInfo mLastTab                      = null;

   private final String FIRST_TAB_TAG  = "Tab1";
   private final String SECOND_TAB_TAG = "Tab2";
   private final String THIRD_TAB_TAG  = "Tab3";
   private final String FOURTH_TAB_TAG = "Tab4";
   /**************************************************************************
    * CLASS: TabInfo  
    * ***********************************************************************/
   private class TabInfo 
   {
        private String tag;
        private Class<?> clss;
        private Bundle args;

        private String tabName;
        private int    tabImageIndex;
        
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) 
        {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }
        
        public void setTabUIParameters(String tabName,int tabImageIndex)
        {
           this.tabName = tabName;
           this.tabImageIndex = tabImageIndex;
        }
   }

   /**************************************************************************
    * CLASS: TabFactory  
    * ***********************************************************************/
   class TabFactory implements TabContentFactory 
   {
       private final Context mContext;
       /**************************************************************************
        * Function: TabFactory()  
        * @param Context context
       * ***********************************************************************/
       public TabFactory(Context context) 
       {
           mContext = context;
       }
       /**************************************************************************
        * Function: createTabContent()  
        * @param String tag
       * ***********************************************************************/
       public View createTabContent(String tag) 
       {
           View v = new View(mContext);
           v.setMinimumWidth(0);
           v.setMinimumHeight(0);
           return v;
       }
   }
   /**************************************************************************
    * Function: onCreate()  
    * @param Bundle savedInstanceState
    * @return (None)
    * @Note: 
    * ***********************************************************************/
   protected void onCreate(Bundle savedInstanceState) 
   {
       super.onCreate(savedInstanceState);
       // Step 1: Inflate layout
       setContentView(R.layout.activity_notebook);

       // Step 2: Setup TabHost
       initialiseTabHost(savedInstanceState);
       if (null != savedInstanceState) 
       {
           mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); 
           //set the tab as per the saved state
       }
   }
   /**************************************************************************
    * Function: onSaveInstanceState()  
    * @param Bundle outState
    * @return (None)
    * @Note: 
    * ***********************************************************************/
   protected void onSaveInstanceState(Bundle outState) 
   {
       outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
       super.onSaveInstanceState(outState);
   }
  
   /**************************************************************************
    * Function: initialiseTabHost()  
    * @param args - Bundle
    * @return (None)
    * @Note: Step 2: Setup TabHost 
    * ***********************************************************************/
   private void initialiseTabHost(Bundle args) 
   {
      TabInfo tabInfo = null;
      mTabHost = (TabHost)findViewById(android.R.id.tabhost);
      mTabHost.setup();
             
      /*-------------------------------------------------------------------------------*/
      String tab_name1 = getResources().getString(R.string.fragment_main);
      tabInfo = new TabInfo(FIRST_TAB_TAG, 
                            FragmentTab_Main.class, 
                            args);
      tabInfo.setTabUIParameters(tab_name1,R.drawable.small_fragment_main);
      FragmentTab_Activity.addTab(this, 
                                    this.mTabHost, 
                                    this.mTabHost.newTabSpec(FIRST_TAB_TAG).setIndicator(tab_name1), 
                                    tabInfo);

      this.mapTabInfo.put(tabInfo.tag, 
                          tabInfo);
      /*-------------------------------------------------------------------------------*/ 
      String tab_name2 = getResources().getString(R.string.fragment_weapon);
      tabInfo = new TabInfo(SECOND_TAB_TAG, 
                            FragmentTab_Weapon.class, 
                            args);
      tabInfo.setTabUIParameters(tab_name2,R.drawable.small_fragment_weapon);
      FragmentTab_Activity.addTab(this, 
                                    this.mTabHost, 
                                    this.mTabHost.newTabSpec(SECOND_TAB_TAG).setIndicator(tab_name2), 
                                    tabInfo);
      this.mapTabInfo.put(tabInfo.tag, 
                          tabInfo);
      /*-------------------------------------------------------------------------------*/
      String tab_name3 = getResources().getString(R.string.fragment_ammo);
      tabInfo = new TabInfo(THIRD_TAB_TAG, 
                            FragmentTab_Ammo.class, 
                            args);
      tabInfo.setTabUIParameters(tab_name3,R.drawable.small_fragment_ammo);
      FragmentTab_Activity.addTab(this, 
                                    this.mTabHost, 
                                    this.mTabHost.newTabSpec(THIRD_TAB_TAG).setIndicator(tab_name3),
                                    tabInfo);
      this.mapTabInfo.put(tabInfo.tag, 
                          tabInfo);
      /*-------------------------------------------------------------------------------*/
      String tab_name4 = getResources().getString(R.string.fragment_info);
      tabInfo = new TabInfo(FOURTH_TAB_TAG, 
                            FragmentTab_Info.class, 
                            args);
      tabInfo.setTabUIParameters(tab_name4,R.drawable.small_fragment_info);
      FragmentTab_Activity.addTab(this, 
                                    this.mTabHost,
                                    this.mTabHost.newTabSpec(FOURTH_TAB_TAG).setIndicator(tab_name4),
                                    tabInfo);
      this.mapTabInfo.put(tabInfo.tag, 
                          tabInfo);
      /*-------------------------------------------------------------------------------*/
      this.onTabChanged(FIRST_TAB_TAG); // Default to first tab
      mTabHost.setOnTabChangedListener(this);
   }
   /**************************************************************************
    * Function: addTab()  
    * @param activity
    * @param tabHost
    * @param tabSpec
    * @param clss
    * @param args
    * @return (None)
    * @Note: 
    * ***********************************************************************/
   private static void addTab(FragmentTab_Activity activity, 
                              TabHost tabHost, 
                              TabHost.TabSpec tabSpec, 
                              TabInfo tabInfo) 
   {

      // Attach a Tab view factory to the spec
      tabSpec = tabHost.newTabSpec(tabInfo.tag).setIndicator(
                        createTabView(activity.getBaseContext(), tabInfo))
                                    .setContent(activity.new TabFactory(activity));
      
       //tabSpec.setContent(activity.new TabFactory(activity));
       String tag = tabSpec.getTag();
       // Check to see if we already have a fragment for this tab, probably
       // from a previously saved state.  If so, de activate it, because our
       // initial state is that a tab isn't shown.
       
       tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
       if ( (null != tabInfo.fragment) && 
                                          (!tabInfo.fragment.isDetached()) ) 
       {
           android.support.v4.app.FragmentTransaction ft = 
                       activity.getSupportFragmentManager().beginTransaction();
           ft.detach(tabInfo.fragment);
           ft.commit();
           activity.getSupportFragmentManager().executePendingTransactions();
       }
       tabHost.addTab(tabSpec);
   }
   
   /**************************************************************************
    * Function: createTabView()
    * @param 
    * @return 
    * @Note: 
    * ***********************************************************************/
   private static View createTabView(Context context,  TabInfo tabInfo) 
   {
      
      View v = LayoutInflater.from(context).inflate(R.layout.tab_item, null,false);
      v.setBackgroundResource(R.layout.tab_indicator_gen);
      TextView   tv = (TextView) v.findViewById(R.id.txt_tabtxt);
      ImageView img = (ImageView)v.findViewById(R.id.img_tabtxt);
      
      //set text and icon 
      tv.setText(tabInfo.tabName);
      img.setBackgroundResource(tabInfo.tabImageIndex);
      return v;
  }
   /**************************************************************************
    * Function: onTabChanged()  
    * @param String tag
    * @return (None)
    * @Note: 
    * ***********************************************************************/
   public void onTabChanged(String tag) 
   {
       TabInfo newTab = (TabInfo) this.mapTabInfo.get(tag);
       
       if (mLastTab != newTab)
       {
           FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
           
           if ( null != mLastTab ) 
           {
               if (null != mLastTab.fragment ) 
               {
                   ft.detach(mLastTab.fragment);
               }
           }
           if ( null != newTab )
           {
               if ( null == newTab.fragment )
               {
                   newTab.fragment = Fragment.instantiate(this,
                                                          newTab.clss.getName(), 
                                                          newTab.args);
                   ft.add(R.id.realtabcontent, 
                          newTab.fragment, 
                          newTab.tag);
               }
               else 
               {
                   ft.attach(newTab.fragment);
               }
           }
           mLastTab = newTab;
           ft.commit();
           this.getSupportFragmentManager().executePendingTransactions();
       }
   }
}