package tienndph30518.goodmaps;



import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




import android.Manifest;

import java.io.IOException;
import java.util.List;

import tienndph30518.goodmaps.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private EditText etSearchLocation;
    private Button btnSearchLocation;
    private Geocoder geocoder;

    private ActivityMapsBinding binding;
      FusedLocationProviderClient mFusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        etSearchLocation = findViewById(R.id.etSearchLocation);
        btnSearchLocation = findViewById(R.id.btnSearchLocation);

        // Khởi tạo đối tượng Geocoder
        geocoder = new Geocoder(this);

        // ...

        // Xử lý sự kiện khi người dùng nhấn nút Tìm kiếm
        btnSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationName = etSearchLocation.getText().toString().trim();
                if (!locationName.isEmpty()) {
                    try {
                        // Lấy danh sách tọa độ từ tên địa điểm tìm kiếm
                        List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address address = addresses.get(0);
                            double latitude = address.getLatitude();
                            double longitude = address.getLongitude();

                            // Thêm Marker vào Google Map
                            LatLng searchLocation = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(searchLocation).title("Địa điểm tìm kiếm"));

                            // Di chuyển Camera của Google Map để hiển thị vị trí tìm kiếm và mốc vị trí
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLocation, 15));
                        } else {
                            Toast.makeText(MapsActivity.this, "Không tìm thấy địa điểm", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "Vui lòng nhập địa điểm", Toast.LENGTH_SHORT).show();
                }
            }
        });









        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(21.0381278, 105.7442122);
        mMap.addMarker(new MarkerOptions().position(sydney).title("FPT Polytechnic Hà Nội"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng hcm = new LatLng(10.7546181, 106.3655609);
        mMap.addMarker(new MarkerOptions().position(hcm).title("Hồ Chí Minh CiTy"));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15);
                marker.showInfoWindow();
                mMap.animateCamera(cameraUpdate, 1000, null);
                return true;
            }
        });

    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            //Yêu cầu người dùng cấp quyền truy cập locction khi chưa được phép
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 7979);
        } else {
            //Thực hiện việc lấy location sau khi đã có quyền
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.checkSelfPermission(MapsActivity.this,
                                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                        }
                    });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7979) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

//


}