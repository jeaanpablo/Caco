package br.com.caco.gui;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.caco.R;
import br.com.caco.adapters.FriendsAdapter;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.model.Friend;
import br.com.caco.model.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.camera.CropImageIntentBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ProfileActivity extends Activity {

	private static int LOAD_IMAGE_RESULTS = 1;
	final int GALLERY_CAPTURE = 1;
    final int CROP_PIC = 2;

	private UserDAO userDao;
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

        userDao  = new UserDAO(this);

        List<User> userList = userDao.getAll();

        final User user = userList.get(0);

        ImageView imageProfile = (ImageView) findViewById(R.id.ImageViewProfileFoto);

        loadImageFromStorage(user.getImagePath(), imageProfile);


        final EditText editNome = (EditText) findViewById(R.id.editTextProfileNome);
        editNome.setText(user.getFirstName()+" "+user.getLastName());

        final EditText editCelular = (EditText) findViewById(R.id.editTextProfileCelular);
        editCelular.setText(""+user.getCellphone());

		btnEditNome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {




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
		        Intent i = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

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



		File file = new File(getFilesDir(), "profile.jpg");

        if (resultCode == RESULT_OK){
            // Let's read picked image data - its URI
        	if (requestCode == GALLERY_CAPTURE) {
        		Uri pickedImage = data.getData();
                // Let's read picked image path using content resolver
             //   String[] filePath = {getPath(pickedImage)};
               // Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
              //  cursor.moveToFirst();
                String imagePath = getPath(pickedImage);
               // cursor.close();
				Uri image = Uri.fromFile(file);


               // performCrop(image, imageProfile.getWidth(), imageProfile.getHeight(), this);

				CropImageIntentBuilder cropImage = new CropImageIntentBuilder(imageProfile.getWidth(), imageProfile.getHeight(), image);
				cropImage.setOutlineColor(0xFF03A9F4);
				cropImage.setSourceImage(data.getData());

				startActivityForResult(cropImage.getIntent(this), CROP_PIC);

            } else if (requestCode == CROP_PIC) {

				userDao  = new UserDAO(this);

				List<User> userList = userDao.getAll();

				User user = userList.get(0);

				Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				String encodedImage = bitmapToString(bitmap);

				user.setImage(encodedImage);
				user.setImageName(user.getId() + file.getName());

				updateProfilePicture(this, user);

                imageProfile.setImageBitmap(bitmap);

                try {
                    saveToInternalSorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }




        }
    }

	public void updateProfilePicture(final Context context, final User user)
	{


		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			public ProgressDialog mProgressDialog;
			List<Friend> list = new ArrayList<Friend>();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				mProgressDialog = new ProgressDialog(context);
				mProgressDialog.setMessage("Por favor espere...");
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

				getFriendByUser(user);

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);

				mProgressDialog.dismiss();


			}

		}.execute();
	}

	public void getFriendByUser(User user) {
		// Create a new HttpClient and Post Header

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://45.79.178.168:8080/Caco-webservice/updateUserImageProfile");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("image", ""+user.getImage()));
			nameValuePairs.add(new BasicNameValuePair("image_name", user.getImageName()));
			nameValuePairs.add(new BasicNameValuePair("id_user", ""+user.getId()));


			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			String string = response.getStatusLine().getReasonPhrase();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			user = null;
		} catch (IOException e) {
			e.printStackTrace();
			user = null;
		}
	}
	private String getPath(Uri uri) {
		if( uri == null ) {
			return null;
		}

		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor;
		if(Build.VERSION.SDK_INT >19)
		{
			// Will return "image:x*"
			String wholeID = DocumentsContract.getDocumentId(uri);
			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];
			// where id is equal to
			String sel = MediaStore.Images.Media._ID + "=?";

			cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					projection, sel, new String[]{ id }, null);
		}
		else
		{
			cursor = getContentResolver().query(uri, projection, null, null, null);
		}
		String path = null;
		try
		{
			int column_index = cursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index).toString();
			cursor.close();
		}
		catch(NullPointerException e) {

		}
		return path;
	}

	public String bitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = null;
		try {
			System.gc();
			temp = Base64.encodeToString(b, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
			b = baos.toByteArray();
			temp = Base64.encodeToString(b, Base64.DEFAULT);
			Log.e("PictureDemo", "Out of memory error catched");
		}
		return temp;
	}


    private void loadImageFromStorage(String path, ImageView img)
    {

        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


    private String saveToInternalSorage(Bitmap bitmapImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return mypath.getAbsolutePath();
    }


}
