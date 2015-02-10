package stadtapp.hfu.de.stadtapp.net;

import android.os.AsyncTask;

import stadtapp.hfu.de.stadtapp.DialogHostActivity;

public class SightListLoader extends AsyncTask<Void, Void, SightList> {
	
	private DialogHostActivity host;
	private SightListLoaderListener list;
	
	public SightListLoader(DialogHostActivity host, SightListLoaderListener list) {
		this.host = host;
		this.list = list;
	}
	
	@Override
	protected SightList doInBackground(Void... params) {
		try {
			return SightList.getSightList(host);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		}
	}
	
	@Override
	protected void onPostExecute(SightList result) {		
		if(result == null) {
			host.showError("Übersicht konnte nicht geladen werden.");
		} else {
			list.listLoaded(result);
		}
	}
}
