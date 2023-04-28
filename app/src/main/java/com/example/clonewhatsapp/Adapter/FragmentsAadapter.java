package com.example.clonewhatsapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.clonewhatsapp.Fragments.Calls;
import com.example.clonewhatsapp.Fragments.Chats;
import com.example.clonewhatsapp.Fragments.Status;

public class FragmentsAadapter extends FragmentPagerAdapter {
    public FragmentsAadapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new Chats();
            case 1: return new Status();
            case 2: return new Calls();
            default: return new Chats();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position==0){
            title = "CHATS";
        }
        if (position==1){
            title = "STATUS";
        }
        if (position==2){
            title = "CALLS";
        }

        return title;
    }

}
