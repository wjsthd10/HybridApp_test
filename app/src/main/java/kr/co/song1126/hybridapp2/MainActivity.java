package kr.co.song1126.hybridapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    WebView wv;
    TextView tv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv=findViewById(R.id.wv);
        tv=findViewById(R.id.tv);
        et=findViewById(R.id.et);

//        웹뷰의 기본설정
        wv.setWebViewClient(new WebViewClient());
        wv.setWebChromeClient(new WebChromeClient());// 웹의 다이얼로그 허용

        WebSettings settings=wv.getSettings();

        settings.setJavaScriptEnabled(true);

//        혹시 JS에서 ajax를 사용하고자 한다면....
//        http: 주소에서만 가능함, 만약 file://로 되는 곳에서 사용하려면
        settings.setAllowUniversalAccessFromFileURLs(true);// 거의 확정적으로 작성함

        // 웹뷰에서 보여주는 javascript와 연결할 객체 추가(파라미터로 객체 생성해서 보내기, 객체의 별명지정하기[ JS에서 이 객체를 식별하는 이름 ])
        wv.addJavascriptInterface(new WebViewConnector(), "Droid");

//        웹뷰가 보여줄 웹문서 로딩
        wv.loadUrl("file:///android_asset/index.html");

    }


    public void clickSend(View view) {
        // WebView가 보여주는 index.html안에 있는
        // 특정 함수( 프로그래머가 만든 setReceivedMessage() )를 호출해서 값 전달
        // JAVA에서는 index.html안에 있는 요소들을 직접 제어하는 것은 불가능하다....html안에 있는 함수만 호출

        String msg=et.getText().toString();
        wv.loadUrl("javascript:setReceivedMessage('"+msg+"')");
        et.setText("");

    }

//    inner  Cless
//    Javascript와 통신을 담당할 연결자 class정의
    class WebViewConnector{

        // javascript에서 호출할 메소드는 반드시 특별한 어노테이션을 지정해야 한다.
        @JavascriptInterface    // 이게 지정된 메소드만 javascript와 통신이 가능하다.
        public void setTextView( String msg ){
            tv.setText(msg);
        }

        //갤러리앱 실행하는 작업 메소드
        @JavascriptInterface
        public void openGallery(){
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivity(intent);
        }
    }

}//Main class


