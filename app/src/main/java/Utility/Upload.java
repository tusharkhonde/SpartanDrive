package Utility;

import android.util.Log;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by TUSHAR_SK on 12/1/15.
 */
public class Upload {


    public static void main(String[] args) throws IOException {
        String url = "https://content.dropboxapi.com/2/files/upload";
        String token = "Bearer s6BUNQGjzWYAAAAAAAAEXqlnpLnZPdDbuMJgVt0AzujLgFEqdkLHOGWB8QNlJ_gG";


//        File file = new File("/Users/TUSHAR_SK/Downloads/Certificate.pdf");
//        FileInputStream imageInFile = new FileInputStream(file);
//        byte imageData[] = new byte[(int) file.length()];
//        imageInFile.read(imageData);
//        String imageDataString = encodeImage(imageData);


        FileBody fileBody = new FileBody(new File("/Users/TUSHAR_SK/Downloads/Certificate.pdf"));
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
        multipartEntity.addPart("file", fileBody);


        postRequest(url,multipartEntity,token,true);

    }

//    public static String encodeImage(byte[] imageByteArray) {
//        return Base64.encodeBase64URLSafeString(imageByteArray);
//    }

    public static String postRequest(String url,MultipartEntity m, String accessToken, Boolean isPost)
    {
        StringBuilder response = null;
        StringBuilder urlBuilder =  null;
        BufferedReader in = null;
        HttpsURLConnection con = null;
        int responseCode = -1;
        DataOutputStream wr = null;
        try
        {

            response = new StringBuilder();

            urlBuilder =  new StringBuilder();
            urlBuilder.append(url);


            URL urlObj = new URL(urlBuilder.toString());

            Log.v("URL : ", urlObj.toString());

            con = (HttpsURLConnection)urlObj.openConnection();
            if (isPost) {

                con.setRequestMethod("POST");
            } else {

                con.setRequestMethod("DELETE");
            }
            con.setDoInput(true);
            con.setDoOutput(true);

            con.setRequestProperty("Authorization", accessToken);
            con.setRequestProperty("Content-Type", "application/octet-stream");
            con.setRequestProperty("Dropbox-API-Arg", "{\n" +
                    "   \"path\": \"/Home/Folder/gameStop.jpg\",\n" +
                    "   \"mode\": \"add\",\n" +
                    "   \"autorename\": true,\n" +
                    "   \"mute\": false\n" +
                    "}");

            OutputStream out = con.getOutputStream();
            m.writeTo(out);


//            // Send post request
//            wr = new DataOutputStream(con.getOutputStream());
//            wr.writeBytes(new ByteArrayInputStream(reqBody));

            responseCode = con.getResponseCode();
            System.out.println(responseCode);

        } catch (Exception e)
        {
            if(responseCode == -1)
            {
                System.out.println("");
            }
            else
            {
                in = new BufferedReader(
                        new InputStreamReader(con.getErrorStream()));
                String inputLine = "";

                try
                {
                    while ((inputLine = in.readLine()) != null)
                    {
                        response.append(inputLine);
                    }
                }
                catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                finally
                {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }

                System.out.println(response.toString());
            }

        }

        finally
        {
            try {
                wr.flush();
                wr.close();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return String.valueOf(responseCode);

    }
}
