package edu.berkeley.cs.cs161;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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
		final List<ResolveInfo> pkgAppsList = getPackageManager().queryIntentActivities(mainIntent, 0);
		
		allAppsArrayAdapter = new ArrayAdapter<String>(this, R.layout.app_display);
		GridView gridview = (GridView) findViewById(R.id.gridview);
		
		
	    gridview.setAdapter(new ImageAdapter(this, pkgAppsList));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent edit = new Intent(parent.getContext(), PolicyEdit.class);
				ResolveInfo item = (ResolveInfo) parent.getItemAtPosition(position);
				edit.putExtra(PolicyEdit.APP_NAME, item.activityInfo.packageName);
				startActivityForResult(edit, ACTIVITY_EDIT);
	        }
	    });
		
	}
}