package stadtapp.hfu.de.stadtapp.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hfu.stadtapp.R;
import stadtapp.hfu.de.stadtapp.net.Sight;

public class ContentFragment extends Fragment {
	private TextView txtView;
	private Sight mySight;

	public ContentFragment(Sight mySight) {
		this.mySight = mySight;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_content, container, false);
		txtView = (TextView) rootView.findViewById(R.id.textView1);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		txtView.setText(mySight.getContent());

	}

}
