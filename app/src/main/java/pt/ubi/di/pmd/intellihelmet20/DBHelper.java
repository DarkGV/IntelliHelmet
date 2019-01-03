package pt.ubi.di.pmd.intellihelmet20;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.Cursor;

public class DBHelper extends SQLiteOpenHelper {

    /*
    DB_MainVers representa a BD principal que aloja os dados do user
    DB_SecondVers representa a BD que guarda o dia de uso, as horas de uso e a temperatura
     */

    private static final int DB_MainVers=1;
    private static final String DB_MainNAME="UserInfo";
    protected static final String M_TABLE_NAME="User";
    protected static final String M_COL1="name";
    protected static final String M_COL2="bloodType";
    protected static final String M_COL3="mainContact";
    protected static final String M_COL5="userIDNumb";

    protected static final String S_TABLE_NAME="UserActiv";
    protected static final String S_COL1="dia";
    protected static final String S_COL2="horas";
    protected static final String S_COL3="temperatura";

    private static int dbChoice=0;

    /*
    Construtores das tabelas, o segundo é para construir e usar a bd que contém as tmps e os dias e horas de uso
    o int fauxPas serve só para diferenciar os construtores logo qualquer int pode ser lá inserido
     */

    public DBHelper(Context context ){  // mudei para PessoaFirstTime em vez de PessoaFragment
        super(context,DB_MainNAME,null,DB_MainVers);
    }

    /*
    Constrói as duas BDs, estou a por em BDs diferentes só para não criar confusão, pode mudar se preferirem outra construção
     */

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+M_TABLE_NAME+"("+M_COL1+" VARCHAR(30) PRIMARY KEY, "+M_COL2+" VARCHAR(30), "+M_COL3+" INT, "+M_COL5+" INT);");
        db.execSQL("CREATE TABLE "+S_TABLE_NAME+"( tripId INTEGER PRIMARY KEY AUTOINCREMENT, "+S_COL1+" VARCHAR(30), "+S_COL2+" VARCHAR(30), "+S_COL3+" VARCHAR(30));");
    }


    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        if(dbChoice==1) {
            db.execSQL("DROP TABLE IF EXISTS " + M_TABLE_NAME + " ;");
            db.execSQL("CREATE TABLE " + M_TABLE_NAME + "(" + M_COL1 + " VARCHAR(30) PRIMARY KEY, " + M_COL2 + " VARCHAR(30), " + M_COL3 + " INT, " + M_COL5 + " INT);");
            return;
        }
        else if(dbChoice==2){
            db.execSQL("DROP TABLE IF EXISTS "+S_TABLE_NAME+" ;");
            db.execSQL("CREATE TABLE "+S_TABLE_NAME+"("+S_COL1+" VARCHAR(30) PRIMARY KEY, "+S_COL2+" VARCHAR(30), "+S_COL3+" VARCHAR(30));");
            return;
        }
        else
            return;
    }

    /*
    Este método é só para ser chamado uma vez em toda a utilização da aplicação, visto que serve só e unicamente
    para criar o user na bd quando esta está vazia. Tem como default 30 para o Emergency timer e 75 para a sensibilidade do
    sensor
     */

    public boolean addUserInfo(String name,String bloodType,int mainContact,int backupContact,int userIDNumb){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv= new ContentValues();

        System.out.println("FDS 1");

        if(db.query(M_TABLE_NAME,null ,"name" ,null ,null , null, null).isNull(0))
            return false;

        cv.put("name",name );
        cv.put("bloodType",bloodType);
        cv.put("mainContact",mainContact);
        cv.put("backUpContact",backupContact);
        cv.put("userIDNumb",userIDNumb);

        if(db.insert(M_TABLE_NAME,null ,cv )>0)
            return true;
        else
            return false;
    }

    /*
    Este método é a que vai gerir as alteracões efetuadas na informação do utilizador
    caso os if são para verificar que informação vai ser alterada.
     */

    public boolean alterUserInfo(String name,String bloodType,int mainContact,int backupContact,int userIDNumb){
        if(name.equals(null) && bloodType.equals(null) && mainContact==0 && backupContact==0 && userIDNumb==0 )
            return false;

        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        try {
            if (!name.equals(null))
                cv.put("name", name);

            if (!bloodType.equals(null))
                cv.put("bloodType", bloodType);

            if (mainContact != 0)
                cv.put("mainContact", mainContact);

            if (backupContact != 0)
                cv.put("backUpContact", backupContact);

            if (userIDNumb != 0)
                cv.put("userIDNumb", userIDNumb);


            db.update("User", cv, null, null);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /*
    Este método trata de adicionar nova informação sobre a Utilização da app, ou seja sempre que for utilizada, ao desligar
    esta função é chamada de modo a registar os dados dessa sessão.
    Estrutura de inserção ideealizada:
    String dia- dia/mes/ano
    String horas- hora:minutos (iniciais)/hora:minutos (finais)
    String temperatura- int C(celsius)\F(Fahrenheit)\K(Kelvin) nota: só é preciso escolher um dos tipos de medição de temp.
     */

    public boolean addUtilData(String dia,String horas,String temperatura){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put("dia",dia);
        cv.put("horas",horas);
        cv.put("temperatura",temperatura);
        if(db.insert(S_TABLE_NAME,null,cv)>0)
            return true;
        else
            return false;
    }

    /*
    Este método vai buscar um cursor que contém toda a informação do user visto que é só um elememto na tabela
    posso usar o método da mesma maneira que uso no getUtilData.
     */

    public Cursor getUserInfo(){
        Cursor userInfo;
        SQLiteDatabase db=getReadableDatabase();
        if(db.query(M_TABLE_NAME,null ,null , null, null, null, null).equals(null)){
            userInfo=db.query(M_TABLE_NAME,null ,null ,null ,null , null, null);
            userInfo.moveToFirst();
            return userInfo;
        }
        else
            return null;
    }

    /*
    Este método vai buscar um cursor que contém todas as linhas da DB, bom para apresentar uma lisatgem de todos os dados
    no entanto devido a possivel inserção infinita de dados aconselho à restrição do tamanho desta BD, as como não sei fazer isso
    não está aqui feito...
     */

    public Cursor getUtilData(){
        Cursor utilData;
        SQLiteDatabase db=getReadableDatabase();
        if(db.query(S_TABLE_NAME,null ,null ,null ,null ,null ,null).getCount()>0){
            utilData=db.query(S_TABLE_NAME,null ,null ,null ,null ,null ,null);
            utilData.moveToFirst();
            return utilData;
        }
        else
            return null;
    }






}

