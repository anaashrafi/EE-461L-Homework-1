package com.anaashrafi.homework1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView showCoordinates;
    TextView temperature;
    TextView humidity;
    TextView windSpeed;
    TextView precipitation;
    MapView mapView1;
    private GoogleMap gmap;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private RequestQueue requestQueue;

    /*
    Create a getRequestQueue() method to return the instance of
    RequestQueue.This kind of implementation ensures that
    the variable is instatiated only once and the same
    instance is used throughout the application
    */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        return requestQueue;
    }
    /*
    public method to add the Request to the the single
    instance of RequestQueue created above.Setting a tag to every
    request helps in grouping them. Tags act as identifier
    for requests and can be used while cancelling them
    */
    public void addToRequestQueue(Request request, String tag) {
        request.setTag(tag);
        getRequestQueue().add(request);
    }

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

    public LatLng saveCoord(Double lat, Double lng){
        LatLng coord = new LatLng(lat,lng);
        return coord;
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
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey;
        //String url = "https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=" + apiKey;
        //System.out.println("hello ana");
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            //showCoordinates.setText("hello");
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("results");
//                            JSONObject results = jsonArray.getJSONObject(0);
//                            JSONObject geometry = results.getJSONObject("geometry");
//                            JSONObject location = geometry.getJSONObject("location");
//                            double lat = location.getDouble("lat");
//                            double lng = location.getDouble("lng");
//                            System.out.println("hello");
//                            System.out.println(lat + ", " + lng);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//        locationQueue.add(request);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONArray jsonArray = ((JSONObject)response).getJSONArray("results");
                            String lat = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                            String lng = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
                            showCoordinates.setText(lat + ", " + lng);
                            System.out.print(lat + lng);

                            LatLng coord = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(coord);
                            gmap.addMarker(markerOptions);

                            gmap.moveCamera(CameraUpdateFactory.newLatLng(coord));

                            getWeather(Double.valueOf(lat),Double.valueOf(lng));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Failure Callback
                        error.printStackTrace();
                        System.out.println("Error response!");
                    }
                });

        addToRequestQueue(jsonObjReq, "getRequest");
    }

    public void getWeather(Double lat, Double lng){
        temperature = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        windSpeed = (TextView) findViewById(R.id.windspeed);
        precipitation = (TextView) findViewById(R.id.precip);


        String apiKey = "72c85726b71451dd21244296351f4184";
        String url = "https://api.darksky.net/forecast/" + apiKey + "/" + lat + "," + lng;
        System.out.println(url);
        //https://api.darksky.net/forecast/72c85726b71451dd21244296351f4184/40.7143528,-74.0059731

//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            //showCoordinates.setText("hello");
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray jsonArray = jsonObject.getJSONArray("currently");
//                            JSONObject results = jsonArray.getJSONObject(0);
//                            double temp = results.getDouble("temperature");
//                            double humid = results.getDouble("humidity");
//                            double wind = results.getDouble("windSpeed");
//                            double precip = results.getDouble("precipProbability");
//                            System.out.println(temp + " " + humid + " " + wind + " " + precip);
//
//                            temperature.append(String.valueOf(temp)+"\u00B0"+"F");
//                            humidity.append(String.valueOf(humid) + "%");
//                            windSpeed.append(String.valueOf(wind) + " mph");
//                            precipitation.append(String.valueOf(precip) + "%");
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//            }
//        });

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject jsonObject = ((JSONObject)response).getJSONObject("currently");
                            double temp = jsonObject.getDouble("temperature");
                            double humid = jsonObject.getDouble("humidity");
                            double wind = jsonObject.getDouble("windSpeed");
                            double precip = jsonObject.getDouble("precipProbability");

                            System.out.println(temp +","+ humid +","+ wind + "," + precip);
                            System.out.println("Hi");
                            temperature.setText("Temperature: " + String.valueOf(temp)+"\u00B0"+"F");
                            humidity.setText("Humidity: " + String.valueOf(humid) + "%");
                            windSpeed.setText("Wind Speed: " + String.valueOf(wind) + " mph");
                            precipitation.setText("Precipitation: " + String.valueOf(precip) + "%");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Failure Callback
                        error.printStackTrace();
                        System.out.println("Error response!");
                    }
                });

        addToRequestQueue(jsonObjReq, "getRequest");
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView1.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView1.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView1.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView1.onStop();
    }
    @Override
    protected void onPause() {
        mapView1.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView1.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView1.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(40.7143528, -74.0059731);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        gmap.addMarker(markerOptions);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
    }
}

    //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
