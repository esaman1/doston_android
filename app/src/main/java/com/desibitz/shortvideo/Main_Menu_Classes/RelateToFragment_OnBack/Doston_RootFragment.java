package com.desibitz.shortvideo.Main_Menu_Classes.RelateToFragment_OnBack;

import androidx.fragment.app.Fragment;

/**
 * Created by AQEEL on 3/30/2018.
 */

public class Doston_RootFragment extends Fragment implements Doston_OnBackPressListener {

    @Override
    public boolean onBackPressed() {
        return new Doston_BackPressImplimentationDoston(this).onBackPressed();
    }
}