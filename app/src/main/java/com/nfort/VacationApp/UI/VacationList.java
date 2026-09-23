package com.nfort.VacationApp.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nfort.VacationApp.R;
import com.nfort.VacationApp.database.VacationRepository;
import com.nfort.VacationApp.entities.Excursion;
import com.nfort.VacationApp.entities.Vacations;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.search.SearchBar;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class VacationList extends AppCompatActivity {
    private VacationRepository repository;
    VacationAdapter vacationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FloatingActionButton fab = findViewById(R.id.floatingActionButton); // Create new Vacation
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });
        ImageButton btnOptions = findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(view -> {
            PopupMenu popupOptions = new PopupMenu(this, view);
            popupOptions.getMenuInflater().inflate(R.menu.menu_vacation_list, popupOptions.getMenu());
            popupOptions.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
            popupOptions.show();
        });

        SearchBar searchBar = findViewById(R.id.searchBar); // Open search activity to search through vacations
        searchBar.setFocusable(false);
        searchBar.setFocusableInTouchMode(false);
        searchBar.setClickable(true);
        searchBar.setOnClickListener(view -> {
            Intent intent = new Intent(VacationList.this, VacationSearch.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new VacationRepository(getApplication());
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override  // Make an onResume to refresh recycler view list when you return to the Vacation List activity
    protected void onResume() {
        super.onResume();
        vacationAdapter.setVacations(repository.getmAllVacations());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.sample){ //Hard insert sample data to test repository
            repository = new VacationRepository(getApplication());
            if (repository.getmAllVacations().isEmpty()) {
                AlertDialog loadingNotif = new AlertDialog.Builder(this)
                        .setTitle("Loading Sample Data...")
                        .setView(R.layout.loading_dialogue)
                        .setCancelable(false)
                        .create();
                loadingNotif.show();

                new Thread(() -> {
                    Vacations vacation1 = new Vacations(0, "Italy", "EuroHotel", "04/22/2027", "04/27/2027");
                    Vacations vacation2 = new Vacations(0,"Britain", "The Yorkshire", "06/02/2026", "06/08/2026");
                    Vacations vacation3 = new Vacations(0, "France", "Le Demeure Montaigne", "07/10/27", "07/18/27");
                    Vacations vacation4 = new Vacations(0, "Australia", "Batman's Hill on Collins", "08/01/27", "08/15/27");
                    Vacations vacation5 = new Vacations(0, "Japan", "Sakura Cross Hotel Kyoto", "09/07/27", "09/19/27");
                    Vacations vacation6 = new Vacations(0, "Malaysia", "The St. Regis Langkawi", "11/14/27", "11/22/27");
                    repository.insert(vacation1);
                    repository.insert(vacation2);
                    repository.insert(vacation3);
                    repository.insert(vacation4);
                    repository.insert(vacation5);
                    repository.insert(vacation6);

                    List<Vacations> insertedVacation = repository.getmAllVacations();
                    int italyID = insertedVacation.get(0).getVacationID();
                    int britainID = insertedVacation.get(1).getVacationID();
                    int franceID = insertedVacation.get(2).getVacationID();
                    int australiaID = insertedVacation.get(3).getVacationID();
                    int japanID = insertedVacation.get(4).getVacationID();
                    int malaysiaID = insertedVacation.get(5).getVacationID();

                    Excursion excursion1 = new Excursion(0, "Venice Boat Tour", "04/25/2027", italyID);
                    Excursion excursion2 = new Excursion(0, "Double Decker Bus Tour", "06/03/2026", britainID);
                    Excursion excursion3 = new Excursion(0, "Cheese Tasting", "07/12/27", franceID);
                    Excursion excursion4 = new Excursion(0, "Great Barrier Reef Dive", "08/05/27", australiaID);
                    Excursion excursion5 = new Excursion(0, "Kimono Tea Ceremony", "09/07/27", japanID);
                    Excursion excursion6 = new Excursion(0, "Langkawi Sky Bridge", "11/16/27", malaysiaID);
                    repository.insert(excursion1);
                    repository.insert(excursion2);
                    repository.insert(excursion3);
                    repository.insert(excursion4);
                    repository.insert(excursion5);
                    repository.insert(excursion6);

                    runOnUiThread(() -> {
                        vacationAdapter.setVacations(repository.getmAllVacations());
                        loadingNotif.dismiss();
                        Toast.makeText(this, "Sample data added successfully!", Toast.LENGTH_SHORT).show();
                    });
                }).start();
            } else {
                Toast.makeText(this, "This data already exists", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (item.getItemId() == R.id.reportCSV) {
            generateReport();
            return true;
        }
        if (item.getItemId() == R.id.sortASC) {
            Toast.makeText(this, "Sorting Vacations in ASC order", Toast.LENGTH_SHORT).show();
            vacationAdapter.setVacations(repository.getVacationsAsc());
            return true;
        }
        if (item.getItemId() == R.id.sortDESC) {
            Toast.makeText(this, "Sorting Vacations in DESC order", Toast.LENGTH_SHORT).show();
            vacationAdapter.setVacations(repository.getVacationsDesc());
            return true;
        }
        if (item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateReport() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler mainHandler = new Handler(Looper.getMainLooper());
        repository = new VacationRepository(getApplication());
        executorService.execute(() -> {
            List<Vacations> allVacations = repository.getmAllVacations();
            File reportCSV = VacationReportGenerator.generateVacationReport(this, allVacations, repository);
            mainHandler.post(() -> {
                if (reportCSV != null && reportCSV.exists() && reportCSV.length() > 0) {
                    Toast.makeText(this, "Report Generated: " + reportCSV.getName(), Toast.LENGTH_SHORT).show();
                    shareReport(reportCSV);
                } else {
                    Toast.makeText(this, "Failed to generate report", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void shareReport(File reportFile) {
        Uri fileUri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider",
                reportFile
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/csv");
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Export Vacation Report"));
    }
}