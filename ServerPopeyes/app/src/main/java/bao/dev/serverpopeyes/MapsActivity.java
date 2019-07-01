package bao.dev.serverpopeyes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import bao.dev.serverpopeyes.Common.Common;
import bao.dev.serverpopeyes.Remote.IGeoCoordinates;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;


    private final static int PLAY_SERVICE_RESOLUTION_REQUEST = 1000;
    private final static int LOCATION_PERMISSION_REQUEST = 1001;

    private FusedLocationProviderClient mLastLocation;

    private GoogleApiClient mGoogleApiClent;

    String link = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=";
    String key = "&key=AIzaSyBaE3VU4GTpO7jXeyDBRySvk5ioqGQcXa0";
    String keySearch = "&key=AIzaSyCB7nFyzK1rrgBHJAFu7Oa--UcU59ib6E4";
    
    String Popeyesadress  = "397A Lê Đại Hành, Phường 11, Quận 11, Hồ Chí Minh, Việt Nam";
    String UserAdress  = "268 Lý Thường Kiệt, Phường 14, Quận 10, Hồ Chí Minh, Việt Nam";
    String UserAdresss = Common.currentRequest.getAddress();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLastLocation = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        buildGoogleApiClient();


        // Vẽ đường cửa hàng tới User
        direction2Point();

    }

    // Lấy vị trí tọa độ hiện tại của cửa hàng Lê Đại Hành
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //GPS cửa hàng
        mMap = googleMap;
        addPermissionLocation();

//        LatLng popeyes = new LatLng(10.767380, 106.653290);
//
//        Bitmap bitmappopeyes = BitmapFactory.decodeResource(getResources(), R.drawable.iconpopeyes);
//        bitmappopeyes = Common.scaleBitmap(bitmappopeyes, 100, 100);
//
//        MarkerOptions popeyesmarker = new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromBitmap(bitmappopeyes))
//                .title("Popeyes Chi Nhánh Lê Đại Hành")
//                .position(popeyes);
//        mMap.addMarker(popeyesmarker);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(popeyes, 16));


        //GPS Cửa Hàng
        shopGPS();
        //GPS Khách Hàng
        userGPS();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Sử dụng nêu muốn get vị trí hiện tại

//        mLastLocation.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations, this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                            //Main2Activity.this.location = location;
//                            LatLng hcm = new LatLng(location.getLatitude(), location.getLongitude());
//                            mMap.addMarker(new MarkerOptions().position(hcm));
//                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hcm, 16f));
//                            Toast.makeText(MapsActivity.this, hcm.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }


    //Check quyền truy cập !
    private void addPermissionLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            addCurrentLocation();
        }
    }
    //Btn vị trí hiện tại !!!
    private void addCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
