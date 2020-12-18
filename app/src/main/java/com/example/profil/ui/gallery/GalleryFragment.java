package com.example.profil.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.profil.R;

public class GalleryFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        /**
         * action valider
         */
//        String url = "http://"+ ip + "/servicephp/ajout.php?nom="+ nom
//                + "&prenom="+ prenom
//                + "&pseudo=" + pseudo;

        return root;
    }
}