package com.zafranitechllcpc.flickrfinder.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zafranitechllcpc.flickrfinder.R;

public class DetailsFragment extends DialogFragment {
    private final String TAG = getClass().getSimpleName();

    public DetailsFragment() {

    }

    public static DetailsFragment newInstance(final String url) {
        DetailsFragment frag = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageviewPhoto = view.findViewById(R.id.imageview_photo);
        String title = getArguments().getString("title", "");
        String url = getArguments().getString("url", null);
        Log.e(TAG, "title: " + title + "\nurl: " + url);

        getDialog().setTitle(title);
        if (url != null) Picasso.get()
                .load(url)
                .resize(1000,1000)
                .centerCrop()
                .into(imageviewPhoto);
    }


}
