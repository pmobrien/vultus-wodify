package com.pmobrien.rest.csv;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.accessors.AthleteAccessor;
import com.pmobrien.rest.neo.accessors.WorkoutAccessor;
import com.pmobrien.rest.neo.pojo.Athlete;
import com.pmobrien.rest.neo.pojo.NeoEntityFactory;
import com.pmobrien.rest.neo.pojo.Performance;
import com.pmobrien.rest.neo.pojo.Workout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CsvReader {
  
  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
  
  private CsvReader() {}
  
  public static void loadAll(String directory) {
    // directory format is:
    //   workouts/    <-- directory should be the path to this
    //     metcons/
    //     weightlifting/
    for(File sub : new File(directory).listFiles()) {
      final File[] workouts = sub.listFiles();
      for(int i = 0; i < workouts.length; ++i) {
        final File workout = workouts[i];
        
        try {
          System.out.print(String.format("Loading [%s of %s] %s.", i + 1, workouts.length, workout.getName()));
              
          load(new FileInputStream(workouts[i].getAbsolutePath()))
              .forEach(row -> saveRow(row, StringUtils.substringBeforeLast(workout.getName(), ".")));

          System.out.println("  Done.");
        } catch(CsvValidationException | IOException ex) {
          ex.printStackTrace(System.out);
        }
      }
    }
  }
  
  private static List<MetconRow> load(InputStream csv) throws FileNotFoundException, IOException, CsvValidationException {
    List<MetconRow> rows = Lists.newArrayList();
    
    try(CSVReader reader = new CSVReader(new InputStreamReader(csv))) {
      reader.readNext();  // throw out the first line (headers)
      
      String[] values;
      while((values = reader.readNext()) != null) {
        rows.add(new MetconRow(String.join(",", values)));
      }
    }
    
    return rows;
  }
  
  private static void saveRow(MetconRow row, String workoutName) {
    Athlete a = new AthleteAccessor().getAthleteByName(row.athleteName);
    Workout w = new WorkoutAccessor().getWorkoutByName(workoutName);
    
    Sessions.sessionOperation(session -> {
      try {
        // TODO cache workouts/athletes?
        
        Athlete athlete = a == null
            ? NeoEntityFactory.create(Athlete.class).setName(row.athleteName)
            : a;
        
        Workout workout = w == null
            ? NeoEntityFactory.create(Workout.class)
                .setName(workoutName)  // not using row.workoutName since it is poorly formatted a lot of time
                .setType(Workout.Type.parse(row.workoutScheme))
            : w;

        Performance performance = NeoEntityFactory.create(Performance.class)
            .setAthlete(athlete)
            .setWorkout(workout)
            .setComment(row.comment)
            .setDate(DATE_FORMATTER.parse(row.date))
            .setPr(Boolean.valueOf(row.pr))
            .setResult(row.result)
            .setType(
                Boolean.valueOf(row.rxPlus)
                    ? Performance.Type.RX_PLUS
                    : Boolean.valueOf(row.rx)
                        ? Performance.Type.RX
                        : Performance.Type.SCALED
            );

        session.save(performance);
      } catch(ParseException ex) {
        ex.printStackTrace(System.out);
      }
    });
  }
  
  private static class MetconRow {
    
    private String date;
    private String athleteName;
    private String workoutScheme;
    private String result;
    private String workoutName;
    private String pr;
    private String comment;
    private String rx;
    private String rxPlus;

    private MetconRow(String line) {
      try {
        String[] items = line.split(",");
      
        date = items[0];
        athleteName = items[2];
        workoutScheme = items[8];
        result = items[9];
        workoutName = items[10];
        pr = items[11];
        comment = items[12];
        rx = items[13];
        rxPlus = items[14];
      } catch(Exception ex) {
        // TODO handle this
      }
    }
  }
}
