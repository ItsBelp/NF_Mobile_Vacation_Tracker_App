package com.example.myapplication.UI;

import android.content.Context;

import com.example.myapplication.database.VacationRepository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class VacationReportGenerator {

    private static final String headerCSV = "Vacation Title,Hotel,Start Date, End Date, Excursion Title,";
    public static File generateVacationReport(Context context, List<Vacations> vacations, VacationRepository repository) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(new Date());
        String displayTimestamp = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.US).format(new Date());
        String fileReportName = "VacationReport_" + timestamp + ".csv";
        File file = new File(context.getFilesDir(), fileReportName);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.append("Vacation Tracker Report\n");
            fileWriter.append("Generated: ").append(displayTimestamp).append("\n");
            fileWriter.append("\n");
            fileWriter.append(headerCSV).append("\n");

            for (Vacations vacation : vacations) {
                List<Excursion> excursions = repository.getAssocExcs(vacation.getVacationID());
                String excTitle = excursions.stream()
                        .map(Excursion::getExcTitle)
                        .collect(Collectors.joining("; "));
                fileWriter.append(escCSV(vacation.getVacationTitle())).append(",");
                fileWriter.append(escCSV(vacation.getVacationHotel())).append(",");
                fileWriter.append(escCSV(vacation.getStartDate())).append(",");
                fileWriter.append(escCSV(vacation.getEndDate())).append(",");
                fileWriter.append(escCSV(excTitle)).append("\n");
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return file;
    }

    private static String escCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}
