package com.example.myapplication.UI;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import com.example.myapplication.R;
import com.example.myapplication.database.VacationRepository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    int excursionID;
    int vacationID;
    String excursionTitle;
    String excursionDate;
    EditText editExcTitle;
    EditText editExcDate;
    VacationRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish()); // create new Listener for custom menu button
//        Button btnExcList = findViewById(R.id.btnSeeAllExc);
//
//        btnExcList.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(ExcursionDetails.this, ExcursionList.class);
//                startActivity(intent);
//            }
//        });

        repository = new VacationRepository(getApplication());
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        excursionTitle = getIntent().getStringExtra("name");
        excursionDate = getIntent().getStringExtra("date");
        editExcTitle = findViewById(R.id.titleText);
        editExcDate = findViewById(R.id.dateStartText);
        editExcTitle.setText(excursionTitle);
        editExcDate.setText(excursionDate);

        if (excursionID == -1) {
            editExcTitle.setFocusable(true);       // If user is making a new vacation, allow them to input text without using Edit Vacation in Menu
            editExcTitle.setClickable(true);
            editExcDate.setFocusable(true);
            editExcDate.setClickable(true);
            editExcTitle.requestFocus();
        } else {
            editExcTitle.setFocusable(false);       // Prevent text fields from being edited unless user chooses Edit Vacation in option menu if vacation exists
            editExcTitle.setClickable(false);
            editExcDate.setFocusable(false);
            editExcDate.setClickable(false);
        }
        ImageButton btnOptions = findViewById(R.id.btnOptions);
        btnOptions.setOnClickListener(view -> {
            PopupMenu popupOptions = new PopupMenu(this, view);
            popupOptions.getMenuInflater().inflate(R.menu.menu_excursiondetails, popupOptions.getMenu());
            if (excursionID == -1) {
                popupOptions.getMenu().findItem(R.id.excursionEdit).setVisible(false);
                popupOptions.getMenu().findItem(R.id.excursionDelete).setVisible(false);
            }
            popupOptions.setOnMenuItemClickListener(item -> onOptionsItemSelected(item));
            popupOptions.show();
        });
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

    private boolean validStartDate(String excursionDate) {
        if (!excursionDate.matches("\\d{2}/\\d{2}/\\d{2}")) return false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        simpleDateFormat.setLenient(false);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);
        simpleDateFormat.set2DigitYearStart(calendar.getTime());
        try {
            Date start = simpleDateFormat.parse(excursionDate);

            Vacations assocVacation = repository.getVacationByID(vacationID);
            if (assocVacation == null) return false;

            Date vacationStartDate = simpleDateFormat.parse(assocVacation.getStartDate());
            Date vacationEndDate = simpleDateFormat.parse(assocVacation.getEndDate());

            return !start.before(vacationStartDate) && !start.after(vacationEndDate);

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

            int requestCode = (title + dateString).hashCode();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this, requestCode, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
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
        if (item.getItemId() == R.id.excursionSave) {
            Excursion excursion;
            String inputStartDate = editExcDate.getText().toString().trim();

            if (!validDate(inputStartDate)) {
                editExcDate.setError("Date invalid. Please use MM/DD/YY format.");
                editExcDate.requestFocus();
                return true;
            }

            if (!validStartDate(inputStartDate)) {
                editExcDate.setError("The Excursion date does not fall within the Vacation timeframe.");
                editExcDate.requestFocus();
                return true;
            }

            if (excursionID == -1) {
                if (repository.getmAllExcursions().isEmpty()) excursionID = 1;
                else
                    excursionID = repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcID() + 1;
                excursion = new Excursion(excursionID,
                        editExcTitle.getText().toString(),
                        editExcDate.getText().toString(),
                        vacationID);
                repository.insert(excursion);
                editExcTitle.clearFocus();
                editExcDate.clearFocus();
            } else {
                excursion = new Excursion(excursionID,
                        editExcTitle.getText().toString(),
                        editExcDate.getText().toString(),
                        vacationID);
                repository.update(excursion);
                editExcTitle.clearFocus();
                editExcDate.clearFocus();
            }
            Toast.makeText(this, excursion.getExcTitle() + " saved", Toast.LENGTH_SHORT).show(); // Show Toast Notification to confirm saved data
            finish();
            return true;
        }

        if (item.getItemId() == R.id.excursionEdit) {
            editExcTitle.setFocusableInTouchMode(true);
            editExcTitle.setClickable(true);
            editExcDate.setFocusableInTouchMode(true);
            editExcDate.setClickable(true);
            editExcTitle.requestFocus();
            return true;
        }

        if (item.getItemId() == R.id.excursionAlerts) {
            String excTitle = editExcTitle.getText().toString();
            String excDate = editExcDate.getText().toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                }
            }
            scheduleAlert(excDate,
                    "Vacation Tracker: " + excTitle, excTitle + " is starting now!");
            return true;
        }

        if (item.getItemId() == R.id.excursionDelete) {
            Excursion excursionDelete = new Excursion(excursionID,
                    editExcTitle.getText().toString(),
                    editExcDate.getText().toString(),
                    vacationID);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete_excursion_title)
                    .setMessage(R.string.delete_confirm_message)
                    .setPositiveButton(R.string.delete_ok, ((dialog, which) -> {
                        Toast.makeText(this, "Deleting Excursion: " + excursionDelete.getExcTitle(), Toast.LENGTH_SHORT).show();
                        repository.delete(excursionDelete);
                        finish();
                    }));
            builder.setNegativeButton(R.string.delete_cancel, null).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}