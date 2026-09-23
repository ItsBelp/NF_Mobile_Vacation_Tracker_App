package com.nfort.VacationApp.entities;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "excursions") // Create a table for the vacation excursions
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excID;
    private String excTitle;
    private String excDate;
    private int vacationID;

    public Excursion(int excID, String excTitle, String excDate, int vacationID) {
        this.excID = excID;
        this.excTitle = excTitle;
        this.excDate = excDate;
        this.vacationID = vacationID;
    }

    public int getExcID() {
        return excID;
    }

    public void setExcID(int excID) {
        this.excID = excID;
    }

    public String getExcTitle() {
        return excTitle;
    }

    public String getExcDate() {
        return excDate;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }
}
