package edu.berkeley.cs.cs161;

import java.util.ArrayList;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TabHost;
import edu.berkeley.cs.cs161.db.SavedApp;
import edu.berkeley.cs.cs161.db.SavedAppsSQLiteHelper;


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

		//add the initial permissions to the database if first time opening this App
		ArrayList<String> masterList = new ArrayList<String>();

		for (String s : FileSystem.policyList) {
			masterList.add(s);
		}
		for (String s : Internet.policyList) {
			masterList.add(s);
		}
		for (String s : PhoneFeatures.policyList) {
			masterList.add(s);
		}
		for (String s : PhoneInfo.policyList) {
			masterList.add(s);
		}
		System.out.println(appId);
		ArrayList<String> acceptablePermissions = new ArrayList<String>();
		try {
			for (String p : masterList) {
				String q = "android.permission." + p;

				if (getPackageManager().checkPermission(q, appId) == PackageManager.PERMISSION_GRANTED) {
					acceptablePermissions.add(p);
				} 
			}
			String[] allPermissions = new String[acceptablePermissions.size()];
			SavedApp savedApp = new SavedApp(appId, (String[])(acceptablePermissions.toArray(allPermissions)));
			sqliteHelper.insertAppIntoTable(savedApp);
		} catch (Exception e0) {
			// TODO Auto-generated catch block
			e0.printStackTrace();
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

	public static void loadReadablePolicies(String[] readable, String[] unreadable) {
		for (int i = 0; i < unreadable.length; i++) {
			String policy = unreadable[i];
			String[] splitPolicy = policy.split("_");

			String readablePolicy = "Can";
			if (splitPolicy.length == 1) {
				readablePolicy = readablePolicy + " use " + splitPolicy[0].toLowerCase();
			} else {
				for (String segment : splitPolicy) {
					segment = segment.toLowerCase();
					readablePolicy = readablePolicy + " " + segment;
				}
			}
			readable[i] = readablePolicy;
		}
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
