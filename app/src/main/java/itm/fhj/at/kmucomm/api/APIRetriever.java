package itm.fhj.at.kmucomm.api;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import itm.fhj.at.kmucomm.util.Config;

/**
 * Created by michael.stifter on 07.06.2015.
 */
public class APIRetriever {

    private String url;

    public APIRetriever() {

    }

    public String retrieveContacts() {
        url = Config.API_REST_USERS;

        StringBuilder stringBuilder = new StringBuilder();

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        boolean success = false;
        int tryCount = 0;

        while (!success && tryCount < Config.NETWORK_TRIES) {
            try {
                tryCount++;

                HttpResponse response = httpClient.execute(httpGet);

                HttpEntity entity = response.getEntity();

                BufferedReader inReader = new BufferedReader(new InputStreamReader(entity.getContent()));

                String line;

                while ((line = inReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                inReader.close();

                return stringBuilder.toString();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

}
