package dk.itu.garbage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ListFragment extends Fragment{
    GarbageViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View v= inflater.inflate(R.layout.fragment_list, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(GarbageViewModel.class);

        final RecyclerView listThings = v.findViewById(R.id.listItems);
        listThings.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemAdapter mAdapter = new ItemAdapter(viewModel);
        listThings.setAdapter(mAdapter);
        viewModel.getUiState().observe(getActivity(), itemDB -> mAdapter.notifyDataSetChanged());
        return v;
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mWhatTextView;
        private final TextView mWhereTextView;
        private final TextView mNoView;
        private final GarbageViewModel viewModel;

        public ItemHolder(View itemView, GarbageViewModel garbageViewModel ) {
            super(itemView);

            this.viewModel = garbageViewModel;

            mNoView = itemView.findViewById(R.id.item_no);
            mWhatTextView = itemView.findViewById(R.id.item_what);
            mWhereTextView = itemView.findViewById(R.id.item_where);
            itemView.setOnClickListener(this);
        }

        public void bind(Item item, int position) {
            mNoView.setText(" " + (position+1) + " ");
            mWhatTextView.setText(item.getWhat());
            mWhereTextView.setText(item.getWhere());
        }

        @Override
        public void onClick(View v) {
            TextView what = (TextView) v.findViewById(R.id.item_what);
            TextView where = (TextView) v.findViewById(R.id.item_where);
            //once we have a value for what, we can delete the item
            if (getActivity() != null) {
                viewModel.onDeleteItemClick(what, where, getActivity());
            }

        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private GarbageViewModel viewModel;

        public ItemAdapter(GarbageViewModel garbageViewModel) {
            this.viewModel = garbageViewModel;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.one_row, parent, false);
            return new ItemHolder(v, viewModel);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            Item item = viewModel.getUiState().getValue().getItemList().get(position);
            holder.bind(item, position);
        }

        @Override
        public int getItemCount() {
            return viewModel.getUiState().getValue().getItemListSize();
        }
    }
}