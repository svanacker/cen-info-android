package org.cen.android.mainboard;

import java.util.Vector;

import javax.vecmath.Vector3d;

import org.cen.android.robot.AndroidRobot;
import org.cen.android.robot.AndroidRobotDeviceRequest;
import org.cen.geom.Point2D;
import org.cen.robot.RobotPosition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * An activity representing a single Devices detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a {@link DevicesListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link DevicesDetailFragment}.
 */
public class DevicesDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devices_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(DevicesDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(DevicesDetailFragment.ARG_ITEM_ID));
			DevicesDetailFragment fragment = new DevicesDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.devices_detail_container, fragment).commit();
		}
		
		TextView textView = (TextView) findViewById(R.id.textView1);
		AndroidRobotDeviceRequest robotDeviceRequest = new AndroidRobotDeviceRequest();
		String deviceName = robotDeviceRequest.getDeviceName();
		
		AndroidRobot robot = new AndroidRobot();
		
		String robotName = robot.getName();
		
		RobotPosition position = new RobotPosition(5,  10,  20);
		
		Point2D point2d = position.getCentralPoint();
		// Point2D point2d = new Point2D.Double(5, 10);
		String point = point2d.toString();
		
		Vector3d vector = new Vector3d(1, 2, 3);
		String vectorName = vector.toString();
		
		textView.setText(deviceName + "-" + robotName + "-" + point + "-" + vectorName);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this,
					DevicesListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
