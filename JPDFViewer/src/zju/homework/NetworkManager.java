package zju.homework;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by stardust on 2016/11/30.
 */
public class NetworkManager {

    private final static String host = "http://localhost:8080";
    public final static String registerUrl = host + "/account/register";            // POST
    public final static String loginUrl = host + "/account";                               // POST
    public final static String createGroupUrl = host + "/group";                     // POST
    public final static String joinGroupUrl = host + "/group/";                       // GET, +groupid
    public final static String uploadAnnotUrl = host + "/annotation";          // POST
    public final static String downloadAnnotUrl = host + "/annotation/";     // GET, +groupid


    public final static int RESPONSE_OK = 200;

    public NetworkManager(){ }

    public boolean getPDFDocument(String addr, String filepath){
        try {
            URL url = new URL(addr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "");
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if( responseCode == RESPONSE_OK ){
                InputStream is = connection.getInputStream();
                OutputStream os = new FileOutputStream(filepath);
                try{
                    int read = 0;
                    byte[] bytes = new byte[1024];
                    while ( (read = is.read(bytes)) != -1 ){
                        os.write(bytes, 0, read);
                    }
                }catch (IOException ex){
                    ex.printStackTrace();
                    return false;
                }finally {
                    try {
                        is.close();
                        os.close();
                    }catch (IOException ex){
                        ex.printStackTrace();
                        return false;
                    }
                }

            }

        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public String getJson(String addr){

        String result = null;
        InputStream is = null;
        try {
            URL url = new URL(addr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "");
            connection.setDoInput(true);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if( responseCode != RESPONSE_OK )
                return connection.getResponseMessage();

            is = connection.getInputStream();
            result = Util.getStringFromInputStream(is);
            is.close();
        }catch (ConnectException ex){
            result = ex.getLocalizedMessage();
        }catch (IOException ex){
            ex.printStackTrace();
        }

        return result;
    }

    public String postJson(String addr, String json){
        String result = null;
        InputStream is = null;
        try{
            URL url = new URL(addr);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("User-Agent", "");
            connection.setReadTimeout(1500);
            connection.setConnectTimeout(1500);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(json.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            if( responseCode != RESPONSE_OK )
                return connection.getResponseMessage();

            is = connection.getInputStream();
            result = Util.getStringFromInputStream(is);
            is.close();
        }catch (ConnectException ex){
            result = ex.getLocalizedMessage();
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return result;
    }


}
