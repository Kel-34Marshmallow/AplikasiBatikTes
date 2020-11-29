package com.example.aplikasibatiktes.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.aplikasibatiktes.R;
import com.example.aplikasibatiktes.adapters.BatikAdapter;
import com.example.aplikasibatiktes.models.Batik;
import com.example.aplikasibatiktes.roomdatabase.BatikViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {


    TextView infoSearch;
    RecyclerView searchRecycler;
    SearchView searchButton;

    List<Batik> dataBatik;
    BatikAdapter searchAdapter;

    BatikViewModel mBatikViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        infoSearch = findViewById(R.id.label_info_search);
        searchRecycler = findViewById(R.id.searchRecyclerView);
        searchButton = findViewById(R.id.kolomcari);

        dataBatik = new ArrayList<>();

        searchAdapter = new BatikAdapter(this, dataBatik);
        searchRecycler.setAdapter(searchAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));

        mBatikViewModel = new ViewModelProvider(this).get(BatikViewModel.class);


        String mainInput = getIntent().getStringExtra("query_search");


        searchButton.setSubmitButtonEnabled(true);
        searchButton.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearchBatik(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearchBatik(newText);
                return false;
            }

            private void doSearchBatik(String inputData) {

                mBatikViewModel.searchBatik(inputData).observe(SearchActivity.this, new Observer<List<Batik>>() {
                    @Override
                    public void onChanged(List<Batik> batiks) {
                        dataBatik = batiks;
                        if (dataBatik.size() > 0) {
                            searchAdapter.setBatikList(dataBatik);
                            infoSearch.setText("Menampilkan hasil pencarian " + inputData);
                        } else {
                            searchAdapter.setBatikList(dataBatik);
                            infoSearch.setText("Tidak menemukan hasil pencarian " + inputData);
                        }
                    }
                });
            }
        });

        searchButton.setQuery(mainInput, true);


    }
}

    /*public void searchBatik(View view) {

        *//*ProgressDialog dialog = utils.showProgressDialog(this, "Loading...");
        dialog.show();*//*

        //Sembunyikan keyboard setelah input
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputManager != null ) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }


        String inputData = searchInput.getText().toString();



        mBatikViewModel.searchBatik(inputData).observe(this, new Observer<List<Batik>>() {
            @Override
            public void onChanged(List<Batik> batiks) {
                dataBatik = batiks;
                if(dataBatik.size() > 0) {
                    searchAdapter.setBatikList(dataBatik);
                    infoSearch.setText("Menampilkan hasil pencarian " + inputData);
                } else {
                    searchAdapter.setBatikList(dataBatik);
                    infoSearch.setText("Tidak menemukan hasil pencarian " + inputData);
                }
            }
        });










        *//*AndroidNetworking.get("https://batikita.herokuapp.com/index.php/batik/" + inputData)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if (response != null) {
                                JSONArray jsonArray = response.getJSONArray("hasil");
                                if (jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        BatikSearch item = new BatikSearch(
                                                data.getInt("id"),
                                                data.getString("nama_batik"),
                                                data.getString("daerah_batik"),
                                                data.getString("makna_batik"),
                                                data.getInt("harga_rendah"),
                                                data.getInt("harga_tinggi"),
                                                data.getInt("hitung_view"),
                                                data.getString("link_batik"));
                                        hasilCariBatik.add(item);

                                        infoSearch.setVisibility(View.VISIBLE);
                                        infoSearch.setText("Menampilkan hasil pencarian " + inputData);

                                        setSearchBatik();
                                    }

                                } else {
                                    infoSearch.setVisibility(View.VISIBLE);
                                    infoSearch.setText("Tidak menemukan hasil pencarian " + inputData);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal di Load!", Toast.LENGTH_LONG).show();
                            }
                            dialog.dismiss();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        infoSearch.setVisibility(View.VISIBLE);
                        infoSearch.setText("Tidak menemukan hasil pencarian " + inputData);
                        if (inputData.equals("")) {
                            Toast.makeText(getApplicationContext(), "Mohon isi kolom pencarian", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });*//*

    }*/



    /*public void setSearchBatik() {
        searchAdapter = new BatikSearchAdapter( dataBatik, this);
        searchRecycler.setAdapter(searchAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter.notifyDataSetChanged();
    }*/

