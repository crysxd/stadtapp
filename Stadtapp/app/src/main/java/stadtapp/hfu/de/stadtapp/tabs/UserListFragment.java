package stadtapp.hfu.de.stadtapp.tabs;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import stadtapp.hfu.de.stadtapp.DialogHostActivity;
import stadtapp.hfu.de.stadtapp.SightActivity;
import stadtapp.hfu.de.stadtapp.net.SightList;
import stadtapp.hfu.de.stadtapp.net.SightListLoader;
import stadtapp.hfu.de.stadtapp.net.SightListLoaderListener;

public class UserListFragment extends ListFragment implements SightListLoaderListener, OnItemClickListener {

	private DialogHostActivity host;

	public UserListFragment(DialogHostActivity host) {
		this.host = host;
				
	}
	
	@Override
	public void listLoaded(SightList list) {
		this.setListAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, list.getAllUsers()));
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
		String s = (String) this.getListAdapter().getItem(position);
		Intent i = new Intent(this.getActivity(), SightActivity.class);
		i.putExtra("user", s);
		this.getActivity().startActivity(i);	
	}
}
