package edu.berkeley.cs.cs161;

import edu.berkeley.cs.cs161.db.SavedAppsSQLiteHelper;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;


public class PolicyEdit extends TabActivity {
	
	public static final String APP_ID = "app_id";
	public static final String APP_NAME = "name";

	private static String appId;
	private static SavedAppsSQLiteHelper sqliteHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		appId = extras.getString(APP_ID);
        sqliteHelper = new SavedAppsSQLiteHelper(this);
		if(extras!=null){
			setTitle(appId);
		}
		
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, Internet.class);
	    intent.putExtra(APP_ID, appId);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("internet").setIndicator("Internet").setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, FileSystem.class);
	    intent.putExtra(APP_ID, appId);
	    spec = tabHost.newTabSpec("filesys").setIndicator("File System").setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, PhoneInfo.class);
	    intent.putExtra(APP_ID, appId);
	    spec = tabHost.newTabSpec("phoneinfo").setIndicator("Phone Info").setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, PhoneFeatures.class);
	    intent.putExtra(APP_ID, appId);
	    spec = tabHost.newTabSpec("phonefeatures").setIndicator("Phone Features").setContent(intent);
	    tabHost.addTab(spec);
	}
	
	public static void loadListViewWithPolicies(ListView lv, final String[] policies) {
        lv.setItemsCanFocus(false);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        String[] permissions;
		try {
			permissions = sqliteHelper.getEnabledAppPermissions(appId);
			
	        for (int i = 0; i < policies.length; i++) {
	        	for (String permission : permissions) {
	        		if (permission.equals(policies[i])) {
	        			lv.setItemChecked(i, true);
	        			break;
	        		}
	        	}
	        }

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        lv.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	CheckedTextView check = (CheckedTextView)v;
	        	if (!check.isChecked()) {
	        		try {
						sqliteHelper.addPermissionToApp(appId, policies[position]);
					} catch (Exception e) {
						((ListView)parent).setItemChecked(position, false);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	} else {

	        		try {
						sqliteHelper.removePermissionFromApp(appId, policies[position]);
					} catch (Exception e) {
						check.setChecked(false);
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
		});
	}
}
