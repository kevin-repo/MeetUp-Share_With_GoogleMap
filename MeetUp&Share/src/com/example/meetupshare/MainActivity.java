package com.example.meetupshare;

import com.example.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Menu
 *
 */
public class MainActivity extends Activity {

	User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.options:
			Intent intent1 = new Intent(this, Options.class);
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("currentUser", currentUser);
			intent1.putExtras(bundle1);
			startActivity(intent1);
			return true;
		case R.id.deconnect:
			Intent intent6 = new Intent(this, Connexion.class);
			Bundle bundle6 = new Bundle();
			bundle6.putSerializable("currentUser", currentUser);
			intent6.putExtras(bundle6);
			startActivity(intent6);
			finish();
			return true;
		case R.id.refresh:
			Intent intent = getIntent();
			startActivity(intent);
			finish();
			return true;	
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
