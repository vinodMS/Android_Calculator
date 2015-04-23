package nl.inholland.calculator;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class CalculationActitity extends Activity {
	
	TextView 				txtStack;
	ListView				listView;
	Button 					buttonBack;
	String					receivedEquation, equation, result;
	ArrayList<String> 		listItems = new ArrayList<String>();
	ArrayAdapter<String> 	arrayAdapter;
	SQLiteDatabase 			storage;
	Toast					noinputToast;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculation);
		//This is where the result would be printed
		txtStack = (TextView) findViewById(R.id.txtStack);
		
		//Here we receive the equation from main activity
		receivedEquation = getIntent().getExtras().getString("txtInput");
		
		//add the equation to list
		listItems.add(receivedEquation);
		
		//This is where the received equation will appear
		listView = (ListView) findViewById(R.id.listView);
		
		//Initializing the adapter to get list items
		arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItems);
		
		//This is where the the list view would set the received equation to display  
		listView.setAdapter(arrayAdapter);
		
		//Initializing database to store equation
		storage = openOrCreateDatabase("LOCAL_STORE",MODE_PRIVATE, null);
		
		buttonBack = (Button) findViewById(R.id.button_return);
		buttonBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	CalculationActitity.this.finish();	
            }
        });
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculation_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(item.getItemId() == R.id.action_exit){
				
				AlertDialog.Builder builder = new AlertDialog.Builder(CalculationActitity.this);
				builder.setMessage("Are you sure you want to exit?");
				builder.setCancelable(false);
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CalculationActitity.this.finish();	
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
		
		if(item.getItemId() == R.id.action_show_expressions){
				retrieveData(listView);
			}
		
		if(item.getItemId() == R.id.action_reset_database){
			resetData();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	 public void getResult(View v) {
		KeyInputParser stack = new KeyInputParser();
		if(receivedEquation.equals("No expression entered")){
			noinputToast = Toast.makeText(CalculationActitity.this, "No expression found, please go back and enter an expression", 6000);
			noinputToast.setGravity(Gravity.CENTER, 0, 0);
			noinputToast.show();
		}
		else{
			float result = stack.processEquation(receivedEquation);
			txtStack.setText(Float.toString(result));
		}
	 }
	 
	 public void backupData(View sender) {
		//Creating table if it does not already exist
		 storage.execSQL("CREATE TABLE IF NOT EXISTS INHolland_Calculator(equation VARCHAR, result VARCHAR);");
		 
		 if(txtStack.getText() == ""){
				Toast eptyToast = Toast.makeText(CalculationActitity.this, "Cannot backup data! Please make sure to enter expression", 12000);
				eptyToast.setGravity(Gravity.CENTER, 0, 0);
				eptyToast.show();
			 }
			 else{
				 equation =receivedEquation.toString();
				 result =txtStack.getText().toString();
				 storage.execSQL("INSERT INTO  INHolland_Calculator VALUES('"+equation+"','"+result+"');");
				 new BackupTask(getApplicationContext()).execute(0);
			 }	 
		 
	 }
	 
	 public class BackupTask extends AsyncTask<Integer, Integer, Void>{
		 
		 private NotificationHelp mNotificationHelper;
		    public BackupTask(Context context){
		        mNotificationHelper = new NotificationHelp(context);
		    }
		    
		    protected void onPreExecute(){
		        //Create the notification in the statusbar
		        mNotificationHelper.createNotification();
		    }


	 	@Override
	    protected Void doInBackground(Integer... integers) {
	        /*time to wait until the process is over
	         * 
	         */
	 		for (int i=0;i<=100;i += 20)
	            {
	                try {
	                    Thread.sleep(1000);
	                    publishProgress(i);

	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        return null;
	    }
	 
			 public void onPostExecute(String string) {
				 equation =receivedEquation.toString();
				 result =txtStack.getText().toString();
				 storage.execSQL("INSERT INTO  INHolland_Calculator VALUES('"+equation+"','"+result+"');");
				 
				 mNotificationHelper.completed();
			 }
			 
			 protected void onProgressUpdate(Integer... progress) {
			        //This method runs on the UI thread, it receives progress updates
			        //from the background thread and publishes them to the status bar
			        mNotificationHelper.progressUpdate(progress[0]);
			    }
	 }
	 
	 public void resetData() {
		 storage.execSQL("DELETE FROM INHolland_Calculator;");
	 }
	 
	 @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public void retrieveData(View sender)	{
	     TableRow tableRow;
	     TextView headerNumber;
	     TextView headerEquation;
	     TextView headerResult;
	     TextView rowNumber;
	     TextView rowsEquation;
	     TextView rowsResult;
	     
	     
	     View view = new View(this);
	     view.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, 1));
	     view.setBackgroundColor(Color.rgb(51, 51, 51));
	     
	     Cursor cursor = storage.rawQuery("SELECT * from INHolland_Calculator", null);
	     int count= cursor.getCount();
	     cursor.moveToFirst();
	     
	     TableLayout tableLayout = new TableLayout(getApplicationContext());
	     tableLayout.setBackgroundColor(Color.GRAY);
	     tableLayout.setScrollBarDefaultDelayBeforeFade(0);
	     tableLayout.setScrollBarSize(10);
	     tableLayout.setVerticalScrollbarPosition(10);
	     tableLayout.setVerticalScrollBarEnabled(true);
	    
	     tableRow = new TableRow(getApplicationContext());

	     headerNumber= new TextView(getApplicationContext());
	     headerNumber.setText("#");
	     headerNumber.setTypeface(null, Typeface.BOLD);
	     headerNumber.setTextColor(Color.BLUE);
	     headerNumber.setTextSize(20);
	     headerNumber.setPadding(25, 20, 25, 20);
	     tableRow.addView(headerNumber);	
	     
	     headerEquation= new TextView(getApplicationContext());
	     headerEquation.setText("Equation");
	     headerEquation.setTypeface(null, Typeface.BOLD);
	     headerEquation.setTextColor(Color.BLUE);
	     headerEquation.setTextSize(20);
	     headerEquation.setPadding(25, 20, 25, 20);
	     tableRow.addView(headerEquation);
	    
	     headerResult= new TextView(getApplicationContext());
	     headerResult.setText("Result");
	     headerResult.setTypeface(null, Typeface.BOLD);
	     headerResult.setTextColor(Color.BLUE);
	     headerResult.setTextSize(20);
	     headerResult.setPadding(25, 20, 25, 20);
	     tableRow.addView(headerResult);
	     
	     tableLayout.addView(tableRow);
	      	
	     for (Integer i = 0; i < count; i++)	{
	          tableRow = new TableRow(getApplicationContext());
	          
	          rowNumber = new TextView(getApplicationContext());
	          rowNumber.setText(String.valueOf(i+1));
	          rowNumber.setTextSize(18);
	          rowNumber.setPadding(25, 20, 25, 20);
	          
	          
	          rowsEquation = new TextView(getApplicationContext());
	          rowsEquation.setText(cursor.getString(cursor.getColumnIndex("equation")));
	          rowsEquation.setTextSize(18);
	          rowsEquation.setPadding(25, 20, 25, 20);
	          
	          rowsResult = new TextView(getApplicationContext());
	          rowsResult.setText(cursor.getString(cursor.getColumnIndex("result")));
	          rowsResult.setTextSize(18);
	          rowsResult.setPadding(25, 20, 25, 20);
	          
	          tableRow.addView(rowNumber);
	          tableRow.addView(rowsEquation);
	          tableRow.addView(rowsResult);
	          tableLayout.addView(tableRow);
	          
	          cursor.moveToNext() ;
	     }
	      setContentView(tableLayout);
	    
	    buttonBack = new Button(this);
	    buttonBack.setText("Return to Calculator");
	    buttonBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	CalculationActitity.this.finish();	
            }
        });
	    
		tableLayout.addView(buttonBack);
	 }
	 
}
