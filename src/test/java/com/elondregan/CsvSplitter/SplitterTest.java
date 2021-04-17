package com.elondregan.CsvSplitter;

import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.util.Comparator;
import java.util.List;

class SplitterTest {

    Splitter splitter = new Splitter();

    @Test
    public void testEmpty(){
        splitter.readFile("src/test/resources/empty.csv");
    }


    @Test
    public void testSplitterBaseCase(){
        splitter.readFile("src/test/resources/test1.csv");
        try{
            CSVReader reader  = new CSVReader(new FileReader("Insurance.co.csv"));
            List<String[]> input = reader.readAll();
            Assertions.assertEquals(input.size(), 4);
            Assertions.assertEquals(input.get(0).length, 5);
            //Now check that it's alphabetical:
            Assertions.assertEquals(input.get(1)[0], "2");
            Assertions.assertEquals(input.get(2)[0], "3");
            Assertions.assertEquals(input.get(3)[0], "1");
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    public void testSplitterDeDupeCase(){
        //File
        splitter.readFile("src/test/resources/testDeDupe.csv");
        try{
            CSVReader reader = new CSVReader(new FileReader("Insurance.co.csv"));
            CSVReader reader2 = new CSVReader(new FileReader("O'Higgens.co.csv"));
            List<String[]> input = reader.readAll();
            Assertions.assertEquals(input.size(), 2);
            Assertions.assertEquals(input.get(0).length, 5);
            input = reader2.readAll();
            Assertions.assertEquals(input.size(), 2);
            Assertions.assertEquals(input.get(0).length, 5);
        } catch (Exception e){
            Assertions.fail();
        }
    }

    @Test
    public void testWeirdNames(){
        splitter.readFile("src/test/resources/WeirdNames.csv");
        try{
            CSVReader csvReader = new CSVReader(new FileReader("csv.csv"));
            CSVReader nullReader = new CSVReader(new FileReader("null.csv"));
            CSVReader reader = new CSVReader(new FileReader("Insurance.co.csv"));

            List<String[]> csvInput = csvReader.readAll();
            List<String[]> nullInput = nullReader.readAll();
            List<String[]> readerInput = reader.readAll();
            Assertions.assertEquals(csvInput.size(), 2);
            Assertions.assertEquals(nullInput.size(), 2);
            Assertions.assertEquals(readerInput.size(), 3);

            //Validate that string null values were written:
            Assertions.assertEquals(readerInput.get(1)[1], "null");
            Assertions.assertEquals(readerInput.get(2)[2], "null");
        }catch (Exception e){
            Assertions.fail();
        }
    }
}