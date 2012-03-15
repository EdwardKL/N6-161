package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;


public class PolicyEdit extends TabActivity {
	
	public static final String APP_ID = "app_id";
	public static final String APP_NAME = "name";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if(extras!=null){
			setTitle(extras.getString(APP_ID));
		}
		
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, Internet.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("internet").setIndicator("Internet").setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, FileSystem.class);
	    spec = tabHost.newTabSpec("filesys").setIndicator("File System").setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, PhoneInfo.class);
	    spec = tabHost.newTabSpec("filesys").setIndicator("Phone Info").setContent(intent);
	    tabHost.addTab(spec);


	}


}
