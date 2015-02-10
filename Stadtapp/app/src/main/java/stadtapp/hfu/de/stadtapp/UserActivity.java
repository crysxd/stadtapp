package stadtapp.hfu.de.stadtapp;

import android.os.Bundle;

import stadtapp.hfu.de.stadtapp.tabs.SightListFragment;

/**
 * Created by wuerthnc on 10.02.15.
 */
public class UserActivity extends DialogHostActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String user = this.getIntent().getExtras().getString("user");

        SightListFragment frag = new SightListFragment(this, user);
        this.getSupportFragmentManager().beginTransaction().replace(android.R.id.content, frag).commit();

        this.getActionBar().setTitle(user);

    }
}
