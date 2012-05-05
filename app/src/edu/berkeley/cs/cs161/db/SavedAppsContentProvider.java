package edu.berkeley.cs.cs161.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class SavedAppsContentProvider extends ContentProvider
{

	private static SavedAppsSQLiteHelper db;


	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs)
	{
		return db.getWritableDatabase().delete(uri.getPath(), selection, selectionArgs);
	}

	@Override
	public String getType(Uri uri)
	{
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		return Uri.withAppendedPath(uri, "" + db.getWritableDatabase().insert(uri.getPath(), null, values));
	}

	@Override
	public boolean onCreate()
	{
		db = new SavedAppsSQLiteHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri table, String[] columns, String selection, String[] selectionArgs, String sortOrder)
	{
		return db.getWritableDatabase().query(table.getPath(), columns, selection, selectionArgs,null,null, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{

		return db.getWritableDatabase().update(uri.getPath(), values, selection, selectionArgs);
	}
	
	public void deleteApp(String pkg_name)
	{
		db.deleteApp(pkg_name);
	}
	
	public int insertAppIntoTable(SavedApp input) throws Exception
	{
		return db.insertAppIntoTable(input);
	}


	// removes permission from an app
	public void removePermissionFromApp(String name, String permission) throws Exception
	{
		db.removePermissionFromApp(name, permission);
	}

	// get app's id and then call appPermissionToAppId
	public void addPermissionToApp(String name, String permission) throws Exception
	{
		db.addPermissionToApp(name, permission);
	}


	// Grab all permissions from a certain app
	public String[] getAppPermissions(String name) throws Exception
	{
		return db.getAppPermissions(name);
	}

	// Only grab the enabled app permissions
	public String[] getEnabledAppPermissions(String name) throws Exception
	{
		return db.getEnabledAppPermissions(name);
	}

	
	public SavedApp getApp(String name) throws Exception
	{
		return new SavedApp(name, getAppPermissions(name));
	}
	
}
