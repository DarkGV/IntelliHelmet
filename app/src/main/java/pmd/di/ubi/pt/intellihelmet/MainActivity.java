package pmd.di.ubi.pt.intellihelmet;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import com.mindorks.placeholderview.PlaceHolderView;

public class MainActivity extends AppCompatActivity {


        private PlaceHolderView mDrawerView;
        private DrawerLayout mDrawer;
        private Toolbar mToolbar;
        private PlaceHolderView mGalleryView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
            mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
            mToolbar = (Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
            setupDrawer();
        }

        public boolean onCreateOptionsMenu(Menu menu) {
            // the inflation code goes here
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_config, menu);
            return super.onCreateOptionsMenu(menu);
        }

        private void setupDrawer(){
            mDrawerView
                    .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_HOME))
                    .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS))
                    .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_HELP));


            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close){
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };

            mDrawer.addDrawerListener(drawerToggle);
            drawerToggle.syncState();
        }
    }

