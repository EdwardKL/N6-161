package edu.berkeley.cs.cs161.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class SavedAppsContentProvider extends ContentProvider
{

	private static SavedAppsSQLiteHelper db;

	private static final String APPS_TABLE_NAME = "saved_apps";
	private static final String APPS_PRIMARY_ID = "id";
	private static final String APPS_COLUMN_PKG_NAME = "pkg_name";

	private static final String APPS_POLICIES_TABLE_NAME = "apps_policies";
	private static final String APPS_POLICIES_COLUMN_APPS_ID = "app_id";
	private static final String APPS_POLICIES_COLUMN_POLICIES_ID = "policy_id";
	private static final String APPS_POLICIES_COLUMN_ENABLED = "enabled";

	private static final String POLICIES_COLUMN_NAME = "name";

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
		return db.getWritableDatabase().query(table.getPath(), columns, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
	{

		return db.getWritableDatabase().update(uri.getPath(), values, selection, selectionArgs);
	}

	// Only grab the enabled app permissions
	public String[] getEnabledAppPermissions(String name) throws Exception
	{
		int appId = getAppId(name);

		// Grab all permissions that belong to the app
//		String query = "SELECT * FROM " + POLICIES_TABLE_NAME + " a, " + APPS_POLICIES_TABLE_NAME + " b WHERE a.id=b." + APPS_POLICIES_COLUMN_POLICIES_ID + " AND b."
//				+ APPS_POLICIES_COLUMN_APPS_ID + "=? AND b." + APPS_POLICIES_COLUMN_ENABLED + "=1;";

		Cursor results = query(Uri.parse("content://edu.cs.berkeley.cs161/"+APPS_POLICIES_TABLE_NAME),new String[]{"*"},  "a.id=b." + APPS_POLICIES_COLUMN_POLICIES_ID + " AND b."
				+ APPS_POLICIES_COLUMN_APPS_ID + "=? AND b." + APPS_POLICIES_COLUMN_ENABLED + "=1;",new String[] { appId + "" },null);

		results.moveToFirst();
		int count = results.getCount();

		String[] permissions = new String[count];

		// iterate through all permissions and build a String array.
		for (int i = 0; i < count; i++)
		{
			permissions[i] = results.getString(results.getColumnIndex(POLICIES_COLUMN_NAME));
			results.moveToNext();
		}
		results.close();
		return permissions;
	}

	// Grab an app's db id
	private int getAppId(String name) throws Exception
	{
		Cursor results = query(Uri.parse("content://edu.cs.berkeley.cs161/"+APPS_TABLE_NAME), new String[]{"id"}, APPS_COLUMN_PKG_NAME + "= ?", new String[] { name }, null);
		
		if (results.getCount() == 0)
		{
			return -1;
		}
		results.moveToFirst();
		int appId = results.getInt(results.getColumnIndex(APPS_PRIMARY_ID));
		results.close();
		return appId;
	}

}
