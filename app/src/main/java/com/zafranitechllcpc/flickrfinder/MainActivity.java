package com.zafranitechllcpc.flickrfinder;

import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.zafranitechllcpc.flickrfinder.browse.BrowseFragment;
import com.zafranitechllcpc.flickrfinder.details.DetailsFragment;
import com.zafranitechllcpc.flickrfinder.models.FlickrItem;

public class MainActivity extends AppCompatActivity implements BrowseFragment.DialogDisplay{
       private BrowseFragment browseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            return;
        }
       browseFragment = BrowseFragment.getInstance(getSupportFragmentManager());
       getSupportFragmentManager().beginTransaction()
               .add(R.id.main_container, browseFragment)
               .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void displayDetailsDialog(@NonNull final FlickrItem flickrItem) {
        DetailsFragment
                .newInstance(flickrItem.getBigUrl())
                .show(this.getSupportFragmentManager(),"DetailsFragment");

    }
}
