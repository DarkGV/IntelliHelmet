package pt.ubi.di.pmd.intellihelmet20;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;


public class Configurations extends Activity {

    private Switch oSstart;
    private SeekBar oSBemTime;
    private SeekBar oSBsensSensitiv;
    private Button oButtOK;
    private DBHelper oDBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurations);

        oSstart=(Switch)findViewById(R.id.SwitchConfig);
        oSBemTime=(SeekBar)findViewById(R.id.EmergenTimeSeek);
        oSBsensSensitiv=(SeekBar)findViewById(R.id.SenseSensitiSeek);
        oButtOK=(Button)findViewById(R.id.ConfigOk);

        /*
        Faz set das configurações de sensibilidade e temporizador de acordo com a escolha do user
         */

        class ClickListener implements View.OnClickListener{
            @Override
            public void onClick(View v){
                int intEmTimer=oSBemTime.getProgress();
                int intSensitiv=oSBsensSensitiv.getProgress();

                if(v.getId()==R.id.ConfigOk){
                    if(oDBH.alterUserInfo(null,null ,0 ,0 , 0, intEmTimer, intSensitiv))
                        Toast.makeText(Configurations.this,"Configurações Alteradas com Sucesso\n Temporizador de Emergência: "+String.valueOf(intEmTimer)+" sec\n Sensibilidade do Sensor: "+String.valueOf(intSensitiv)+" sec",Toast.LENGTH_LONG).show();
                }
            }
        }
        ClickListener oCLConfig=new ClickListener();

        oButtOK.setOnClickListener(oCLConfig);
    }

    /*
    Inserir na classe abaixo o necessário para começar a parte principal da app! Aquela que se ativa com o switch
     */

    public void startActiv(View v){
        Intent iMainActiv=new Intent(this,);
        startActivity(iMainActiv);
    }


}
