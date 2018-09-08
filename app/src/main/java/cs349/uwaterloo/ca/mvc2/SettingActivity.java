package cs349.uwaterloo.ca.mvc2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.Observable;
import java.util.Observer;

public class SettingActivity extends AppCompatActivity implements Observer {
    Simon simon;
    private RadioGroup numBall; //  ball
    private RadioGroup mode; // mode
    private Button back; // back button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //simon
        simon = Simon.getInstance();
        simon.addObserver(this);

        // set the widgets
        back = (Button) findViewById(R.id.back);
        numBall = (RadioGroup) findViewById(R.id.num_ball);
        mode = (RadioGroup) findViewById(R.id.radio);


        // set the radio button to the right amount
        int ball = simon.getNumButtons();
        switch (ball) {
            case 1:
                numBall.check(R.id.b1);
                break;
            case 2:
                numBall.check(R.id.b2);
                break;
            case 3:
                numBall.check(R.id.b3);
                break;
            case 4:
                numBall.check(R.id.b4);
                break;
            case 5:
                numBall.check(R.id.b5);
                break;
            case 6:
                numBall.check(R.id.b6);
                break;
            default:
                numBall.check(R.id.b6);
                break;
        }

        Simon.Mode m = simon.getMode();
        switch (m) {
            case EASY:
                mode.check(R.id.easy);
                break;
            case NORMAL:
                mode.check(R.id.normal);
                break;
            case HARD:
                mode.check(R.id.hard);
                break;
            default:
                mode.check(R.id.hard);
        }

        // Create controller to increment counter
        back.setOnClickListener(new View.OnClickListener()
        {

            @Override

            public void onClick(View v)
            {

                //launch game view
                // Create Intent to launch gameview
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);


                Simon.Mode m = Simon.Mode.NORMAL;
                switch (mode.getCheckedRadioButtonId()) {
                    case R.id.easy:
                        m = Simon.Mode.EASY;
                        break;
                    case R.id.hard:
                        m = Simon.Mode.HARD;
                        break;


                }
                simon.setMode(m); // set the mode, not part of the old game


                switch (numBall.getCheckedRadioButtonId()) {
                    case R.id.b1:
                        simon.setInstance(1);
                        break;
                    case R.id.b2:
                        simon.setInstance(2);
                        break;
                    case R.id.b3:
                        simon.setInstance(3);
                        break;
                    case R.id.b4:
                        simon.setInstance(4);
                        break;
                    case R.id.b5:
                        simon.setInstance(5);
                        break;
                    case R.id.b6:
                        simon.setInstance(6);
                        break;



                }

                //simon.newRound();
                //simon.setButtons(ball);


                // Start activite
                startActivity(intent);
                finish();


            }

        });






    }
    protected void onDestroy() {
        simon.deleteObserver(this);
        super.onDestroy();
    }
    public void update(Observable o, Object arg)
    {
        int ball = simon.getNumButtons();
        // make sure the setting is correct when you revisit
        switch (ball) {
            case 1:
                numBall.check(R.id.b1);
                break;
            case 2:
                numBall.check(R.id.b2);
                break;
            case 3:
                numBall.check(R.id.b3);
                break;
            case 4:
                numBall.check(R.id.b4);
                break;
            case 5:
                numBall.check(R.id.b5);
                break;
            case 6:
                numBall.check(R.id.b6);
                break;
            default:
                numBall.check(R.id.b6);
                break;
        }

        Simon.Mode m = simon.getMode();
        switch (m) {
            case EASY:
                mode.check(R.id.easy);
                break;
            case NORMAL:
                mode.check(R.id.normal);
                break;
            case HARD:
                mode.check(R.id.hard);
                break;
            default:
                mode.check(R.id.hard);
        }
    }

}
