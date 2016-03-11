package com.example.cnlive.jsonobject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private TextView textView;
    private static final int GETMESSAGE = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETMESSAGE:
                    String response = (String) msg.obj;
                    textView.setText(response);
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        button.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                getMessageFromUrl();
                break;
            default:
                break;
        }
    }

    private void getMessageFromUrl() {
        new Thread(new Runnable() {
            HttpURLConnection connection = null;

            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.baidu.com/");
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
//                        connection.setRequestMethod("POST");
//                        DataOutputStream out=new DataOutputStream(connection.getOutputStream());
//                        out.writeBytes("username=admin&password=123456");//用post方法实现向服务器传送数据，写到服务器上。
//                       每条数据都需要以键值对的形式传送。数据与数据之间用&连接。
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder builder = new StringBuilder();
                        String line = "";
                        if ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        parseJsonWithJsonObject(builder.toString());
                        Message message = handler.obtainMessage();
                        message.what = GETMESSAGE;
                        message.obj = builder.toString();
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }

        }).start();
    }
    /**
     * Json数据串如下：
     * [{"id":"0","name":"aaaaa","version":"1"},{"id":"1","name":"bbbbb","version":"2"},{"id":"2","name":"ccccc","version":"3"},]
     * 每一个[]是一个数组，每一个{}是一个对象，如果最外层是数组就先用JSONArray获得这个数组，再解析里面的对象。如果最外层是对象。例如：
     * {
     "programmers": [{
     "firstName": "Brett",
     "lastName": "McLaughlin",
     "email": "aaaa"
     }, {
     "firstName": "Jason",
     "lastName": "Hunter",
     "email": "bbbb"
     }, {
     "firstName": "Elliotte",
     "lastName": "Harold",
     "email": "cccc"
     }],
     "authors": [{
     "firstName": "Isaac",
     "lastName": "Asimov",
     "genre": "sciencefiction"
     }, {
     "firstName": "Tad",
     "lastName": "Williams",
     "genre": "fantasy"
     }, {
     "firstName": "Frank",
     "lastName": "Peretti",
     "genre": "christianfiction"
     }],
     "musicians": [{
     "firstName": "Eric",
     "lastName": "Clapton",
     "instrument": "guitar"
     }, {
     "firstName": "Sergei",
     "lastName": "Rachmaninoff",
     "instrument": "piano"
     }]
     }
上面这个json串以{}开始，因此最外层是对象，然后里面是数组，然后是对象。programmers是数组名。
     */
    private void parseJsonWithJsonObject(String s)  {
        try {
            JSONArray jsonArray=new JSONArray(s);
            // 首先JSON数据最外层是一个数组，这个是根据返回的数据进行判断的，里面的是一个一个的对象，因此需要先取出数组中的所有数据。
            for(int i=0;i<jsonArray.length();i++){//然后进行循环取出数组中的这三个对象。
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id=jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String version=jsonObject.getString("version");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}