package edu.berkeley.barlene;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.berkeley.barlene.R;

@SuppressWarnings("deprecation")
public class BarleneActivity extends Activity
{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button yes = (Button) findViewById(R.id.buttonYes);
		Button no = (Button) findViewById(R.id.buttonNo);

		yes.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				for (int i = 0; i < 5; i++)
					sendSMS("OH MY GOSH, SO THE YES!!", "5555218135");
			}
		});

		no.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				for (int i = 0; i < 5; i++)
					sendSMS("WELL TOO BAD, STILL YES!!", "5555218135");
			}
		});

	}

	private void sendSMS(String textContent, String destination)
	{
		SmsManager sm = SmsManager.getDefault();
		// here is where the destination of the text should go
		sm.sendTextMessage(destination, null, textContent, null, null);
	}
}