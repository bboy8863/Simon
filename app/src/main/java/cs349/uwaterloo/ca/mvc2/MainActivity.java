package cs349.uwaterloo.ca.mvc2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer
{
    // Private Vars
    //Simon simon;
    Button start;
    Button setting;

    //Button mIncrementButton2;

    /**
     * OnCreate
     * -- Called when application is initially launched.
     *    @see <a href="https://developer.android.com/guide/components/activities/activity-lifecycle.html">Android LifeCycle</a>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "MainActivity: OnCreate chen");
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("SIMON");


        // Get button reference.
        start = (Button) findViewById(R.id.start_button);
        setting = (Button) findViewById(R.id.setting_button);

        // Create controller to increment counter
        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                // Start activity
                startActivity(intent);
                finish();

            }
        });
        // setting listener
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch game view
                // Create Intent to launch gameview
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                // Start activity
                startActivity(intent);
                finish();
            }
        });


    }

    public void update(Observable o, Object arg)
    {

    }
    protected void onDestroy() {
        super.onDestroy();
    }
}
