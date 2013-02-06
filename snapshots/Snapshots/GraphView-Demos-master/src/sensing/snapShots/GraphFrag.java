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
import sensing.snapShots.model.Graph;
import sensing.snapShots.model.Profile;

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

public class GraphFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.graph_predicter, container, false);
        return view;
		}

    
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        BacMan bacMan = new BacMan(new Profile(), 3);
        bacMan.addAlcohol(50);
        
        drawGraph(bacMan.predictTillFlat(15*60, 100));

    }
    
    
    public void drawGraph(ArrayDeque<SnapShot> queue) {
    	View graphView = Graph.getGraphView(getActivity(), queue);
		FrameLayout layout = (FrameLayout) getView().findViewById(R.id.graph);
		layout.removeAllViews();
		layout.addView(graphView);
	}


}