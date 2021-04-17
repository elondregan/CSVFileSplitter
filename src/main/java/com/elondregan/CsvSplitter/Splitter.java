package com.elondregan.CsvSplitter;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Splitter {

    private HashMap<String, CSVWriter> writerMap;
    private final String[] header = new String[]{"User Id", "First Name", "Last Name", "Version", "Insurance Company"};


    public Splitter(){
        writerMap = new HashMap<>();
    }

    public void readFile(String fileName){
        try {
            CSVReader reader = new CSVReader(new FileReader(fileName));

            //The challenge here is we don't know the upper bounds of the
            //csv file, or its ordering, we can't assume we can hold the entire
            //csv files contents in memory, but we have another problem as well,
            //we can't sort once the records are in the CSV file efficiently, if we
            //need to re-order, that operation really stinks, so we need to take a risk
            //and pull in all the records.

            //Skip the header file
            List<String[]> allRecords = reader.readAll();

            //try to sort the list before we pick over it. NLogN operation.


            for(int i = 1; i < allRecords.size(); i++){
                String[] nextRecord = allRecords.get(i);

                String userId = nextRecord[0];
                String firstName = nextRecord[1];
                String lastName = nextRecord[2];
                String version = nextRecord[3];
                String insuranceCompany = nextRecord[4];

                //This writes the basic data.
                checkAndInitializeFileWriter(insuranceCompany);
                writerMap.get(insuranceCompany).writeNext(new String[]{userId, firstName, lastName, version, insuranceCompany});
                writerMap.get(insuranceCompany).flush();
            }

        } catch (Exception e){

        }

    }

    private void checkAndInitializeFileWriter(String insuranceCompany){
        if(writerMap.get(insuranceCompany) == null){
            FileWriter writer;
            try {
                writer = new FileWriter(insuranceCompany + ".csv");
                CSVWriter csvWriter = new CSVWriter(writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END);
                csvWriter.writeNext(header);
                writerMap.put(insuranceCompany, csvWriter);
            } catch (Exception e){
                //Keeping the compiler happy
                System.out.println(e.getMessage());
            }
        }
    }

}
