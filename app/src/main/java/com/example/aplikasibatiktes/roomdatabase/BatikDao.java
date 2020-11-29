package com.example.aplikasibatiktes.roomdatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.aplikasibatiktes.models.Batik;
import com.example.aplikasibatiktes.models.BatikSlide;

import java.util.List;

@Dao
public interface BatikDao {

    @Query("SELECT * from batik_table ORDER BY nama_batik ASC")
    LiveData<List<Batik>> getAllBatik();

    @Query("SELECT * from batik_popular_table ORDER BY nama_batik ASC")
    LiveData<List<BatikSlide>> getAllBatikPopular();

    @Query("SELECT * from batik_table WHERE nama_batik LIKE '%' || :search || '%'")
    LiveData<List<Batik>> searchBatik(String search);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllBatik(List<Batik> batiks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllBatikPopular(List<BatikSlide> batiks);

    @Query("DELETE FROM batik_table")
    void deleteAll();

    @Query("DELETE FROM batik_popular_table")
    void deleteAllPopular();
}
