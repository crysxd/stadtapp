package stadtapp.hfu.de.stadtapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import de.hfu.stadtapp.R;
import stadtapp.hfu.de.stadtapp.net.Sight;
import stadtapp.hfu.de.stadtapp.net.SightList;
import stadtapp.hfu.de.stadtapp.net.SightSaver;


public class EditActivity extends DialogHostActivity implements OnClickListener {

	private Sight mySight;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private Bitmap currentImage;
    private File imageFile;
    private String user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_edit);

		try {
			String sightId = (String) this.getIntent().getExtras().get("sight");
			mySight = SightList.getSightList(this).get(sightId);
			
		} catch (Exception e) {
			e.printStackTrace();
			mySight = null;
			
		}
		
		if(mySight == null)
			mySight = new Sight();


        this.user = PreferenceManager.getDefaultSharedPreferences(this).getString("user", null);
        if(this.user == null) {
            final EditText userInput = new EditText(this);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter user name");
            builder.setMessage("Please enter a user name");
            builder.setView(userInput);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String user = userInput.getText().toString();

                    if(user.length() == 0) {
                        Toast.makeText(EditActivity.this, "Not a valid user name", Toast.LENGTH_LONG).show();
                        EditActivity.this.finish();
                        return;
                        
                    }

                    PreferenceManager.getDefaultSharedPreferences(EditActivity.this).edit().putString("user", user).commit();
                    EditActivity.this.user = user;
                    
                }
            });

            builder.create().show();


        }
		
		((EditText) findViewById(R.id.editTextName)).setText(mySight.getName());
		((EditText) findViewById(R.id.editTextContent)).setText(mySight.getContent());
		((ImageView) findViewById(R.id.photoPreview)).setImageBitmap(mySight.getImage());
		((Button) findViewById(R.id.buttonMakePhoto)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonRotateLeft)).setOnClickListener(this);
		((Button) findViewById(R.id.buttonRotateRight)).setOnClickListener(this);
		
		LocationProvider.getLocationProvider(this);

		currentImage = mySight.getImage();
		getActionBar().setIcon(R.drawable.ic_launcher_light);
	
	}
	
	@Override
	public void onBackPressed() {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setMessage("Do you really want to discard all changes?");
		b.setTitle("Discard changes");
		b.setPositiveButton("Discard changes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditActivity.super.onBackPressed();
				
			}
		});
		b.setNegativeButton("Cancel", null);
		b.create().show();
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_save) {
			this.mySight.setName(((EditText) findViewById(R.id.editTextName)).getText().toString());
            this.mySight.setUser(this.user);
			this.mySight.setContent(((EditText) findViewById(R.id.editTextContent)).getText().toString());
			this.mySight.setImage(this, currentImage);
			Location l = LocationProvider.getLocationProvider(this).getCurrentLocation();
			this.mySight.setLatitude(l.getLatitude());
			this.mySight.setLongitude(l.getLongitude());
			try {
				SightList.getSightList(this).put(this.mySight.getId(), this.mySight);
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			SightSaver saver = new SightSaver(this.mySight, this);
			saver.execute();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonMakePhoto) {
			try {
	            imageFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
	            
		        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
		        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);	
			}catch(Exception e) {
				e.printStackTrace();
				this.showError("Image creation failed.");
			}
			
		} else if(v.getId() == R.id.buttonRotateLeft && currentImage != null) {
			Matrix matrix = new Matrix();
			matrix.postRotate(-90);
			currentImage = Bitmap.createBitmap(currentImage, 0, 0, 
					currentImage.getWidth(), currentImage.getHeight(), 
			                              matrix, true);
    		((ImageView) findViewById(R.id.photoPreview)).setImageBitmap(currentImage);

		} else if(v.getId() == R.id.buttonRotateRight && currentImage != null) {
			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			currentImage = Bitmap.createBitmap(currentImage, 0, 0, 
					currentImage.getWidth(), currentImage.getHeight(), 
			                              matrix, true);
    		((ImageView) findViewById(R.id.photoPreview)).setImageBitmap(currentImage);

		}
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

            	 // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                currentImage = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);  
                currentImage = Bitmap.createScaledBitmap(currentImage, 1440, (int) (currentImage.getHeight() * ( 1440. / (double)currentImage.getWidth())), false);
        		((ImageView) findViewById(R.id.photoPreview)).setImageBitmap(currentImage);

            }
        }        
    }   
}
