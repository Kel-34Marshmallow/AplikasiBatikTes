package com.example.aplikasibatiktes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.aplikasibatiktes.models.Batik;
import com.example.aplikasibatiktes.models.BatikSlide;
import com.example.aplikasibatiktes.R;
import com.example.aplikasibatiktes.adapters.BatikAdapter;
import com.example.aplikasibatiktes.adapters.BatikSliderAdapter;
import com.example.aplikasibatiktes.roomdatabase.BatikViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //DataBatik
    List<Batik> dataBatik;
    List<BatikSlide> dataBatikPopular;

    //recyclerView
    BatikAdapter batikAdapter;
    RecyclerView idRecyclerView;

    //Slider layout
    SliderView sliderView;

    // Refresh layout
    TextView labelNoInternet;
    Button refreshButton;

    //Search layout
    EditText searchInput;
    Button searchButton;
    SearchView searchView;


    BatikViewModel mBatikViewModel;

    BatikSliderAdapter SlideAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idRecyclerView = findViewById(R.id.idRecyclerView);
        sliderView = findViewById(R.id.imageSlider);
        labelNoInternet = findViewById(R.id.label_no_internet);
        refreshButton = findViewById(R.id.refresh_button);
        searchView = findViewById(R.id.kolomcari);


        AndroidNetworking.initialize(getApplicationContext());
        dataBatik = new ArrayList<>();
        dataBatikPopular = new ArrayList<>();



        //RecycleView Batik
        batikAdapter = new BatikAdapter(this, dataBatik);
        idRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        idRecyclerView.setAdapter(batikAdapter);

        //Slider Batik
        SlideAdapter = new BatikSliderAdapter(this, dataBatikPopular);
        sliderView.setSliderAdapter(SlideAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();


        //ambilData();
        mBatikViewModel = new ViewModelProvider(this).get(BatikViewModel.class);


        getAllData();
        mBatikViewModel.getAllBatik().observe(this, new Observer<List<Batik>>() {
            @Override
            public void onChanged(List<Batik> batiks) {

                dataBatik = batiks;
                batikAdapter.setBatikList(dataBatik);


            }
        });

        getAllPopularBatik();
        mBatikViewModel.getAllBatikPopular().observe(this, new Observer<List<BatikSlide>>() {
            @Override
            public void onChanged(List<BatikSlide> batikSlides) {

                dataBatikPopular = batikSlides;
                SlideAdapter.setBatikList(dataBatikPopular);

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class)
                        .putExtra("query_search", query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });




    }


    private void getAllData() {

        /*ProgressDialog dialog = utils.showProgressDialog(MainActivity.this, "Loading...");
        dialog.show();*/




        Toast.makeText(MainActivity.this, "Sedang Memuat ...", Toast.LENGTH_SHORT).show();

        AndroidNetworking.get("https://batikita.herokuapp.com/index.php/batik/all")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response != null) {
                                JSONArray jsonArray = response.getJSONArray("hasil");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    Batik item = new Batik(
                                            data.getInt("id"),
                                            data.getString("nama_batik"),
                                            data.getString("daerah_batik"),
                                            data.getString("makna_batik"),
                                            data.getInt("harga_rendah"),
                                            data.getInt("harga_tinggi"),
                                            data.getInt("hitung_view"),
                                            data.getString("link_batik"));
                                    dataBatik.add(item);
                                }

                                mBatikViewModel.insert(dataBatik);

                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal di Load!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("JSON Parser", "Error parsing data " + e.toString());

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MainActivity.this, "No Internet Connection ...", Toast.LENGTH_SHORT).show();
                        if (dataBatik.isEmpty()) {
                            displayRefresh();
                        }
                    }
                });

    }

    private void getAllPopularBatik() {
        AndroidNetworking.get("https://batikita.herokuapp.com/index.php/batik/popular")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response != null) {
                                JSONArray jsonArray = response.getJSONArray("hasil");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    BatikSlide item = new BatikSlide(
                                            data.getInt("id"),
                                            data.getString("nama_batik"),
                                            data.getString("link_batik"));
                                    dataBatikPopular.add(item);
                                }

                                mBatikViewModel.insertPopular(dataBatikPopular);


                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal di Load!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("JSON Parser", "Error parsing data " + e.toString());

                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    public void refresh(View view) {
        hideRefresh();

        getAllData();
        getAllPopularBatik();
    }

    public void hideRefresh() {
        labelNoInternet.setVisibility(View.GONE);
        refreshButton.setVisibility(View.GONE);
    }

    public void displayRefresh() {
        labelNoInternet.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
    }




    /*public void setAllBatik() {
        myAdapter = new MyAdapter(this);
        idRecyclerView.setAdapter(myAdapter);
        idRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter.notifyDataSetChanged();
    }*/

    /*public void setBatikSlide() {
        sliderView.setSliderAdapter(new SliderAdapterExample(this, dataBatikPopular));
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.startAutoCycle();
    }*/


}
