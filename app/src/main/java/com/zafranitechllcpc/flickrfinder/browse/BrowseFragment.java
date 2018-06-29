package com.zafranitechllcpc.flickrfinder.browse;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.zafranitechllcpc.flickrfinder.R;
import com.zafranitechllcpc.flickrfinder.models.FlickrItem;
import com.zafranitechllcpc.flickrfinder.network.FlickrRequester;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BrowseFragment extends Fragment implements FlickrRequester.ResponseHandler, BrowseAdapter.OnLoadMoreListener {
    public static final String TAG = "BrowseFragment";

    private EditText editText;
    private FlickrRequester flickrRequester;
    private BrowseAdapter browseAdapter;

    public static BrowseFragment getInstance(FragmentManager fragmentManager) {
        BrowseFragment fragment = (BrowseFragment) fragmentManager.findFragmentByTag(BrowseFragment.TAG);
        if (fragment == null) {
            fragment = new BrowseFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
        }
        return fragment;
    }

    public interface DialogDisplay{
        void displayDetailsDialog(FlickrItem item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button searchButton = view.findViewById(R.id.button_search);
        editText = view.findViewById(R.id.edittext_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                browseAdapter.clearFlickrItemList();
                flickrRequester = new FlickrRequester(getActivity(),BrowseFragment.this);
                Log.e(TAG, "api key: " + getResources().getString(R.string.flickr_api_key));
                flickrRequester.searchByText(editText.getText().toString(), 1);

            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_browse);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        browseAdapter = new BrowseAdapter(getActivity(), new ArrayList<FlickrItem>(), recyclerView);
        browseAdapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(browseAdapter);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (flickrRequester != null) {
            flickrRequester.cancel(true);
            flickrRequester = null;
        }
        super.onDestroy();
    }

    @Override
    public void onLoadMore() {
        int itemCount = browseAdapter.getItemCount();
        Log.e(TAG, "Item Count is currently: " + itemCount
            + ".'\nCurrent page is " + itemCount/25 );

        int page = (itemCount/25) + 1;
        flickrRequester = new FlickrRequester(getActivity(),BrowseFragment.this);
        flickrRequester.searchByText(editText.getText().toString(), page);
    }

    @Override
    public void completion(JSONObject response) {
        Log.e(TAG, response.toString());
        List<FlickrItem> itemList = new ArrayList<>();
        try {
            JSONObject photos = response.getJSONObject("photos");
            JSONArray photo = photos.getJSONArray("photo");
            JSONObject photoObject;

            for (int i =0 ; i< photo.length(); i++){
                    photoObject = photo.getJSONObject(i);
                    Log.e(TAG, photoObject.toString());
                    FlickrItem item = mapItem(photoObject);
                    itemList.add(item);
            }
        } catch( JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Item list after query: " + itemList.toString());

        browseAdapter.updateFlickrItemList(itemList);
    }

    public static FlickrItem mapItem(JSONObject photoObject) {
        FlickrItem item = new FlickrItem();
        item.setId(photoObject.optInt("id", 0));
        item.setFarmId(photoObject.optInt("farm", 0));
        item.setServerId(photoObject.optInt("server", 0));
        item.setSecret(photoObject.optString("secret", ""));
        item.setTitle(photoObject.optString("title", ""));
        item.setSmallUrl(photoObject.optString("url_n",null));
        item.setBigUrl(photoObject.optString("url_l",null));
        return item;
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        }
    }
}
