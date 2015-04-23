package nl.inholland.calculator;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TextView 	txtInput;
	public  String 		value ="";
	public  Toast		welcomeToast;
	
	
		@SuppressLint("ShowToast")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			//set values to text input field
			txtInput = (TextView)findViewById(R.id.box1).findViewById(R.id.txtInput);
			
			
			SharedPreferences settings = getSharedPreferences("CALCULATOR_PREFER", 0);
			txtInput.setText(settings.getString("expression", ""));
			
			if(txtInput.getText() == ""){
				welcomeToast = Toast.makeText(MainActivity.this, "Welcome to Calculator", 6000);
				welcomeToast.setGravity(Gravity.CENTER, 0, 0);
				welcomeToast.show();
			} 
			else {
				welcomeToast = Toast.makeText(MainActivity.this, "Your temporary Data has been restored", 6000);
				welcomeToast.setGravity(Gravity.CENTER, 0, 0);
				welcomeToast.show();
			}
			
			txtInput = (TextView) findViewById(R.id.txtInput);
			final Button buttonEquals = (Button) findViewById(R.id.button_equals);
			
			buttonEquals.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this, CalculationActitity.class);
					if(txtInput.getText() == ""){
						intent.putExtra("txtInput", "No expression entered");
					}
					else{
						intent.putExtra("txtInput", txtInput.getText().toString());
					}
					startActivity(intent);
					
				}
			});
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/* The onOptionsItemSelected method has been overridden
	 * to set menu item actions
	 * (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_view_storage){
			Intent intent = new Intent(MainActivity.this, CalculationActitity.class);
			intent.putExtra("txtInput", txtInput.getText().toString());
			startActivity(intent);
		}
		
		if(item.getItemId() == R.id.action_exit){
				
				/* The menu action exit triggers an dialogs box,
				 * which will prompt the user to specify their intent. 
				 * bases on the intent, the application will be exited
				 * or dialog will be cancelled
				 */
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("Are you sure you want to exit?");
				builder.setCancelable(false);
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MainActivity.this.finish();	
					}
				});
				
			   builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			   AlertDialog alert = builder.create();
			   alert.show();
			}
		return super.onOptionsItemSelected(item);
	}
	
	/* This onStop method has been overridden to save temporary 
	 * input data to the preference file.
	 * This will prevent from the user loosing any unsaved expressions
	 * @see android.app.Activity#onStop()
	 */

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		SharedPreferences settings = getSharedPreferences("CALCULATOR_PREFER", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("expression", txtInput.getText().toString());
		editor.commit();
	}

	/* This method is executed when a button is pressed.
	 * The value of the button will be read and then
	 * sets it on the text view
	 */
	
	  public void updateDisplay(View sender){
	  	 Button button = (Button) sender; 
	  	 value = value + button.getText().toString();
	  	 txtInput.setText(value);
	  	 Log.d("bug", "" + value);
	  }
	  
	/*This method is executed when CE button is pressed
	 *The text view memory and field would be cleared
	 *and set to 0
	 */	  
	  
	  public void clearDisplay(View sender) {
	  		value = "";
	  		txtInput.setText(value);
	  }

}
