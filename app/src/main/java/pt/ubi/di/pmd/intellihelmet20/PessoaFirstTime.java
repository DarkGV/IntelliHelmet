package pt.ubi.di.pmd.intellihelmet20;

import android.app.Activity;
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

public class PessoaFirstTime extends Activity{

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

        oETnome=(EditText)findViewById(R.id.NameField);
        oRGbloodTp=(RadioGroup)findViewById(R.id.BloodChoice);
        oETmainCont=(EditText)findViewById(R.id.MainContField);
        oETbackUpCont=(EditText)findViewById(R.id.BackUpField);
        oETidCard=(EditText)findViewById(R.id.IDField);
        oButtOk=(Button)findViewById(R.id.SaveInfoButt);
        oDBH=new DBHelper(this);

        oCstartingData=oDBH.getUserInfo();

        /*
        Caso já exista o user na DB então insere-se a infromação que contém nos respetivos
        lugares de modo ao user perceber que informação é que está registada e se precisa ou não
        de a alterar
         */

        if(oCstartingData.getCount()!=0){
            oETnome.setText(oCstartingData.getString(0), TextView.BufferType.EDITABLE);
            checkBTinDB();
            oETmainCont.setText(oCstartingData.getString(2), TextView.BufferType.EDITABLE);
            oETbackUpCont.setText(oCstartingData.getString(3), TextView.BufferType.EDITABLE);
            oETidCard.setText(oCstartingData.getString(4), TextView.BufferType.EDITABLE);
        }

        class ClickListener implements View.OnClickListener{
            @Override
            public void onClick(View v){
                RadioButton oRGtemp;
                Cursor oCtemp=oDBH.getUserInfo();

                String sName=oETnome.getText().toString();
                int iBloodRPid=oRGbloodTp.getCheckedRadioButtonId();
                oRGtemp=(RadioButton)oRGbloodTp.findViewById(iBloodRPid);
                String sBloodTP=oRGtemp.getText().toString();
                long lMainCont=Long.valueOf(oETmainCont.getText().toString());
                long lBackUP=Long.valueOf(oETbackUpCont.getText().toString());
                long lID=Long.valueOf(oETidCard.getText().toString());

                /*
                Dentro deste if, verifico primeiro se existe ou não user na DB, caso não, chamo o
                método addUserInfo de modo a adicionar o user com as definições default de temporizador
                e de sensibilidade, relembro que são os valores 30 e 75 respetivamente,
                caso contrário, ou seja já existe user, então chamo alterUserInfo só para alterar a informação
                da DB sem mexer no temporizador ou na sensibilidade
                 */

                if(v.getId()==R.id.SaveInfoButt){

                    if(sName.isEmpty()||sBloodTP.isEmpty()||lMainCont==0||lID==0)
                        Toast.makeText(PessoaFirstTime.this,"Erro:Falta preencher,pelo menos, 1 campo", Toast.LENGTH_LONG).show();
                    else
                        //Este if serve para verificar se o user existe ou não;
                        if(oCtemp.getCount()<0){
                            try{
                                oDBH.addUserInfo(sName,sBloodTP ,lMainCont ,lBackUP ,lID);
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                        else if(oDBH.alterUserInfo(sName,sBloodTP ,lMainCont ,lBackUP ,lID , 0, 0))
                            Toast.makeText(PessoaFirstTime.this,"Informação Registada com sucesso", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(PessoaFirstTime.this,"Erro:Não foi possivel registar a informação", Toast.LENGTH_LONG).show();
                }
            }
        }

        ClickListener oCLInfoChange=new ClickListener();
        oButtOk.setOnClickListener(oCLInfoChange);
    }
    /*
    serve só e unicamente para fazer check automático ao tipo de sangue que o user tem na DB
     */

    private void checkBTinDB(){

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

}