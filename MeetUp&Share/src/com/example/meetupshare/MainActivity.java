package com.example.meetupshare;

import com.example.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

	User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.home:
			Intent intent = new Intent(this.getApplicationContext(), Home.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("currentUser", currentUser);
			intent.putExtras(bundle);
			startActivity(intent);
			return true;
		case R.id.options:
			Intent intent1 = new Intent(this, Options.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("currentUser", currentUser);
			intent1.putExtras(bundle1);
			startActivity(intent1);
			return true;
		case R.id.contact:
			Intent intent2 = new Intent(this, Contacts.class);
			Bundle bundle2 = new Bundle();
			bundle2.putSerializable("currentUser", currentUser);
			intent2.putExtras(bundle2);
			startActivity(intent2);
			return true;
			//marchepas
		case R.id.calandar:
			Intent intent3 = new Intent(this, Calandar.class);
			Bundle bundle3 = new Bundle();
			bundle3.putSerializable("currentUser", currentUser);
			intent3.putExtras(bundle3);
			startActivity(intent3);
			return true;
		case R.id.newevent:
			Intent intent4 = new Intent(this, Nouvel_evenement.class);
			Bundle bundle4 = new Bundle();
			bundle4.putSerializable("currentUser", currentUser);
			intent4.putExtras(bundle4);
			startActivity(intent4);
			return true;
		case R.id.photos:
			Intent intent5 = new Intent(this, SeePics.class);
			Bundle bundle5 = new Bundle();
			bundle5.putSerializable("currentUser", currentUser);
			intent5.putExtras(bundle5);
			startActivity(intent5);
		case R.id.deconnect:
			Intent intent6 = new Intent(this, Connexion.class);
			Bundle bundle6 = new Bundle();
			bundle6.putSerializable("currentUser", currentUser);
			intent6.putExtras(bundle6);
			startActivity(intent6);
			finish();
			return true;
			



		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
