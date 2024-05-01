package dk.itu.garbage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Info2Fragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_info_2, container, false);
        Button goBack_button = v.findViewById(R.id.goBack_button);

        goBack_button.setOnClickListener(view ->
                getActivity().
                        getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_ui,
                                new UIFragment())
                        .replace(R.id.container_list,
                                new ListFragment())
                        .commit()
        );
        return v;
    }
}