package com.example.myapplication.UI;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.VacationRepository;
import com.example.myapplication.entities.Vacations;

import java.util.List;

public class VacationSearch extends AppCompatActivity {

    private VacationRepository repository;

    VacationAdapter vacationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_search);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchQueryMethod(query);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish()); // create new Listener for custom menu button

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new VacationRepository(getApplication());
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intentSearch = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intentSearch.getStringExtra(SearchManager.QUERY);
            searchQueryMethod(query);
        }
    }

    private void searchQueryMethod(String query) {
        List<Vacations> results = repository.searchVacations(query);
        if (results.isEmpty()) {
            Toast.makeText(this, "No Vacations Found For: " + query, Toast.LENGTH_SHORT).show();
        }
        vacationAdapter.setVacations(results);
    }
}