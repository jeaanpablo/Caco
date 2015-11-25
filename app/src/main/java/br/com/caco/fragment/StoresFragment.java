package br.com.caco.fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import br.com.caco.R;
import br.com.caco.adapters.AllStoresAdapter;
import br.com.caco.gui.FidelityCardActivity;
import br.com.caco.model.Store;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class StoresFragment extends Fragment {

	private String tab;
	private int color;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_all_stores, null);
		inflateStoreList(view);
		return view;
	}

	@Override
	public void onResume() {
		((FidelityCardActivity) getActivity()).setTitle("Lojas");
		super.onResume();
	}


	public void inflateStoreList(final View view)
	{


		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			public ProgressDialog mProgressDialog;
			List<Store> list = new ArrayList<Store>();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				mProgressDialog = new ProgressDialog(view.getContext());
				mProgressDialog.setMessage("Por favor espere...");
				mProgressDialog.setCancelable(false);
				mProgressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

				list = getStores();

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);

				AllStoresAdapter adapter = new AllStoresAdapter(
						view.getContext(), list);
				ListView listView = (ListView) view
						.findViewById(R.id.listAllStores);
				listView.setAdapter(adapter);

				mProgressDialog.dismiss();


			}

		}.execute();
	}


	public List<Store> getStores() {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		//HttpPost httppost = new HttpPost("http://45.79.178.168:8080/Caco-webservice/userLogin");
		HttpGet httpGet = new HttpGet("http://45.79.178.168:8080/Caco-webservice/getAllStores");

		List<Store> list = new ArrayList<Store>();

		try {


			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httpGet);

			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			for (String line = null; (line = reader.readLine()) != null;) {
				builder.append(line).append("\n");
			}
			//JSONObject jsonObject = new JSONObject(builder.toString());

			JSONArray jsonArray = new JSONArray(builder.toString());

			for(int i=0;i<jsonArray.length();i++) {
				JSONObject lines = (JSONObject) new JSONTokener(jsonArray.getString(i)).nextValue();
				Store store = new Store();

				store.setId(lines.getInt("idStore"));
				store.setName(lines.getString("fantasyName"));
				store.setAddress(lines.getString("address"));
				store.setFidelity(true);
				store.setStoreLogo(R.drawable.mr_kistch);

				list.add(store);

			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//user = null;
		} catch (IOException e) {
			e.printStackTrace();
			//user = null;
		} catch (JSONException e) {
			e.printStackTrace();
			//user = null;
		}

		return list;
	}



}
