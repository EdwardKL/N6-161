package android.barley; 
import android.util.Log;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BarleyEnforcer {

	private static final String LOGTAG = "BarleyEnforcer";
	public static final String INTERNET = "INTERNET";
	public static final String READ_PHONE_STATE = "READ_PHONE_STATE";
	
	// If application @name doesn't have all of @requested_permissions, deny.
	// Return false if denied, true if allowed.
	public static boolean allowed(String name, String [] requested_permissions) {
		Log.e(LOGTAG, "Begin enforcement for "+name);
		for(int k=0; k< requested_permissions.length;k++){
			Log.e(LOGTAG, "Requested Permission: "+requested_permissions[k]);
		}
	    	SQLiteDatabase db;
	    	Boolean deny = true;
	    	try{
			db = SQLiteDatabase.openDatabase("/data/data/edu.berkeley.cs.cs161/databases/saved_apps", null, SQLiteDatabase.OPEN_READONLY);
			int appId = -1;
			Cursor results = db.query("saved_apps",null,"pkg_name = ?",new String[] {name},null,null,null);
    		if (results.getCount() != 0){
    			Log.e(LOGTAG, "Found results!");
    			results.moveToFirst();
    			appId = results.getInt(results.getColumnIndex("id"));
    			results.close();
    			results = db.rawQuery("SELECT * FROM all_policies a INNER JOIN apps_policies b ON a.id=b.policy_id WHERE b.app_id=? AND b.enabled=1", new String[] {appId + ""});
    			results.moveToFirst();
    			int count = results.getCount();
    			int found = 0;
    			String[] permissions = new String[count];
    			for (int i=0; i < count; i++){
    				permissions[i] = results.getString(results.getColumnIndex("name"));
    				Log.e(LOGTAG, "Found permission: "+permissions[i]);
    				for (int j=0; j < requested_permissions.length; j++){
	    				if(permissions[i].contains(requested_permissions[j])){
						Log.e(LOGTAG, "Found match: "+requested_permissions[j]);
	    					found += 1;
	    					if(requested_permissions.length == found){
	    						results.close();
	    						db.close();
	        					Log.e(LOGTAG, "Not denying");
	        					return true;
	    					}
	    					break;
	    				}
    				}
    				results.moveToNext();
    			}
    			results.close();
    		} else { results.close();db.close(); return true; }
    		db.close();
		} catch (Exception e){
			Log.e(LOGTAG, "Error: "+e.getMessage());
			e.printStackTrace();
			return true;
		}
		return false;
	}


}