//                Toast.makeText(MapsActivity.this, "abc", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

        @Override
        public void onConnectionSuspended ( int i){
        //Làm gì khi mất kết nối
            Toast.makeText(this, "Mất định vị GPS", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnectionFailed (@NonNull ConnectionResult connectionResult){
        //Làm gì khi chưa bật GPS
            Toast.makeText(this, "Vui lòng bật định vị GPS", Toast.LENGTH_SHORT).show();
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                addCurrentLocation();
            }else{
                Toast.makeText(this, "Not OK", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Client googleAPi

    private void buildGoogleApiClient() {
        if (mGoogleApiClent == null) {
            mGoogleApiClent = new GoogleApiClient.Builder(MapsActivity.this)
                    .addConnectionCallbacks(MapsActivity.this)
                    .addOnConnectionFailedListener(MapsActivity.this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClent.connect();
        }
    }


    /*Xay dung duong di hai diem


     */

    private void shopGPS() {
        try {
            //Chuyển dữ liệu sang định dạng %20
            String linkSearch = link
                    + URLEncoder.encode(Popeyesadress,"UTF-8")
                    + keySearch;
//            Toast.makeText(this,linkSearch , Toast.LENGTH_SHORT).show();
            //Truyền link thông qua Request Volley
            RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(linkSearch,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = (JSONObject) response.getJSONArray("results").get(0);
                                double lat = jsonObject.getJSONObject("geometry")
                                        .getJSONObject("location")
                                        .getDouble("lat");

                                double lng = jsonObject.getJSONObject("geometry")
                                        .getJSONObject("location")
                                        .getDouble("lng");


                                Bitmap bitmappopeyes = BitmapFactory.decodeResource(getResources(), R.drawable.iconpopeyes);
                                bitmappopeyes = Common.scaleBitmap(bitmappopeyes, 100, 100);
                                LatLng shopLocation = new LatLng(lat,lng);
                                MarkerOptions popeyesmarker = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmappopeyes))
                                        .title("Popeyes Chi Nhánh Lê Đại Hành")
                                        .position(shopLocation);
                                mMap.addMarker(popeyesmarker);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(shopLocation, 16));

//                                Toast.makeText(MapsActivity.this, "I'm here", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MapsActivity.this, "searchGPS is not OK", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);


        }catch (Exception ex){

        }
    }

    private void userGPS(){
        try {
            //Chuyển dữ liệu sang định dạng %20
            String linkSearch = link
                    + URLEncoder.encode(UserAdresss,"UTF-8")
                    + keySearch;
//            Toast.makeText(this,linkSearch , Toast.LENGTH_SHORT).show();
            //Truyền link thông qua Request Volley
            RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(linkSearch,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = (JSONObject) response.getJSONArray("results").get(0);
                                double lat = jsonObject.getJSONObject("geometry")
                                        .getJSONObject("location")
                                        .getDouble("lat");

                                double lng = jsonObject.getJSONObject("geometry")
                                        .getJSONObject("location")
                                        .getDouble("lng");



                                Bitmap userBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.iconkhachhang);
                                userBitmap = Common.scaleBitmap(userBitmap, 100, 100);
                                LatLng userLocation = new LatLng(lat,lng);
                                MarkerOptions userMarker = new MarkerOptions()
                                        .icon(BitmapDescriptorFactory.fromBitmap(userBitmap))
                                        .title("Oder of +" + Common.currentRequest.getPhone())
                                        .position(userLocation);

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16));
                                mMap.addMarker(userMarker);
//                                Toast.makeText(MapsActivity.this, "I'm here", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MapsActivity.this, "searchGPS is not OK", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );

            requestQueue.add(jsonObjectRequest);


        }catch (Exception ex){

        }
    }

    private void direction2Point() {
        try {
            String linkGo = "https://maps.googleapis.com/maps/api/directions/json?origin="
                    +URLEncoder.encode(Popeyesadress,"UTF-8")
                    +"&destination="
                    +URLEncoder.encode(UserAdresss,"UTF-8")
                    +key;
            String linkSearch = linkGo;


            RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);

            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(linkSearch,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    parseJSon(response.toString());
//                                    Toast.makeText(MapsActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });

            requestQueue.add(jsonObjectRequest);

        }catch (Exception ex){

        }
    }

    private void parseJSon(String data) {
        if (data == null)
            return;


        try {
            JSONObject jsonData = new JSONObject(data);
            JSONArray jsonRoutes = jsonData.getJSONArray("routes");
            for (int i = 0; i < jsonRoutes.length(); i++) {
                JSONObject jsonRoute = jsonRoutes.getJSONObject(i);


                JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
                JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                JSONObject jsonLeg = jsonLegs.getJSONObject(0);
                JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
                JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
                JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

                List<LatLng> arrayList = decodePolyLine(overview_polylineJson.getString("points"));

                //Vẽ đường đi từ hcm -> latLng -> Đường thẳng
                mMap.addPolyline(new PolylineOptions().
                        color(Color.RED).
                        width(15).
                        addAll(arrayList));




            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}