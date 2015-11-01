package com.tbg.simplestvallet.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.tbg.simplestvallet.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void switchToChartTab() {
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setCurrentTab(1);
    }

    public void switchToPendingTab() {
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setCurrentTab(2);
    }

    private void initTabs() {
        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.container);

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("tab1");
        tabSpec1.setIndicator(getBaseContext().getString(R.string.tab_input_title));

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2");
        tabSpec2.setIndicator(getBaseContext().getString(R.string.tab_chart_title));

        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3");
        tabSpec3.setIndicator(getBaseContext().getString(R.string.tab_pending_title));

        tabHost.addTab(tabSpec1, InputFragment.class, null);
        tabHost.addTab(tabSpec2, ChartFragment.class, null);
        tabHost.addTab(tabSpec3, PendingListFragment.class, null);
    }
}
