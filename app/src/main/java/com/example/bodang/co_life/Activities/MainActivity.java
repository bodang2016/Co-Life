package com.example.bodang.co_life.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bodang.co_life.Management.BackgroundService;
import com.example.bodang.co_life.Management.Client;
import com.example.bodang.co_life.Management.PermissionsChecker;
import com.example.bodang.co_life.R;
import com.example.bodang.co_life.Fragments.ToolFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private PermissionsChecker mPermissionsChecker;

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_GROUP = "Groupname";
    private final String DefaultUnameValue = "Guest";
    public static final String DefaultGroupValue = "You have not enrolled in any group";
    public static String UnameValue;
    public static String UgroupValue;
    public static boolean isLogedin = false;
    private getGroupTask mgetGroupTask = null;
    public static Activity mainActivity;
    private Intent serviceIntent;

    public static Client client;
    public static TextView userName;
    public static TextView userGroupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = MainActivity.this;
        mPermissionsChecker = new PermissionsChecker(this);
        serviceIntent = new Intent(MainActivity.this, BackgroundService.class);
        startService(serviceIntent);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        client = new Client();
        displayView(R.id.nav_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View header = LayoutInflater.from(MainActivity.this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        userName = (TextView) header.findViewById(R.id.userName);
        userGroupID = (TextView) header.findViewById(R.id.userGroupID);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (!userName.getText().equals(DefaultUnameValue)) {
                    intent.setClass(MainActivity.this, UserDetailActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent messageListIntent = new Intent();
                messageListIntent.setClass(MainActivity.this, Reply.class);
                messageListIntent.putExtra("requestername"," ");
                messageListIntent.putExtra("myname",UnameValue);
                messageListIntent.putExtra("reply",false);
                startActivity(messageListIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        return true;
    }

    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_list:
                fragment = new com.example.bodang.co_life.Fragments.ListFragment();
                title = "Co-Life";
                break;
            case R.id.nav_map:
                fragment = new com.example.bodang.co_life.Fragments.MapFragment();
                title = "Map";
                break;
            case R.id.nav_tool:
                fragment = new ToolFragment();
                title = "Tools";
                break;

        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
        switch (resultCode) {
            case 0:
                if (data != null) {
                    Bundle b = data.getExtras();
                    final String str = b.getString("username");
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userName.setText(str);
                            savePreferences();
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = (String) userName.getText();
        editor.putString(PREF_UNAME, UnameValue);
        editor.commit();
    }

    private void saveGroupPreferences(String groupID) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_GROUP, groupID);
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        UgroupValue = settings.getString(PREF_GROUP, DefaultGroupValue);
        userName.setText(UnameValue);
        if (userName.getText() != DefaultUnameValue) {
            isLogedin = true;
            if (!UgroupValue.equals(DefaultGroupValue)) {
                stopService(serviceIntent);
                startService(serviceIntent);
                userGroupID.setText("You are enroled in Group " + UgroupValue);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
        loadPreferences();
        if (isLogedin) {
            mgetGroupTask = new getGroupTask(UnameValue);
            mgetGroupTask.execute((Void) null);
        }
        loadPreferences();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, 2, PERMISSIONS);
    }

    public class getGroupTask extends AsyncTask<Void, Void, Boolean> {
        private final String mUsername;
        private String GroupID;

        public getGroupTask(String userName) {
            super();
            mUsername = userName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int result = client.Init();
            if (result == 1) {
                int idcheck = client.roomId(mUsername);
                GroupID = String.valueOf(idcheck);
                if (idcheck != 0) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                saveGroupPreferences(GroupID);
                loadPreferences();
            } else {
                saveGroupPreferences(DefaultGroupValue);
                loadPreferences();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mgetGroupTask = null;
        }
    }

}
