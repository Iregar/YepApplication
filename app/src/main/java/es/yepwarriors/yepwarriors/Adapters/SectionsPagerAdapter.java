package es.yepwarriors.yepwarriors.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import es.yepwarriors.yepwarriors.ui.FriendsFragment;
import es.yepwarriors.yepwarriors.ui.InboxFragment;
import es.yepwarriors.yepwarriors.R;


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public static final int INBOX_TAB = 0;
    public static final int FRIENDS_TAB = 1;
    private static final int NUMBER_OF_TABS =2;
    Context context;

    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position==0){
            return new InboxFragment();
        }else {
            return  new FriendsFragment();

        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return context.getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return context.getString(R.string.title_section2).toUpperCase(l);

        }
        return null;
    }

    public int getIcon(int position) {
        switch (position) {
            case INBOX_TAB:
                return R.drawable.ic_tab_inbox;
            case FRIENDS_TAB:
                return R.drawable.ic_tab_friends;
        }
        return R.drawable.ic_tab_inbox;
    }
}