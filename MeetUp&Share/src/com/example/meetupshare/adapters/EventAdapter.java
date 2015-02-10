package com.example.meetupshare.adapters;

import java.util.List;

import com.example.meetupshare.R;
import com.example.models.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventAdapter extends BaseAdapter{

	private List<Event> mEventList;	    	
	private Context mContext;    	
	private LayoutInflater mInflater;


	public EventAdapter(List<Event> list, Context context) {
		mEventList = list;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mEventList.size();
	}

	@Override
	public Object getItem(int position) {
		return mEventList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView idEvenement;
		TextView titreEvenement;
		TextView dateEvenement;
		TextView heureEvenement;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.event_list, null);
			holder.idEvenement = (TextView)convertView.findViewById(R.id.id_event_list);
			holder.titreEvenement = (TextView)convertView.findViewById(R.id.titre_event_list);
			holder.dateEvenement = (TextView)convertView.findViewById(R.id.date_event_list);
			holder.heureEvenement = (TextView)convertView.findViewById(R.id.heure_event_list);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.idEvenement.setText(Long.toString(mEventList.get(position).getId()));
		holder.titreEvenement.setText(mEventList.get(position).getTitre());
		holder.dateEvenement.setText(mEventList.get(position).getDate());
		holder.heureEvenement.setText(mEventList.get(position).getHeure());

		return convertView;
	}

	public void setEventList(List<Event> list) {
		this.mEventList = list;
	}

}
