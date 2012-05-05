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
}
