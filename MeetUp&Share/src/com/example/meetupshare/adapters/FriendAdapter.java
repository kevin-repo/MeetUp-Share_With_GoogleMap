package com.example.meetupshare.adapters;

import java.util.List;

import com.example.meetupshare.R;
import com.example.models.Event;
import com.example.models.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendAdapter extends BaseAdapter{
	
	private List<User> mFriendList;	    	
	private Context mContext;    	
	private LayoutInflater mInflater;


	public FriendAdapter(List<User> list, Context context) {
		mFriendList = list;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mFriendList.size();
	}

	@Override
	public Object getItem(int position) {
		return mFriendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layoutItem;
		if (convertView == null) {
			layoutItem = (LinearLayout) mInflater.inflate(R.layout.friend_list, parent, false);
		} else {
			layoutItem = (LinearLayout) convertView;
		} 
		
		TextView id = (TextView)layoutItem.findViewById(R.id.id_friend_list);
		TextView firstName = (TextView)layoutItem.findViewById(R.id.firstname_friend_list);
		TextView lastName = (TextView)layoutItem.findViewById(R.id.lastname_friend_list);
		
		id.setText(Long.toString(mFriendList.get(position).getId()));
		firstName.setText(mFriendList.get(position).getFirstname());
		lastName.setText(mFriendList.get(position).getLastname());
		
		return layoutItem;
	}

	public void setFriendList(List<User> list) {
		this.mFriendList = list;
	}
}
