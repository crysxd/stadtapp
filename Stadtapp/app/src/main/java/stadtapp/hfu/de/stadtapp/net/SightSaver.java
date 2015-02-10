package stadtapp.hfu.de.stadtapp.net;

import android.os.AsyncTask;

import stadtapp.hfu.de.stadtapp.DialogHostActivity;

public class SightSaver extends AsyncTask<Void, Void, Boolean> {
	
	private final Sight SIGHT_TO_SAVE;
	private final DialogHostActivity DIALOG_HOST;
	
	public SightSaver(Sight s, DialogHostActivity host) {
		this.SIGHT_TO_SAVE = s;
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
			this.SIGHT_TO_SAVE.save();
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
			this.DIALOG_HOST.showError("Sight could not be saved.");
		
		else
			this.DIALOG_HOST.finish();
		
	}

}
