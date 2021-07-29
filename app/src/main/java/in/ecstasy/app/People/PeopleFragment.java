package in.ecstasy.app.People;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import in.ecstasy.app.People.Fragments.AddFriendsFragment;
import in.ecstasy.app.People.Fragments.FriendsFragment;
import in.ecstasy.app.R;


public class PeopleFragment extends Fragment {

    TabLayout tablayout;
    ViewPager mview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_people, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tablayout = getView().findViewById(R.id.tabs);
        mview = getView().findViewById(R.id.container);

        mview.setAdapter(new PagerAdapter(getFragmentManager()));
        mview.addOnPageChangeListener((new TabLayout.TabLayoutOnPageChangeListener(tablayout)));
        tablayout.setupWithViewPager(mview);
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    public class PagerAdapter extends FragmentStatePagerAdapter {


        private String[] tabTitles = new String[]{"  Friends  ", "  Add Friends  "};

        public PagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FriendsFragment();
                case 1:
                    return new AddFriendsFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}