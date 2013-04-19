package sensing.snapShots;

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;
import sensing.snapShots.R;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import sensing.snapShots.model.*;

public class GraphActivity extends Activity {

	public static Profile profile;
	public static BacMan model;
	public static double oz;
	public static double percent;
	public static int hunger;
	public static double bacreading;
	//public static int ml;
	Button addDrinkButton;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// Hides top bar to save space on graph activity
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.graphs);
		// model.canOfBeer();
		// drawGraph(model.predictTillFlat(900, 50));

		int weight = getIntent().getIntExtra("weight", 150);
		int freq = getIntent().getIntExtra("drinking_frequency", 3);
		boolean smoker = getIntent().getBooleanExtra("smoker", false);
		boolean gender = getIntent().getBooleanExtra("gender", false);
		int drinknum = getIntent().getIntExtra("drinks", 0);

		profile = new Profile(weight, freq, smoker, gender);
		model = new BacMan(profile, 3); // med full

		int i = 1;
		while (i <= drinknum && drinknum != 0) {
			model.standardDrink();
			i += 1;
		}

		oz = 0.0;
		percent = 0.0;
		bacreading = 0.0;
		updateText(oz,percent);
		selectHunger(1);
		hunger = 1;
		
		drawGraph(model.predictTillFlat(900, 50));

		addDrinkButton = (Button) findViewById(R.id.button_predict);

	}

	public void drawGraph(ArrayDeque<SnapShot> queue) {

		// Create GraphViewData array and populate with SnapShot queue
		Calendar firstTS = queue.peekFirst().timestamp;

		GraphViewData[] graphdata = new GraphViewData[queue.size()];
		GraphViewData[] line = new GraphViewData[queue.size()];
		//GraphViewData[] reading = new GraphViewData[queue.size()];

		double bac = 0.08;
		

		int i = 0;
		int lasttimestamp = 0;
		while (!queue.isEmpty()) {
			int time = BacMan.minutesSinceTimestamp(queue.peek().timestamp,
					firstTS);
			double bacval = queue.poll().bac;
			graphdata[i] = new GraphViewData(time, bacval);
			line[i] = new GraphViewData(time, bac);
			//reading[i] = new GraphViewData(time, bacreading);
			i += 1;
			lasttimestamp = time;
		}

		// add data to series
		GraphViewSeries bacSeries = new GraphViewSeries("B.A.C.",
				new GraphViewStyle(Color.rgb(255, 166, 15), 3), graphdata);
		// bacSeries.resetData(graphdata);
		GraphViewSeries lineSeries = new GraphViewSeries("LegalLimit",
				new GraphViewStyle(Color.rgb(250, 00, 00), 3), line);
		//GraphViewSeries lineReading = new GraphViewSeries(line);
		// lineSeries.resetData(line);

		Date date = new Date();
		Timestamp time = new Timestamp(date.getTime());

		// graph with dynamically generated horizontal and vertical labels
		GraphView graphView;
		graphView = new LineGraphView(this // context
				, "B.A.C." + time.toString() // heading
				
		);
		//((LineGraphView) graphView).setDrawBackground(true);

		graphView.addSeries(lineSeries);
		graphView.addSeries(bacSeries); // data
		//graphView.addSeries(lineReading);

		graphView.setViewPort(0, lasttimestamp);
		graphView.setScrollable(true);
		// optional - activate scaling / zooming
		graphView.setScalable(true);

		LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
		layout.addView(graphView);

		updateText(oz,percent);
		
	}

	public void addDrinkOnClick(View v) {
		EditText textOunces  = (EditText) findViewById(R.id.ounces_edittext);
		EditText textPercent = (EditText) findViewById(R.id.percent_text);
		
		oz = Double.parseDouble(textOunces.getText().toString());
		percent = Double.parseDouble(textPercent.getText().toString());
		
		double ratio = (double) percent / 100;
		double mconvert = (double) 29.57;
		double mili = (double ) (oz * ratio) * mconvert;
		int ml = (int) mili;
		//toast(Double.toString(mili));
		model.addAlcohol(ml);
		setContentView(R.layout.graphs);
		drawGraph(model.predictTillFlat(900, 50));
	}
	
	public void bacReading(View v) {
		Random generator = new Random();
		bacreading = generator.nextDouble() * 1.0;
		setContentView(R.layout.graphs);
		drawGraph(model.predictTillFlat(900, 50));
	}
	
	public void drinkSelect(View v) {
		Button shot = (Button) findViewById(R.id.shot_button);
		Button beer = (Button) findViewById(R.id.beer_button);
		Button pint = (Button) findViewById(R.id.pint_button);
		Button mix  = (Button) findViewById(R.id.mix_button);
		Button wine = (Button) findViewById(R.id.wine_button);
		
		shot.setEnabled(true);
		beer.setEnabled(true);
		pint.setEnabled(true);
		mix.setEnabled(true);
		wine.setEnabled(true);
		
		if( shot.getId() == ((Button)v).getId() ){
	          // it was the first button
			toast("Shot Selected");
			shot.setEnabled(false);
			oz = 1.5;
			percent = 40;
			
	    }
		if( beer.getId() == ((Button)v).getId() ){
	          // it was the second button
			toast("Beer Selected");
			beer.setEnabled(false);
			oz = 12;
			percent = 5;
	    }
		if( pint.getId() == ((Button)v).getId() ){
	          // it was the second button
			toast("Pint Selected");
			pint.setEnabled(false);
			oz = 16;
			percent = 5;
	    }
		if( mix.getId() == ((Button)v).getId() ){
	          // it was the second button
			toast("Cocktail Selected");
			mix.setEnabled(false);
			oz = 6.5;
			percent = 10;
	    }
		if( wine.getId() == ((Button)v).getId() ){
	          // it was the second button
			toast("Wine Selected");
			wine.setEnabled(false);
			oz = 10;
			percent = 13;
	    }
		
		updateText(oz,percent);
		
	}
	
	public void hungerChange(View v) {
		Button b1 = (Button) findViewById(R.id.hunger_1);
		Button b2 = (Button) findViewById(R.id.hunger_2);
		Button b3 = (Button) findViewById(R.id.hunger_3);
		Button b4 = (Button) findViewById(R.id.hunger_4);
		Button b5 = (Button) findViewById(R.id.hunger_5);
		
		if( b1.getId() == ((Button)v).getId() ){
			hunger = 1;
			selectHunger(hunger);
	    }
		if( b2.getId() == ((Button)v).getId() ){
			hunger = 2;
			selectHunger(hunger);
	    }
		if( b3.getId() == ((Button)v).getId() ){
			hunger = 3;
			selectHunger(hunger);
	    }
		if( b4.getId() == ((Button)v).getId() ){
			hunger = 4;
			selectHunger(hunger);
	    }
		if( b5.getId() == ((Button)v).getId() ){
			hunger = 5;
			selectHunger(hunger);
	    }
		
	}
	
	private void selectHunger(int level) {
		findViewById(R.id.hunger_1).setEnabled(true);
		findViewById(R.id.hunger_2).setEnabled(true);
		findViewById(R.id.hunger_3).setEnabled(true);
		findViewById(R.id.hunger_4).setEnabled(true);
		findViewById(R.id.hunger_5).setEnabled(true);
		if(level==1) findViewById(R.id.hunger_1).setEnabled(false);
		if(level==2) findViewById(R.id.hunger_2).setEnabled(false);
		if(level==3) findViewById(R.id.hunger_3).setEnabled(false);
		if(level==4) findViewById(R.id.hunger_4).setEnabled(false);
		if(level==5) findViewById(R.id.hunger_5).setEnabled(false);
	}
	
	private void toast(String msg){
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(this, text, duration);
		toast.show();
	}
	
	private void updateText(double ounces, double percent) {
		
		EditText textOunces  = (EditText) findViewById(R.id.ounces_edittext);
		EditText textPercent = (EditText) findViewById(R.id.percent_text);
		
		textOunces.setText(Double.toString(ounces), TextView.BufferType.EDITABLE);
		textPercent.setText(Double.toString(percent), TextView.BufferType.EDITABLE);
		
	}
	
	public void setHungerText(int val) {
		TextView hungerText = (TextView) findViewById(R.id.percent_text);
		hungerText.setText("Hunger Level: " + Integer.toString(val));
	}
	
	

}