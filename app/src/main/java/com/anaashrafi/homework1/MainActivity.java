package com.anaashrafi.homework1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView showCoordinates;
    MapView mapView1;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView1 = findViewById(R.id.mapView);
        mapView1.onCreate(mapViewBundle);
        mapView1.getMapAsync(this);
    }

    public void searchCoordinates (View view) {
        // Get the text view.
        showCoordinates = (TextView)
                findViewById(R.id.textView);
        RequestQueue locationQueue = Volley.newRequestQueue(this);
        TextView add = (TextView) findViewById(R.id.editText);

        // get address
        String address = add.getText().toString();
        //showCoordinates.setText("hello");
        //System.out.println(address);
        String apiKey = "AIzaSyAHlcjxuGjqJWSW9hLurtYwXycVYwShNJc";
        //String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=" + apiKey;
        //System.out.println("hello ana");
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //showCoordinates.setText("hello");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            JSONObject results = jsonArray.getJSONObject(0);
                            JSONObject geometry = results.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            double lat = location.getDouble("lat");
                            double lng = location.getDouble("lng");
                            System.out.println("hello");
                            System.out.println(lat + ", " + lng);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        locationQueue.add(request);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}

    //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
