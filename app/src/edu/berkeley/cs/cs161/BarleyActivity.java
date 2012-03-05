package edu.berkeley.cs.cs161;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BarleyActivity extends Activity
{
	ArrayAdapter<String> allAppsArrayAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//Find a list of all installed apps
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, 0);
		
		allAppsArrayAdapter = new ArrayAdapter<String>(this, R.layout.app_display);

		
		ListView allAppsListView = (ListView) findViewById(R.id.app_list);
		
		
		allAppsListView.setAdapter(allAppsArrayAdapter);

		
		
		if (pkgAppsList.size() > 0)
		{
			findViewById(R.id.app_list).setVisibility(View.VISIBLE);
			for (ResolveInfo app: pkgAppsList)
			{
				allAppsArrayAdapter.add(app.activityInfo.name);
			}
		}
		else
		{
			String noDevices = getResources().getText(R.string.no_apps).toString();
			allAppsArrayAdapter.add(noDevices);
		}
		
		
	}
}