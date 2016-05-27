package csie.yuntech.edu.tw.yangyang_eat_fanfan;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by a on 2015/12/18.
 */
public class yangyang_googlemap_code extends Activity  implements LocationListener {
    private static final String MAP_URL = "file:///android_asset/ooa_googleMap.html";
    private WebView webView;
    private boolean webviewReady = false;
    private Button location_button;
    private EditText address_edittext;
    private Location mostRecentLocation;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yangyang_googlemap_layout);
        location_button = (Button) findViewById(R.id.location_button);
        address_edittext = (EditText) findViewById(R.id.address_editText);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Bundle bundle = this.getIntent().getExtras();
        final String restaurant_name  = bundle.getString("restaurant_name");
        address_edittext.setText(address_edittext.getText().toString() + restaurant_name);
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webviewReady) {
                    // webView.loadUrl("javascript:codeAddress('" + address_edittext.getText().toString() + "')");
                    webView.loadUrl("javascript:calcRoute('" + address_edittext.getText().toString() + "')");
                }
            }
        });
        getLocation();
        setupWebView();//設定webview
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /** Sets up the WebView object and loads the URL of the page **/
    private void setupWebView(){

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        //Wait for the page to load then send the location information
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webviewReady = true;//webview已經載入完畢
            }

        });
        webView.loadUrl(MAP_URL);
        webView.addJavascriptInterface(new JavaScriptInterface(), "android");
    }
    /** Sets up the interface for getting access to Latitude and Longitude data from device **/
    private class JavaScriptInterface {
        @JavascriptInterface
        public double getLatitude(){
            return mostRecentLocation.getLatitude();
        }
        @JavascriptInterface
        public double getLongitude(){
            return mostRecentLocation.getLongitude();
        }

    }
    /** The Location Manager manages location providers. This code searches
     for the best provider of data (GPS, WiFi/cell phone tower lookup,
     some other mechanism) and finds the last known location.
     **/
    private void getLocation() {
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 1, 0, this);
        mostRecentLocation = locationManager.getLastKnownLocation(provider);
        /*
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria,true);

        //In order to make sure the device is getting location, request updates.
        locationManager.requestLocationUpdates(provider, 1, 0, this);
        mostRecentLocation = locationManager.getLastKnownLocation(provider);
        */
    }

    /** Sets the mostRecentLocation object to the current location of the device **/
    @Override
    public void onLocationChanged(Location location) {
        mostRecentLocation = location;
    }

    /** The following methods are only necessary because WebMapActivity implements LocationListener **/
    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
