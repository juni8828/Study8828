package com.example.thiscopy;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ParsingJson extends AsyncTask<String, Void, String>
{

    /** Json 파일을 Parsing 하기 위해 만들어진 Class
     * 2019. 06 Yun I.H */

    final static String TAG="ParsingJson";

    JSONArray jsonArray;
    private String parsingStr,receiveMsg;
    private String parsingURL;
    String Types[]; // type String
    String clientKey="#########################";
    String values[][]; // 저장되는 파싱 값들
    int valueCnt; // 파싱 값들 갯수

    //1. 필요한 변수선언
    //2. 필요한 함수 생성
    //3. json 받아오기
    //4. json 으로 받아온 문자열을 type 마다 하나씩 끊어서 주기!

    public void setParsingURL(String url)
    {
        parsingURL = url;
    }

    public void setParsingTypes(String types[])
    {
        /* type의 갯수만큼 객체 생성 후 값 할당 */
        Types=new String[types.length];

        for(int i=0;i<types.length;i++)
        {
            Types[i]=types[i];
        }
    }


    public int getValueCnt()
    {
        return valueCnt;
    }

    /* 비동기 진행으로 json 받아오기 */
    @Override
    protected String doInBackground(String... strings)
    {
        URL url=null;
        try{
            url=new URL(parsingURL);

            /* http로 커넥션 연결 */
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            //url클래스 기능제약존재 URLConnection 객체로
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
            //url을 application/x-www-form-urlencoded;charset=UTF-8 형식으로 서버에 전달
            conn.setRequestProperty("x-waple-authorization",clientKey);
            //API키값 서버에 전달

            /* conn의 접근이 허용이 되면! */
            if(conn.getResponseCode()==conn.HTTP_OK){
                /* 해당 url에 있는 Input 값(String) reader로 가져오기 */
                InputStreamReader reader= new InputStreamReader(conn.getInputStream(),"UTF-8");
                //coNN에있는 input값을 reader로 전달
                BufferedReader bufferedReader=new BufferedReader(reader);
                //reader에 있는값을 스트링으로 받아서 bufferreader에 넣음
                StringBuffer stringBuffer =new StringBuffer();
                //stringBuffer에 16바이트 를 저장할 수 있는 버퍼를 가진 공간을 할당

                /* url로 가져온 StringLine 값이 없을때까지 반복 */
                while((parsingStr=bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(parsingStr);
                    //stringbuffer에 parsingstr안에있는 url값 넣음
                    Log.d(TAG,"url로 부터 가져온 값 :" + parsingStr);
                }
                receiveMsg=stringBuffer.toString();
                //string버퍼에있는 url값을 string으로 receivemsg로 전달
                Log.d(TAG,"receiveMSG : " + receiveMsg);
                bufferedReader.close();
            }
            else
                {
                Log.d(TAG,"json 접근 거부! : "+conn.getResponseCode());
                }
        }
        /*오류출력*/
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        /*오류출력*/
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }


    /* json 으로 받아온 문자열을 type 마다 하나씩 끊어서 주기! */
    private String[] JsonParser(JSONArray jsonArray, String[] type,int index)
    {
        String[] ret=new String[type.length];
        //ret에 type배열만큼의 크기를 할당
        try
        {
            /* index에 해당하는 line 값을 가져오자! */
            JSONObject jsonObject = jsonArray.getJSONObject(index);

            /* 해당 type 마다 ret 값 채워주기 */
            for (int j = 0; j < ret.length; j++)
            {
                ret[j] = jsonObject.optString(type[j]);
                //ret은 2차원 배열
            }
        }
        /*오류출력*/
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return ret;
    }


    public String[][] getData(String title)
    {
        /* JsonArray 미리 셋팅 후 하나씩 각 타이틀마다 Line을 나누어 함수에 전달 */
        try
        {
            jsonArray = new JSONObject(receiveMsg).getJSONArray(title);
            valueCnt = jsonArray.length();
            Log.d(TAG,"jsonArray : " + jsonArray.toString());
            Log.d(TAG,"title : " + title);
            Log.d(TAG,"valueCnt : " + valueCnt);
            values = new String[valueCnt][Types.length]; // 행 -> 데이터 값, 열 -> 항목명 으로 보면 됨!

            for (int j = 0; j < valueCnt; j++)
            {
                values[j] = JsonParser(jsonArray, Types, j);
            }
        }
        /*오류출력*/
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return values;
    }

    public String getOneData(String type)
    {
        String ret= "Null";

        /* JsonArray 미리 셋팅 후 하나의 값만 함수에 전달 */
        try
        {
            JSONObject jsonObject=new JSONObject(receiveMsg);

            ret = jsonObject.getString(type);
        }
        /*오류출력*/
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return ret;
    }
}