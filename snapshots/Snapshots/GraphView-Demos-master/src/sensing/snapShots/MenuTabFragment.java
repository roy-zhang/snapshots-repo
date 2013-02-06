package sensing.snapShots;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MenuTabFragment extends ListFragment {
    int mCurCheckPosition = 0;
    //add fragments here
    String[] tabNames = new String[]{"profile", "graph", "sensor"};

    

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, tabNames));
       
        if (savedInstanceState != null) 
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        switchTabTo(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	switchTabTo( position);
    }

    
    public void switchTabTo( int index) {
    	
    	//if (mCurCheckPosition == index) //do nothing
    	//	return;
    	
    	if (getFragmentManager() != null){
  

    		FragmentTransaction transaction = getFragmentManager().beginTransaction();
    		transaction.replace(R.id.fragment_content, MainActivity.fragmentLookup[index]);
	        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	        transaction.commit();


	    	mCurCheckPosition = index;
	    	toast("index "+index);
	    	getListView().setItemChecked(index, true);
    	}

    }
    
   
	private void toast(String msg){
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getActivity(), text, duration);
		toast.show();
	}

}