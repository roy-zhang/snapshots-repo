package sensing.snapShots.storage;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Set;

import sensing.snapShots.model.BacMan;
import sensing.snapShots.model.Profile;
import sensing.snapShots.model.SnapShot;

import android.content.Context;
import android.content.SharedPreferences;

//example usage:

// setOfNames = StorageMan.perfsAvaliable(context);

//bobPrefs = new StorageMan(context, "Bob");
//bobProfile = bobPrefs.getProfile()
// bobPrefs.saveProfile(bobProfile)

public class StorageMan {

	private Context context;
	public String PrefsName;
	
	public static Profile _profile = new Profile();
	public static BacMan _bacMan;
	public static  ArrayDeque<SnapShot> _snapShots;
	
	private static final String Pref_Listing = "listing";
	public static Set<String> prefsAvailable(Context context) {

		SharedPreferences listing = context.getSharedPreferences(Pref_Listing,	0);
		return listing.getAll().keySet();
	}
	// use shared preferences for key value pairs of primitives
	public StorageMan(Context con, String WhosePreference) {
		context = con;
		PrefsName = WhosePreference;

		if (!prefsAvailable(context).contains(PrefsName))
			addPrefToListing(con, PrefsName);
	}

	// setting that profile
	public void saveProfile(Profile profile) {
		setValue("weight", profile.weight);
		setValue("drinker", profile.drinker);
		setValue("smoker", profile.smoker);
		setValue("isMale", profile.isMale);
		setValue("elimRateAdjuster", profile.elimRateAdjuster);
		setValue("absRateAdjuster", profile.absRateAdjuster);
	}

	public Profile getProfile() {
		Profile returnMe = new Profile((Integer) getValue("weight",
				Integer.class), (Integer) getValue("drinker", Integer.class),
				(Boolean) getValue("smokes", Boolean.class),
				(Boolean) getValue("isMale", Boolean.class));

		returnMe.absRateAdjuster = (Float) getValue("absRateAdjuster",
				Float.class);
		returnMe.elimRateAdjuster = (Float) getValue("elimRateAdjuster",
				Float.class);

		return returnMe;
	}

	
	//  private methods---------------------------
	private Object getValue(String key, Class type) {
		return getValue(context, PrefsName, key, type);
	}

	private void setValue(String key, Object val) {
		storePair(context, PrefsName, key, val);
	}

	public static boolean delete(Context context, String prefsName) {
		SharedPreferences.Editor editor = context.getSharedPreferences(prefsName, 0).edit();
		editor.clear();
		editor.commit();
		
		return context.deleteFile("/data/data/sensing.snapShots/shared_prefs/"+prefsName+".xml");
	}

	// using type of key to get type of value
	private static Object getValue(Context context, String filename, String key,
			Class type) {
		SharedPreferences prefs = context.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		if (type.equals(							String.class))
			return prefs.getString((String) key, "");
		else if (type.equals(						Boolean.class))
			return prefs.getBoolean((String) key, true);
		else if (type.equals(						Float.class))
			return prefs.getFloat((String) key, 0.0f);
		else if (type.equals(						Integer.class))
			return prefs.getInt((String) key, 0);
		else if (type.equals(						Long.class))
			return prefs.getLong((String) key, 0);
		else if (type.equals(						Set.class)) {
			return prefs
					.getStringSet((String) key, new LinkedHashSet<String>());
		} else
			try {
				throw new Exception( type.toString()
						+ " type not supported");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	}

	private static void storePair(Context context, String filename, String key,
			Object value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				filename, 0).edit();

		if (value.getClass().equals(String.class))
			editor.putString(key, (String) value);
		else if (value.getClass().equals(Boolean.class))
			editor.putBoolean(key, (Boolean) value);
		else if (value.getClass().equals(Float.class))
			editor.putFloat(key, (Float) value);
		else if (value.getClass().equals(Long.class))
			editor.putLong(key, (Long) value);
		else if (value.getClass().equals(Integer.class))
			editor.putInt(key, (Integer) value);
		else if (value.getClass().equals(Set.class))
			editor.putStringSet(key, (Set<String>) value);
		else
			try {
				throw new Exception(value.getClass().toString()
						+ " type not supported");
			} catch (Exception e) {
				e.printStackTrace();
			}

		editor.commit();
	}

	private static void addPrefToListing(Context context, String filename) {
		storePair(context, Pref_Listing, filename, filename);
	}


}
