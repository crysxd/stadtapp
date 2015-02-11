package stadtapp.hfu.de.stadtapp.tabs;

import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import stadtapp.hfu.de.stadtapp.DialogHostActivity;
import stadtapp.hfu.de.stadtapp.LocationProvider;
import stadtapp.hfu.de.stadtapp.SightActivity;
import stadtapp.hfu.de.stadtapp.net.Sight;
import stadtapp.hfu.de.stadtapp.net.SightList;
import stadtapp.hfu.de.stadtapp.net.SightListLoader;
import stadtapp.hfu.de.stadtapp.net.SightListLoaderListener;


public class SightMapFragment extends SupportMapFragment implements SightListLoaderListener, OnMarkerClickListener {

	private SightList list = null;
	private HashMap<String, Sight> markerSightPair = new HashMap<>();
	private SightList mySight = null;
    private DialogHostActivity host;

	public SightMapFragment(SightList sight) {
		mySight = sight;
	}
	
	public SightMapFragment(DialogHostActivity host) {
        this.host = host;

	}

	@Override
	public void onResume() {
        super.onResume();

		if(mySight != null) {
			this.listLoaded(this.mySight);

		} else {
            SightListLoader loader = new SightListLoader(host, this);
            loader.execute();

        }
	}

	@Override
	public void listLoaded(SightList list) {
		this.list = list;
		
		if(list == null || this.getMap() == null)
			return;

		this.getMap().clear();
		this.getMap().setOnMarkerClickListener(this);
		this.getMap().setMyLocationEnabled(true);

		for(Sight e : list.values()) {
			MarkerOptions m = new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude())).title(e.getName());
			markerSightPair.put(e.getName(), e);
			this.getMap().addMarker(m);
		}	

		Location myLocation = LocationProvider.getLocationProvider(getActivity()).getCurrentLocation();
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
		this.getMap().moveCamera(center);
		this.getMap().animateCamera(zoom);
		
	}

	@Override
	public boolean onMarkerClick(Marker m) {
		Sight s = markerSightPair.get(m.getTitle());

		Intent i = new Intent(getActivity(), SightActivity.class);
		i.putExtra("sight", s.getId());
		startActivity(i);

		return true;
	}
}

//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.stadtapp.MainActivity;
//import com.example.stadtapp.R;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//public class SightListMapFragment extends Fragment {
//
//	private View view;
//	/**
//	 * Note that this may be null if the Google Play services APK is not
//	 * available.
//	 */
//
//	private GoogleMap mMap;
//	private Double latitude, longitude;
//	private FragmentManager fragmentManager;
//	
//	public SightListMapFragment(FragmentManager manager) {
//		fragmentManager = manager;
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		if (container == null) {
//			return null;
//		}
//		// view = (RelativeLayout) inflater.inflate(R.layout.map_layout, container, false);
//		view =  inflater.inflate(R.layout.map_layout, container, false);
//		// Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map   		
//		latitude = 26.78;
//		longitude = 72.56;
//
//		setUpMapIfNeeded(); // For setting up the MapFragment
//
//		/*
//            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//                @Override
//                public void onMyLocationChange(Location location) {
//                        CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//                        CameraUpdate zoom=CameraUpdateFactory.zoomTo(11);
//                        mMap.moveCamera(center);
//                        mMap.animateCamera(zoom);
//                }
//            });
//		 */
//
//		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//			@Override
//			public void onMapClick(LatLng point) {
//				MarkerOptions marker = new MarkerOptions().position(
//						new LatLng(point.latitude, point.longitude)).title("New Marker");
//
//				mMap.addMarker(marker);
//				System.out.println(point.latitude+"---"+ point.longitude);  
//			}
//		});
//
//
//		return view;
//	}
//
//	/***** Sets up the map if it is possible to do so *****/
//	public void setUpMapIfNeeded() {
//		// Do a null check to confirm that we have not already instantiated the map.
//		if (mMap == null) {
//			// Try to obtain the map from the SupportMapFragment.
//			mMap = ((MapFragment) fragmentManager.findFragmentById(R.id.location_map)).getMap();
//			// Check if we were successful in obtaining the map.
//			if (mMap != null)
//				setUpMap();
//		}
//	}
//
//	/**
//	 * This is where we can add markers or lines, add listeners or move the
//	 * camera.
//	 * <p>
//	 * This should only be called once and when we are sure that {@link #mMap}
//	 * is not null.
//	 */
//
//
//
//	private void setUpMap() {
//		// For showing a move to my loction button
//		mMap.setMyLocationEnabled(true);
//		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//		mMap.getUiSettings().setCompassEnabled(true); 
//		mMap.getUiSettings().setMyLocationButtonEnabled(true); // For testing
//		mMap.addMarker(new MarkerOptions()
//		.position(new LatLng(48.05136247223902, 8.207470066845417))
//		.title("A Bau - Eingang"));
//		// For dropping a marker at a point on the Map
//		//mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Home").snippet("Home Address"));
//		// For zooming automatically to the Dropped PIN Location
//		//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
//		//longitude), 12.0f));
//
//
//	}
//
//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		if (mMap != null)
//			setUpMap();
//
//		if (mMap == null) {
//			// Try to obtain the map from the SupportMapFragment.
//			mMap = ((MapFragment) fragmentManager
//					.findFragmentById(R.id.location_map)).getMap(); // getMap is deprecated
//			// Check if we were successful in obtaining the map.
//			if (mMap != null)
//				setUpMap();
//		}
//	}
//
//	/**** The mapfragment's id must be removed from the FragmentManager
//	 **** or else if the same it is passed on the next time then 
//	 **** app will crash ****/
//	@Override
//	public void onDestroyView() {
//		super.onDestroyView();
//		if (mMap != null) {
//			fragmentManager.beginTransaction()
//			.remove(fragmentManager.findFragmentById(R.id.location_map)).commit();
//			mMap = null;
//		}
//	}
//}