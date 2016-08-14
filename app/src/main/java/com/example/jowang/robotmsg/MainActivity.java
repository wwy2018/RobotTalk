package com.example.jowang.robotmsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ListView listView;
    private RobotAdapter adapter;
    private ArrayList<RobotMessage> arraylist;
    private EditText inputMsg;
    private Button btnSend;
    boolean isConnected = false;
    private static final String myURL = "http://www.tuling123.com/openapi/api";
    private static final String API = "bef4ac52a68f46198096edc8e965bf61";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            RobotMessage robotMessage = (RobotMessage) msg.obj;
            arraylist.add(robotMessage);
            adapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.robot_bar);
        toolbar.getBackground().setAlpha(200);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        listView = (ListView) findViewById(R.id.my_list_view);
        inputMsg = (EditText) findViewById(R.id.id_input_msg);
        btnSend = (Button) findViewById(R.id.id_send_msg);
        arraylist = new ArrayList<>();
        arraylist.add(new RobotMessage("你好，我是小熊！", RobotMessage.Type.OUTPUT, new Date()));
        adapter = new RobotAdapter(this, arraylist);
        listView.setAdapter(adapter);
        isConnected = ConnectivityReceiver.isConnected();
        System.out.println(isConnected);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnected) {
                    final String input = inputMsg.getText().toString().trim();
                    if (TextUtils.isEmpty(input)) {
                        return;
                    }
                    RobotMessage output = new RobotMessage();
                    output.setDate(new Date());
                    output.setMsg(input);
                    output.setType(RobotMessage.Type.INPUT);
                    arraylist.add(output);
                    adapter.notifyDataSetChanged();
                    inputMsg.setText("");
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... voids) {
                            String url = myURL + "?key=" + API + "&info=" + input;
                            try {
                                InputStream inputStream = new URL(url).openStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                                String line = null;
                                StringBuffer stringBuffer = new StringBuffer();
                                while ((line = reader.readLine()) != null) {
                                    stringBuffer.append(line);
                                }
                                inputStream.close();
                                reader.close();
                                return stringBuffer.toString();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            RobotMessage robotMessage = new RobotMessage();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                Result result = new Result();
                                result.setText(jsonObject.getString("text"));
                                robotMessage.setMsg(result.getText());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            robotMessage.setDate(new Date());
                            robotMessage.setType(RobotMessage.Type.OUTPUT);
                            Message m = Message.obtain();
                            m.obj = robotMessage;
                            handler.sendMessage(m);
                        }

                    }.execute();
                }else {
                    Toast.makeText(getApplicationContext(),"世界上最远的距离是没有WIFI",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        finish();
        startActivity(new Intent(MainActivity.this,MainActivity.class));
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }


}
