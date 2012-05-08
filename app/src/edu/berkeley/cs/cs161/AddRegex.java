package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddRegex extends Activity {
	private String regex;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
		regex = extras.getString("RegexString");
        setContentView(R.layout.add_reg_ex);
        
        Button confirmButton = (Button) findViewById(R.id.confirmButton);
    	final EditText edit = (EditText) findViewById(R.id.addRegExText);
    	
    	edit.setText(regex);

        confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = edit.getText().toString();
				Intent i = new Intent();
				i.putExtra("RegexType", getIntent().getIntExtra("RegexType", 0));
				i.putExtra("RegexString", text);
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
}
