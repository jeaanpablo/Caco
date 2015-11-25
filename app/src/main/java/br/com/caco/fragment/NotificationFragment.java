package br.com.caco.fragment;

import java.util.ArrayList;
import java.util.List;

import br.com.caco.R;
import br.com.caco.adapters.NotificationsAdapter;
import br.com.caco.model.Notification;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NotificationFragment extends Fragment {

	private String tab;
	private int color;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				R.layout.fragmente_notifications, null);

		List<Notification> list = new ArrayList<Notification>();

		for (int i = 0; i < 10; i++) {
			Notification item = new Notification("Goku", "Indicou Mr.Kistch para voc�", R.drawable.goku);
			list.add(item);
		}
		NotificationsAdapter adapter = new NotificationsAdapter (view.getContext(), list);
		ListView listView = (ListView) view
				.findViewById(R.id.listNotifications);
		listView.setAdapter(adapter);

		// tv.setText(tab);
		// view.setBackgroundResource(color);
		return view;
	}

}
