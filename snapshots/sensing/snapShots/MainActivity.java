package sensing.snapShots;


import sensing.snapShots.R;
import sensing.snapShots.storage.StorageMan;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	
    //add fragments here
    public static String[] tabNames = new String[]{"profile", "graph", "sensor"};
    public static Fragment[] fragmentLookup = new Fragment[]{
    		new ProfileSelectionFragment(),
    		new GraphFragment(),
    		new GraphFrag()
    };
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
        // Grab the instance of TabFragment that was included with the layout and have it
        // launch the initial tab.
        ((MenuTabFragment) getFragmentManager().findFragmentById(R.id.tabs)).switchTabTo(0);
	}	
	
	
	
// graph buttons going to this activity from xml onclick
	public void hungerChange(View v){
		((GraphFragment) fragmentLookup[1]).hungerChange(v);
	}
	public void drinkSelect(View v){
		((GraphFragment) fragmentLookup[1]).drinkSelect(v);
	}
	public void bacReading(View v){
		((GraphFragment) fragmentLookup[1]).bacReading(v);
	}
	public void addDrinkOnClick(View v){
		((GraphFragment) fragmentLookup[1]).addDrinkOnClick(v);
	}
	
	
	
	
}
