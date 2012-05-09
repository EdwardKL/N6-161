package edu.berkeley.cs.cs161.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.berkeley.cs.cs161.FileSystem;
import edu.berkeley.cs.cs161.Internet;
import edu.berkeley.cs.cs161.PhoneFeatures;
import edu.berkeley.cs.cs161.PhoneInfo;

public class SavedAppsSQLiteHelper extends SQLiteOpenHelper
{

	public static enum RegexType
	{
		FILESYSTEM_BLACKLIST, FILESYSTEM_WHITELIST, INTERNET_BLACKLIST, INTERNET_WHITELIST
	};

	private static SQLiteDatabase db;
	private static final String DATABASE_NAME = "saved_apps";
	private static final int DATABASE_VERSION = 4;

	private static final String APPS_TABLE_NAME = "saved_apps";
	private static final String APPS_PRIMARY_ID = "id";
	private static final String APPS_COLUMN_PKG_NAME = "pkg_name";
	private static final String APPS_COLUMN_INTERNET_WHITELIST = "internet_whitelist";
	private static final String APPS_COLUMN_INTERNET_BLACKLIST = "internet_blacklist";
	private static final String APPS_COLUMN_FILESYSTEM_WHITELIST = "filesystem_whitelist";
	private static final String APPS_COLUMN_FILESYSTEM_BLACKLIST = "filesystem_blacklist";
	private static final String APPS_TABLE_CREATE = "CREATE TABLE " + APPS_TABLE_NAME + " (" + APPS_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + APPS_COLUMN_PKG_NAME
			+ " TEXT, " + APPS_COLUMN_FILESYSTEM_BLACKLIST + " TEXT, " + APPS_COLUMN_FILESYSTEM_WHITELIST + " TEXT, " + APPS_COLUMN_INTERNET_BLACKLIST + " TEXT, "
			+ APPS_COLUMN_INTERNET_WHITELIST + " TEXT " + ");";

	private static final String APPS_POLICIES_TABLE_NAME = "apps_policies";
	private static final String APPS_POLICIES_PRIMARY_ID = "id";
	private static final String APPS_POLICIES_COLUMN_APPS_ID = "app_id";
	private static final String APPS_POLICIES_COLUMN_POLICIES_ID = "policy_id";
	private static final String APPS_POLICIES_COLUMN_ENABLED = "enabled";
	private static final String APPS_POLICIES_TABLE_CREATE = "CREATE TABLE " + APPS_POLICIES_TABLE_NAME + " (" + APPS_POLICIES_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
			+ APPS_POLICIES_COLUMN_APPS_ID + " INTEGER, " + APPS_POLICIES_COLUMN_POLICIES_ID + " INTEGER, " + APPS_POLICIES_COLUMN_ENABLED + " BOOLEAN);";

	private static final String POLICIES_TABLE_NAME = "all_policies";
	private static final String POLICIES_PRIMARY_ID = "id";
	private static final String POLICIES_COLUMN_NAME = "name";
	private static final String POLICIES_COLUMN_DESCRIPTION = "description";
	private static final String POLICIES_TABLE_CREATE = "CREATE TABLE " + POLICIES_TABLE_NAME + " (" + POLICIES_PRIMARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
			+ POLICIES_COLUMN_NAME + " TEXT , " + POLICIES_COLUMN_DESCRIPTION + " TEXT);";

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
		db = getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(APPS_TABLE_CREATE);
		db.execSQL(APPS_POLICIES_TABLE_CREATE);
		db.execSQL(POLICIES_TABLE_CREATE);

		SavedAppsSQLiteHelper.db = db;

