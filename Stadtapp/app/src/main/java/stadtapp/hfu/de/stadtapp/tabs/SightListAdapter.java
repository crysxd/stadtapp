package stadtapp.hfu.de.stadtapp.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hfu.stadtapp.R;
import stadtapp.hfu.de.stadtapp.LocationProvider;
import stadtapp.hfu.de.stadtapp.net.Sight;
import stadtapp.hfu.de.stadtapp.net.SightList;

public class SightListAdapter extends ArrayAdapter<Sight> {

	public SightListAdapter(Context context, SightList list) {
		super(context, R.layout.sight_list_row, new ArrayList<Sight>(list.values()));
		LocationProvider.getLocationProvider(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// 2. Get rowView from inflater

		View rowView = null;
		
		if(convertView == null) {
			// 1. Create inflater 
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		

			rowView = inflater.inflate(R.layout.sight_list_row, parent, false);
		} else {
			rowView = convertView;
			
		}
		
		// 3. Get icon,title & counter views from the rowView
		ImageView imgView = (ImageView) rowView.findViewById(R.id.imageViewThumbnail); 
		TextView titleView = (TextView) rowView.findViewById(R.id.textView1);
		TextView counterView = (TextView) rowView.findViewById(R.id.textView2);

		// 4. Set the text for textView 
		Sight s = getItem(position);
		imgView.setImageBitmap(s.getThumbnail());
		titleView.setText(s.getName());
		double dist = LocationProvider.getLocationProvider(getContext()).distanceToLocation(s.getLatitude(), s.getLongitude());
		counterView.setText(String.format("%.2f km", dist/1000));

		return rowView;
		
	}

}
