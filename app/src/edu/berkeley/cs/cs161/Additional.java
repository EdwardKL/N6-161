package edu.berkeley.cs.cs161;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.berkeley.cs.cs161.db.SavedAppsSQLiteHelper.RegexType;

public class Additional extends ListActivity {
	private static final int FBlack = Menu.FIRST;
	private static final int FWhite = Menu.FIRST + 1;
	private static final int IBlack = Menu.FIRST + 2;
	private static final int IWhite = Menu.FIRST + 3;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additional);
		fillData();

        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
        		Builder builder = new AlertDialog.Builder(parent.getContext());
        		builder.setTitle("Warning!");
        		builder.setMessage("Are you sure you would like to edit this regex?");
        		builder.setCancelable(true);
        		builder.setNeutralButton("Close", new OnClickListener() {
        			public void onClick(DialogInterface dialog, int id) {
        				dialog.cancel();
        			}
        		});
        		builder.setPositiveButton("Edit", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						RegexType r = RegexType.values()[position];
						addNewRegEx(r);
					}
				});
        		AlertDialog help = builder.create();
        		help.show();
        	}
		});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, FBlack, 0, "Edit File System BlackList");
        menu.add(0, IBlack, 0, "Edit Internet BlackList");
        menu.add(0, FWhite, 0, "Edit File System WhiteList");
        menu.add(0, IWhite, 0, "Edit Internet WhiteList");
        return true;
	}
	
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	int temp = item.getItemId();
    	if (temp == IWhite || temp == IBlack || temp == FWhite || temp == FBlack) {
                RegexType type = RegexType.values()[temp-Menu.FIRST];
    			addNewRegEx(type);
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    private void addNewRegEx (RegexType type) {
    	Intent i = new Intent(this, AddRegex.class);
    	i.putExtra("RegexType", type);
    	i.putExtra("RegexString", (String)PolicyEdit.getRegexFromDb(type));
    	startActivityForResult(i, 0);
    }
    
    private void fillData() {
    	String BlackFile = (String)PolicyEdit.getRegexFromDb(RegexType.values()[0]);
    	String WhiteFile = (String)PolicyEdit.getRegexFromDb(RegexType.values()[1]);
    	String BlackInternet = (String)PolicyEdit.getRegexFromDb(RegexType.values()[2]);
    	String WhiteInternet = (String)PolicyEdit.getRegexFromDb(RegexType.values()[3]);
    	ArrayAdapter<String> aa = new ArrayAdapter<String>(this, 
    			android.R.layout.simple_list_item_1, new String[]{
    			"File System Black List: " + BlackFile, "File System White List: " + WhiteFile, 
    			"Internet Black List: " + BlackInternet, "Internet White List: " + WhiteInternet});
        setListAdapter(aa);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	int typeId = intent.getExtras().getInt("RegexType");
    	RegexType type = RegexType.values()[typeId];
    	String regex = intent.getExtras().getString("RegexString");
    	PolicyEdit.insertRegexIntoDb(regex, type);
    	fillData();
    }
}
