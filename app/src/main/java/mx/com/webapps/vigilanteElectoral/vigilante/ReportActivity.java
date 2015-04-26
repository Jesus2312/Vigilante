package mx.com.webapps.vigilanteElectoral.vigilante;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import mx.com.webapps.vigilanteElectoral.webservice.GpsHandler;
import mx.com.webapps.vigilanteElectoral.webservice.WsConsume;
import vigilante.org.vigilante.R;


public class ReportActivity extends ActionBarActivity {


    private ProgressDialog dialog;
    private Spinner lbEstados ;
    private EditText lbDenunciado;
    private EditText lbDenuncia;
    private EditText lbCalles ;
    private EditText lbEmail;
    private String videoFile=null;
    private String photoFile= null;
    private String estadoSeleccionado;
    private String gpsData;
    private String fecha;
    String fn;
    String fv;
    private TextView labelFoto;
    private TextView labelVideo;
    private VigilanteReporte reporte ;
    private WebView wbMapa;
    private Button btLocalizacion;
    private Calendar calendar= Calendar.getInstance();
    private static  final String SUBIR_FOTO= "Seleccionar Foto";
    private static final String SUBIR_VIDEO="Seleccionar Video";
    private static final String NO_GPS = "NO_GPS";
    private static final  int  PHOTO_CHOOSE = 0;
    private static  final  int VIDEO_CHOOSE = 1;
    private final static String [] estados = {"Aguascalientes",
            "Baja California",
            "Baja California Sur",
            "Campeche",
            "Coahuila",
            "Colima",
            "Chiapas",
            "Chihuahua",
            "Distrito Federal",
            "Durango",
            "Guanajuato",
            "Guerrero",
            "Hidalgo",
            "Jalisco",
            "México",
            "Michoacán",
            "Morelos",
            "Nayarit",
            "Nuevo León",
            "Oaxaca",
            "Puebla",
            "Querétaro",
            "Quintana Roo",
            "San Luis Potosí",
            "Sinaloa",
            "Sonora",
            "Tabasco",
            "Tamaulipas",
            "Tlaxcala",
            "Veracruz",
            "Yucatán",
            "Zacatecas"};


    public static final String FTP_USER = "vigilantedroid@vigilante.desarrollosenlanube.com";
    public static final String FTP_PASS  ="Apocono2015!";
    static final String FTP_HOST= "vigilante.desarrollosenlanube.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        cargaEstados();
        cargaMapa();
        cargaCamposEditables();

