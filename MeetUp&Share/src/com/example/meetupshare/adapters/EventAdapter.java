package com.example.meetupshare.adapters;

import java.util.ArrayList;
import java.util.List;

import com.example.meetupshare.R;
import com.example.models.Event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Adapteur pour la liste des evenements
 *
 */
public class EventAdapter extends ArrayAdapter<Event>{

	private List<Event> mEventList;	    	
	private Context mContext;    	
	private LayoutInflater mInflater;
	private List<String> mIdCheckedItems;
	private List<Integer> mPositionItemsChecked;
	private boolean mHideCheckBox;

	public EventAdapter(Context context, int ressourceId, List<Event> list, boolean hideCheckbox) {
		super(context, ressourceId);
		mEventList = list;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mIdCheckedItems = new ArrayList<String>();
		mPositionItemsChecked = new ArrayList<Integer>();
		mHideCheckBox = hideCheckbox;
	}

	@Override
	public int getCount() {
		return mEventList.size();
	}

	@Override
	public Event getItem(int position) {
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
		TextView urlEvenement;
		TextView lieuEvenement;
		CheckBox checkbox;
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
			holder.urlEvenement = (TextView)convertView.findViewById(R.id.url_event_list);
			holder.checkbox = (CheckBox)convertView.findViewById(R.id.event_list_checkBox);
			holder.lieuEvenement = (TextView)convertView.findViewById(R.id.place_event_list);

			holder.checkbox.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if(((CheckBox) v).isChecked()){
						//ajout au conteneur des contacts coches
						mIdCheckedItems.add(holder.idEvenement.getText().toString());
					}else{
						//suppression au conteneur des contacts coches
			        	mIdCheckedItems.remove(holder.idEvenement.getText().toString());
					}
				}
			});
			
			if(mHideCheckBox){
				holder.checkbox.setVisibility(View.GONE);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.idEvenement.setText(Long.toString(mEventList.get(position).getId()));
		holder.titreEvenement.setText(mEventList.get(position).getTitre());
		holder.dateEvenement.setText(mEventList.get(position).getDate());
		holder.heureEvenement.setText(mEventList.get(position).getHeure());
		holder.urlEvenement.setText(mEventList.get(position).getUrl());
		holder.lieuEvenement.setText(mEventList.get(position).getLocation());
		holder.checkbox.setChecked(false);

		return convertView;
	}

	public void setEventList(List<Event> list) {
		this.mEventList = list;
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
