package com.example.meetupshare.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.meetupshare.R;
import com.example.models.User;

public class ParticipantAdapter extends ArrayAdapter<User>{

	private List<User> mParticipantList;	    	
	private Context mContext;    	
	private LayoutInflater mInflater;

	public ParticipantAdapter(Context context, int ressourceId, List<User> list) {
		super(context, ressourceId);
		mParticipantList = list;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mParticipantList.size();
	}

	@Override
	public User getItem(int position) {
		return mParticipantList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView id;
		TextView firstName;
		TextView lastName;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.participant_list, null);
			holder.id = (TextView)convertView.findViewById(R.id.id_participant_list);
			holder.firstName = (TextView)convertView.findViewById(R.id.firstname_participant_list);
			holder.lastName = (TextView)convertView.findViewById(R.id.lastname_participant_list);		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}	
		holder.id.setText(Long.toString(mParticipantList.get(position).getId()));
		holder.firstName.setText(mParticipantList.get(position).getFirstname());
		holder.lastName.setText(mParticipantList.get(position).getLastname());
		
		return convertView;
	}
	
	public void setParticipantList(List<User> list) {
		this.mParticipantList = list;
	}
	
	public void add(User object) {
		mParticipantList.add(object);
		notifyDataSetChanged();
	}

	
}
