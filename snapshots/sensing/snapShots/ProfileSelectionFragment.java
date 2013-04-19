package sensing.snapShots;

import java.util.Set;

import sensing.snapShots.model.Profile;
import sensing.snapShots.storage.StorageMan;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class ProfileSelectionFragment extends Fragment implements OnItemSelectedListener {

	private StorageMan storageMan;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	View view = inflater.inflate(R.layout.profile_selection, container, false);
        
        return view;
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("prefName", storageMan.PrefsName);
    }
    
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        if (savedInstanceState != null && savedInstanceState.containsKey("prefName"))
        	storageMan = new StorageMan(getActivity(), savedInstanceState.getString("prefName"));
        

        ((Button) getView().findViewById(R.id.Save_Profile)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) { // takes edited fields and
											// stores them to
											// preferences
				saveInfoFields();
				populateSpinner();
			}
		});

		((Button) getView().findViewById(R.id.Delete_Profile)).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						
						if (deletePref())
							toast(" pref deleted");
						else
							toast(" pref not deleted");
						populateSpinner();
					}
				});

		((Spinner) getView().findViewById(R.id.profile_selector)).setOnItemSelectedListener(this);

		populateSpinner();
		
		resetInfoFields(storageMan);
    }
    
public Profile getProfile(){
	return storageMan.getProfile();
}
    
    
    //ui stuff
    
	private void populateSpinner() {
		// returns storageMan set to the person's preferences
		// which I guess just lets you save profile and get profile

		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				getActivity(), android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Set<String> profiles = StorageMan.prefsAvailable( getActivity() );

		if (profiles.size() > 0) {

			for (String name : profiles)
				adapter.add(name);

			Spinner spinner = (Spinner) getActivity().findViewById(R.id.profile_selector);
			spinner.setAdapter(adapter);

			if (null != storageMan)
				spinner.setSelection(adapter.getPosition(storageMan.PrefsName));
			else{
				spinner.setSelection(0);
				storageMan = new StorageMan(getActivity(), adapter.getItem(0).toString() );
			}

		} else {
			Profile defaultProfile = new Profile();
			storageMan = new StorageMan(getActivity(), "-");
			storageMan.saveProfile(defaultProfile);
			resetInfoFields(storageMan);

			populateSpinner();
		}

	}
	
	

	private void resetInfoFields(StorageMan storageMan) {
		Profile profile = storageMan.getProfile();

		((EditText) getActivity().findViewById(R.id.profileTable_name))
				.setText(storageMan.PrefsName);

		((EditText) getActivity().findViewById(R.id.profileTable_weight))
				.setText(profile.weight + "" );

		if (profile.isMale)
			((RadioButton) getActivity().findViewById(R.id.profileTable_male))
					.setChecked(true);
		else
			((RadioButton) getActivity().findViewById(R.id.profileTable_female))
					.setChecked(true);

		((CheckBox) getActivity().findViewById(R.id.profileTable_smoker))
				.setChecked(profile.smoker);

		((RatingBar) getActivity().findViewById(R.id.profileTable_drinker))
				.setRating(profile.drinker);
	}

	private void saveInfoFields() {
		// assumes storageMan exists to save to if prefName is the same

		String name = ((EditText) getActivity().findViewById(R.id.profileTable_name))
				.getText().toString();
		int weight = Integer
				.parseInt(((EditText) getActivity().findViewById(R.id.profileTable_weight))
						.getText().toString());
		boolean isMale = ((RadioButton) getActivity().findViewById(R.id.profileTable_male))
				.isChecked();
		boolean smoker = ((CheckBox) getActivity().findViewById(R.id.profileTable_smoker))
				.isChecked();
		int drinker = (int) ((RatingBar) getActivity().findViewById(R.id.profileTable_drinker))
				.getRating();

		if (!name.equals(storageMan.PrefsName)) {
			storageMan = new StorageMan(getActivity(), name);
		}

		storageMan.saveProfile(new Profile(weight, drinker, smoker, isMale));
		
		toast("pref saved");
	}

	private boolean deletePref() {
		String name = storageMan.PrefsName;
		storageMan = null;
		
		return StorageMan.delete(getActivity(), name);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String profileStr = (String) parent.getItemAtPosition(pos);
		if (!profileStr.equals(storageMan.PrefsName)){
			storageMan = new StorageMan(getActivity(), profileStr);
			resetInfoFields(storageMan);
			
			StorageMan._profile = storageMan.getProfile();
		}
		
		
		toast(profileStr + " selected");
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// populateSpinner();
		toast("nothing selected");
	}
	
	private void toast(String msg){
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getActivity(), text, duration);
		toast.show();
	}


}