		for (String policy : Internet.policies)
		{
			insertPolicyToPolicies(policy);
		}
		for (String policy : PhoneInfo.policies)
		{
			insertPolicyToPolicies(policy);
		}
		for (String policy : PhoneFeatures.policies)
		{
			insertPolicyToPolicies(policy);
		}
		for (String policy : FileSystem.policies)
		{
			insertPolicyToPolicies(policy);
		}
		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		String sql1 = "ALTER TABLE " + APPS_TABLE_NAME + " ADD COLUMN " + APPS_COLUMN_FILESYSTEM_BLACKLIST + " TEXT";
		String sql2 = "ALTER TABLE " + APPS_TABLE_NAME + " ADD COLUMN " + APPS_COLUMN_INTERNET_BLACKLIST + " TEXT";
		String sql3 = "ALTER TABLE " + APPS_TABLE_NAME + " ADD COLUMN " + APPS_COLUMN_FILESYSTEM_WHITELIST + " TEXT";
		String sql4 = "ALTER TABLE " + APPS_TABLE_NAME + " ADD COLUMN " + APPS_COLUMN_INTERNET_WHITELIST + " TEXT";
		db.execSQL(sql1);
		db.execSQL(sql2);
		db.execSQL(sql3);
		db.execSQL(sql4);
		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void deleteApp(String pkg_name)
	{
		db.delete(APPS_TABLE_NAME, APPS_COLUMN_PKG_NAME + "= ?", new String[] { pkg_name });
	}

	public int insertAppIntoTable(SavedApp input) throws Exception
	{
		if (getAppId(input.getName()) == -1)
		{
			ContentValues values = new ContentValues();
			values.put(APPS_COLUMN_PKG_NAME, input.getName());
			int appId = (int) (db.insert(APPS_TABLE_NAME, null, values));

			// Add all the permissions that are currently set
			setupPermissionsForApp(input.getName(), input.getPermissions(), appId);
			try
			{
				Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
			}
			catch (Exception e)
			{
				Log.e("Runtime Hack", "Error: " + e.getMessage());
				e.printStackTrace();
			}
			return appId;
		}
		throw new Exception("App already exists");
	}

