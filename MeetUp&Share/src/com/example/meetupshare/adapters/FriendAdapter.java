package com.example.meetupshare.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.meetupshare.R;
import com.example.models.User;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class FriendAdapter extends ArrayAdapter<User>{

	private List<User> mFriendList;	    	
	private Context mContext;    	
	private LayoutInflater mInflater;
	private SparseBooleanArray mSelectedItemsIds;
	private List<String> mIdCheckedItems;
	private List<Integer> mPositionItemsChecked;

	public FriendAdapter(Context context, int ressourceId, List<User> list) {
		super(context, ressourceId);
		mFriendList = list;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mSelectedItemsIds = new SparseBooleanArray();
		mIdCheckedItems = new ArrayList<String>();
		mPositionItemsChecked = new ArrayList<Integer>();
	}

	@Override
	public int getCount() {
		return mFriendList.size();
	}

	@Override
	public User getItem(int position) {
		return mFriendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView id;
		TextView firstName;
		TextView lastName;
		CheckBox checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Integer p = position;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.friend_list, null);
			holder.id = (TextView)convertView.findViewById(R.id.id_friend_list);
			holder.firstName = (TextView)convertView.findViewById(R.id.firstname_friend_list);
			holder.lastName = (TextView)convertView.findViewById(R.id.lastname_friend_list);
			holder.checkbox = (CheckBox)convertView.findViewById(R.id.friend_list_checkBox);

			holder.checkbox.setOnClickListener(new View.OnClickListener() {
			      public void onClick(View v) {
			        if(((CheckBox) v).isChecked()){
			        	//ajout au conteneur des contacts coches
			        	Log.d("checked", holder.id.getText().toString());
			        	mIdCheckedItems.add(holder.id.getText().toString());
			        	mPositionItemsChecked.add(p);
			        }else{
			        	Log.d("checked", "decoche");
			        }
			      }
			    });
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}	
		holder.id.setText(Long.toString(mFriendList.get(position).getId()));
		holder.firstName.setText(mFriendList.get(position).getFirstname());
		holder.lastName.setText(mFriendList.get(position).getLastname());
		holder.checkbox.setChecked(false);
		
		return convertView;
	}
	
	public void setFriendList(List<User> list) {
		this.mFriendList = list;
	}

	public int getSelectedCount() {
		return mSelectedItemsIds.size();
	}

	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}

	public List<String> getIdCheckedItems(){
		return mIdCheckedItems;
	}
	
	public int getCountIdCheckedItemsList(){
		return mIdCheckedItems.size();
	}
	
	public void setIdCheckedItems(List<String> list){
		mIdCheckedItems = list;
	}
	
	public void initializeIdCheckedItems(){
		this.mIdCheckedItems = new ArrayList<String>();
	}
	
	public void remove(User object) {
		mFriendList.remove(object);
		notifyDataSetChanged();
	}

	public List<Integer> getmPositionItemsChecked() {
		return mPositionItemsChecked;
	}

	public void setmPositionItemsChecked(List<Integer> mPositionItemsChecked) {
		this.mPositionItemsChecked = mPositionItemsChecked;
	}
	
	public void initializemPositionItemsChecked(){
		this.mPositionItemsChecked = new ArrayList<Integer>();
	}


}
