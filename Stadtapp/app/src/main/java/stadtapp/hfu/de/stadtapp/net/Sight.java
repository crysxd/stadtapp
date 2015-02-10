package stadtapp.hfu.de.stadtapp.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hfu.stadtapp.R;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.BinaryResource;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import static us.monoid.web.Resty.content;
import static us.monoid.web.Resty.delete;
import static us.monoid.web.Resty.put;

public class Sight {

	private String _id;
	private String _rev = "";
	private String name = "";
	private String content = "";
    private String user = "Unmatched";
	private double latitude = 0.1;
	private double longitude = 0.1;
	private Bitmap image = null;
	private Bitmap thumbnail = null;
	private SightList parent;

	public Sight() {
		_id = System.currentTimeMillis() + "";
	}

	public Sight(Context ctx, String id) throws Exception {
		Resty r = new Resty();
		JSONResource json = r.json("http://141.28.100.212:5984/stadtapp/" + id);
		parse(ctx, json.toObject());
	}

	public Sight(Context ctx, JSONObject json) throws JSONException, IOException {
		parse(ctx, json);
	}

	private void parse(Context ctx, JSONObject json) throws JSONException, IOException {
		this._id = (String) json.get("_id");
		this._rev = (String) json.get("_rev");
		this.name = (String) json.get("name");
		this.content = (String) json.get("content");
		this.latitude = (Double) json.get("latitude");
		this.longitude = (Double) json.get("longitude");
        this.user = (String) json.get("user");

		try {
			Resty r = new Resty();
			BinaryResource bytes = r.bytes("http://141.28.100.212:5984/stadtapp/" + _id + "/image.jpg");
			this.setImage(ctx, BitmapFactory.decodeStream(bytes.stream()));
		} catch(Exception e) {
			this.setImage(ctx, null);
		}
	}

	public void save() throws Exception {
		Resty r = new Resty();

		String query = "";	
		if(_rev.length() > 0) {
			query = "{\"_rev\":\"" + _rev + "\",\"name\":\"" + name + "\", \"content\":\"" + content + "\", \"latitude\":" + latitude + ", \"longitude\":" + longitude + ", \"user\":\"" + user + "\"}";
		} else {
			query = "{\"name\":\"" + name + "\", \"content\":\"" + content + "\", \"latitude\":" + latitude + ", \"longitude\":" + longitude + ", \"user\":\"" + user + "\"}";
		}
		
		JSONResource json = r.json("http://141.28.100.212:5984/stadtapp/" + _id, put(content(query)));
		
		if(image != null) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			image.compress(CompressFormat.JPEG, 80, bos);
			json = r.json("http://141.28.100.212:5984/stadtapp/" + _id + "/image.jpg?rev=" + json.get("rev"), put(content(bos.toByteArray())));
		} else {
			json = r.json("http://141.28.100.212:5984/stadtapp/" + _id + "/image.jpg?rev=" + json.get("rev"), delete());
		}
		
		this._rev = (String) json.get("rev");
		
	}

	public void drop() throws Exception {
		Resty r = new Resty();
		JSONResource json = r.json("http://141.28.100.212:5984/stadtapp/" + _id + "/image.jpg?rev=" + _rev, delete());
		r.json("http://141.28.100.212:5984/stadtapp/" + _id + "?rev=" + json.get("rev"), delete());

		if(parent != null)
			parent.remove(this._id);
		
	}
	
	public String getName() {
		return Uri.decode(this.name);
	}

	public void setName(String name) {
		this.name = Uri.encode(name);
	}

	public String getContent() {
		return Uri.decode(content);
	}

	public void setContent(String content) {
		this.content = Uri.encode(content);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Bitmap getImage() {
		return image;
	}

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setImage(Context ctx, Bitmap image) {
		if(image == null) {
			image = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_icon_no_image);
		}
		
		this.image = image;	
		this.thumbnail = Bitmap.createScaledBitmap(this.image, 100, (int) (this.image.getHeight() * ( 100. / (double)this.image.getWidth())), false);

	}
	
	public Bitmap getThumbnail() {
		return thumbnail;
	}

	public String getId() {
		return _id;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	protected void setParent(SightList parent) {
		this.parent = parent;
	}
}
