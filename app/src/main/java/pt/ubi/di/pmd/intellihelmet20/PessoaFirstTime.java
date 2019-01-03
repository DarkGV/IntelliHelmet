package pt.ubi.di.pmd.intellihelmet20;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

/*
    A class DBHelper está comentada, para mais informações sobre estes métodos
    chamados usando oDBH é lá que as podem encontrar
*/

public class PessoaFirstTime extends Activity {

    private EditText oETnome;
    private RadioGroup oRGbloodTp;
    private EditText oETmainCont;
    private EditText oETbackUpCont;
    private EditText oETidCard;
    private Button oButtOk;
    private DBHelper oDBH;
    private Cursor oCstartingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pessoa);

        oETnome = (EditText) findViewById(R.id.NameField);
        oRGbloodTp = (RadioGroup) findViewById(R.id.BloodChoice);
        oETmainCont = (EditText) findViewById(R.id.MainContField);
        oETidCard = (EditText) findViewById(R.id.IDField);
        oButtOk = (Button) findViewById(R.id.SaveInfoButt);
        oButtOk.setVisibility(View.VISIBLE);
        oDBH = new DBHelper(this);
        oCstartingData = oDBH.getUserInfo();
        Context oContext = this;
        oButtOk.setOnClickListener(new View.OnClickListener() {
        SharedPreferences oSP=getSharedPreferences(Main.PREFS_NAME, 0);;
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbaux = oDBH.getWritableDatabase();
                if(oSP.getBoolean("my_first_time",true)) {

                    RadioButton oBloodType = (RadioButton) oRGbloodTp.findViewById(oRGbloodTp.getCheckedRadioButtonId());
                    String stmt = "INSERT INTO " + oDBH.M_TABLE_NAME + " VALUES ('" +
                            oETnome.getText().toString() +
                            "', '" + oBloodType.getText().toString() +
                            "', " + oETmainCont.getText().toString() +
                            ", " + oETidCard.getText().toString() + ");";

                    dbaux.execSQL(stmt);
                }
                else{
                    String stmt= "UPDATE User SET mainContact="+ oETmainCont.getText().toString()+", backUpContact="+ oETbackUpCont.getText().toString()+" WHERE name='"+ oETnome.getText().toString()+"';";
                    dbaux.execSQL(stmt);

                }
                
                SharedPreferences settings = getSharedPreferences(Main.PREFS_NAME, 0);
                settings.edit().putBoolean("my_first_time", false).commit();
                ((PessoaFirstTime) oContext).finish();
                return;

            }
        });

        SQLiteDatabase db= oDBH.getReadableDatabase();
        oCstartingData=db.query("User",new String[]{"*"},null,null,null,null,null);

        if (oCstartingData.moveToFirst()) {
            oETnome.setText(oCstartingData.getString(0), TextView.BufferType.EDITABLE);
            oETnome.setEnabled(false);
            checkBTinDB();
            oETmainCont.setText(oCstartingData.getString(2), TextView.BufferType.EDITABLE);
            oETidCard.setText(oCstartingData.getString(3), TextView.BufferType.EDITABLE);
            oETidCard.setEnabled(false);
        }
    }

    private void checkBTinDB(){
        oRGbloodTp.setEnabled(false);
        if (oCstartingData.getString(1).equals("A+"))
            oRGbloodTp.check(R.id.Ap);
        else if (oCstartingData.getString(1).equals("A-"))
            oRGbloodTp.check(R.id.Am);
        else if (oCstartingData.getString(1).equals("AB+"))
            oRGbloodTp.check(R.id.ABp);
        else if (oCstartingData.getString(1).equals("AB-"))
            oRGbloodTp.check(R.id.ABm);
        else if (oCstartingData.getString(1).equals("B+"))
            oRGbloodTp.check(R.id.Bp);
        else if (oCstartingData.getString(1).equals("B-"))
            oRGbloodTp.check(R.id.Bm);
        else if (oCstartingData.getString(1).equals("O+"))
            oRGbloodTp.check(R.id.Op);
        else if (oCstartingData.getString(1).equals("O-"))
            oRGbloodTp.check(R.id.Om);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        SharedPreferences settings = getSharedPreferences(Main.PREFS_NAME, 0);
        if(settings.getBoolean("my_first_time", true)) {
            Intent oIntent = new Intent(this, PessoaFirstTime.class);
            startActivity(oIntent);
        }

    }
}