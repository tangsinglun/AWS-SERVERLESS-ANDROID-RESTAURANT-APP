package website.programming.androideatitserver;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import website.programming.androideatitserver.Common.Common;
import website.programming.androideatitserver.Common.DirectionJSONParser;
import website.programming.androideatitserver.Remote.IGeoCoordinates;

public class TrackingOrder extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private GoogleMap mMap;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST=1000;
    private final static int LOCATION_PERMISSIOIN_REQUEST=1001;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private static int UPDATE_INTERVAL=1000;
    private static int FASTEST_INTERVAL=5000;
    private static int DISPLACEMENT=10;

    private IGeoCoordinates mService;

    private Marker mMapMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);

        mService = Common.getGeoCodeService();

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestRuntimePermission();
        }
        else {
            if(checkPlayServices())
            {
                buildGoogleApiClient();
                createLocationRequest();
            }
        }
        Log.i("OnCreate", "Display Location");
        displayLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void displayLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestRuntimePermission();
        }
        else {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null)
            {


                if(mMapMarker != null)
                    mMapMarker.remove(); //Remove already marker
                Log.i("Display Location:" ,"Has Last Location");
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.delivery);
                bitmap = Common.scaleBitmap(bitmap,70,70);
                //Add Marker in your location and move the camera
                LatLng yourLocation = new LatLng(latitude,longitude);

                bitmap = Common.scaleBitmap(bitmap,70,70);
                mMapMarker = mMap.addMarker(new MarkerOptions().position(yourLocation).title(Common.currentUser.getPhone())
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)));

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),17.0f));

                //After add Marker for your location, Add Marker for this Order and draw route
                drawRoute(yourLocation,Common.currentRequest.getAddress());

            }
            else {
                Toast.makeText(this,"Could not get Location",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void drawRoute(final LatLng yourLocation, String address) {
        mService.getGeoCode(address).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.i("Route",jsonObject.toString());
                    String lat = ((JSONArray)jsonObject.get("results"))
                            .getJSONObject(0)
                            .getJSONObject(("geometry"))
                            .getJSONObject("location")
                            .get("lat").toString();

                    String lng = ((JSONArray)jsonObject.get("results"))
                            .getJSONObject(0)
                            .getJSONObject(("geometry"))
                            .getJSONObject("location")
                            .get("lng").toString();

                    LatLng orderLocation = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.person);
                    bitmap = Common.scaleBitmap(bitmap,70,70);

                    MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .title("Order of " + Common.currentRequest.getPhone())
                            .position(orderLocation);
                    mMap.addMarker(marker);
                    //mService.getDirections(yourLocation.latitude+","+yourLocation.longitude,
                    //        orderLocation.latitude + "," + orderLocation.longitude)

                    //String requestApi= "https://maps.googleapis.com/maps/api/directions/json?mode=driving&transit_routing_preference=less_driving&origin=22.3450167,114.1933017&destination=Chuk+Un,+Hong+Kong&key="+getResources().getString(R.string.google_maps_key);
                    //Draw Route
                    mService.getDirections(yourLocation.latitude+","+yourLocation.longitude,
                            orderLocation.latitude + "," + orderLocation.longitude)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    new ParseTask().execute(response.body().toString());
                                    Log.i("Get Direction : ", response.body().toString());
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.i("Get Direction : ", "Failure: " + call.toString() );
                                }
                            });



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();


    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                Toast.makeText(this,"This Device is not Supported!",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void requestRuntimePermission() {
        ActivityCompat.requestPermissions(this,new String[]
                {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },LOCATION_PERMISSIOIN_REQUEST);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case LOCATION_PERMISSIOIN_REQUEST:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(checkPlayServices())
                    {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
                break;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
            if (mLastLocation == null) {
                Log.i("OnConnected ", "Start Location Updates");
                //displayLocation();
                startLocationUpdates();
            }
    }

    private void startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    private class ParseTask extends AsyncTask<String,Integer,List<List<HashMap<String,String>>>> {

        ProgressDialog mDialog = new ProgressDialog(TrackingOrder.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            mDialog.setMessage("Please wait.....");
            mDialog.show();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes=null;
            try{
                jObject = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();
                routes = parser.parse(jObject);
                Log.i("Do It BackGround ","doing");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;

        }

        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            mDialog.dismiss();

            ArrayList points = null;
            PolylineOptions lineOptions= null;

            for(int i=0;i<lists.size();i++)
            {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = lists.get(i);

                for(int j=0;j<path.size();j++) {

                    Log.i("onPostExecute : ", String.valueOf(lists.size()));
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                    //Log.i("Points", String.valueOf(position.latitude) + "," + String.valueOf(position.longitude));

                    lineOptions.addAll(points);
                    lineOptions.width(12);
                    lineOptions.color(Color.BLUE);
            }

            if (lists.size()>0)
                  mMap.addPolyline(lineOptions);

        }


}
}
