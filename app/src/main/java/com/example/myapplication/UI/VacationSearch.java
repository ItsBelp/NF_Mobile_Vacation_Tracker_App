package com.example.myapplication.UI;

import android.app.SearchManager;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.VacationRepository;
import com.example.myapplication.entities.Vacations;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.w3c.dom.Text;

import java.util.List;

public class VacationSearch extends AppCompatActivity {

    private VacationRepository repository;
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    VacationAdapter vacationAdapter;
    ShimmerFrameLayout shimmerContainer;

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

        shimmerContainer = findViewById(R.id.shimmerContainer);
        shimmerContainer.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new VacationRepository(getApplication());
        vacationAdapter = new VacationAdapter(this);
        ImageView magNotFound = findViewById(R.id.magNotFound);
        TextView searchNotFound = findViewById(R.id.searchNotFound);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchBar = findViewById(R.id.searchBar);
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHandler.removeCallbacks(searchRunnable);
                showShimmer();
                magNotFound.setVisibility(View.GONE);
                searchNotFound.setVisibility(View.GONE);
                searchQueryMethod(query);
                searchBar.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                magNotFound.setVisibility(View.GONE);
                searchNotFound.setVisibility(View.GONE);
                showShimmer();
                searchRunnable = () -> searchQueryMethod(newText);
                searchHandler.postDelayed(searchRunnable, 300);
                return true;
            }
        });

        searchBar.requestFocus();
    }

    private void searchQueryMethod(String query) {
        new Thread(() -> {
            List<Vacations> results = repository.searchVacations(query);
            ImageView magNotFound = findViewById(R.id.magNotFound);
            TextView searchNotFound = findViewById(R.id.searchNotFound);
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            runOnUiThread(() -> {
                if (results.isEmpty()) {
                    magNotFound.setVisibility(View.VISIBLE);
                    searchNotFound.setVisibility(View.VISIBLE);
                    shimmerContainer.stopShimmer();
                    shimmerContainer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    vacationAdapter.setVacations(results);
                    hideShimmer();
                }
            });
        }).start();
    }

    private void showShimmer() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView != null && shimmerContainer != null) {
            recyclerView.setVisibility(View.GONE);
            shimmerContainer.setVisibility(View.VISIBLE);
            shimmerContainer.startShimmer();
        }
    }

    private void hideShimmer() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        if (shimmerContainer != null && recyclerView != null) {
            shimmerContainer.stopShimmer();
            shimmerContainer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

}