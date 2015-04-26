package mx.com.webapps.vigilanteElectoral.webservice;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


/**
 * Created by jlopez on 3/10/2015.
 */
public class WsConsume {

    private  String _url;
    HttpClient httpClient;
    HttpContext localContext;
    HttpGet httpGet;
    private ArrayList<NameValuePair> wsParameters  = new ArrayList<>();




    public WsConsume()
    {

    }

    public WsConsume(String url)
    {
        _url= url;

    }

    public String getResponse ()
    {
        BufferedReader reader = null;
        HttpURLConnection con;
        try
        {
            URL url = new URL("http://api.saeta.org.mx/Token");
            con = (HttpURLConnection) url.openConnection();
          //  con.addRequestProperty("grant_type","password");
           // con.addRequestProperty("username","USRAUDITBETO");
           // con.addRequestProperty("password","7dJp4L");

            StringBuilder sb = new StringBuilder();



           // reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while ((line= reader.readLine())!= null)
            {
                sb.append(line +"\n");

            }

            return sb.toString();
        }
        catch (Exception e)
        {
           e.printStackTrace();
            return null;
        }
        finally {
            try
            {
                if (reader!= null) reader.close();
            }
            catch (Exception d)
            {
                d.printStackTrace();
                return null;
            }

        }


    }


    public void setParameters(ArrayList<NameValuePair> _params)
    {
        this.wsParameters =_params;
    }


    public void getReponse2()
    {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        try
        {
            ArrayList<NameValuePair> param  = new ArrayList<>();
            //URI website = new URI("http://api.saeta.org.mx/Token");
            HttpPost httpPost = new HttpPost("http://api.saeta.org.mx/Token");

            JSONObject object = new JSONObject();

            object.put("grant_type","password");
            object.put("username","USRAUDITBETO");
            object.put("password","7dJp4L");

            param.add(new BasicNameValuePair("grant_type","password"));
            param.add(new BasicNameValuePair("username","USRAUDITBETO"));
            param.add(new BasicNameValuePair("password","7dJp4L"));

            httpPost.setEntity(new UrlEncodedFormEntity(param));

            response= httpClient.execute(httpPost);

            Object o = response;

        }
        catch (Exception e )
        {

        }
    }

    public String getPostWsResponse()
    {
        String result= null;
        try
        {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response;
            HttpPost post = new HttpPost(this._url);
   //         UrlEncodedFormEntity entity = new UrlEncodedFormEntity(this.wsParameters);
     //       post.setEntity(entity);
            post.setEntity(new UrlEncodedFormEntity(this.wsParameters));
            response= client.execute(post);

            InputStream stream = response.getEntity().getContent();

            String json = convertInputStreamToString(stream);

            result= json;

        }
        catch ( Exception f)
        {
            return null;

        }
        finally
        {
           // Manejar recursor Dispose en este bloque.
        }
        return  result;
    }


//    public  String wsPostRequest(String token,CEncuesta p)
//    {
//        String res ="";
//        try
//        {
//            ArrayList<NameValuePair> l = FEncode(p);
//            DefaultHttpClient client = new DefaultHttpClient();
//            HttpResponse response;
//            HttpPost post = new HttpPost(this._url);
//            String x = "Bearer " + UserSession.TOKEN_KEY;
//            post.setHeader("Authorization",x);
//            post.setEntity(new UrlEncodedFormEntity(l));
//            response= client.execute(post);
//            InputStream stream = response.getEntity().getContent();
//            String json = convertInputStreamToString(stream);
//            res= json;
//       }
//        catch (Exception e)
//        {
//
//        }
//        return  res;
//    }


    public String wsGetRequest(ArrayList<HttpParams> args)
    {
        String result ="";
        try
        {
            DefaultHttpClient client= new DefaultHttpClient();
            HttpGet  request= new HttpGet(this._url);

            for (HttpParams s :args)
            {
                request.setParams(s);
            }

            HttpResponse response = client.execute(request);

            HttpEntity entity = response.getEntity();

        }
        catch (Exception h)
        {
            return  null;
        }
        return  result;
    }


