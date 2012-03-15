package edu.berkeley.cs.cs161;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BarleyActivity extends Activity
{
	ArrayAdapter<String> allAppsArrayAdapter;
	private static final int ACTIVITY_EDIT=1;
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

		
				
		//Add click abilities
		OnItemClickListener ocl = new OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View v, int position, long id){
				Intent edit = new Intent(parent.getContext(), PolicyEdit.class);
				Object item = parent.getItemAtPosition(position);
				edit.putExtra(PolicyEdit.APP_ID, item.toString());
				startActivityForResult(edit, ACTIVITY_EDIT);
			}
		};
		allAppsListView.setOnItemClickListener(ocl);
		
		
		allAppsListView.setAdapter(allAppsArrayAdapter);

		//Add styling to the divider
		int[] colors = {0xFF666666, 0xFFFFFFFF}; 
		allAppsListView.setDivider(new GradientDrawable(Orientation.TOP_BOTTOM, colors));
		allAppsListView.setDividerHeight(3);
		
		//Insert the names of the apps into the list
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