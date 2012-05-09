package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.berkeley.cs.cs161.db.SavedAppsSQLiteHelper.RegexType;

public class Regexes extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regexes);
		
		final EditText blackFile = (EditText)findViewById(R.id.fBRegex);
		final EditText whiteFile = (EditText)findViewById(R.id.fWRegex);
		final EditText blackInternet = (EditText)findViewById(R.id.iBRegex);
		final EditText whiteInternet = (EditText)findViewById(R.id.iWRegex);
		Button saveButton = (Button)findViewById(R.id.save);
		
		blackFile.setText(PolicyEdit.getRegexFromDb(RegexType.values()[0]));
		whiteFile.setText(PolicyEdit.getRegexFromDb(RegexType.values()[1]));
		blackInternet.setText(PolicyEdit.getRegexFromDb(RegexType.values()[2]));
		whiteInternet.setText(PolicyEdit.getRegexFromDb(RegexType.values()[3]));
		
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PolicyEdit.insertRegexIntoDb(blackFile.getText().toString(), RegexType.values()[0]);
				PolicyEdit.insertRegexIntoDb(whiteFile.getText().toString(), RegexType.values()[1]);
				PolicyEdit.insertRegexIntoDb(blackInternet.getText().toString(), RegexType.values()[2]);
				PolicyEdit.insertRegexIntoDb(whiteInternet.getText().toString(), RegexType.values()[3]);
				Toast toast = Toast.makeText(getApplicationContext(), "Your changes have been saved", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
		});
	}
}
