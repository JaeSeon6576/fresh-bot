package com.example.nonmoon;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import com.google.gson.Gson;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnInitListener {

    EditText edit;
    TextView textv;
    Button button1;
    String data;
    final int DIALOG_DATE = 1;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    public String de = sdf.format(date);
    String yea;
    String mon ;
    String day ;
    String nowyear;
    String nowmon;
    String nowday;
    int local;

    Spinner Main_spinner;

    RadioGroup rGroup;
    RadioButton wholesale, retail;
    String market;//도매가격, 소매가격

    String openApiURL;
    String accessKey;
    String analysisCode;
    String text;




   public String voicresult;

    String inprdlst_nm,ingrad, instndrd, inexamin_amt;

    int textsize = 25;
    ScrollView scroll;
    ImageButton imgbutton;
    private final int REQ_CODE_SPEECH_INPUT = 100;



    float voi;
    float voispeed;

    public TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit= (EditText)findViewById(R.id.edit);
        textv= (TextView)findViewById(R.id.text);
        scroll=(ScrollView)findViewById(R.id.scroll) ;
        imgbutton = (ImageButton)findViewById(R.id.imgbutton);


        myTTS = new TextToSpeech(this, this);

        //공지사항

        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);



        oDialog.setMessage("1. 본 앱은 공공데이터를 사용하므로\n 주말 및 공휴일에는 조회가 어려울수 있습니다.\n\n" +
                "2. 본 앱은 상업적인 용도로 사용이 불가능합니다.")
                .setTitle("공지")
                .setNeutralButton("확인", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .setCancelable(true) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();






        //지역설정
        Main_spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.location,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Main_spinner.setAdapter(adapter);
        Main_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //각 항목 클릭시 포지션값을 토스트에 띄운다.

                switch (position) {
                    case 0:
                        local = 1104;
                        Toast.makeText(getApplicationContext(), "서울을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        myTTS.speak("서울을 선택하셨습니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                        break;
                    case 1:
                        local = 2501;
                        Toast.makeText(getApplicationContext(), "대전을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        myTTS.speak("대전을 선택하셨습니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                        break;
                    case 2:
                        local = 2200;
                        Toast.makeText(getApplicationContext(), "대구를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        myTTS.speak("대구를 선택하셨습니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                        break;
                    case 3:
                        local = 2600;
                        Toast.makeText(getApplicationContext(), "울산을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        myTTS.speak("울산을 선택하셨습니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                        break;
                    case 4:
                        local = 2100;
                        Toast.makeText(getApplicationContext(), "부산을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                        myTTS.speak("부산을 선택하셨습니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //도매,소매 설정
        rGroup = (RadioGroup) findViewById(R.id.rGroup_market);
        wholesale = (RadioButton) findViewById(R.id.rb_wholesale);
        retail = (RadioButton) findViewById(R.id.rb_retail);

        market = "소비자가격";
        retail.setChecked(true);

        RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener(){
            public void onClick(View v){
                switch (rGroup.getCheckedRadioButtonId()){
                    case R.id.rb_retail:
                        market = "소비자가격";
                        Toast.makeText(getApplicationContext(),"소매가격을 검색합니다.", Toast.LENGTH_SHORT).show();
                        myTTS.speak("소매가격을 검색합니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                        break;
                    case R.id.rb_wholesale:
                        market = "도매가격";
                        Toast.makeText(getApplicationContext(),"도매가격을 검색합니다.", Toast.LENGTH_SHORT).show();
                        myTTS.speak("도매가격을 검색합니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                        break;
                }
            }
        };
        wholesale.setOnClickListener(optionOnClickListener);
        retail.setOnClickListener(optionOnClickListener);

        //로딩화면
        Intent intent = new Intent(this, LodingActivity.class);
        startActivity(intent);

        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();

            }
        });

        button1 = (Button)findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick(v);

            }
        });

        //현재 시간
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String getTime = sdf.format(date);
        nowyear = getTime.substring(0,4);
        nowmon = getTime.substring(4,6);
        nowday =getTime.substring(6,8);





        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            showDialog(DIALOG_DATE);
            //날짜설정
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
//             음성 톤 설정
//            if (voi == 0.5f){
//                voi = 1.0f;
//                myTTS.setPitch(voi);
//            }else{
//                voi =0.5f;
//                myTTS.setPitch(voi);         // 음성 톤을 2.0배 올려준다.
//            }

            ShowToneDialog();



        } else if (id == R.id.nav_gallery) {


//            if (textsize == 45){
//                Toast.makeText(getApplicationContext(),"더이상 크기를 줄일수없습니다.", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                textsize += 5;
//                textv.setTextSize(textsize);
//            }

            ShowTextSizeDialog();

        } else if (id == R.id.nav_slideshow) {


//            if(textv.getCurrentTextColor()!= Color.WHITE){
//                textv.setTextColor(Color.WHITE);
//                scroll.setBackgroundColor(Color.BLACK);
//            }
//            else if(textv.getCurrentTextColor()==Color.WHITE){
//                textv.setTextColor(Color.BLACK);
//                scroll.setBackgroundColor(Color.WHITE);
//            }

            ShowNegDialog();

        } else if (id == R.id.nav_manage) {

//            if (voispeed == 4.0f){
//                voispeed = 1.0f;
//                myTTS.setSpeechRate(voispeed);
//            }else{
//                voispeed =4.0f;
//                myTTS.setSpeechRate(voispeed);         // 음성 속도 올려준다.
//            }

            ShowSpdDialog();

        } else if (id == R.id.nav_share) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("제작자");
            dlg.setMessage("제작자 : 이재선, 김종우, 이소정, 전경향");
            dlg.setPositiveButton("확인", null);
            dlg.show();

        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("http://www.data.go.kr"));

            startActivity(intent);






        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //메뉴

    public void mOnClick(View v){

        switch( v.getId() ){
            case R.id.button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        data= getXmlData();//아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                textv.setText(data); //TextView에 문자열  data 출력
                                if(TextUtils.isEmpty(textv.getText())){
                                    textv.setText("결과가 없습니다. 날짜 및 품목을 확인해 주세요.");
                                    myTTS.speak("결과가 없습니다. 날짜 및 품목을 확인해 주세요.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                                }else{
                                    String dan;
                                    dan = instndrd;
                                    if(dan.indexOf("KG") > -1){
                                        int x = dan.indexOf("KG");
                                        String danhead = dan.substring(0,x);
                                        danhead += "키로그람";
                                        dan = danhead;
                                    }else if(dan.indexOf("G") > -1){
                                        int x = dan.indexOf("G");
                                        String danhead = dan.substring(0,x);
                                        danhead += "그람";
                                        dan = danhead;
                                    }
                                myTTS.speak(mon+"월"+day+"일"+inprdlst_nm+"의"+dan+"당 가격은"+inexamin_amt+"원"+"입니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                                     }



                            }
                        });
                    }
                }).start();
                break;
        }
    }

    String getXmlData(){//xml에서 데이터를 가져온다

        StringBuffer buffer=new StringBuffer();
        String str= edit.getText().toString();//EditText에 작성된 Text얻어오기
        String search = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding     // 검색 위한 변수


        String queryUrl="http://apis.data.go.kr/B552895/LocalGovPriceInfoService/getLocalGovPriceResearchSearch?" +
                "serviceKey=IPm2cP7UXMPF3t7ZyKnfAtoO%2FPXOA1WPaD94JvqqHVexYmpOPuIawou0V9XiEBQOqr9fWXjB1XRUFRjdcqqyTg%3D%3D&" +
                "numOfRows=5&pageSize=5&pageNo=1&startPage=1&_returnType=xml%2Cjson&examin_de=" +de+
                //"&examin_area_nm=&examin_mrkt_nm&examin_nm="+market+"&prdlst_nm="+search+
                "&examin_area_cd="+local+"&examin_mrkt_nm=&prdlst_nm="+search+ //"&distb_step="+market+
                "&examin_amt=&bfrt_examin_amt=&stndrd= null&grad=";


        try {
            URL url= new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is= url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory= XmlPullParserFactory.newInstance();
            XmlPullParser xpp= factory.newPullParser();
            xpp.setInput( new InputStreamReader(is, "UTF-8") ); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType= xpp.getEventType();

            while( eventType != XmlPullParser.END_DOCUMENT ){
                switch( eventType ){
                    case XmlPullParser.START_DOCUMENT:

                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        First:
                        {
                            tag = xpp.getName();//태그 이름 얻어오기

                            if (tag.equals("item")) ;// 첫번째 검색결과
                            if (tag.equals("distb_step")) {
                                // buffer.append("도매/소매 :");
                                xpp.next();
                                if (!xpp.getText().equals(market)) {
                                    break First;
                                }
                            } else if (tag.equals("prdlst_nm")) {
                                buffer.append("품목 : ");
                                xpp.next();
                                buffer.append(xpp.getText());//addr 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                inprdlst_nm = xpp.getText();
                                buffer.append("\n"); //줄바꿈 문자 추가
                            } else if (tag.equals("examin_de")) {
                                buffer.append("일자 : ");
                                xpp.next();
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            } else if (tag.equals("examin_area_nm")) {
                                buffer.append("위치 :");
                                xpp.next();
                                buffer.append(xpp.getText());//cpId
                                buffer.append("\n");
                            } else if (tag.equals("examin_mrkt_nm")) {
                                buffer.append("판매처 :");
                                xpp.next();
                                buffer.append(xpp.getText());//cpNm
                                buffer.append("\n");
                            } else if (tag.equals("examin_amt")) {
                                buffer.append("조사가격 (원):");
                                xpp.next();
                                buffer.append(xpp.getText());//
                                inexamin_amt = xpp.getText();
                                buffer.append("\n");
                            } else if (tag.equals("bfrt_examin_amt")) {
                                buffer.append("전일 조사가격 (원) :");
                                xpp.next();
                                buffer.append(xpp.getText());//
                                buffer.append("\n");
                            } else if (tag.equals("stndrd")) {
                                buffer.append("규격 :");
                                xpp.next();
                                buffer.append(xpp.getText());//csId
                                instndrd = xpp.getText();
                                buffer.append("\n");
                            } else if (tag.equals("grad")) {
                                buffer.append("등급 :");
                                xpp.next();
                                buffer.append(xpp.getText());
                                ingrad = xpp.getText();
                                buffer.append("\n--------------------------------------------");
                            }
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag= xpp.getName(); //태그 이름 얻어오기

                        if(tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType= xpp.next();
            }

        } catch (Exception e) {

            // TODO Auto-generated catch blocke.printStackTrace();
        }

        return buffer.toString();//StringBuffer 문자열 객체 반환

    }
    protected Dialog onCreateDialog(int id) {
        switch(id){
            case DIALOG_DATE :
                DatePickerDialog dpd = new DatePickerDialog
                        (MainActivity.this, // 현재화면의 제어권자
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view,
                                                          int year, int monthOfYear,int dayOfMonth) {
                                       yea = String.format("%04d", year);
                                       mon = String.format("%02d", monthOfYear+1);
                                       day = String.format("%02d", dayOfMonth);//자리수 맞추기

                                        Toast.makeText(getApplicationContext(),
                                                year+"년 "+mon+"월 "+day+"일 을 선택했습니다",
                                                Toast.LENGTH_SHORT).show();
                                        de = yea+mon+day;
                                    }
                                }
                                , // 사용자가 날짜설정 후 다이얼로그 빠져나올때
                                //    호출할 리스너 등록
                                Integer.parseInt(nowyear),  Integer.parseInt(nowmon)-1,  Integer.parseInt(nowday)); // 기본값 연월일
                return dpd;
        }

        return super.onCreateDialog(id);
    }


    @Override
    public void onInit(int status) {

myTTS.setLanguage(Locale.KOREAN);


    }
    protected void onDestroy() {
        super.onDestroy();
        myTTS.shutdown();
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "",
                    Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case REQ_CODE_SPEECH_INPUT: {


            if (resultCode == RESULT_OK && null != data) {

                ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

               //result.get(0)  음성 결과 text
                NaLang naturalLang = new NaLang();
                naturalLang.setstr = result.get(0);
                naturalLang.Langchange();

                if(naturalLang.getstrn.contains("전날")||naturalLang.getstr.contains("전날")||
                naturalLang.getstrn.contains("어제")||naturalLang.getstr.contains("어제")||
                naturalLang.getstrn.contains("어저께")||naturalLang.getstr.contains("어저께")){

                    daySet(1);
                }else if(naturalLang.getstrn.contains("전전날")||naturalLang.getstr.contains("전전날")||
                        naturalLang.getstrn.contains("그저께")||naturalLang.getstr.contains("그저께")||
                        naturalLang.getstrn.contains("이틀 전")||naturalLang.getstr.contains("이틀 전")||
                        naturalLang.getstrn.contains("그제")||naturalLang.getstr.contains("그제")) {

                    daySet(2);
                }else if(naturalLang.getstrn.contains("글피")||naturalLang.getstr.contains("글피")||
                        naturalLang.getstrn.contains("엊그제")||naturalLang.getstr.contains("엊그제")||
                        naturalLang.getstrn.contains("삼일 전")||naturalLang.getstr.contains("삼일 전")||
                        naturalLang.getstrn.contains("3일전")||naturalLang.getstr.contains("3일전")) {
                    daySet(3);

                }else if(naturalLang.getstrn.contains("서울")) {
                    Main_spinner.setSelection(0);
                    local = 1104;
                }else if(naturalLang.getstrn.contains("대전")) {
                    Main_spinner.setSelection(1);
                    local = 2501;
                }else if(naturalLang.getstrn.contains("대구")) {
                    Main_spinner.setSelection(2);
                    local = 2200;
                }else if(naturalLang.getstrn.contains("울산")) {
                    Main_spinner.setSelection(3);
                    local = 2600;
                }else if(naturalLang.getstrn.contains("부산")) {
                    Main_spinner.setSelection(4);
                    local = 2100;
                }else if(naturalLang.getstrn.contains("도매")||naturalLang.getstrn.contains("도매")){
                    wholesale.setChecked(true);
                    retail.setChecked(false);
                    market = "도매가격";
                    myTTS.speak("도매가격을 검색합니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                }else if(naturalLang.getstrn.contains("소매")||naturalLang.getstrn.contains("소비자가")) {
                    wholesale.setChecked(false);
                    retail.setChecked(true);
                    market = "소비자가격";
                    myTTS.speak("소매가격을 검색합니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
                }else if(naturalLang.getstr.contains("검색")){

                }
                else{
                    edit.setText(naturalLang.getstr.toString());//음성 받아오는 변수
                    button1.callOnClick();
                }
            }
            break;
        }

    }
}
//이어폰 버튼 키 이벤트
   @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getKeyCode() == KeyEvent.KEYCODE_HEADSETHOOK && event.getAction() == KeyEvent.ACTION_UP){
           imgbutton.callOnClick();
        }else if(event.getKeyCode() ==  KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP){

          String strTemp = edit.getText().toString();
          if(TextUtils.isEmpty(strTemp)) {
                edit.setText("");
          }else{
              edit.setText(strTemp.substring(0, strTemp.length() - 1));
              edit.setSelection(edit.length());
          }

        }

        return false;

    }
    public void daySet(int i){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String getTime = sdf.format(date);
        nowyear = getTime.substring(0,4);
        nowmon = getTime.substring(4,6);
        nowday =getTime.substring(6,8);//현재 시간 초기화

        int nnowday = Integer.parseInt(nowday)-i;
        yea = nowyear;
        mon = nowmon;
        String nnnowday = String.format("%02d",nnowday);
        day = nnnowday;
        de = nowyear+nowmon+nnnowday;
        myTTS.speak("날짜를"+yea+"년"+mon+"월"+day+"일 로 변경했습니다.",TextToSpeech.QUEUE_FLUSH,null);//음성출력
        Toast.makeText(getApplicationContext(),"날짜를"+yea+"년"+mon+"월"+day+"일 로 변경했습니다.", Toast.LENGTH_SHORT).show();
    }



    //시크바 메뉴
    public void ShowToneDialog(){

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final SeekBar seek = new SeekBar(this);
        seek.setMax(2);
        seek.setProgress(1);
        popDialog.setTitle("음성 톤을 설정하세요");

        popDialog.setView(seek);
        if(voi == 0.8f){
            seek.setProgress(0);
        }
        else if(voi == 1.0f){
            seek.setProgress(1);
        }else if(voi == 1.3f){
            seek.setProgress(2);
        }
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

                switch (progress){
                    case 0:
                        voi = 0.8f;
                        break;
                    case 1:
                        voi = 1.0f;
                        break;
                    case 2:
                        voi = 1.3f;
                        break;
                }
              myTTS.setPitch(voi);
            }
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        popDialog.setPositiveButton("OK",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        popDialog.create();
        popDialog.show();
    }

    //글 크기 메뉴
    public void ShowTextSizeDialog(){

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final Button btnsmall = new Button(this);
        final Button btnbig = new Button(this);
        final TextView testtext = new TextView(this);


        btnsmall.setText("-");
        btnbig.setText("+");
        testtext.setText("크기 테스트 ABC 123");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout baseLayout = new LinearLayout(this);
        LinearLayout baseLayout2 = new LinearLayout(this);
        params.width = 450;
        params.height = 200;
        btnsmall.setLayoutParams(params);
        btnbig.setLayoutParams(params);
        btnsmall.setTextSize(50);
        btnbig.setTextSize(50);
        testtext.setTextSize(textsize);

        baseLayout.setOrientation(LinearLayout.VERTICAL);
        baseLayout.setGravity(1);
        baseLayout2.addView(btnsmall);
        baseLayout2.addView(btnbig);

        baseLayout.setOrientation(LinearLayout.VERTICAL);

        baseLayout.addView(baseLayout2);
        baseLayout.addView(testtext);

        popDialog.setTitle("텍스트 크기를 설정하세요");

        popDialog.setView(baseLayout);


        btnsmall.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (textsize == 5){
                Toast.makeText(getApplicationContext(),"최소 크기 입니다.", Toast.LENGTH_SHORT).show();

                }
                else {
                    textsize -= 5;
                    textv.setTextSize(textsize);
                    testtext.setTextSize(textsize);
                }

            }
        });
        btnbig.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (textsize == 45){
                    Toast.makeText(getApplicationContext(),"최대 크기 입니다.", Toast.LENGTH_SHORT).show();
                  //  txtsize.setText(textsize);
                }
                else {
                    textsize += 5;
                    textv.setTextSize(textsize);
                  //  txtsize.setText(textsize);
                    testtext.setTextSize(textsize);
                }
            }
        });




        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        popDialog.create();
        popDialog.show();
    }

    public void ShowSpdDialog(){

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final SeekBar seek = new SeekBar(this);
        seek.setMax(4);
        seek.setProgress(2);
        popDialog.setTitle("음성 속도 설정하세요");

        popDialog.setView(seek);
//        if(voi == 0.8f){
//            seek.setProgress(0);
//        }
//        else if(voi == 1.0f){
//            seek.setProgress(1);
//        }

        if (voispeed == 0.25f){
            seek.setProgress(0);
            }else if((voispeed == 0.5f)){
            seek.setProgress(1);
            }else if((voispeed == 1.0f)){
            seek.setProgress(2);
            }else if((voispeed == 1.5f)){
                seek.setProgress(3);
            }else if((voispeed == 2.0f)){
                seek.setProgress(4);
            }

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){

                switch (progress){
                    case 0:
                        voispeed =0.25f;
                        break;
                    case 1:
                        voispeed =0.5f;
                        break;
                    case 2:
                        voispeed =1.0f;
                        break;
                    case 3:
                        voispeed =1.5f;
                        break;
                    case 4:
                        voispeed =2.0f;
                        break;
                }
                myTTS.setSpeechRate(voispeed);         // 음성 속도 올려준다.
            }
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        popDialog.create();
        popDialog.show();
    }

    int chekcolor=0;

    //흑색반전 스위치 메뉴
    public void ShowNegDialog() {


        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final Switch swit = new Switch(this);
        final TextView text1 = new TextView(this);
        final TextView text2 = new TextView(this);


        LinearLayout baseLayout = new LinearLayout(this);


    if (chekcolor == 1) {
        swit.setChecked(true);
    }

        swit.setShowText(true);
        swit.getSwitchMinWidth();
        swit.setTextOff("백색");
        swit.setTextOn("흑색");
        swit.setSwitchMinWidth(500);

        swit.getThumbDrawable();


        popDialog.setTitle("배경색 반전을 설정하세요");
        baseLayout.setOrientation(LinearLayout.HORIZONTAL);
        baseLayout.addView(text1);
        baseLayout.addView(swit);
        baseLayout.addView(text2);

        popDialog.setView(baseLayout);


        swit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    textv.setTextColor(Color.WHITE);
                    scroll.setBackgroundColor(Color.BLACK);
                    chekcolor = 1;

                } else if (b == false) {
                    textv.setTextColor(Color.BLACK);
                    scroll.setBackgroundColor(Color.WHITE);
                    chekcolor = 0;
                }

            }
        });


        popDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        popDialog.create();
        popDialog.show();
    }
}
