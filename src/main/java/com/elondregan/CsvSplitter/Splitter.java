package com.elondregan.CsvSplitter;

import com.elondregan.model.InsuranceToUserId;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class Splitter {

    private Map<String, CSVWriter> writerMap;
    private final String[] header = new String[]{"User Id", "First Name", "Last Name", "Version", "Insurance Company"};

    public Splitter(){
        writerMap = new LinkedHashMap<>();
    }

    //The challenge here is we don't know the upper bounds of the
    //csv file, or its ordering, we can't assume we can hold the entire
    //csv files contents in memory, but we have another problem as well,
    //we can't sort once the records are in the CSV file efficiently, if we
    //need to re-order, that operation really stinks, so we need to take a risk
    //and pull in all the records.
    public void readFile(String fileName){
        try {
            CSVReader reader = new CSVReader(new FileReader(fileName));

            List<String[]> allRecords = reader.readAll();
            allRecords.remove(0);
            HashMap<InsuranceToUserId, String[]> uniqueInsuranceToUserid = new HashMap<>();

            //DeDupe the records! O(N)
            deDupe(allRecords, uniqueInsuranceToUserid);
            allRecords = new ArrayList<>(uniqueInsuranceToUserid.values());
            sort(allRecords);

            for(String[] s : allRecords){
                String insuranceCompany = s[4];
                checkAndInitializeFileWriter(insuranceCompany);
                writerMap.get(insuranceCompany).writeNext(new String[]{s[0], s[1], s[2], s[3], insuranceCompany});
            }
        } catch (Exception e){
            System.out.println("An error occured while processing CSV file!");
        }

        try {
            for (String s : writerMap.keySet()) {
                writerMap.get(s).close();
            }
        }catch (Exception e){
            System.out.println("Unable to Flush writer w/ error: " + e.getMessage());
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

    private void deDupe(List<String[]> allRecords, HashMap<InsuranceToUserId, String[]> uniqueInsuranceToUserid){
        for(int i = 0; i < allRecords.size(); i++){
            String[] nextRecord = allRecords.get(i);

            String userId = nextRecord[0];
            Integer version = Integer.parseInt(nextRecord[3]);
            String insuranceCompany = nextRecord[4];
            InsuranceToUserId key = new InsuranceToUserId(insuranceCompany, userId);
            //We need a map that compares insurance company to user ID;
            uniqueInsuranceToUserid.computeIfPresent(key, (k,v) ->{
                if(version > Integer.parseInt(v[3])) return nextRecord;
                return v;
            });
            uniqueInsuranceToUserid.putIfAbsent(new InsuranceToUserId(insuranceCompany, userId), nextRecord);
        }
    }

    private void sort(List<String[]> allRecords){
        //Skip the header file and then sort the contents.
        allRecords.sort((o1, o2) -> {
            if(o1[2].compareTo(o2[2]) == 0){
                return o1[1].compareTo(o2[1]);
            } else {
                return o1[2].compareTo(o2[2]);
            }
        });
    }

}
