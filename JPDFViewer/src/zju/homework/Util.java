package zju.homework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;
import java.util.Random;

/**
 * Created by stardust on 2016/11/30.
 */
public class Util {
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();

    }

    public static String getStringFromInputStream(InputStream is) throws IOException {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;

        while ( (line = br.readLine()) != null ){
            sb.append(line);
        }

        is.close();
        br.close();
        return sb.toString();
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException{
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    public static String inputStreamToBase64(InputStream is){
        byte[] bytes = null;
        try{
            bytes = getBytesFromInputStream(is);
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return new String(Base64.encode(bytes));
    }

    public static void base64ToFile(String base64str, File tmpFile){
        try{
            FileOutputStream fout = new FileOutputStream(tmpFile);
            fout.write(Base64.decode(base64str));
            fout.close();
        } catch (Base64DecodingException ex){
          ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return ;
    }

    public static String objectToJson(Object obj){
        try{
            return mapper.writeValueAsString(obj);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Object jsonToObject(String json, TypeReference type){
        try{
            Object obj = mapper.readValue(json, type);
            return obj;
        }
        catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static int randInt(int min, int max) {

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static String getTempDir(){
        return System.getProperty("java.io.tmpdir");
    }

}
