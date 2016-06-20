package in.udacity.gagan.cinephilia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import in.udacity.gagan.cinephilia.Adapter.mListAdapter;

public class MainActivity extends AppCompatActivity implements mListAdapter.OnAdapterItemClickListener, DetailActivityFragment.NotifyDataChange{

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.frameLayout) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            if (savedInstanceState != null) {
                return;
                // In two-pane mode, show the detail view in this activity by
                // adding or replacing the detail fragment using a
                // fragment transaction.
            }
        } else {
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getItemDetails(String id, String backdrop, String title, int darkcolor,
                               int lightcolor) {

        if (!mTwoPane) {
            Intent mIntent = new Intent(MainActivity.this, DetailActivity.class);
            mIntent.putExtra("id", id);
            mIntent.putExtra("backdrop_path", backdrop);
            mIntent.putExtra("title", title);
            mIntent.putExtra("darkColor", darkcolor);
            mIntent.putExtra("lightColor", lightcolor);
            startActivity(mIntent);
        } else {

            Bundle args = new Bundle();
            args.putString("id", id);
            args.putString("backdrop_path", backdrop);
            args.putString("title", title);
            args.putInt("darkColor", darkcolor);
            args.putInt("lightColor", lightcolor);
            DetailActivityFragment displayFrag = new DetailActivityFragment();
            displayFrag.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, displayFrag).commit();
        }
    }

    @Override
    public void notifyChange() {
        MainActivityFragment maf = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_left);
        if(maf != null)
            maf.setDataChange();
    }
}
