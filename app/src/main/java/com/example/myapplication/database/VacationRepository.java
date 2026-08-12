package com.example.myapplication.database;

import android.app.Application;

import com.example.myapplication.dao.ExcursionDAO;
import com.example.myapplication.dao.VacationDAO;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacations;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationRepository {

    Vacations currentVacation;
    private VacationDAO mVacationDAO;
    private ExcursionDAO mExcursionDAO; //the "m" before the variable name indicates it is an instance variable.

    private List<Excursion> mAllExcursions;
    private List<Vacations> mAllVacations;

    private static int NUM_OF_THREADS=4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);

    public VacationRepository(Application application){
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mVacationDAO = db.vacationDAO();
        mExcursionDAO = db.excursionDAO();
    }

    public List<Vacations>getmAllVacations(){
        databaseExecutor.execute(()-> {
            mAllVacations = mVacationDAO.getAllVacations();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllVacations;
    }

    public List<Vacations>searchVacations(String query) {
        databaseExecutor.execute(()-> {
            mAllVacations = mVacationDAO.searchVacations(query);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllVacations;
    }

    public Vacations getVacationByID(int id) {
        databaseExecutor.execute(() -> {
            currentVacation = mVacationDAO.getVacationByID(id);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return currentVacation;
    }

    public void insert(Vacations vacation){
        databaseExecutor.execute(()-> {
            mVacationDAO.insert(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void update(Vacations vacation) {
        databaseExecutor.execute(()-> {
            mVacationDAO.update(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void delete(Vacations vacation) {
        databaseExecutor.execute(()-> {
            mVacationDAO.delete(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Excursion>getmAllExcursions() {
        databaseExecutor.execute(()-> {
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllExcursions;
    }

    public List<Excursion>getAssocExcs(int vacationID){
        databaseExecutor.execute(()-> {
            mAllExcursions = mExcursionDAO.getAssocExcs(vacationID);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mAllExcursions;
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(()-> {
            mExcursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(()-> {
            mExcursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(()-> {
            mExcursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
