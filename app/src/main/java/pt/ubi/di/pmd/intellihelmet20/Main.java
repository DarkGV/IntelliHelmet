package pt.ubi.di.pmd.intellihelmet20;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;




public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    BluetoothConnection oTM;
    private static boolean serviceRunning = false;

    private ServiceConnection mConnection = new ServiceConnection () {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service )
        {
            BluetoothConnection.BluetoothComm binder=(BluetoothConnection.BluetoothComm) service ;
            oTM = binder.getService();
            Main.setRunning();
        }

        @Override public void onServiceDisconnected (ComponentName arg0)
        { serviceRunning = false ; }
    };


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toogle);
        toogle.syncState();

        if (savedInstanceState == null) {
            HomeFragment home = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    home).commit();
            home.recvService(mConnection);
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                HomeFragment home = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home).commit();
                home.recvService(mConnection);
                break;
            case R.id.nav_pessoa:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PessoaFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "DEI share", Toast.LENGTH_LONG).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public static boolean isServiceRunning(){
            return serviceRunning;
    }

    public static void setRunning(){
            serviceRunning = true;
    }
}
