package com.pmobrien.rest.csv;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.pmobrien.rest.neo.Sessions;
import com.pmobrien.rest.neo.accessors.AthleteAccessor;
import com.pmobrien.rest.neo.pojo.Athlete;
import com.pmobrien.rest.neo.pojo.NeoEntityFactory;
import com.pmobrien.rest.neo.pojo.Performance;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

public class CsvReader {

  private static final String RESOURCE_PACKAGE = "com.pmobrien.rest.conf.metcons";
  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
  
  private CsvReader() {}
  
  public static void loadAll() {
    Reflections reflections = new Reflections(RESOURCE_PACKAGE, new ResourcesScanner());
    reflections.getResources(name -> {
      try {
        load(CsvReader.class.getClassLoader().getResourceAsStream(RESOURCE_PACKAGE.replace(".", "/") + "/" + name))
            .forEach(row -> saveRow(row));
      } catch(IOException | CsvValidationException ex) {
        ex.printStackTrace(System.out);
      }
      
      return true;
    });
  }
  
  private static List<MetconRow> load(InputStream csv) throws FileNotFoundException, IOException, CsvValidationException {
    List<MetconRow> rows = Lists.newArrayList();
    
    try(CSVReader reader = new CSVReader(new InputStreamReader(csv))) {
      String[] values;
      while((values = reader.readNext()) != null) {
        rows.add(new MetconRow(String.join(",", values)));
      }
    }
    
    return rows;
  }
  
  private static void saveRow(MetconRow row) {
    Athlete a = new AthleteAccessor().getAthleteByName(row.athleteName);
    
    Sessions.sessionOperation(session -> {
      try {
        Athlete athlete = a == null
            ? NeoEntityFactory.create(Athlete.class).setName(row.athleteName)
            : a;

        Performance performance = NeoEntityFactory.create(Performance.class)
            .setAthlete(athlete)
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
    private String scheme;
    private String result;
    private String name;
    private String pr;
    private String comment;
    private String rx;
    private String rxPlus;

    private MetconRow(String line) {
      try {
        String[] items = line.split(",");
      
        date = items[0];
        athleteName = items[2];
        scheme = items[8];
        result = items[9];
        name = items[10];
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
