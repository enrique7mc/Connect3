package com.enrique7mc.connect3;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private TableLayout tableLayout;
    private List<ImageView> tiles;
    private Boolean[] gameState;
    private LinearLayout message;
    private Boolean winner;
    private boolean turnOne;
    private boolean gameIsActive = true;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        message = (LinearLayout)findViewById(R.id.message);
        tiles = new ArrayList<>();
        gameState = new Boolean[9];

        setupViews();
        hideViews();
    }

    private void setupViews() {
        for(int i = 0, j = tableLayout.getChildCount(); i < j; i++) {
            View view = tableLayout.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                for (int k = 0, c = row.getChildCount(); k < c; k++ ) {
                    ImageView image = (ImageView) row.getChildAt(k);
                    tiles.add(image);
                    image.setOnClickListener(listener);
                }
            }
        }
    }

    private void hideViews() {
        for(ImageView tile : tiles) {
            tile.setAlpha(0f);
        }

        message.setVisibility(View.GONE);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = tiles.indexOf(v);

            if (gameState[index] != null || !gameIsActive) {
                return;
            }

            int resource = turnOne ? R.drawable.yellow : R.drawable.red;
            gameState[index] = turnOne;

            turnOne = !turnOne;
            ((ImageView)v).setImageResource(resource);

            v.setTranslationY(-1000f);
            v.setAlpha(1.0f);
            v.animate()
             .rotationBy(360f)
             .translationYBy(1000f)
             .setDuration(800);
            count++;

            if (haveWinner() || finished()) {
                message.setTranslationY(-1000f);
                message.setVisibility(View.VISIBLE);
                message.animate().translationYBy(1000f).setDuration(1500);
                TextView textView = (TextView) message.findViewById(R.id.textView);
                String label;

                if (winner == null) {
                    label = "It is a tie";
                } else {
                    label = winner.booleanValue() ? "Yellow wins" : "Red wins";
                }

                gameIsActive = false;

                textView.setText(label);
            }
        }
    };

    private boolean haveWinner() {
        for (int i = 0; i < 7; i = i + 3) {
            if (gameState[i] == gameState[i + 1] && gameState[i] == gameState[i + 2] && gameState[i] != null) {
                winner = gameState[i];
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (gameState[i] == gameState[i + 3] && gameState[i] == gameState[i + 6] && gameState[i] != null) {
                winner = gameState[i];
                return true;
            }
        }

        if(gameState[0] == gameState[4] && gameState[0] == gameState[8] && gameState[0] != null) {
            winner = gameState[0];
            return true;
        }

        if(gameState[2] == gameState[4] && gameState[2] == gameState[6] && gameState[2] != null) {
            winner = gameState[2];
            return true;
        }

        return false;
    }

    private boolean finished() {
        return count >= 9;
    }

    public void restartGame(View view) {
        gameState = new Boolean[9];
        winner = null;
        turnOne = false;
        gameIsActive = true;
        count = 0;
        hideViews();
    }
}
