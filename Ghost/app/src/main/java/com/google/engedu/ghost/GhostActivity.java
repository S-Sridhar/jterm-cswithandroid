/* Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String TAG = "GhostActivity";

    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private static final String KEY_USER_TURN = "keyUserTurn";
    private static final String KEY_CURRENT_WORD = "keyCurrentWord";
    private static final String KEY_SAVED_STATUS = "keySavedStatus";

    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private String currentWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            // Initialize your dictionary from the InputStream.
           // dictionary = new SimpleDictionary(inputStream);
            dictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        if (savedInstanceState == null) {
            onStart(null);
        } else {
            userTurn = savedInstanceState.getBoolean(KEY_USER_TURN);
            currentWord = savedInstanceState.getString(KEY_CURRENT_WORD);
            String gameStatus = savedInstanceState.getString(KEY_SAVED_STATUS);
            ((TextView) findViewById(R.id.gameStatus)).setText(gameStatus);
            ((TextView) findViewById(R.id.ghostText)).setText(currentWord);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_USER_TURN, userTurn);
        outState.putString(KEY_CURRENT_WORD, currentWord);
        outState.putString(KEY_SAVED_STATUS,
                ((TextView) findViewById(R.id.gameStatus)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int unicode = event.getUnicodeChar();
        if ((unicode > 89 && unicode <91) || (unicode > 96 && unicode <123 )) {
            String character = ((char) unicode + "").toLowerCase();
            currentWord += character;
            TextView ghostText = (TextView) findViewById(R.id.ghostText);
            ghostText.setText(currentWord);
            computerTurn();
            /*if(dictionary.isWord(currentWord)) {
                TextView gameStatus = (TextView) findViewById(R.id.gameStatus);
                String displayStatus = "This is a valid word";
                gameStatus.setText(displayStatus);
                computerTurn();
            }*/
        }
        return super.onKeyUp(keyCode, event);

    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    /**
     * Handler for the "challenge" button.
     * @param view
     */
    public void challenge(View view) {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        label.setText(USER_TURN);

        if (currentWord.length() >= 4 && dictionary.isWord(currentWord)) {
            String validWord = "Computer finished with a valid word, computer loses. You Win!";
            label.setText(validWord);
        } else {
            String nextWord = dictionary.getGoodWordStartingWith(currentWord);
            if (nextWord == null) {
                label.setText("There is no valid word starting with this prefix. You Win!");
                //userTurn = true;
                //challenge();
            } else {
                label.setText(currentWord + " is a valid prefix and not a word, you lose. Computer Wins!");
                ((TextView) findViewById(R.id.ghostText)).setText(nextWord);
            }
        }
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
       label.setText(COMPUTER_TURN);

        if (currentWord.length() >= 4 && dictionary.isWord(currentWord)) {
            String validWord = "You finished with a valid word, you lose. Computer Wins!";
            label.setText(validWord);
        } else if (dictionary.getGoodWordStartingWith(currentWord) == null) {
            //String nextWord = dictionary.getGoodWordStartingWith(currentWord);
            //if (nextWord == null) {
            label.setText("There is no valid word starting with this prefix, you lose. Computer Wins!");
        } else {
            label.setText(currentWord + " is a valid prefix and not a word.");
            String nextWord = dictionary.getGoodWordStartingWith(currentWord);
            String nextLetter = nextWord.substring(currentWord.length(), currentWord.length() +1);
            currentWord += nextLetter;
            ((TextView) findViewById(R.id.ghostText)).setText(currentWord);
        }

        userTurn = true;
        label.setText(USER_TURN);
    }
}
