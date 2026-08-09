package com.example.myapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.dao.ExcursionDAO;
import com.example.myapplication.dao.VacationDAO;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacations;

// Creates an asynchronous database for Vacations

@Database(entities = {Vacations.class, Excursion.class}, version = 1, exportSchema = false)
public abstract class VacationDatabaseBuilder extends RoomDatabase {
    public abstract ExcursionDAO excursionDAO();
    public abstract VacationDAO vacationDAO();
    private static volatile VacationDatabaseBuilder INSTANCE;

    static VacationDatabaseBuilder getDatabase(final Context context){
        if (INSTANCE==null){
            synchronized (VacationDatabaseBuilder.class){
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VacationDatabaseBuilder.class, "VacationDatabase.db")
                            // .fallbackToDestructiveMigration() <-- Has been deprecated
                            // .allowMainThreadQueries() <-- Makes database synchronous
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
