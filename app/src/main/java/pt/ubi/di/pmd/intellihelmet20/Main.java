package pt.ubi.di.pmd.intellihelmet20;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
//HELLO



public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    protected static final String PREFS_NAME = "MyPreferences"; // sharedPreferences


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {

            Intent intent = new Intent(this, PessoaFirstTime.class);
            startActivity(intent);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            HomeFragment home = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    home).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                if(!BluetoothConnection.isRunning()) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_pessoa:
                if(!BluetoothConnection.isRunning()) {
                    Intent oIntent = new Intent(this, PessoaFirstTime.class);
                    startActivity(oIntent);
                }
                else{
                    Toast.makeText(this, "You cannot change view", Toast.LENGTH_SHORT).show();
                    // Vê se consegues com que isto mude o checked item para outro sff.
                    // Testa isso trocando a variavel no serviço para true.
                    // ---------------------------------------------------------------------------
                    // |               !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!                      |
                    // | !!!!!!!!!!!!!!!!NÃO TE ESQUEÇAS DE VOLTAR A METER FALSO!!!!!!!!!!!!!!!! |
                    // |                !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!                       |
                    // |-------------------------------------------------------------------------|
                    // change();
                }
                break;
            case R.id.nav_settings:
                if(!BluetoothConnection.isRunning()) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                else navigationView.setCheckedItem(R.id.nav_home);
                break;
            case R.id.nav_info:
                if(!BluetoothConnection.isRunning()) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment()).commit();
                else navigationView.setCheckedItem(R.id.nav_home);
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
    private void change(){
            navigationView.setCheckedItem(R.id.nav_home);
    }
}
