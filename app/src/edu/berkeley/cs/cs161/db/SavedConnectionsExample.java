package edu.berkeley.cs.cs161.db;

import android.content.Context;

public class SavedConnectionsExample
{
	private SavedAppsSQLiteHelper savedAppsDb;

	//This is how you build one of these helper things
	public SavedConnectionsExample(Context context)
	{
		savedAppsDb = new SavedAppsSQLiteHelper(context);
	}

	public void asdf()
	{
		try
		{
			savedAppsDb.addPermissionToApp("asdfApp","ability to eat chocolate");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
