package com.example.user.simplechatapplication;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mtoolBar;
    private ViewPager mviewPager;
    private SectionPageAdapter msectionPageAdapter;
    private TabLayout mtabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mtoolBar = findViewById(R.id.main_page_header);
        setSupportActionBar(mtoolBar);
        getSupportActionBar().setTitle("simpleChat");
        mviewPager = findViewById(R.id.tab_pager);
        msectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(msectionPageAdapter);
        mtabLayout = findViewById(R.id.main_tools);
        mtabLayout.setupWithViewPager(mviewPager);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            startPriorActivity();
        }
    }

    private void startPriorActivity() {
        Intent intent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_page_header_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.user_log_out : FirebaseAuth.getInstance().signOut();
            startPriorActivity();
        }

        return true;
    }

    public class SectionPageAdapter extends FragmentPagerAdapter{

        public SectionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0 : RequestFragment requestFragment = new RequestFragment();
                return requestFragment;
                case 1 : ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
                case 2 : FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;
                default : return  null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
                case 0 : return "Reqeusts";
                case 1 : return "Chats";
                case 2 : return "Friends";
                default : return  null;
            }
        }
    }
}
