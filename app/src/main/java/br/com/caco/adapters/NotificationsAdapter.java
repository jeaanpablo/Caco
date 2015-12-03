
	
	package br.com.caco.adapters;

	import java.util.List;

	import br.com.caco.R;
import br.com.caco.model.Notification;
import br.com.caco.model.Store;

	import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

	public class NotificationsAdapter extends BaseAdapter {

		private Context context;
		private List<Notification> notificationList;

		public NotificationsAdapter(Context context,
				List<Notification> notificationList) {
			this.context = context;
			this.notificationList = notificationList;
		}

		@Override
		public int getCount() {

			return notificationList.size();
		}

		@Override
		public Object getItem(int position) {

			return notificationList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return notificationList.indexOf(getItem(position));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Notification item = notificationList.get(position);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater
					.inflate(R.layout.list_notifications_item, null);
			TextView textNotificationTitle = (TextView) v
					.findViewById(R.id.textViewNotificationTitle);
			TextView textNotificationMessage = (TextView) v
					.findViewById(R.id.textViewNotificationsMessage);
			ImageView imageNotification = (ImageView) v
					.findViewById(R.id.imageViewNotificationImage);

			textNotificationTitle.setText(item.getTitle());
			textNotificationMessage.setText(item.getMessage());
			imageNotification.setImageResource(item.getImage());

			return v;
		}

	}