    public  String TestGet(String token)
    {
        String result ="";
        try
        {
           String uri="http://api.saeta.org.mx/Auditoria";
            URL url = new URL(uri);



            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            String strCred ="Bearer " +token;
            connection.setRequestProperty("Authorization",strCred);
            InputStream s = connection.getInputStream();

            result = convertInputStreamToString(s);
        }
        catch (Exception h)
        {
            return  null;
        }
        return  result;
    }


//    public String makeAndroidMultipart (File file, String mimeTpe ) throws  UnsupportedEncodingException
//    {
//        int maxBufferSize = 1 * 1024 * 1024;
//        String boundary =  "*****";
//        String crlf = "\r\n";
//        String twoHyphens = "--";
//        HttpsURLConnection httpsURLConnection ;
//        String res = null;
//        HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//        DefaultHttpClient client = new DefaultHttpClient();
//        SchemeRegistry registry = new SchemeRegistry();
//        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
//        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
//        registry.register(new Scheme("https", socketFactory, 443));
//        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
//        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
//        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//        HttpPost httpPost = new HttpPost(this._url);
//        String x = "Bearer " + UserSession.TOKEN_KEY;
//        httpPost.setHeader("Authorization",x);
//
//        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] imageBytes = getBytesFromFile(file);
//
//        // open a URL connection to the Servlet
//        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(imageBytes);
//
//        try
//        {
//            String frmData = "form-data; name=\"Media\"; filename="+ file.getAbsolutePath();
//            URL url = new URL(this._url);
//            httpsURLConnection = (HttpsURLConnection) url.openConnection();
//            httpsURLConnection.setDoOutput(true);
//            httpsURLConnection.setRequestMethod("POST");
//            httpsURLConnection.setRequestProperty("Accept", "*/*");
//            httpsURLConnection.setRequestProperty("Authorization", "Bearer "+ UserSession.TOKEN_KEY);
//            httpsURLConnection.setRequestProperty("Connection", "Keep-Alive");
//            httpsURLConnection.setRequestProperty("Cache-Control", "no-cache");
//            httpsURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//            DataOutputStream request = new DataOutputStream(httpsURLConnection.getOutputStream());
//            request.writeBytes(twoHyphens + boundary + crlf);
//            request.writeBytes("Content-Disposition: form-data; name=\"Media\";filename=\"" + file.getAbsolutePath() + "\"" + crlf);
//            request.writeBytes("Content-Type: " + mimeTpe);
//            request.writeBytes(crlf);
//
//
//            int bytesAvailable = fileInputStream.available();
//            int bufferSize=Math.min(bytesAvailable,maxBufferSize);
//
//            byte[] buffer = new byte[bufferSize];
//
//            int bytesRead = fileInputStream.read(buffer,0,bufferSize);
//
//            while (bytesRead > 0) {
//                request.write(buffer, 0, bufferSize);
//                bytesAvailable = fileInputStream.available();
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//            }
//
//
//            request.writeBytes(crlf);
//            request.writeBytes(twoHyphens + boundary + crlf);
//            request.flush();
//            request.close();
//
//            InputStream responseStream = httpsURLConnection.getInputStream();
//            String respuesta = convertInputStreamToString(responseStream);
//            System.out.println(respuesta);
//        }
//
//        catch (Exception r)
//        {
//            res="0";
//        }
//        return res;
//
//    }

    private byte[] getBytesFromFile (File file)
    {
       int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);

        } catch (FileNotFoundException e) {
             bytes= null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            bytes = null;
        }
      return bytes;
    }

