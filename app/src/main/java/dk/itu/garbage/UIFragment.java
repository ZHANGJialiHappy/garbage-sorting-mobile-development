package dk.itu.garbage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class UIFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v= inflater.inflate(R.layout.fragment_ui, container, false);
        final GarbageViewModel viewModel = new ViewModelProvider(requireActivity()).get(GarbageViewModel.class);


        EditText what_input = v.findViewById(R.id.what_text);
        EditText where_input = v.findViewById(R.id.where_text);

        Button info_button = v.findViewById(R.id.info_button);

        info_button.setOnClickListener(view ->
                getActivity().
                        getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_ui,
                                new InfoFragment()).replace(R.id.container_list,
                                new Info2Fragment()).commit()
                        );




        Button where_button = v.findViewById(R.id.where_button);
        where_button.setOnClickListener(view -> viewModel.onFindItemClick(what_input, where_input, getActivity()));

        Button add_button = v.findViewById(R.id.add_button);
        add_button.setOnClickListener(view -> viewModel.onAddItemClick(what_input, where_input, getActivity()));

        Button delete_button = v.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(view ->viewModel.onDeleteItemClick(what_input, where_input, getActivity()));


        return v;
    }


}