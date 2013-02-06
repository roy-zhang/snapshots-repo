package sensing.snapShots.model;

import java.util.ArrayDeque;
import java.util.Calendar;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewStyle;

public class Graph {
	

	public static View getGraphView(Context context, ArrayDeque<SnapShot> queue) {

		// Create GraphViewData array and populate with SnapShot queue
		Calendar firstTS = queue.peekFirst().timestamp;
		Calendar lastTS = queue.peekLast().timestamp;

		GraphViewSeries[] series  =  translateQueue(queue);

		// graph with dynamically generated horizontal and vertical labels
		GraphView graphView = new LineGraphView(context, "B.A.C."); // heading
				
		graphView.addSeries(series[0]);
		graphView.addSeries(series[1]); 
		
		graphView.setViewPort(0, BacMan.minutesSinceTimestamp(lastTS, firstTS));
		graphView.setScalable(true);
		
		return graphView;
	}
	
	private static GraphViewSeries[]  translateQueue(ArrayDeque<SnapShot> queue){
		Calendar firstTS = queue.peekFirst().timestamp;
		
		Object[] snapShots = queue.toArray();
		GraphViewData[] graphdata = new GraphViewData[queue.size()];
		GraphViewData[] bacLine   = new GraphViewData[queue.size()];
		
		for (int i = 0; i < snapShots.length; i++){
			int time = BacMan.minutesSinceTimestamp(((SnapShot)snapShots[i]).timestamp, firstTS);
			
			graphdata[i] = new GraphViewData(time, ((SnapShot)snapShots[i]).bac);
			bacLine[i] 	 = new GraphViewData(time, 0.08);
		}
		
		return new GraphViewSeries[]{
				new GraphViewSeries("B.A.C.",	new GraphViewStyle(Color.rgb(255, 166, 15), 3), graphdata),
				new GraphViewSeries("LegalLimit",	new GraphViewStyle(Color.rgb(250, 00, 00), 3), bacLine)
		} ;
	}
	
	

}
