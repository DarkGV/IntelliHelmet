package pmd.di.ubi.pt.intellihelmet;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class Configurations extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // the inflation code goes here
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_config, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
