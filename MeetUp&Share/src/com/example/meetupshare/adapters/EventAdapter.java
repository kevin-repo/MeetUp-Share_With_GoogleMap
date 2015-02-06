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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layoutItem;
		if (convertView == null) {
			layoutItem = (LinearLayout) mInflater.inflate(R.layout.event_list, parent, false);
		} else {
			layoutItem = (LinearLayout) convertView;
		} 
		
		TextView titreEvenement = (TextView)layoutItem.findViewById(R.id.titre_event_list);
		TextView dateEvenement = (TextView)layoutItem.findViewById(R.id.date_event_list);
		TextView heureEvenement = (TextView)layoutItem.findViewById(R.id.heure_event_list);
		    
		titreEvenement.setText(mEventList.get(position).getTitre());
		dateEvenement.setText(mEventList.get(position).getDate());
		heureEvenement.setText(mEventList.get(position).getHeure());
		
		return layoutItem;
	}

}
