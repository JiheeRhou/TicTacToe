package com.georgian.assignment2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WelcomeFragment extends Fragment {

    EditText playerOne;     // Player One's name EditText
    EditText playerTwo;     // Player Two's name EditText
    Button startGameBtn;    // Start Game Button

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        startGameBtn = view.findViewById(R.id.startGameBtn);
        playerOne = view.findViewById(R.id.playerOneTV);
        playerTwo = view.findViewById(R.id.playerTwoTV);

        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String playerOneStr = playerOne.getText().toString().trim();
                String playerTwoStr = playerTwo.getText().toString().trim();

                // Check the Player One's name
                if (TextUtils.isEmpty(playerOneStr)) {
                    Toast.makeText(getActivity(), "Enter the Player One's name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check the Player Two's name
                if (TextUtils.isEmpty(playerTwoStr)) {
                    Toast.makeText(getActivity(), "Enter the Player Two's name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a Bundle
                Bundle bundle = new Bundle();

                // Put the data to the Bundle
                bundle.putString("playerOne", playerOneStr);
                bundle.putString("playerTwo", playerTwoStr);

                GameFragment gameFragment = new GameFragment();
                gameFragment.setArguments(bundle);

                // Replace WelcomeFragment with GameFragment
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentLayout, gameFragment);
                ft.commit();
            }
        });

        return view;
    }
}