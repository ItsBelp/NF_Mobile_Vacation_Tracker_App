package com.example.myapplication.UI;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.VacationRepository;
import com.example.myapplication.entities.Excursion;

import java.util.List;

public class ExcursionList extends AppCompatActivity {

    private VacationRepository repository;
    ExcursionAdapter excursionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new VacationRepository(getApplication());
        excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        excursionAdapter.setExcursions(repository.getmAllExcursions());

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Excursion> excursions = repository.getmAllExcursions();
        excursionAdapter.setExcursions(excursions);
    }
}