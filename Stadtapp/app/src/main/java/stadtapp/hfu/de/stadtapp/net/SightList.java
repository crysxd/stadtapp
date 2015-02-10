package stadtapp.hfu.de.stadtapp.net;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

public class SightList extends HashMap<String, Sight> {

	private static final long serialVersionUID = 1L;
	private static SightList instance;
	
	public synchronized static SightList getSightList(Context ctx) throws Exception {
		if(instance == null)
			instance =  new SightList(ctx);
		return instance;
	}
	
	private SightList(Context ctx) throws Exception {
		Resty r = new Resty();
		JSONResource json = r.json("http://141.28.100.212:5984/stadtapp/_design/list/_view/all");
		JSONArray rows = (JSONArray) json.get("rows");
		
		for(int i=0; i<rows.length(); i++) {
			JSONObject row = rows.getJSONObject(i);
			Sight s = new Sight(ctx, row.getJSONObject("value"));
			this.put(s.getId(), s);
		}
	}
	
	@Override
	public Sight put(String key, Sight value) {
		value.setParent(this);
		return super.put(key, value);
	}

    public Object[] getAllUsers() {
        List<String> users = new ArrayList<>();
        for(Sight s : this.values()) {
            if(users.contains(s.getUser())) {
                users.add(s.getUser());

            }
        }

        return users.toArray();

    }
}
