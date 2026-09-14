package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.entities.Vacations;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    // Test Vacation values cannot be null
    @Test
    public void vacationObjectNotNull() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        assertNotNull(vacation);
    }

    @Test
    public void vacationTitleNotNull() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        assertNotNull(vacation.getVacationTitle());
    }

    @Test
    public void vacationHotelNotNull() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        assertNotNull(vacation.getVacationHotel());
    }

    @Test
    public void vacationStartDateNotNull() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        assertNotNull(vacation.getStartDate());
    }

    @Test
    public void vacationEndDateNotNull() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        assertNotNull(vacation.getEndDate());
    }

    @Test
    public void vacationIDNotNull() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        assertNotNull(vacation.getVacationID());
    }

}