	private void setupPermissionsForApp(String name, String[] permissions, int appId) throws Exception
	{
		for (String permission : permissions)
		{
			addPermissionToAppId(name, permission, appId);
		}
		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// removes permission from an app
	public void removePermissionFromApp(String name, String permission) throws Exception
	{
		int appId = getAppId(name);
		removePermissionFromAppId(name, permission, appId);
		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void addRegexToApp(String name, String regex, RegexType type) throws Exception
	{
		int appId = getAppId(name);
		if (appId == -1)
			throw new Exception("invalid app");
		String column = "null";
		switch (type)
		{
			case FILESYSTEM_BLACKLIST:
				column = APPS_COLUMN_FILESYSTEM_BLACKLIST;
				break;
			case FILESYSTEM_WHITELIST:
				column = APPS_COLUMN_FILESYSTEM_WHITELIST;
				break;
			case INTERNET_BLACKLIST:
				column = APPS_COLUMN_INTERNET_BLACKLIST;
				break;
			case INTERNET_WHITELIST:
				column = APPS_COLUMN_INTERNET_WHITELIST;
				break;
		}
		// insert the two foreign keys into the association table
		ContentValues values = new ContentValues();
		values.put(column, regex);
		db.update(APPS_TABLE_NAME, values, APPS_PRIMARY_ID + "=?", new String[] { appId + "" });

		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public String getRegexFromApp(String name, RegexType type) throws Exception
	{
		int appId = getAppId(name);
		if (appId == -1)
			throw new Exception("invalid app");
		String column = "null";
		switch (type)
		{
			case FILESYSTEM_BLACKLIST:
				column = APPS_COLUMN_FILESYSTEM_BLACKLIST;
				break;
			case FILESYSTEM_WHITELIST:
				column = APPS_COLUMN_FILESYSTEM_WHITELIST;
				break;
			case INTERNET_BLACKLIST:
				column = APPS_COLUMN_INTERNET_BLACKLIST;
				break;
			case INTERNET_WHITELIST:
				column = APPS_COLUMN_INTERNET_WHITELIST;
				break;
			default:
				throw new Exception("invalid regex type");
		}

		Cursor results = db.query(APPS_TABLE_NAME, null, APPS_PRIMARY_ID + "= ?", new String[] { appId + "" }, null, null, null);

		results.moveToFirst();
		String regex = results.getString(results.getColumnIndex(column));
		results.close();

		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
		return regex;
	}

	// get app's id and then call appPermissionToAppId
	public void addPermissionToApp(String name, String permission) throws Exception
	{
		int appId = getAppId(name);
		addPermissionToAppId(name, permission, appId);
		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Grab an app's db id
	private int getAppId(String name) throws Exception
	{
		Cursor results = db.query(APPS_TABLE_NAME, null, APPS_COLUMN_PKG_NAME + "= ?", new String[] { name }, null, null, null);
		if (results.getCount() == 0)
		{
			return -1;
		}
		results.moveToFirst();
		int appId = results.getInt(results.getColumnIndex(APPS_PRIMARY_ID));
		results.close();
		return appId;
	}

	private void removePermissionFromAppId(String name, String permission, int appId) throws Exception
	{
		Cursor results = db.query(POLICIES_TABLE_NAME, new String[] { POLICIES_PRIMARY_ID }, POLICIES_COLUMN_NAME + "= ?", new String[] { permission }, null, null, null);
		results.moveToFirst();
		int policyId = results.getInt(results.getColumnIndexOrThrow(POLICIES_PRIMARY_ID));

		ContentValues val = new ContentValues();
		val.put(APPS_POLICIES_COLUMN_ENABLED, false);

		db.update(APPS_POLICIES_TABLE_NAME, val, APPS_POLICIES_COLUMN_APPS_ID + "=? AND " + APPS_POLICIES_COLUMN_POLICIES_ID + "=?", new String[] { appId + "", policyId + "" });
		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// If we already know what the app's id is just add associate a policy with the app
	private void addPermissionToAppId(String name, String permission, int appId) throws Exception
	{
		Cursor results = db.query(POLICIES_TABLE_NAME, null, POLICIES_COLUMN_NAME + "= ?", new String[] { permission }, null, null, null);

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
		values.put(APPS_POLICIES_COLUMN_ENABLED, true);
		values.put(APPS_POLICIES_COLUMN_POLICIES_ID, policyId);
		db.insert(APPS_POLICIES_TABLE_NAME, null, values);
		results.close();
		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Insert an unseen policy into the table of policies
	private long insertPolicyToPolicies(String name)
	{
		ContentValues values = new ContentValues();
		values.put(POLICIES_COLUMN_NAME, name);
		try
		{
			Runtime.getRuntime().exec("chmod 744 /data/data/edu.berkeley.cs.cs161/databases/saved_apps");
		}
		catch (Exception e)
		{
			Log.e("Runtime Hack", "Error: " + e.getMessage());
			e.printStackTrace();
		}
		return db.insert(POLICIES_TABLE_NAME, null, values);
	}

	// Grab all permissions from a certain app
	public String[] getAppPermissions(String name) throws Exception
	{
		int appId = getAppId(name);

		if (appId == -1)
		{
			return null;
		}

		// Grab all permissions that belong to the app
		String query = "SELECT * FROM " + POLICIES_TABLE_NAME + " a INNER JOIN " + APPS_POLICIES_TABLE_NAME + " b ON a.id=b." + APPS_POLICIES_COLUMN_POLICIES_ID + " WHERE b."
				+ APPS_POLICIES_COLUMN_APPS_ID + "=?";

		return getPermissionsFromQuery(appId, query);
	}

	// Only grab the enabled app permissions
	public String[] getEnabledAppPermissions(String name) throws Exception
	{
		int appId = getAppId(name);

		// Grab all permissions that belong to the app
		String query = "SELECT * FROM " + POLICIES_TABLE_NAME + " a, " + APPS_POLICIES_TABLE_NAME + " b WHERE a.id=b." + APPS_POLICIES_COLUMN_POLICIES_ID + " AND b."
				+ APPS_POLICIES_COLUMN_APPS_ID + "=? AND b." + APPS_POLICIES_COLUMN_ENABLED + "=1;";

		return getPermissionsFromQuery(appId, query);
	}

	private String[] getPermissionsFromQuery(int appId, String query)
	{
		Cursor results = db.rawQuery(query, new String[] { appId + "" });

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

	public SavedApp getApp(String name) throws Exception
	{
		String[] permissions = getAppPermissions(name);
		if (permissions != null)
			return new SavedApp(name, getAppPermissions(name));
		return null;
	}

	public void close()
	{
		db.close();
	}
}