        // por default no envia gps
         gpsData = NO_GPS;
         btLocalizacion = (Button) findViewById(R.id.BtLocalizacion);
        labelFoto = (TextView) findViewById(R.id.LabelFoto);
        labelVideo = (TextView) findViewById(R.id.LabelVideo);
    }

    private void cargaCamposEditables ()
    {

        lbDenuncia = (EditText) findViewById(R.id.TbDenuncia);
        lbDenunciado = (EditText) findViewById(R.id.TbDenunciado);
        lbCalles = (EditText) findViewById(R.id.TbCalles);
        lbEmail = (EditText) findViewById(R.id.TbEmail3);

    }
    private void cargaEstados ()
    {
        lbEstados= (Spinner) findViewById(R.id.LbEstados);
        lbEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estadoSeleccionado = lbEstados.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapterEstados = new ArrayAdapter<String>(ReportActivity.this,android.R.layout.simple_spinner_item,estados);
        lbEstados.setAdapter(adapterEstados);
    }


    private void cargaMapa ()
    {
        wbMapa = (WebView) findViewById(R.id.WebViewMapa);
        wbMapa.getSettings().setJavaScriptEnabled(true); // enable javascript
        wbMapa.setWebViewClient(new WebViewClient());
        wbMapa.loadUrl(VigilanteUrls.mapaUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report, menu);
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


    public void setFechaClick (View v)
    {

        new DatePickerDialog(ReportActivity.this,d,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        fecha = sdf.format(date);

    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            Date date = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            fecha = sdf.format(date);
        }
    };


    public  void selectFileClick (View v)
    {

        Button b = (Button)v;
        String text = b.getText().toString();

        switch ( text)
        {
            case SUBIR_FOTO:
                showFileChooser(PHOTO_CHOOSE);
                break;

            case SUBIR_VIDEO:
                 showFileChooser(VIDEO_CHOOSE);
                break;
        }

    }

    private String getFileAbsolutePath (Uri uri,String []media)
    {
        String absolutePath= null;
        Cursor cursor =  this.getContentResolver().query(uri,media,null,null,null);
        int  colIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        absolutePath = cursor.getString(colIndex);
        return  absolutePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        File dummy;

        if((requestCode ==PHOTO_CHOOSE || requestCode == VIDEO_CHOOSE)&& resultCode ==RESULT_OK)
        {
            String [] proj = {MediaStore.Video.Media.DATA};
            Uri photoData = data.getData();
            if (requestCode == PHOTO_CHOOSE) {
                photoFile = getFileAbsolutePath(photoData, proj);

                dummy = new File (photoFile);

                fn = getDeviceId()+dummy.getName();

                String m = labelFoto.getText().toString() + " [foto cargada]";
                  labelFoto.setText(m);


                            }
            else if  (requestCode == VIDEO_CHOOSE)
            {
                videoFile = getFileAbsolutePath(photoData, proj);
                dummy = new File(videoFile);
                fv = getDeviceId()+dummy.getName();
                String m = labelVideo.getText().toString()+" [video cargado]";
                labelVideo.setText(m);
            }
        }//
    }

    private void showFileChooser(int fileType) {

        String ftype="" ;

        switch ( fileType)
        {
            case PHOTO_CHOOSE:
                ftype = "image/*";
                break;

            case VIDEO_CHOOSE:
                ftype="video/*";
                break;
        }


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(ftype);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    fileType);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void  localizacionClick (View v)
    {

      GpsHandler gpsHandler = new GpsHandler(ReportActivity.this);
       try
        {
            if (!btLocalizacion.getText().equals("Remover mi geolocalización")) {
                double lat = gpsHandler.getLatitude();
                double lon = gpsHandler.getLongitude();
                gpsData = String.valueOf(lat) + String.valueOf(lon);

                if (gpsData != NO_GPS) {
                    Toast.makeText(ReportActivity.this, "Localizacion cargada", Toast.LENGTH_LONG).show();
                    //https://www.google.com/maps/place//@40.7028722,-73.9868281,15z/data
                   // String b1 ="https://www.google.com/maps/place/@"+String.valueOf(lat)+String.valueOf(lon)+",15z/data";
                    String  b1="https://www.google.com/maps?q=loc:"+String.valueOf(lat)+","+String.valueOf(lon);
                    //String b1 = "https://www.google.com.mx/maps/";
                    //b1 += "@" + lat + "," + lon + ",18z";
                    btLocalizacion.setText("Remover mi geolocalización");
                    wbMapa.loadUrl(b1);
                }
            }
            else
            {
                gpsData = NO_GPS;
                Toast.makeText(ReportActivity.this,"Localizacion fue removida", Toast.LENGTH_LONG).show();
                wbMapa.loadUrl(VigilanteUrls.mapaUrl);
                btLocalizacion.setText("Si Acepta enviar su geolocalización  por favor haga click aquí");
            }
        }
        catch (Exception gpsException)
        {
            Toast.makeText(ReportActivity.this,"No se pudo obtener localizacion asegurese de tener activado GPS",Toast.LENGTH_LONG).show();
            gpsData = NO_GPS;
        }
    }

    public void btEnviarClick (View v )
    {
        VigilanteReporte reporte = new VigilanteReporte();
        if (lbDenuncia.getText().toString().equals(""))
        {
            Toast.makeText(ReportActivity.this, "Porfavor ingrese una descripcion de los hechos",Toast.LENGTH_LONG).show();
            lbDenuncia.requestFocus();

            return;
        }
        if (lbDenunciado.getText().toString().equals(""))
        {
            Toast.makeText(ReportActivity.this, "Porfavor ingrese candidato/campana a denunciar", Toast.LENGTH_LONG).show();
            lbDenunciado.requestFocus();
            return;

        }
        if (lbCalles.getText().toString().equals(""))
        {
            Toast.makeText(ReportActivity.this, "Porfavor ingrese las calles donde sucedio el evento", Toast.LENGTH_LONG).show();
            lbCalles.requestFocus();
            return;
        }

        String denuncia = lbDenuncia.getText().toString().replace(","," ");
        String denunciado = lbDenunciado.getText().toString().replace(",", " ");
        String calles = lbCalles.getText().toString().replace(",", " ");
        String gps = gpsData;
        String email = lbEmail.getText().toString().replace(",", " ");
        reporte.setDenuncia(denuncia);
        reporte.setDenunciado(denunciado);
        reporte.setCalles(calles);
        reporte.setGps(gps);
        reporte.setMail(email);
        reporte.setFecha(fecha);
        reporte.setEstado(estadoSeleccionado);
        reporte.setFotofile(fn);
        reporte.setVideofile(fv);

        // Llamada al web service.
        new asynHelper(reporte).execute();

    }


    public int uploadFile(File fileName){


        FTPClient client = new FTPClient();
        int result;
        try {

            client.connect(FTP_HOST,21);
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("/media/");
            client.upload(fileName, new MyTransferListener());
            result =1;
        } catch (Exception e) {
            result =-1;
            try {
                client.disconnect(true);
            } catch (Exception e2) {
               result =-1;
            }
        }
        return result;
    }

    private String getDeviceId ()
    {
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String uid = tManager.getDeviceId();
        return uid.toUpperCase();
    }

    protected  class asynHelper extends AsyncTask<String,String,String>
    {

        private String result;
        private  VigilanteReporte reporte ;
        public  asynHelper(VigilanteReporte r)
        {
            reporte = r;
        }

        private int postFile (String fname)
        { int result = 0;
            try {
                File f = new File(fname);

                if (f.exists()) {
                    //rename file with device id at the begin of name
                    String dvid = getDeviceId();
                    String nfn = f.getParent()+"/"+dvid+ f.getName();
                    File nf = new File(nfn);
                    f.renameTo(nf);
                    result = uploadFile(nf);
                } else {
                    result = 1;
                }
                return result;
            }
            catch (Exception f)
            {
                result =-1;
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
             dialog= new ProgressDialog(ReportActivity.this);
            dialog.setMessage("Subiendo denuncia, porfavor espere...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected  void onPostExecute (final String result) {
            dialog.dismiss();

            if (result == VigilanteUrls.OK)
            {
                Toast.makeText(ReportActivity.this, "Denuncia subida correctamente.", Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(ReportActivity.this, result, Toast.LENGTH_LONG).show();
                finish();

            }
        }
//
        @Override
        protected String doInBackground(String... params) {

            int postRes=1;
            String res="";
            WsConsume consumer = new WsConsume(VigilanteUrls.reporteUrl);
             try
             {
                     if (videoFile!= null) {
                         postRes = postFile(videoFile);
                     }

                     if (photoFile!= null) {
                         postRes = postFile(photoFile);
                     }

                 // Construct parameters
                 ArrayList<NameValuePair> p = reporte.getPropertiesList();
                 consumer.setParameters(p);
                 String response =consumer.getPostWsResponse();

                 if(postRes!=1 && response.contains("OK"))
                 {
                     res= "Denuncia subida correctamente, no se pudo postear archivos multimedia";
                 }
                 else if (postRes==1 && response.contains("OK")){
                     res = VigilanteUrls.OK;
                 }
             }
             catch (Exception g)
             {
                    res = "Error al subir denuncia, detalles: "+ g.getMessage();
             }
            result = res;
            return res;
        }
    }

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {
            Log.d("FTP", "Load started");
        }

        public void transferred(int length) {

            Log.d("FTP", "transfered:"+ length);
             }

        public void completed() {

            Log.d("FTP", "Transfer completed");

        }

        public void aborted() {


        }

        public void failed() {

          Log.d("FTP", "Transfer Failed.");
        }

    }

}
