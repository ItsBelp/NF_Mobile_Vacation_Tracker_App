package com.example.myapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.entities.Vacations;

import java.util.List;

@Dao
public interface VacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacations vacations);

    @Update
    void update(Vacations vacations);

    @Delete
    void delete(Vacations vacations);

    @Query("SELECT * FROM vacations ORDER BY vacationID ASC")
    List<Vacations> getAllVacations();

    @Query("SELECT * FROM vacations ORDER BY vacationTitle ASC")
    List<Vacations> getVacationsAsc();

    @Query("SELECT * FROM vacations ORDER BY vacationTitle DESC")
    List<Vacations> getVacationsDesc();

    @Query("SELECT * FROM vacations WHERE vacationID = :id")
    Vacations getVacationByID(int id);

    @Query("SELECT * FROM vacations WHERE vacationTitle LIKE '%' || :query || '%' " +
            "OR vacationHotel LIKE '%' || :query || '%' ")
    List<Vacations> searchVacations(String query);
}
