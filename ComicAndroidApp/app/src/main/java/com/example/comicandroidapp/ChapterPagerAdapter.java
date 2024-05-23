package com.example.comicandroidapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ChapterPagerAdapter extends FragmentStateAdapter {
    private final List<List<String>> chapterGroups;

    public ChapterPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<List<String>> chapterGroups) {
        super(fragmentActivity);
        this.chapterGroups = chapterGroups;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ChapterFragment.newInstance(chapterGroups.get(position));
    }

    @Override
    public int getItemCount() {
        return chapterGroups.size();
    }
}
