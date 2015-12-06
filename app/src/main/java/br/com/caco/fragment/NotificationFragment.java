package br.com.caco.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import br.com.caco.R;
import br.com.caco.adapters.NotificationsAdapter;
import br.com.caco.database.dao.NotificationDAO;
import br.com.caco.database.dao.UserDAO;
import br.com.caco.model.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NotificationFragment extends Fragment {

	private String tab;
	private int color;
	List<Notification> list;
	NotificationsAdapter adapter;

	private NotificationDAO notificationDAO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActivity().registerReceiver(this.broadCastNewMessage, new IntentFilter("bcNewMessage"));


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(
				R.layout.fragmente_notifications, null);

		NotificationDAO notificationDAO = new NotificationDAO(view.getContext());

		list = notificationDAO.getAll();


		adapter = new NotificationsAdapter (view.getContext(), list);
		ListView listView = (ListView) view
				.findViewById(R.id.listNotifications);
		listView.setAdapter(adapter);

		// tv.setText(tab);
		// view.setBackgroundResource(color);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		getActivity().unregisterReceiver(broadCastNewMessage);
	}

	BroadcastReceiver broadCastNewMessage = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			String message = intent.getStringExtra("message");

			notificationDAO = new NotificationDAO(context);

            Notification notif = new Notification();


			StringTokenizer tokens = new StringTokenizer(message, ";");

			String type = tokens.nextToken();

            notif.setType(type);

			if(type.equalsIgnoreCase("FRIEND"))
			{

				String idFriend = tokens.nextToken();

				String friendName = tokens.nextToken();

                notif.setIdUserRequester(Integer.parseInt(idFriend));
                notif.setNameUserRequester(friendName);
                notif.setImgPath("iauhdiasuh");

                list.add(notif);
				adapter.updateAdapter(list);

			}


			if(type.equalsIgnoreCase("STORE"))
			{

				String idFriend = tokens.nextToken();

				String friendName = tokens.nextToken();

				String idStore = tokens.nextToken();

				String storeName = tokens.nextToken();

                notif.setIdUserRequester(Integer.parseInt(idFriend));
                notif.setNameUserRequester(friendName);
                notif.setIdStore(Integer.parseInt(idStore));
                notif.setNameStore(storeName);
                notif.setType(type);

                list.add(notif);
                adapter.updateAdapter(list);

			}

			// here you can update your db with new messages and update the ui (chat message list)
			//msgAdapter.notifyDataSetChanged();
			//mListView.requestLayout();
			//...
		}
	};

}
