package com.example.myapplication;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacations;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    // Test Vacation values cannot be null-------------------------------------------------------------
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


    // Test Excursion values cannot be null------------------------------------------------------------
    @Test
    public void excursionObjectNotNull() {
        Excursion excursion = new Excursion(1, "Venice Boat Tour", "04/25/27", 1);
        assertNotNull(excursion);
    }

    @Test
    public void excursionTitleNotNull() {
        Excursion excursion = new Excursion(1, "Venice Boat Tour", "04/25/27", 1);
        assertNotNull(excursion.getExcTitle());
    }

    @Test
    public void excursionDateNotNull() {
        Excursion excursion = new Excursion(1, "Venice Boat Tour", "04/25/27", 1);
        assertNotNull(excursion.getExcDate());
    }

    // Test Unique ID checks---------------------------------------------------------------------------
    @Test
    public void vacationsDiffID() {
        Vacations vacation1 = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        Vacations vacation2 = new Vacations(2, "Britain", "The Yorkshire", "06/02/27", "06/08/27");
        assertNotEquals(vacation1.getVacationID(), vacation2.getVacationID());
    }

    @Test
    public void excursionsDiffID() {
        Excursion excursion1 = new Excursion(1, "Venice Boat Tour", "04/25/27", 1);
        Excursion excursion2 = new Excursion(2, "Double Decker Bus Tour", "06/03/27", 2);
        assertNotEquals(excursion1.getExcID(), excursion2.getExcID());
    }
}