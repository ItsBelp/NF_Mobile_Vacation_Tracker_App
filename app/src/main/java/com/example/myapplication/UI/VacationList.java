package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacations;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

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
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        repository = new VacationRepository(getApplication());
        List<Vacations> allVacations = repository.getmAllVacations();
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
            Vacations vacation = new Vacations(0, "Italy", "EuroHotel", "04/22/2027", "04/27/2027");
            repository.insert(vacation);
            vacation = new Vacations(0,"Britain", "The Yorkshire", "06/02/2026", "06/08/2026");
            repository.insert(vacation);
            Excursion excursion = new Excursion(0, "Venice Boat Tour", "04/25/2027", 1);
            repository.insert(excursion);
            excursion = new Excursion(0, "Double Decker Bus Tour", "06/03/2026", 2);
            repository.insert(excursion);
            return true;
        }
        if (item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        return true;
    }
}