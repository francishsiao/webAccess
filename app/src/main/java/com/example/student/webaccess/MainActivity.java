package com.example.student.webaccess;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> mydata;
    ArrayList<String> mylinks;
    ListView lv;
    Handler handler;
    ArrayAdapter<String> adapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        handler = new Handler();
        lv = (ListView)findViewById(R.id.listView);

        new Thread() {
            private InputStream inputStream = null;
            StringBuffer sb = new StringBuffer();
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL("http://udn.com/rssfeed/news/1");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    inputStream = new BufferedInputStream(conn.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String inputLine = "";
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    Log.d("NET", sb.toString());
                    String result = sb.toString();

                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();

                    DataHandler dataHandler = new DataHandler();
                    xr.setContentHandler(dataHandler);
                    xr.parse(new InputSource(new StringReader(result)) );

                    mydata = dataHandler.getData();
                    mylinks = dataHandler.getLinks();

                    inputStream.close();
                    conn.disconnect();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new ArrayAdapter<String>(
                                context,
                                android.R.layout.simple_list_item_1,
                                mydata
                            );
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent it = new Intent(MainActivity.this, DetailActivity.class);
                                    it.putExtra("link", mylinks.get(position));
                                    startActivity(it);
                                }
                            });
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
