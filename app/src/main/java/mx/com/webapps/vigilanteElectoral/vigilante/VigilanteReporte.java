package mx.com.webapps.vigilanteElectoral.vigilante;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Jesus Lopez on 4/25/2015.
 */
public class VigilanteReporte {

    private String denunciado;

    public String getDenuncia() {
        return denuncia;
    }

    public void setDenuncia(String denuncia) {
        this.denuncia = denuncia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCalles() {
        return entrecalleycalle;
    }

    public void setCalles(String calles) {
        this.entrecalleycalle = calles;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getFecha() {
        return fechahora;
    }

    public void setFecha(String fecha) {
        this.fechahora = fecha;
    }

    public String getMail() {
        return correo;
    }

    public void setMail(String mail) {
        this.correo = mail;
    }

    private String denuncia;
    private String estado;
    private String entrecalleycalle;
    private String gps;
    private String fechahora;
    private String correo;
    private String fotofile;
    private String videofile;

    public String getFotofile() {
        return fotofile;
    }

    public void setFotofile(String fotofile) {
        this.fotofile = fotofile;
    }

    public String getVideofile() {
        return videofile;
    }

    public void setVideofile(String videofile) {
        this.videofile = videofile;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    private String video;
    private String fotografia;

    public String getDenunciado() {
        return denunciado;
    }

    public void setDenunciado(String denunciado) {
        this.denunciado = denunciado;
    }

    public VigilanteReporte()
    {

    }

    public   ArrayList<NameValuePair> getPropertiesList ()
    {
        ArrayList<NameValuePair> properties = null;
        try
        {
            properties = new ArrayList<NameValuePair>();
            for (Field f : getClass().getDeclaredFields()) {
                BasicNameValuePair tp;
                String fname;
                String fvalue ;
                try
               {
                   fname= f.getName();
                   fvalue = f.get(this).toString();


               }
               catch (NullPointerException g)
               {
                   fname = f.getName();
                   fvalue ="";
               }
                tp= new BasicNameValuePair(fname,fvalue);
                properties.add(tp);

            }
        }
        catch (Exception t)
        {
            properties = null;
        }
        return  properties;
    }
}
