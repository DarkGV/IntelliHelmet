package pt.ubi.di.pmd.intellihelmet20;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class PessoaFragment extends Fragment {

    EditText oETnome;
    RadioGroup oRGbloodTp;
    EditText oETmainCont;
    EditText oETbackUpCont;
    EditText oETidCard;
    Button oButtOk;
    DBHelper oDBH;
    private Cursor oCstartingData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pessoa, container, false);


        oETnome=(EditText)view.findViewById(R.id.NameField);
        oRGbloodTp=(RadioGroup)view.findViewById(R.id.BloodChoice);
        oETmainCont=(EditText)view.findViewById(R.id.MainContField);
        oETbackUpCont=(EditText)view.findViewById(R.id.BackUpField);
        oETidCard=(EditText)view.findViewById(R.id.IDField);
        oButtOk=(Button)view.findViewById(R.id.SaveInfoButt);
        //oDBH=new DBHelper(this);

        oCstartingData=oDBH.getUserInfo();

        return inflater.inflate(R.layout.fragment_pessoa, container, false);

    }
}
