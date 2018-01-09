/* Copyright 2016 Google Inc.
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

package com.google.engedu.blackhole;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    // Colors used to differentiate human player from computer player.
    private static final int[] COLORS = {Color.rgb(255, 128, 128), Color.rgb(128, 128, 255)};
    int button[]={R.id.u1,R.id.u2,R.id.u3,R.id.u3,R.id.u4,R.id.u5,R.id.u6,R.id.u7,R.id.u8,R.id.u9,R.id.u10,R.id.c1,R.id.c2,R.id.c3,R.id.c4,R.id.c5,R.id.c6,R.id.c7,R.id.c8,R.id.c9,R.id.c10};
    // The main board instance.
    private BlackHoleBoard board;
    TextView tr=null,tf=null;

    // Initialize the board on launch.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tr= (TextView) findViewById(R.id.utxt);
        tf= (TextView) findViewById(R.id.ctxt);


        board = new BlackHoleBoard();
        onReset(null);

    }

    /* Shared handler for all the game buttons. When the user takes a turn we mark the button as
     * having been clicked and let the computer take a turn.
     */
    int c=0;

    public void onClickHandler(View view) {

        Button clicked = (Button) view;
        if (clicked.isEnabled()) {
            markButtonAsClicked(clicked);
            int val=board.getCurrentPlayerValue();

            c=c+val;
            tr.setText("User Score:-"+c);
            tf.setText("Computer Score:-"+c);

            computerTurn();


        }

    }

    // Change the button that was clicked and update the board accordingly.
    private void markButtonAsClicked(Button clicked) {
        clicked.setEnabled(false);
        int val=board.getCurrentPlayerValue();


        for(int i:button)
        {
            Button b=(Button) findViewById(i);
            String fg= (String) b.getText();
            if(fg.equalsIgnoreCase(""+val))
            {
   b.setEnabled(false);
                b.setBackgroundColor(5);
            }
        }
        clicked.setText("" + board.getCurrentPlayerValue());
        clicked.getBackground().setColorFilter(
                COLORS[board.getCurrentPlayer()], PorterDuff.Mode.MULTIPLY);
        String buttonLabel = getResources().getResourceEntryName(clicked.getId());
        board.setValue(Integer.parseInt(buttonLabel.substring(6)));
        if (board.gameOver()) {
            handleEndOfGame();
        }
    }

    // When the game is over, declare a winner.
    private void handleEndOfGame() {
        disableAllButtons();
        int score = board.getScore();
        String message = null;

        tr.setText("User Score:-"+BlackHoleBoard.uscore);
        tf.setText("Computer Score:-"+BlackHoleBoard.cscore);



        int ind= BlackHoleBoard.emp;
        int id1 = getResources().getIdentifier("button" + ind, "id", getPackageName());
        Button b = (Button) findViewById(id1);
        b.getBackground().setColorFilter(0xFF000000,PorterDuff.Mode.MULTIPLY);

        for(int i: BlackHoleBoard.indexArray)
        {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            Button b1 = (Button) findViewById(id);
            b1.getBackground().setColorFilter(null);

        }



        if (score > 0) {
            message = "You win by " + score;
        }
        else if (score < 0) {
            message = "You lose by " + -score;
        }
        if (message != null) {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.show();
            Log.i("BlackHole", message);
        }

    }

    // When the game is over disable all buttons (really just the one button that is left).
    private void disableAllButtons() {

        for (int i = 0; i < BlackHoleBoard.BOARD_SIZE; i++) {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            Button b2 = (Button) findViewById(id);
            b2.setEnabled(false);

        }

    }

    // Let the computer take a turn.
    private void computerTurn() {
        int position = board.pickMove();
        int id = getResources().getIdentifier("button" + position, "id", getPackageName());
        Button b = (Button) findViewById(id);
        if (b == null) {
            Log.i("Blackhole", "Couldn't find button " + position + " with id " + id);
        }
        markButtonAsClicked(b);
    }

    // Handler for the reset button. Resets both the board and the game buttons.
    public void onReset(View view) {
        board.reset();

        int ind= BlackHoleBoard.emp;
        int id1 = getResources().getIdentifier("button" + ind, "id", getPackageName());
        Button b = (Button) findViewById(id1);
        b.getBackground().setColorFilter(null);

        for (int i = 0; i < BlackHoleBoard.BOARD_SIZE; i++) {
            int id = getResources().getIdentifier("button" + i, "id", getPackageName());
            Button b1 = (Button) findViewById(id);
            b1.setEnabled(true);
            b1.setText("?");
            b1.getBackground().setColorFilter(null);


        }
        int a=0;

      for(int i=0;i<=10;i++) {

          int id=button[i];
          Button b2 = (Button) findViewById(id);

           b2.setEnabled(true);
          b2.setBackgroundColor(Color.rgb(255,128,128));

          
        }
        for(int i=11;i<21;i++) {
            int id=button[i];
            Button b3 = (Button) findViewById(id);

            b3.setEnabled(true);
            b3.setBackgroundColor(Color.rgb(128,128,255));
            //b3.getBackground().setColorFilter(Color.rgb(128,128,255), PorterDuff.Mode.MULTIPLY);

        }
        c=0;
        tr.setText("User Score:-0");
        tf.setText("Computer Score:-0");

        BlackHoleBoard.indexArray.clear();
    }
}
