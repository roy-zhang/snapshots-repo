package sensing.snapShots;

import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;

import sensing.snapShots.model.BacMan;

import sensing.snapShots.model.SnapShot;
import sensing.snapShots.storage.StorageMan;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

public class GraphFragment extends Fragment {

	public static double oz;
	public static double percent;
	public static int hunger;
	public static double bacreading;
	//public static int ml;
	Button addDrinkButton;
	
	public static BacMan model;
	
	
	public static GraphFragment newInstance(){
		
		GraphFragment f = new GraphFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString("prefName", "---");
        f.setArguments(bdl);
        return f;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.graphs, container, false);
        return view;
    }
    
    
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

     		// model.canOfBeer();
     		// drawGraph(model.predictTillFlat(900, 50));

        	toast(StorageMan._profile.toString());

     		 model = new BacMan(StorageMan._profile, 3); // med full

     		oz = 0.0;
     		percent = 0.0;
     		bacreading = 0.0;
     		updateText(oz,percent);
     		selectHunger(1);
     		hunger = 1;
     		
     		drawGraph(model.predictTillFlat(900, 50));

     		addDrinkButton = (Button) getView().findViewById(R.id.button_predict);
		
		toast("created");
    }
    
    public void drawGraph(ArrayDeque<SnapShot> queue) {
    	
		toast("queue size "+queue.size());

		View graphView = sensing.snapShots.model.Graph.getGraphView(getActivity(), queue);

		

		updateText(oz,percent);
		
		FrameLayout layout = (FrameLayout) getView().findViewById(R.id.graph1);
		layout.removeAllViewsInLayout();
		layout.addView(graphView);


	}
    
	public void addDrinkOnClick(View v) {
		EditText textOunces  = (EditText) getView().findViewById(R.id.ounces_edittext);
		EditText textPercent = (EditText) getView().findViewById(R.id.percent_text);
		
		oz = Double.parseDouble(textOunces.getText().toString());
		percent = Double.parseDouble(textPercent.getText().toString());
		
		double ratio = (double) percent / 100;
		double mconvert = (double) 29.57;
		double mili = (double ) (oz * ratio) * mconvert;
		int ml = (int) mili;
		toast(Double.toString(mili));
		model.addAlcohol(ml);
		
		//setContentView(R.layout.graphs);
		
		ArrayDeque<SnapShot> listed = model.predictTillFlat(900, 50);
		toast(listed.size() + "size");
		toast(listed.getFirst().toString() );
		toast(listed.getLast().toString() );
		drawGraph(model.predictTillFlat(900, 50));
	}
	
	public void bacReading(View v) {
		Random generator = new Random();
		bacreading = generator.nextDouble() * 1.0;
		// setContentView(R.layout.graphs);
		drawGraph(model.predictTillFlat(900, 50));
	}
	
	public void drinkSelect(View v) {
		Button shot = (Button) getView().findViewById(R.id.shot_button);
		Button beer = (Button) getView().findViewById(R.id.beer_button);
		Button pint = (Button) getView().findViewById(R.id.pint_button);
		Button mix  = (Button) getView().findViewById(R.id.mix_button);
		Button wine = (Button) getView().findViewById(R.id.wine_button);
		
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
		Button b1 = (Button) getView().findViewById(R.id.hunger_1);
		Button b2 = (Button) getView().findViewById(R.id.hunger_2);
		Button b3 = (Button) getView().findViewById(R.id.hunger_3);
		Button b4 = (Button) getView().findViewById(R.id.hunger_4);
		Button b5 = (Button) getView().findViewById(R.id.hunger_5);
		
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
		getView().findViewById(R.id.hunger_1).setEnabled(true);
		getView().findViewById(R.id.hunger_2).setEnabled(true);
		getView().findViewById(R.id.hunger_3).setEnabled(true);
		getView().findViewById(R.id.hunger_4).setEnabled(true);
		getView().findViewById(R.id.hunger_5).setEnabled(true);
		if(level==1) getView().findViewById(R.id.hunger_1).setEnabled(false);
		if(level==2) getView().findViewById(R.id.hunger_2).setEnabled(false);
		if(level==3) getView().findViewById(R.id.hunger_3).setEnabled(false);
		if(level==4) getView().findViewById(R.id.hunger_4).setEnabled(false);
		if(level==5) getView().findViewById(R.id.hunger_5).setEnabled(false);
	}
	
	private void toast(String msg){
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getActivity(), text, duration);
		toast.show();
	}
	
	private void updateText(double ounces, double percent) {
		
		EditText textOunces  = (EditText) getView().findViewById(R.id.ounces_edittext);
		EditText textPercent = (EditText) getView().findViewById(R.id.percent_text);
		
		textOunces.setText(Double.toString(ounces), TextView.BufferType.EDITABLE);
		textPercent.setText(Double.toString(percent), TextView.BufferType.EDITABLE);
		
	}
	
	public void setHungerText(int val) {
		TextView hungerText = (TextView) getView().findViewById(R.id.percent_text);
		hungerText.setText("Hunger Level: " + Integer.toString(val));
	}
	
	

}