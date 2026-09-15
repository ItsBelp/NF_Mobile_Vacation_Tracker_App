package com.example.myapplication;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.myapplication.dao.ExcursionDAO;
import com.example.myapplication.dao.VacationDAO;
import com.example.myapplication.database.VacationDatabaseBuilder;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacations;

import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private VacationDatabaseBuilder vacationDB;
    private VacationDAO vacationDAO;
    private ExcursionDAO excursionDAO;

    @Before
    public void createDatabase() { // Create a clean database before each test
        Context context = ApplicationProvider.getApplicationContext();
        vacationDB = Room.inMemoryDatabaseBuilder(context, VacationDatabaseBuilder.class)
                .allowMainThreadQueries()
                .build();
        vacationDAO = vacationDB.vacationDAO();
        excursionDAO = vacationDB.excursionDAO();
    }

    @After
    public void closeDatabase() { // Close database after each test
        vacationDB.close();
    }

    @Test
    public void checkVacPersist() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        vacationDAO.insert(vacation);
        List<Vacations> allVacations = vacationDAO.getAllVacations();
        assertEquals("Italy", allVacations.get(1).getVacationTitle());
    }
    @Test
    public void checkExcPersist() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        vacationDAO.insert(vacation);
        List<Vacations> allVacations = vacationDAO.getAllVacations();
        int vacationID = allVacations.get(1).getVacationID();

        Excursion excursion = new Excursion(1, "Venice Boat Tour", "04/25/27", vacationID);
        excursionDAO.insert(excursion);

        List<Excursion> allExcursions = excursionDAO.getAllExcursions();
        assertFalse(allExcursions.isEmpty());
    }

    @Test
    public void checkVacUpdate() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        vacationDAO.insert(vacation);

        List<Vacations> allVacations = vacationDAO.getAllVacations();
        Vacations vacation1 = allVacations.get(1);

        Vacations vacation1Edit = new Vacations(vacation1.getVacationID(), "France", "HotelParis", "07/10/27", "07/20/27");
        vacationDAO.update(vacation1Edit);

        List<Vacations> updateVacList = vacationDAO.getAllVacations();
        assertEquals("France", updateVacList.get(1).getVacationTitle());
    }

    @Test
    public void checkExcUpdate() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        vacationDAO.insert(vacation);
        List<Vacations> allVacations = vacationDAO.getAllVacations();
        int vacationID = allVacations.get(1).getVacationID();

        Excursion excursion = new Excursion(1, "Venice Boat Tour", "04/25/27", vacationID);
        excursionDAO.insert(excursion);
        List<Excursion> allExcursions = excursionDAO.getAllExcursions();
        Excursion excursion1 = allExcursions.get(1);

        Excursion exc1Edit = new Excursion(excursion1.getExcID(), "Cheese Tasting", "07/12/27", vacationID);
        excursionDAO.update(exc1Edit);

        List<Excursion> updateExcList = excursionDAO.getAllExcursions();
        assertEquals("Cheese Tasting", updateExcList.get(1).getExcTitle());
    }

    @Test
    public void checkVacDel() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        vacationDAO.insert(vacation);

        List<Vacations> allVacations = vacationDAO.getAllVacations();
        vacationDAO.delete(allVacations.get(1));

        List<Vacations> vacDelList = vacationDAO.getAllVacations();
        assertTrue(vacDelList.isEmpty());
    }

    @Test
    public void checkExcDel() {
        Vacations vacation = new Vacations(1, "Italy", "EuroHotel", "04/22/27", "04/27/27");
        vacationDAO.insert(vacation);
        List<Vacations> allVacations = vacationDAO.getAllVacations();
        int vacationID = allVacations.get(1).getVacationID();

        Excursion excursion = new Excursion(1, "Venice Boat Tour", "04/25/27", vacationID);
        excursionDAO.insert(excursion);

        List<Excursion> allExcursions = excursionDAO.getAllExcursions();
        excursionDAO.delete(allExcursions.get(1));

        List<Excursion> excDelList = excursionDAO.getAllExcursions();
        assertTrue(excDelList.isEmpty());
    }

}
