package mx.com.webapps.vigilanteElectoral.vigilante;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import mx.com.webapps.vigilanteElectoral.webservice.GpsHandler;
import vigilante.org.vigilante.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestNetwork();
        RequestGpsOn();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void RequestNetwork ()
    {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = manager.getActiveNetworkInfo();

        if (nf== null)
        {
            new AlertDialog.Builder(this)
                    .setTitle("Red Requerida")
                    .setMessage("Esta aplicacion requiere conexion a internet, desea activarla?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Intent i = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(i);

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }
        else
        {

        }

    }


    private  void RequestGpsOn ()
    {

        GpsHandler handler = new GpsHandler(MainActivity.this);
        boolean gpsEnabled = handler.isGpsEnabled();

        if (!gpsEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle("GPS Requerido")
                    .setMessage("Esta aplicacion puede requerir tener el GPS activado, desea activarlo?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }

    }

    public void btVigilalosClick (View v)
    {
        try {
            ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nf = manager.getActiveNetworkInfo();

            if (nf!= null)
            {
            startActivity(new Intent("vigilante.org.report"));
            }
            else
            {
                Toast.makeText(MainActivity.this,"Debe activar su conexion a internet para continuar.", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception f)
        {
            Log.d("Exception", f.getMessage());
        }
    }
}
