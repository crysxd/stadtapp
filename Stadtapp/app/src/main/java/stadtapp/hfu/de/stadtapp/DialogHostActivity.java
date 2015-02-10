package stadtapp.hfu.de.stadtapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

public class DialogHostActivity extends FragmentActivity {
	
	private ProgressDialog progress = null;

	public void showPorgress(String title) {
		if(progress != null)
			hideProgress();
		
		progress = new ProgressDialog(this);
		progress.setCanceledOnTouchOutside(false);
		progress.setCancelable(false);
		progress.setMessage(title);
		progress.show();
	}
	
	public void hideProgress() {
		if(progress == null)
			return;
		
		progress.dismiss();
		progress = null;
	}
	
	public void showError(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message).setTitle("Fehler");
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}

}
