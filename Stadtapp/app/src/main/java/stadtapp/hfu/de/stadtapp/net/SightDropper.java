package stadtapp.hfu.de.stadtapp.net;

import android.os.AsyncTask;

import stadtapp.hfu.de.stadtapp.DialogHostActivity;

public class SightDropper extends AsyncTask<Void, Void, Boolean> {
	
	private final Sight SIGHT_TO_DROP;
	private final DialogHostActivity DIALOG_HOST;
	
	public SightDropper(Sight s, DialogHostActivity host) {
		this.SIGHT_TO_DROP = s;
		this.DIALOG_HOST = host;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.DIALOG_HOST.showPorgress("Saving...");
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			this.SIGHT_TO_DROP.drop();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		this.DIALOG_HOST.hideProgress();

		if(!result)
			this.DIALOG_HOST.showError("Sight could not be deleted.");
		
		else
			this.DIALOG_HOST.finish();
		
	}

}
