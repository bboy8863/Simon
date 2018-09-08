package cs349.uwaterloo.ca.mvc2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import java.lang.Thread;
import java.util.ArrayList;



import java.util.Observable;
import java.util.Observer;

public class GameActivity extends AppCompatActivity implements Observer {
    Simon simon;
    Button back;
    Button restart = null;
    private TextView score;
    private TextView message;
    private ArrayList<Button> listOfButtons;
    final Animation mAnimation = new AlphaAnimation(1, 0);
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getSupportActionBar().setTitle("Simon Game");

        layout = (LinearLayout) findViewById(R.id.layout);

        // get simon
        simon = Simon.getInstance();
        simon.addObserver(this);
        simon.init(simon.getNumButtons(), false);
        simon.setMode(simon.getMode());
        simon.newRound();

        listOfButtons = new ArrayList<Button>();




        // set the widgets
        back = (Button) findViewById(R.id.back);
        message = (TextView)findViewById(R.id.message);
        score = (TextView)findViewById(R.id.score);

        // Create controller to increment counter
        back.setOnClickListener(new View.OnClickListener()
        {

            @Override

            public void onClick(View v)
            {

                //launch game view
                // Create Intent to launch gameview
                Intent intent = new Intent(GameActivity.this, MainActivity.class);


                // Start activite
                startActivity(intent);
                finish();


            }

        });
        // set the animation for the button

        mAnimation.setDuration(200);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.ABSOLUTE);
        mAnimation.setRepeatMode(Animation.REVERSE);


        //listOfButton = (ListView)findViewById(R.id.list_view);
        for (int i = 0; i < simon.getNumButtons(); ++i) {
            Button b = new Button(this);
            b.setText(String.valueOf(i+1));
            b.setGravity(Gravity.TOP);
            b.setBackgroundResource(R.drawable.ripple);
            b.setBackgroundColor(Color.HSVToColor(new float[] {(50.f+i*10.0f),0.7f,0.7f}));
            b.setEnabled(false);

            final int real_i = i;
            final Button real_b = b;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    real_b.startAnimation(mAnimation);;

                    if (simon.getState() == Simon.State.HUMAN) {

                        simon.verifyButton(real_i);
                    }
                }

            });
            listOfButtons.add(b);
            layout.addView(b);

        }






        simon.setChangedAndNotify();

    }

    //disable or enable all buttons
    void enableButtons(boolean enable) {
        for (Button b : listOfButtons) {
            b.setEnabled(enable);
        }
    }
    //boolean wait = false;

    public void update(Observable o, Object arg)
    {

        if (score != null) {
            score.setText("score: "+simon.getScore());
        }


        if (message!=null ) {
            Handler handler = new Handler();
            switch (simon.getState()) {


                case COMPUTER: {
                    enableButtons(false); // button disabled


                    int delayFactor = 1000; // delay factor depending on the mode of the game
                    switch (simon.getMode()) {
                        case EASY:
                            delayFactor = 2000;
                            break;
                        case NORMAL:
                            delayFactor = 1000;
                            break;
                        case HARD:
                            delayFactor = 500;


                    }

                    // toast
                    Context context = getApplicationContext();
                    CharSequence text = "WATCH WHAT I DO";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 200);
                    toast.show();

                    int i = 0; //  for postdelay
                    while (simon.getState() == Simon.State.COMPUTER) {


                        final int b = simon.nextButton();


                        handler.postDelayed(new Runnable() {
                            public void run() {


                                listOfButtons.get(b).startAnimation(mAnimation);
                                //listOfButtons.get(b).setText("FLASH");

                            }
                        }, i * delayFactor + 5000); // 4000 is for the "win" and "what i do"
                        ++i;


                    }

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Context context = getApplicationContext();
                            CharSequence text = "YOUR TURN";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 200);
                            toast.show();



                        }
                    }, i * delayFactor + 5000);

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            enableButtons(true);
                            //message.setText("YOUR TURN");

                        }
                    }, i * delayFactor + 7000); // 7000 for the "win" + "your turn" toast


                    break;
                }
                case HUMAN: {

                    enableButtons(true);
                    break;
                }
                case WIN: {
                    enableButtons(false);
                    message.setText("WIN!");

                    // win toast
                    Context context = getApplicationContext();
                    CharSequence text = "WIN";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 200);
                    toast.show();

                    // button for next round
                    restart = new Button(this);
                    restart.setText("NEXT ROUND");


                    restart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            message.setText("");

                            simon.newRound();
                            layout.removeView(restart);


                        }

                    });

                    layout.addView(restart,0);

                    break;
                }
                case LOSE: {
                    enableButtons(false);
                    message.setText("LOSE :(");
                    restart = new Button(this);
                    restart.setText("PLAY AGAIN");



                    restart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            simon.init(simon.getNumButtons(), false);
                            message.setText(""); // remove the lose text
                            layout.removeView(restart);


                        }

                    });

                    layout.addView(restart,0);


                    break;
                }
                case START:

                    simon.newRound();
                    break;
                default:
                    break;


            }
        }







    }
    @Override
    protected void onDestroy() {
        simon.deleteObserver(this);
        super.onDestroy();
    }




}
