package br.com.caco.gui;

import java.io.File;

import br.com.caco.R;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	
	private static int LOAD_IMAGE_RESULTS = 1;
	final int GALLERY_CAPTURE = 1;
    final int CROP_PIC = 2;
	
	private String selectedImagePath;
   
    private String filemanagerstring;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profile);

		getActionBar().setTitle(R.string.menu_perfil);

		final ImageView btnEditNome = (ImageView) findViewById(R.id.imageViewProfileButtonName);
		final ImageView btnEditCelular = (ImageView) findViewById(R.id.imageViewProfileCelular);
		final ImageView btnEditFoto = (ImageView) findViewById(R.id.ImageViewProfileFotoEdit);

		btnEditNome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				EditText editNome = (EditText) findViewById(R.id.editTextProfileNome);
				
				
				if(editNome.isEnabled() == false)
				{
					btnEditNome.setImageResource(R.drawable.ic_action_accept);
					editNome.setEnabled(true);
				}
				else{
					btnEditNome.setImageResource(R.drawable.ic_action_edit);
					editNome.setEnabled(false);
				}
				
				

			}
		});

		btnEditCelular.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText editCelular = (EditText) findViewById(R.id.editTextProfileCelular);
				
				if(editCelular.isEnabled() == false)
				{
					btnEditCelular.setImageResource(R.drawable.ic_action_accept);
					editCelular.setEnabled(true);
				}
				else{
					btnEditCelular.setImageResource(R.drawable.ic_action_edit);
					editCelular.setEnabled(false);
				}
				

			}
		});
		
		btnEditFoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				 // Create the Intent for Image Gallery.
		        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		         
		        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
		        startActivityForResult(i, LOAD_IMAGE_RESULTS);
				
				

			}
		});
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
      //  if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null)
        ImageView imageProfile = (ImageView) findViewById(R.id.ImageViewProfileFoto);
        
        if (resultCode == RESULT_OK){
            // Let's read picked image data - its URI
        	if (requestCode == GALLERY_CAPTURE) {
        		Uri pickedImage = data.getData();
                // Let's read picked image path using content resolver
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                cursor.close();
                
                
                performCrop(pickedImage, imageProfile.getWidth(), imageProfile.getHeight());
            } else if (requestCode == CROP_PIC) {
            	// Now we need to set the GUI ImageView data with data read from the picked file.
            	// get the returned data
                Bundle extras = data.getExtras();
                // get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");

                //imageProfile.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                imageProfile.setImageBitmap(thePic);
                 
                // At the end remember to close the cursor or you will end with the RuntimeException!
            }
            
             
            
            
        }
    }
	
	private void performCrop(Uri picUri, int x, int y) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", x);
            cropIntent.putExtra("outputY", y);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
