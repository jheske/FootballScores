package barqsoft.footballscores.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

import barqsoft.footballscores.adapters.ScoresPagerAdapter;
import barqsoft.footballscores.R;

public class MainActivity extends ActionBarActivity {
    private final String TAG = getClass().getSimpleName();
    public static int selected_match_id;
    public static int current_fragment = 2;
    private final String save_tag = "Save Test";

    //private PagerFragment my_main_pager_fragment;
    public ViewPager mPagerHandler;
    private ScoresPagerAdapter mPagerAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupStetho();
        setupViewPager();
    }

    /**
     * Replace default toolbar with custom toolbar defined
     * in layouts/app_bar.xml
     */
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupViewPager() {
        mPagerHandler = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScoresPagerAdapter(this,getSupportFragmentManager());
        mPagerHandler.setAdapter(mPagerAdapter);
        mPagerHandler.setCurrentItem(current_fragment);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mPagerHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A very useful library for debugging Android apps
     * using Chrome, even has a database inspector!
     * <p/>
     * chrome://inspect
     */
    private void setupStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
