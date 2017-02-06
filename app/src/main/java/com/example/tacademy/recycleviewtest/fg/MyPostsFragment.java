package com.example.tacademy.recycleviewtest.fg;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tacademy.recycleviewtest.R;

public class MyPostsFragment extends Fragment {


    public MyPostsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_posts, container, false);
    }

}
