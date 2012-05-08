package edu.berkeley.cs.cs161;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddRegex extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_reg_ex);
        
        Button confirmButton = (Button) findViewById(R.id.confirmButton);
    	EditText edit = (EditText) findViewById(R.id.addRegExText);

		final Intent i = new Intent(this, Additional.class);
		i.putExtra("RegexType", getIntent().getIntExtra("RegexType", 0));
		i.putExtra("RegexString", edit.getText().toString());
        confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}
}
