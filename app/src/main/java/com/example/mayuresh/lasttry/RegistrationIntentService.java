package com.example.mayuresh.lasttry;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegServicePush";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(Constants.GCM_SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
            // Notify UI that registration has completed, so the progress indicator can be hidden.

    }

    private void sendRegistrationToServer(String token) {
        MainActivity.newRegID = token;
        WebServerRegistrationTask webServer = new WebServerRegistrationTask();
        webServer.execute();
    }

    public class WebServerRegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegistrationIntentService.this);

            URL url = null;
            try {
                url = new URL(Constants.WEB_SERVER_URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();

                sharedPreferences.edit().putString(Constants.PREF_GCM_REG_ID, "").apply();
            }
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("regID", MainActivity.newRegID);

            StringBuilder postBody = new StringBuilder();
            Iterator<Map.Entry<String, String>> iterator = dataMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Entry<String, String> param = (Entry<String, String>) iterator.next();
                postBody.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    postBody.append('&');
                }
            }
            String body = postBody.toString();
            byte[] bytes = body.getBytes();

            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setFixedLengthStreamingMode(bytes.length);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                OutputStream out = conn.getOutputStream();
                out.write(bytes);
                out.close();
                String response = "";
                InputStream is = null;
                try {
                    is = conn.getInputStream();
                    int ch;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                        sb.append((char) ch);
                    }
                    response = sb.toString();

                } catch (IOException e) {
                    throw e;
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
                int status = conn.getResponseCode();
                if (status == 200) {
                    if (response.equals("1")) {
                        sharedPreferences.edit().putString(Constants.PREF_GCM_REG_ID, MainActivity.newRegID).apply();
                        Intent registrationComplete = new Intent(Constants.SERVER_SUCCESS);
                        LocalBroadcastManager.getInstance(RegistrationIntentService.this).sendBroadcast(registrationComplete);
                    }
                } else {
                    throw new IOException("Request failed with error code "
                            + status);
                }
            } catch (ProtocolException pe) {
                pe.printStackTrace();
                sharedPreferences.edit().putString(Constants.PREF_GCM_REG_ID, "").apply();

            } catch (IOException io) {
                io.printStackTrace();
                sharedPreferences.edit().putString(Constants.PREF_GCM_REG_ID, "").apply();

            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }
    }
}