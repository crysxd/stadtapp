package stadtapp.hfu.de.stadtapp.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.hfu.stadtapp.R;
import stadtapp.hfu.de.stadtapp.net.Sight;

public class ImageFragment extends Fragment {
	
	private ImageView imgView;
	private Sight mySight;
	
	public ImageFragment(Sight mySight) {
		this.mySight = mySight;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image, container, false);
        imgView = (ImageView) rootView.findViewById(R.id.imageView1);
        return rootView;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	imgView.setImageBitmap(mySight.getImage());

    }

}
