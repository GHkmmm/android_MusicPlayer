package com.example.musicplayer.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.R;

public class AlbumFragment extends Fragment {
    Bitmap bmCover;

    public AlbumFragment(Bitmap bmCover) {
        this.bmCover = bmCover;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.album_fragment, container, false);
        setCover(view);
        return view;
    }

    public void setCover(View view){
        ImageView cover = view.findViewById(R.id.play_cover);
        cover.setImageBitmap(bmCover);
    }
}
