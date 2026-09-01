package com.example.myapplication.UI;

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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.VacationRepository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacations;
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
//        if (item.getItemId() == R.id.sample){ //Hard insert sample data to test repository
//            repository = new VacationRepository(getApplication());
//            Vacations vacation = new Vacations(0, "Italy", "EuroHotel", "04/22/2027", "04/27/2027");
//            repository.insert(vacation);
//            vacation = new Vacations(0,"Britain", "The Yorkshire", "06/02/2026", "06/08/2026");
//            repository.insert(vacation);
//            Excursion excursion = new Excursion(0, "Venice Boat Tour", "04/25/2027", 1);
//            repository.insert(excursion);
//            excursion = new Excursion(0, "Double Decker Bus Tour", "06/03/2026", 2);
//            repository.insert(excursion);
//            return true;
//        }
        if (item.getItemId() == R.id.reportCSV) {
            generateReport();
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