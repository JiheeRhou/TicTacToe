package com.georgian.assignment2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Arrays;

public class GameFragment extends Fragment {


    TextView playerOneTV, playerTwoTV, messageTV;      // Player One, Play Two, Message TextView
    Button playAgainBtn, quitBtn;       // Play Again, Quit Buttons

    String playerOne, playerTwo;        // Player One's name, Play Two's name from Welcome fragment

    // Ths game end flag (true: the game end)
    boolean endGameFlag = false;

    // The turn of the player (Player One: 1 / Player Two: 2
    int activePlayer = 1;

    // Each grid box: empty(0) / PlayerOne(1) / PlayerTwo(2)
    int[] gameState = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    // The sets of positions of win
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};

    // The count of taps (9: end of the game)
    public static int counter = 0;

    // The player One's points
    public static int playerOnePoints = 0;

    // The player Two's points
    public static int playerTowPoints = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_game, container, false);

        messageTV = view.findViewById(R.id.messageTV);
        playerOneTV = view.findViewById(R.id.playerOneTV);
        playerTwoTV = view.findViewById(R.id.playerTwoTV);
        playAgainBtn = view.findViewById(R.id.playAgainBtn);
        quitBtn = view.findViewById(R.id.quitBtn);

        // Reset the game
        gameReset(view);

        playerOnePoints = 0;
        playerTowPoints = 0;

        // Get the data from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            playerOne = bundle.getString("playerOne");
            playerTwo = bundle.getString("playerTwo");

            messageTV.setText(String.format("%s's Turn", playerOne));

            setPoints();
        }

        counter = 0;

        // Set the onClick event for each grid box
        for (int i = 0; i < 9; i++){
            int id = getResources().getIdentifier("imageView" + i, "id", requireContext().getPackageName());
            ImageView imageView = view.findViewById(id);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerTap(v);
                }
            });
        }

        // Set the onClick event for the Play Again button
        playAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the game
                gameReset(view);
            }
        });

        // Set the onClick event for the Quit button
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace GameFragment with WelcomeFragment
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentLayout, new WelcomeFragment());
                ft.commit();

            }
        });

        return view;
    }

    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        // If the game ended, return
        if (endGameFlag) {
            return;
        }

        // If the tapped image is empty
        if (gameState[tappedImage] == 0) {
            // Increase the counter
            counter++;

            // Mark this position
            gameState[tappedImage] = activePlayer;

            // Effect to the image
            img.setTranslationY(-1000f);

            // Switch the active player (1 and 2)
            if (activePlayer == 1) {
                // Set the image of x
                img.setImageResource(R.drawable.o);
                activePlayer = 2;

                // Change the turn
                messageTV.setText(String.format("%s's Turn", playerTwo));
            } else {
                // Set the image of o
                img.setImageResource(R.drawable.x);
                activePlayer = 1;

                // Change the turn
                messageTV.setText(String.format("%s's Turn", playerOne));
            }
            img.animate().translationYBy(1000f).setDuration(300);
        }

        // Check there is no winner
        boolean noWinner = true;
        // Find the winner if counter > 4
        if (counter > 4) {
            for (int[] winPosition : winPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                        gameState[winPosition[1]] == gameState[winPosition[2]] &&
                        gameState[winPosition[0]] != 0) {
                    noWinner = false;

                    String winnerStr;

                    // Find the winner
                    if (gameState[winPosition[0]] == 1) {
                        playerOnePoints++;
                        winnerStr = String.format("Congrats %s!", playerOne);
                    } else {
                        playerTowPoints++;
                        winnerStr = String.format("Congrats %s!", playerTwo);
                    }

                    // End game and send the winner message
                    endGame(view, winnerStr);
                }
            }
            // Set the message "Tie" when check all the boxes and there is no winner
            if (counter == 9 && noWinner) {
                endGame(view, "The Game was Tie!");
            }
        }
    }

    // Set the points of players
    public void setPoints() {
        playerOneTV.setText(String.format("%s (O): %s", playerOne, playerOnePoints));
        playerTwoTV.setText(String.format("%s (X): %s", playerTwo, playerTowPoints));
    }

    // Set the result of game to show winner
    public void endGame(View view, String message) {
        // Set ths game end flag end
        endGameFlag = true;

        // Set the message the result of the game
        messageTV.setText(message);
        setPoints();

        // Set visible the two buttons (Play Again and Quick)
        playAgainBtn.setVisibility(View.VISIBLE);
        quitBtn.setVisibility(View.VISIBLE);
    }

    // Reset the game
    public void gameReset(View view) {
        // Reset the endGameFlag, activePlayer, counter
        endGameFlag = false;
        activePlayer = 1;
        counter = 0;

        // Set all to default
        Arrays.fill(gameState, 0);
        // Remove all the images from the boxes inside the grid
        for (int i = 0; i < 9; i++) {
            int id = getResources().getIdentifier("imageView" + i, "id", requireContext().getPackageName());
            ImageView imageView = view.findViewById(id);
            imageView.setImageResource(0);
        }

        // Set invisible the two buttons (Play Again and Quick)
        playAgainBtn.setVisibility(View.INVISIBLE);
        quitBtn.setVisibility(View.INVISIBLE);

        // Set the turn
        messageTV.setText(String.format("%s's Turn", playerOne));
    }
}