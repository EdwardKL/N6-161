package edu.berkeley.cs.cs161.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SavedAppsSQLiteHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "saved_apps";
	private static final int DATABASE_VERSION = 2;

	private static final String APPS_TABLE_NAME = "saved_apps";
	private static final String APPS_PRIMARY_ID = "id";
	private static final String APPS_COLUMN_PKG_NAME = "pkg_name";
	private static final String APPS_TABLE_CREATE = "CREATE TABLE " + APPS_TABLE_NAME + " (" + APPS_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + APPS_COLUMN_PKG_NAME
			+ " TEXT);";

	private static final String APPS_POLICIES_TABLE_NAME = "apps_policies";
	private static final String APPS_POLICIES_PRIMARY_ID = "id";
	private static final String APPS_POLICIES_COLUMN_APPS_ID = "app_id";
	private static final String APPS_POLICIES_COLUMN_POLICIES_ID = "policy_id";
	private static final String APPS_POLICIES_TABLE_CREATE = "CREATE TABLE " + APPS_POLICIES_TABLE_NAME + " (" + APPS_POLICIES_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
			+ APPS_POLICIES_COLUMN_APPS_ID + " INTEGER, " + APPS_POLICIES_COLUMN_POLICIES_ID + " INTEGER);";

	private static final String POLICIES_TABLE_NAME = "all_policies";
	private static final String POLICIES_PRIMARY_ID = "id";
	private static final String POLICIES_COLUMN_NAME = "name";
	private static final String POLICIES_COLUMN_DESCRIPTION = "description";
	private static final String POLICIES_TABLE_CREATE = "CREATE TABLE " + POLICIES_TABLE_NAME + " (" + POLICIES_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
			+ POLICIES_COLUMN_NAME + " TEXT" + POLICIES_COLUMN_DESCRIPTION + " TEXT);";

	public static String getAppsTableName()
	{
		return APPS_TABLE_NAME;
	}

	public static String getAppsColumnPkgName()
	{
		return APPS_COLUMN_PKG_NAME;
	}

	public static String getAppsPoliciesTableName()
	{
		return APPS_POLICIES_TABLE_NAME;
	}

	public static String getAppsPoliciesAppsId()
	{
		return APPS_POLICIES_COLUMN_APPS_ID;
	}

	public static String getAppsPoliciesPoliciesId()
	{
		return APPS_POLICIES_COLUMN_POLICIES_ID;
	}

	public static String getPoliciesTableName()
	{
		return POLICIES_TABLE_NAME;
	}

	public static String getPoliciesTableDescription()
	{
		return POLICIES_COLUMN_DESCRIPTION;
	}

	public SavedAppsSQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(APPS_TABLE_CREATE);
		db.execSQL(APPS_POLICIES_TABLE_CREATE);
		db.execSQL(POLICIES_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub

	}

	public void deleteApp(String pkg_name)
	{
		getReadableDatabase().delete(APPS_TABLE_NAME, APPS_COLUMN_PKG_NAME + "= ?", new String[] { pkg_name });
	}

	public void insertAppIntoTable(SavedApp input) throws Exception
	{
		ContentValues values = new ContentValues();
		values.put(APPS_COLUMN_PKG_NAME, input.getName());
		int appId = (int) (getReadableDatabase().insert(APPS_TABLE_NAME, null, values));

		// Add all the permissions that are currently set
		setupPermissionsForApp(input.getName(), input.getPermissions(), appId);
	}

	private void setupPermissionsForApp(String name, String[] permissions, int appId) throws Exception
	{
		for (String permission : permissions)
		{
			addPermissionToAppId(name, permission, appId);
		}
	}

	// get app's id and then call appPermissionToAppId
	public void addPermissionToApp(String name, String permission) throws Exception
	{
		int appId = getAppId(name);
		addPermissionToAppId(name, permission, appId);
	}

	// Grab an app's db id
	private int getAppId(String name)
	{
		Cursor results = getReadableDatabase().query(APPS_TABLE_NAME, null, APPS_COLUMN_PKG_NAME + "= ?", new String[] { name }, null, null, null);
		results.moveToFirst();
		int appId = results.getInt(results.getColumnIndex(APPS_PRIMARY_ID));
		results.close();
		return appId;
	}

	// If we already know what the app's id is just add associate a policy with the app
	private void addPermissionToAppId(String name, String permission, int appId) throws Exception
	{
		Cursor results = getReadableDatabase().query(POLICIES_TABLE_NAME, null, POLICIES_TABLE_NAME + "= ?", new String[] { permission }, null, null, null);

		int policyId;
		// policy doesn't exist, add to db
		if (results.getCount() == 0)
		{
			policyId = (int) (insertPolicyToPolicies(permission));
		}
		else if (results.getCount() == 1)
		{
			results.moveToFirst();
			policyId = results.getInt(results.getColumnIndex(POLICIES_PRIMARY_ID));
		}
		else
		{
			throw new Exception("For some reason there is a duplicate policy with name: " + permission);
		}

		// insert the two foreign keys into the association table
		ContentValues values = new ContentValues();
		values.put(APPS_POLICIES_COLUMN_APPS_ID, appId);
		values.put(APPS_POLICIES_COLUMN_POLICIES_ID, policyId);
		getReadableDatabase().insert(APPS_POLICIES_TABLE_NAME, null, values);
		results.close();
	}

	// Insert an unseen policy into the table of policies
	private long insertPolicyToPolicies(String name)
	{
		ContentValues values = new ContentValues();
		values.put(POLICIES_COLUMN_NAME, name);
		return getReadableDatabase().insert(POLICIES_TABLE_NAME, null, values);
	}

	//Grab all permissions from a certain app
	public String[] getAppPermissions(String name)
	{
		int appId = getAppId(name);
		
		//Grab all permissions that belong to the app
		String query = "SELECT * FROM " + POLICIES_TABLE_NAME + " a INNER JOIN " + APPS_POLICIES_TABLE_NAME + " b ON a.id=b." + APPS_POLICIES_COLUMN_POLICIES_ID + " WHERE b."
				+ APPS_POLICIES_COLUMN_APPS_ID + "=?";

		Cursor results = getReadableDatabase().rawQuery(query, new String[] { appId+"" });

		results.moveToFirst();
		int count= results.getCount();
		
		String[] permissions = new String[count];
		
		//iterate through all permissions and build a String array.
		for(int i=0;i<count;i++)
		{
			permissions[i]=results.getString(results.getColumnIndex(POLICIES_COLUMN_NAME));
			results.moveToNext();
		}
		results.close();
		return permissions;
	}
	
	public SavedApp getApp(String name)
	{
		return new SavedApp(name, getAppPermissions(name));
	}
}