//    public String makeMultipartHttpsCall(File file, String mimeType ) throws  UnsupportedEncodingException
//    {
//        String res = null;
//        HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//        DefaultHttpClient client = new DefaultHttpClient();
//        SchemeRegistry registry = new SchemeRegistry();
//        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
//        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
//        registry.register(new Scheme("https", socketFactory, 443));
//        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
//        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
//        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//        HttpPost httpPost = new HttpPost(this._url);
//        String x = "Bearer " + UserSession.TOKEN_KEY;
//        httpPost.setHeader("Authorization",x);
//        if (this.wsParameters.size()>0) {
//            httpPost.setEntity(new UrlEncodedFormEntity(this.wsParameters));
//        }
//
//        try
//        {           // httpPost.setEntity(new UrlEncodedFormEntity(l));
//          MultipartEntity mpEntity = new MultipartEntity();
//          ContentBody contentBody = new FileBody(file,mimeType);
//          mpEntity.addPart("userfile", contentBody);
//          httpPost.setEntity(mpEntity);
//          HttpResponse response = httpClient.execute(httpPost);
//          InputStream stream = response.getEntity().getContent();
//          String json = convertInputStreamToString(stream);
//          res= json;
//
//        }
//        catch (UnsupportedEncodingException g)
//        {
//            throw  g;
//        }
//        catch (Exception r)
//        {
//            res="0";
//        }
//        return res;
//    }


//    public String  makeHttpsGetCall (CEncuesta e) throws UnsupportedEncodingException {
//        String res = null;
//        HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//        DefaultHttpClient client = new DefaultHttpClient();
//        SchemeRegistry registry = new SchemeRegistry();
//        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
//        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
//        registry.register(new Scheme("https", socketFactory, 443));
//        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
//        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
//        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//        HttpPost httpPost = new HttpPost(this._url);
//        String x = "Bearer " + UserSession.TOKEN_KEY;
//        httpPost.setHeader("Authorization",x);
//        if (this.wsParameters.size()>0) {
//            httpPost.setEntity(new UrlEncodedFormEntity(this.wsParameters));
//        }
//
//        try
//        {
//            ArrayList<NameValuePair> l = FEncode(e);
//            httpPost.setEntity(new UrlEncodedFormEntity(l));
//            HttpResponse response = httpClient.execute(httpPost);
//            InputStream stream = response.getEntity().getContent();
//            String json = convertInputStreamToString(stream);
//            res= json;
//        }
//        catch (Exception ex){
//            res="0";
//        }
//        return  res;
//    }

//    public String  doHttpsGetCall (CEncuesta e) throws Exception, UnsupportedEncodingException, UnknownHostException {
//        String res = null;
//        HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//        DefaultHttpClient client = new DefaultHttpClient();
//        SchemeRegistry registry = new SchemeRegistry();
//        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
//        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
//        registry.register(new Scheme("https", socketFactory, 443));
//        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
//        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
//        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//        HttpPost httpPost = new HttpPost(this._url);
//        String x = "Bearer " + UserSession.TOKEN_KEY;
//        httpPost.setHeader("Authorization",x);
//        if (this.wsParameters.size()>0) {
//            httpPost.setEntity(new UrlEncodedFormEntity(this.wsParameters));
//        }
//
//        try
//        {
//           // ArrayList<NameValuePair> l = FEncode(e);
//           // httpPost.setEntity(new UrlEncodedFormEntity(l));
//            HttpResponse response = httpClient.execute(httpPost);
//            InputStream stream = response.getEntity().getContent();
//            String json = convertInputStreamToString(stream);
//            res= json;
//        }
//        catch (UnknownHostException u)
//        {
//            throw u;
//        }
//        catch (Exception ex){
//            throw   ex;
//        }
//        return  res;
//    }


    public String  makeHttpsGetCall () throws UnsupportedEncodingException {
        String res = null;
        HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        DefaultHttpClient client = new DefaultHttpClient();
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        HttpPost httpPost = new HttpPost(this._url);

        if (this.wsParameters.size()>0) {
            httpPost.setEntity(new UrlEncodedFormEntity(this.wsParameters));
        }

        try
        {
            HttpResponse response = httpClient.execute(httpPost);
            InputStream stream = response.getEntity().getContent();
            String json = convertInputStreamToString(stream);
            res= json;
        }        catch (Exception ex){
            res="0";
        }
        return  res;
    }


    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }




}
