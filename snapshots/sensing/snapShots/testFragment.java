package sensing.snapShots;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class testFragment extends Fragment {

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    //set the layout you want to display in First Fragment
    View view = inflater.inflate(R.layout.tabs,     container, false);
    return view;

}

}