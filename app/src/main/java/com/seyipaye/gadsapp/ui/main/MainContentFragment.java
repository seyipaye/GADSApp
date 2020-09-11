package com.seyipaye.gadsapp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.seyipaye.gadsapp.databinding.FragmentMainBinding;
import com.seyipaye.gadsapp.databinding.MainRvItemBinding;
import com.seyipaye.gadsapp.ui.Callbacks;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainContentFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    int index = 1;
    private FragmentMainBinding binding;
    private MutableLiveData<List<MainRVItem>> mutableLearningLeaders;

    public static MainContentFragment newInstance(int index) {
        MainContentFragment fragment = new MainContentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater);
        View view = binding.getRoot();

        MyAdapter mAdapter = new MyAdapter();
        binding.mainRV.setAdapter(mAdapter);

        ((MainActivity) getActivity()).getData(index, new Callbacks.Leaders() {

            @Override
            public void onFetchStarted() {
                Log.i(TAG, "onFetchStarted: ");
            }

            @Override
            public void onFetchSuccess(List<MainRVItem> mainRVItemList) {
                //Log.i(TAG, "onFetchSuccesssss: " + mainRVItemList);
                binding.progressBar.setVisibility(View.GONE);
                binding.error.setVisibility(View.GONE);
                mAdapter.setData(mainRVItemList);

            }

            @Override
            public void onFetchFailure(String message) {
                Log.e(TAG, "onFetchFailure: " + message);
                binding.error.setVisibility(View.VISIBLE);
                binding.error.setText(message);
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        return view;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<MainRVItem> items;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // Each data item is just a string in this case
            MainRvItemBinding binding;
            public MyViewHolder(MainRvItemBinding b) {
                super(b.getRoot());
                binding = b;
            }
        }

        public MyAdapter() {}

        public void setData(List<MainRVItem> myDataset) {
            items = myDataset;
            notifyDataSetChanged();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
            return new MyViewHolder(MainRvItemBinding.inflate(getLayoutInflater(), parent, false));
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            MainRVItem thisItem = items.get(position);
            holder.binding.title.setText(thisItem.name);

            Glide.with(getActivity())
                    .load(thisItem.badgeUrl)
                    //.centerCrop()
                    //.fitCenter()
                    .centerInside()
                    .into(holder.binding.badge);

            if(thisItem.score == 0) {
                // It is The learning leaders list
                holder.binding.details.setText(thisItem.hours + " learning hours, " + thisItem.country);
            } else {
                holder.binding.details.setText(thisItem.score + " skill IQ Score, " + thisItem.country);

            }

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return items != null ? items.size() : 0;
        }
    }
}