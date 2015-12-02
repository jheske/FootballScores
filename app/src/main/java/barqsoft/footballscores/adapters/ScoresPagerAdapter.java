package barqsoft.footballscores.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.format.Time;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;

import barqsoft.footballscores.R;
import barqsoft.footballscores.activities.MainActivityFragment;
import barqsoft.footballscores.service.ScoresFetchService;


public class ScoresPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = getClass().getSimpleName();
    public static final int NUM_PAGES = 5;
    private Context mContext;
  //  private MainActivityFragment[] viewFragments = new MainActivityFragment[7];

    // Sparse array to keep track of registered fragments in memory
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public ScoresPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // Register the fragment when the item is instantiated
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    // Unregister when the item is inactive
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int i)  {
        Log.i(TAG,"Getting fragment " + Integer.toString(i) + " " + getPageTitle(i));
        return MainActivityFragment.newInstance(i);
    }

    @Override
    public int getCount()  {
        return NUM_PAGES;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position)  {
       return getDayName(mContext,System.currentTimeMillis()+((position- ScoresFetchService.DAYS_BACK)*86400000));
    }

    public String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today"
        // instead of the actual day name.
        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        }
        else if ( julianDay == currentJulianDay -1) {
            return context.getString(R.string.yesterday);
        }
        else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }
}
