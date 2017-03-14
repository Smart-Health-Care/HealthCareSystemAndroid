package io.iqube.healthcaresystem.NotNeeded;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import io.iqube.healthcaresystem.R;
import io.realm.Realm;

public class ConnectActivity extends AppCompatActivity {
    Button connect;
    TextView post_Data;
    NSDClass nsdClass;
    private String HASH = "e6aa3022bcf28765dc01179a02dbd00c5fbd16c8cb75436857086b2c17c2d4c7";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        connect = (Button) findViewById(R.id.connect);
        post_Data = (TextView) findViewById(R.id.text);
        nsdClass = new NSDClass();
        nsdClass.Register(this);
        new DataListenerThread().execute();
        Realm.init(this);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new SendData().execute();
            }
        });
    }
    ServerSocket ss;
    Socket s;
    private class DataListenerThread extends AsyncTask<Void,String,Void> {

        @Override
        protected void onProgressUpdate(String... values) {
            //Toast.makeText(ConnectActivity.this,values[0],Toast.LENGTH_LONG).show();
            int json_index = values[0].indexOf('{');
            int url_start_index = values[0].indexOf('T')+1;
            int url_end_index = values[0].indexOf('H');
            String url = values[0].substring(url_start_index,url_end_index);
            String data = values[0].substring(json_index);
            try {
                JSONObject jsonObject = new JSONObject(data);
                if(url.contains("/data")){
                    Realm realm = Realm.getDefaultInstance();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Toast.makeText(ConnectActivity.this,data,Toast.LENGTH_LONG).show();
            post_Data.setText(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                ss = new ServerSocket(8000);
                while(true){
                    //Server is waiting for client here, if needed
                    try {
                        s = ss.accept();
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        PrintWriter output = new PrintWriter(s.getOutputStream(), true); //Autoflush
                        int buff = s.getReceiveBufferSize();
                        String line = "";
                        char[] buffer = new char[buff];
                        int a = input.read(buffer);
                        line += new String(buffer, 0, a);
                        publishProgress(line);
                        Log.d("Data", line);
                        if (line.equals("end"))
                            break;
                        output.println("Received");
                    }catch(StringIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                    Realm realm = Realm.getDefaultInstance();


//                    RealmResults<Medicine> realmResults = realm.where(Medicine.class).findAll();
//                    String json = "[";
//                    for(int i = realmResults.size() - 1; i >= 0; i--){
//                        json+="{";
//                        json+="\"name\":\""+realmResults.get(i).name+"\",";
//                        json+="\"quantity\":\""+realmResults.get(i).quantity+"\",";
//                        json+="\"date\":\""+realmResults.get(i).date+"\",";
//                        json+="\"time\":\""+realmResults.get(i).time+"\"";
//                        json+="}";
//                        if(i!=0)
//                            json+=",";
//                    }
//                    json+="]";


                    s.close();
                }
                ss.close();

            }catch(UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    private class SendData extends AsyncTask<Void,String,Void>{
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Toast.makeText(ConnectActivity.this,values[0],Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Socket s = new Socket("healthcare.local",8000);

                //outgoing stream redirect to socket
                OutputStream out = s.getOutputStream();

                PrintWriter output = new PrintWriter(out);
                output.println("hello");
                BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));

                //read line(s)
                int buff = s.getReceiveBufferSize();
                String line = "";
                char[] buffer = new char[buff];
                int a = input.read(buffer);
                line += new String(buffer, 0, a);
                publishProgress(line);
                //Close connection
                s.close();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onServiceFound(String data){
        if(data.equals(NSDClass.SERVICE_RESOLVED)){
            Toast.makeText(this,"Device Connected",Toast.LENGTH_LONG).show();

            Log.d("NSD","Device Found");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ss!=null)
        if(!ss.isClosed()){
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(s!=null)
        if(!s.isClosed()){
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
