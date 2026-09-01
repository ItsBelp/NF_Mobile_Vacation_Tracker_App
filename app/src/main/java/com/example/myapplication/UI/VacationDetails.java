package com.example.myapplication.UI;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    String name;
    String hotelName;
    String dateStart;
    String dateEnd;
    int vacationID;
    EditText editName;
    EditText editHotelName;
    EditText editStartDate;
    EditText editEndDate;
    VacationRepository repository;
    ExcursionAdapter excursionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish()); // create new Listener for custom menu button
        ImageButton btnOptions = findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(view -> {
            PopupMenu popupOptions = new PopupMenu(this, view);
            popupOptions.getMenuInflater().inflate(R.menu.menu_vacationdetails, popupOptions.getMenu());
            if (vacationID == -1) {
                popupOptions.getMenu().findItem(R.id.vacationEdit).setVisible(false);
                popupOptions.getMenu().findItem(R.id.vacationDelete).setVisible(false);
            }
            popupOptions.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
            popupOptions.show();
        });

        Button btnAddExc = findViewById(R.id.btnAddExc);  // User adds excursion within related vacation
        btnAddExc.setOnClickListener(v -> {
            Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
            intent.putExtra("vacationID", vacationID);
            startActivity(intent);
        });

        excursionAdapter = new ExcursionAdapter(this);
        editName = findViewById(R.id.titleText);
        editHotelName = findViewById(R.id.hotelText);
        editStartDate = findViewById(R.id.dateStartText);
        editEndDate = findViewById(R.id.dateEndText);
        vacationID = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        hotelName = getIntent().getStringExtra("vacationHotel");
        dateStart = getIntent().getStringExtra("startDate");
        dateEnd = getIntent().getStringExtra("endDate");
        editName.setText(name);
        editHotelName.setText(hotelName);
        editStartDate.setText(dateStart);
        editEndDate.setText(dateEnd);

        if (vacationID == -1) {
            editName.setFocusable(true);       // If user is making a new vacation, allow them to input text without using Edit Vacation in Menu
            editName.setClickable(true);
            editHotelName.setFocusable(true);
            editHotelName.setClickable(true);
            editStartDate.setFocusable(true);
            editStartDate.setClickable(true);
            editEndDate.setFocusable(true);
            editEndDate.setClickable(true);
            editName.requestFocus();
        } else {
            editName.setFocusable(false);       // Prevent text fields from being edited unless user chooses Edit Vacation in option menu if vacation exists
            editName.setClickable(false);
            editHotelName.setFocusable(false);
            editHotelName.setClickable(false);
            editStartDate.setFocusable(false);
            editStartDate.setClickable(false);
            editEndDate.setFocusable(false);
            editEndDate.setClickable(false);
        }

        RecyclerView recyclerView = findViewById(R.id.excursionRecyclerView);
        repository = new VacationRepository(getApplication());
        excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        excursionAdapter.setExcursions(repository.getAssocExcs(vacationID));

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Excursion> excursions = repository.getAssocExcs(vacationID);
        excursionAdapter.setExcursions(excursions);
    }

    private boolean validDate(String date) {
        if (date == null || date.isEmpty()) return false;
        if (!date.matches("\\d{2}/\\d{2}/\\d{2}")) {  // Use Regex to enforce date format since date can be input to repository as MM/dd/yy and MM/dd/yyyy
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        simpleDateFormat.setLenient(false); // Will throw an error at invalid dates
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);
        simpleDateFormat.set2DigitYearStart(calendar.getTime());
        try {
            simpleDateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean validEndDate(String dateStart, String dateEnd) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        simpleDateFormat.setLenient(false);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);
        simpleDateFormat.set2DigitYearStart(calendar.getTime());
        try {
            Date start = simpleDateFormat.parse(dateStart);
            Date end = simpleDateFormat.parse(dateEnd);
            return end.after(start);
        } catch (ParseException e) {
            return false;
        }
    }

    private void scheduleAlert(String dateString, String title, String msg) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
            Calendar calendar = Calendar.getInstance();
            calendar.set(2000, Calendar.JANUARY, 1);
            simpleDateFormat.set2DigitYearStart(calendar.getTime());
            Date date = simpleDateFormat.parse(dateString);
            if (date == null) return;

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, NotificationReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("message", msg);
            intent.setAction(title + dateString);

            int requestCode = (title + dateString).hashCode();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this, requestCode, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
            }
            Toast.makeText(this, "Alert set for " + dateString, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(this, "No Alert Set", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.vacationSave) {
            Vacations vacation;
            String inputStartDate = editStartDate.getText().toString().trim();
            String inputEndDate = editEndDate.getText().toString().trim();

            if (!validDate(inputStartDate)) {
                editStartDate.setError("Date invalid. Please use MM/DD/YY format.");
                editStartDate.requestFocus();
                return true;
            }
            if (!validDate(inputEndDate)) {
                editEndDate.setError("Date invalid. Please use MM/DD/YY format.");
                editEndDate.requestFocus();
                return true;
            }
            if (!validEndDate(inputStartDate, inputEndDate)) {
                editEndDate.setError("Vacation End Date must be after Start Date");
                editEndDate.requestFocus();
                return true;
            }

            if (vacationID ==-1) {
                if (repository.getmAllVacations().isEmpty()) vacationID = 1;
                else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() -1).getVacationID() + 1;
                vacation = new Vacations(vacationID,
                        editName.getText().toString(),
                        editHotelName.getText().toString(),
                        editStartDate.getText().toString(),
                        editEndDate.getText().toString());
                repository.insert(vacation);
                editName.clearFocus();
                editHotelName.clearFocus();
                editStartDate.clearFocus();
                editEndDate.clearFocus();
            }
            else {
                vacation = new Vacations(vacationID,
                        editName.getText().toString(),
                        editHotelName.getText().toString(),
                        editStartDate.getText().toString(),
                        editEndDate.getText().toString());
                repository.update(vacation);
                editName.clearFocus();
                editHotelName.clearFocus();
                editStartDate.clearFocus();
                editEndDate.clearFocus();
            }
            Toast.makeText(this, vacation.getVacationTitle() + " saved", Toast.LENGTH_SHORT).show(); // Show Toast Notification to confirm saved data
            return true;
        }

        if (item.getItemId() == R.id.vacationShare) {
            StringBuilder shareVacation = new StringBuilder();
            shareVacation.append("Vacation: ").append(editName.getText().toString()).append("\n");
            shareVacation.append("Hotel: ").append(editHotelName.getText().toString()).append("\n");
            shareVacation.append("Start Date: ").append(editStartDate.getText().toString()).append("\n");
            shareVacation.append("End Date: ").append(editEndDate.getText().toString()).append("\n");

            List<Excursion> excursions = repository.getAssocExcs(vacationID);
            if (!excursions.isEmpty()) {
                shareVacation.append("\nExcursions:\n");
                for (Excursion excursion:excursions) {
                    shareVacation.append("- ").append(excursion.getExcTitle())
                            .append(" (").append(excursion.getExcDate()).append(")\n");
                }
            }

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vacation Details: " + editName.getText().toString());
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareVacation.toString());
            startActivity(Intent.createChooser(shareIntent, "Share Vacation Details"));
            return true;
        }

        if (item.getItemId() == R.id.vacationEdit) {
            editName.setFocusableInTouchMode(true);
            editName.setClickable(true);
            editHotelName.setFocusableInTouchMode(true);
            editHotelName.setClickable(true);
            editStartDate.setFocusableInTouchMode(true);
            editStartDate.setClickable(true);
            editEndDate.setFocusableInTouchMode(true);
            editEndDate.setClickable(true);
            editName.requestFocus();
            return true;
        }

        if (item.getItemId() == R.id.vacationAlerts) {
            String vacationName = editName.getText().toString();
            String startDate = editStartDate.getText().toString();
            String endDate = editEndDate.getText().toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
                }
            }
            scheduleAlert(startDate,
                    "Vacation Tracker: " + vacationName, "Heads up! Your vacation " + vacationName + " starts today!");
            scheduleAlert(endDate,
                    "Vacation Tracker: " + vacationName, "Your vacation " + vacationName + " ends today!");
            return true;
        }

        if (item.getItemId() == R.id.vacationDelete) {
            List<Excursion> assocExcursions = repository.getAssocExcs(vacationID); // Confirm there are no excursions linked to the vacation before deleting
            if (!assocExcursions.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.delete_vacation_deny)
                        .setMessage(R.string.delete_vacation_exc)
                        .setPositiveButton(R.string.delete_ack, null)
                        .show();
                return true;
            }

            Vacations vacationDelete = new Vacations(vacationID,
                    editName.getText().toString(),
                    editHotelName.getText().toString(),
                    editStartDate.getText().toString(),
                    editEndDate.getText().toString());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete_vacation_title)
                    .setMessage(R.string.delete_confirm_message)
                    .setPositiveButton(R.string.delete_ok, ((dialog, which) -> {
                        Toast.makeText(this, "Deleting Vacation: " + vacationDelete.getVacationTitle(), Toast.LENGTH_SHORT).show();
                        repository.delete(vacationDelete);
                        finish();
                    }));
            builder.setNegativeButton(R.string.delete_cancel, null).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}