package stadtapp.hfu.de.stadtapp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import stadtapp.hfu.de.stadtapp.tabs.SightMapFragment;

import de.hfu.stadtapp.R;
import stadtapp.hfu.de.stadtapp.net.Sight;
import stadtapp.hfu.de.stadtapp.net.SightDropper;
import stadtapp.hfu.de.stadtapp.net.SightList;
import stadtapp.hfu.de.stadtapp.tabs.ContentFragment;
import stadtapp.hfu.de.stadtapp.tabs.ImageFragment;

public class SightActivity extends DialogHostActivity {

	private MyPagerAdapter adapterViewPager;
	private ViewPager vpPager;
	private Sight mySight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		
		String sightId = getIntent().getExtras().getString("sight");
		try {
			this.mySight = SightList.getSightList(this).get(sightId);
		} catch (Exception e) {
			this.mySight = new Sight();
		}

		ActionBar actionBar = getActionBar();
		// Specify that tabs should be displayed in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a tab listener that is called when the user changes tabs.
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				vpPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
			}
		};
		
		vpPager.setOnPageChangeListener(
	            new ViewPager.SimpleOnPageChangeListener() {
	                @Override
	                public void onPageSelected(int position) {
	                    // When swiping between pages, select the
	                    // corresponding tab.
	                    getActionBar().setSelectedNavigationItem(position);
	                }
	            });

		// Add 3 tabs, specifying the tab's text and TabListener
		for (int i = 0; i < adapterViewPager.getCount(); i++) {
			actionBar.addTab(actionBar.newTab().setText(adapterViewPager.getPageTitle(i)).setTabListener(tabListener));
		}
		
		getActionBar().setIcon(R.drawable.ic_launcher_light);

	}

	@Override
	protected void onResume() {
		super.onResume();
		vpPager.setCurrentItem(getActionBar().getSelectedTab().getPosition());	
		getActionBar().setTitle(mySight.getName());
		getActionBar().setSubtitle(String.format("%.2f km | posted by %s", LocationProvider.getLocationProvider(this).distanceToLocation(mySight.getLatitude(), mySight.getLongitude())/1000, mySight.getUser()));

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sight, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_edit) {
			Intent i = new Intent(this, EditActivity.class);
			i.putExtra("sight", mySight.getId());
			startActivity(i);

			return true;
		}
		if (id == R.id.action_delete) {
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setMessage("Do you really want to delete this sight?");
			b.setTitle("Delete sight");
			b.setPositiveButton("Delete sight", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SightDropper d = new SightDropper(mySight, SightActivity.this);
					d.execute();
//					finish();
					
				}
			});
			b.setNegativeButton("Cancel", null);
			b.create().show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class MyPagerAdapter extends FragmentPagerAdapter {
		private int NUM_ITEMS = 3;
		
		public MyPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new ContentFragment(mySight);
			case 1:
				return new ImageFragment(mySight);
			case 2:
				return new SightMapFragment(SightList.createSingleSightList(mySight));
			default:
				return null;
			}
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Details";
			case 1:
				return "Images";
			case 2:
				return "Map";
			default:
				return null;
			}		
		}
	}
}