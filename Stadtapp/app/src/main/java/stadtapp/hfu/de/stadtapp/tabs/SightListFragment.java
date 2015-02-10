package stadtapp.hfu.de.stadtapp.tabs;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import stadtapp.hfu.de.stadtapp.DialogHostActivity;
import stadtapp.hfu.de.stadtapp.SightActivity;
import stadtapp.hfu.de.stadtapp.net.Sight;
import stadtapp.hfu.de.stadtapp.net.SightList;
import stadtapp.hfu.de.stadtapp.net.SightListLoader;
import stadtapp.hfu.de.stadtapp.net.SightListLoaderListener;

public class SightListFragment extends ListFragment implements SightListLoaderListener, OnItemClickListener {
		
	private DialogHostActivity host;
	
	public SightListFragment(DialogHostActivity host) {
		this.host = host;
				
	}
	
	@Override
	public void listLoaded(SightList list) {
		this.setListAdapter(new SightListAdapter(getActivity(), list));
		this.getListView().setOnItemClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		SightListLoader loader = new SightListLoader(host, this);
		loader.execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Sight s = (Sight) this.getListAdapter().getItem(position);
		Intent i = new Intent(this.getActivity(), SightActivity.class);
		i.putExtra("sight", s.getId());
		this.getActivity().startActivity(i);	
	}
}
