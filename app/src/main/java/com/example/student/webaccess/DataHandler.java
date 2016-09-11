package com.example.student.webaccess;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by student on 2016/8/28.
 */
public class DataHandler extends DefaultHandler {
    private boolean isTitle = false;

    boolean isItem = false;
    boolean isLink = false;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> links = new ArrayList<>();
    //ArrayList<Map<String,String>> data;
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        //Log.d("XML", localName);
        if (localName.equals("item"))
        {
            isItem = true;
        }
        if (localName.equals("title"))
        {
            isTitle = true;
        }
        if (localName.equals("link"))
        {
            isLink = true;
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        if (localName.equals("title"))
        {
            isTitle = false;
        }
        if (localName.equals("item"))
        {
            isItem = false;
        }
        if (localName.equals("link"))
        {
            isLink = false;
        }

    }

    @Override
    public void characters(char ch[], int start, int length) {
        String chars = new String(ch).substring(start, start + length);
        chars = chars.trim();
        if (isTitle && isItem)
        {
            Log.d("XML", "title:" + chars);
            data.add(chars);
        }else if(isLink && isItem){
            Log.d("XML", "link:" + chars);
            links.add(chars);
        }
    }

    public ArrayList<String> getData()
    {
        return data;
    }

    public ArrayList<String> getLinks()
    {
        return links;
    }

}
