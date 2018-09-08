package cs349.uwaterloo.ca.mvc2;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Simon extends Observable {
	// possible game states:
	// PAUSE - nothing happening
	// COMPUTER - computer is play a sequence of buttons
	// HUMAN - human is guessing the sequence of buttons
	// LOSE and WIN - game is over in one of thise states
	public static enum State { START, COMPUTER, HUMAN, LOSE, WIN };
    public static enum Mode { EASY, NORMAL,HARD };

    Mode mode = Mode.NORMAL;

	private static final Simon ourInstance = new Simon(4);

	static Simon getInstance()
	{

		return ourInstance;
	}

    /*static void reset()
    {
        ourInstance = new Simon(4);
    }
    */

	void setInstance(int ball)
	{

		init(ball,false);
	}

	
	// the game state and score
	State state;
	int score;

	// length of sequence
	int length;
	// number of possible buttons
	int buttons;

	// the sequence of buttons and current button
	Vector<Integer> sequence = new Vector<Integer>();
	int index;

	boolean debug;

	void init(int _buttons, boolean _debug){


		// true will output additional information
		// (you will want to turn this off before)
		debug = _debug;

		length = 1;
		buttons = _buttons;
		state = State.START;
		score = 0;

        setChangedAndNotify();

		//if (debug) { cout << "[DEBUG] starting " << buttons << " button game" << endl; }
	}

	Simon(int _buttons) { init(_buttons, false); }

	Simon(int _buttons, boolean _debug) { init(_buttons, _debug); }


	int getNumButtons() { return buttons; }

	int getScore() { return score; }

	State getState() { return state; }

	String getStateAsString() {

		switch (getState()) {

		case START:
			return "START";
			//break;

		case COMPUTER:
			return "COMPUTER";
			//break;

		case HUMAN:
			return "HUMAN";
			//break;

		case LOSE:
			return "LOSE";
			//break;

		case WIN:
			return "WIN";
			//break;
		default:
			return "Unkown State";
			//break;
		}
	}
    void setMode(Mode _mode) {mode = _mode;}
	Mode getMode() { return mode; }

	void newRound() {

		/*if (debug) {
			cout << "[DEBUG] newRound, Simon::state "
			     << getStateAsString() << endl;
		}
		*/

		// reset if they lost last time
		if (state == State.LOSE) {
			//if (debug) { cout << "[DEBUG] reset length and score after loss" << endl; }
			length = 1;
			score = 0;
		}

		sequence.clear();

		//if (debug) { cout << "[DEBUG] new sequence: "; }

		for (int i = 0; i < length; i++) {
			int randomNum = ThreadLocalRandom.current().nextInt(0, 100);
			int b = randomNum % buttons;
			sequence.add(b);
			//if (debug) { cout << b << " "; }
		}
		//if (debug) { cout << endl; }

		index = 0;
		state = State.COMPUTER;
		setChangedAndNotify();

	}

	// call this to get next button to show when computer is playing
	int nextButton() {

		if (state != State.COMPUTER) {
		//	cout << "[WARNING] nextButton called in "
		//	     << getStateAsString() << endl;
			return -1;
		}

		// this is the next button to show in the sequence
		int button = sequence.get(index);

		//if (debug) {
		//	cout << "[DEBUG] nextButton:  index " << index
		//	     << " button " << button
		//	     << endl;
		//}

		// advance to next button
		index++;

		// if all the buttons were shown, give
		// the human a chance to guess the sequence
		if (index >= sequence.size()) {
			index = 0;
			state = State.HUMAN;
		}

		return button;
	}

	boolean verifyButton(int button) {

		if (state != State.HUMAN) {
			//cout << "[WARNING] verifyButton called in "
			//     << getStateAsString() << endl;
			return false;
		}

		// did they press the right button?
		boolean correct = (button == sequence.get(index));

		//if (debug) {
		//	cout << "[DEBUG] verifyButton: index " << index
			//     << ", pushed " << button
			//     << ", sequence " << sequence[index];
		//}

		// advance to next button
		index++;

		// pushed the wrong buttons
		if (!correct) {
			state = State.LOSE;


			// they got it right
		} else {
			//if (debug) { cout << ", correct." << endl; }

			// if last button, then the win the round
			if (index == sequence.size()) {
				state = State.WIN;
				// update the score and increase the difficulty
				score++;
				length++;


			}
		}
        setChangedAndNotify();
		return correct;
	}
	void setChangedAndNotify() {
		setChanged();
		notifyObservers();
	}

}