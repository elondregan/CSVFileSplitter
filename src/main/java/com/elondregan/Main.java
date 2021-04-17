package com.elondregan;

import com.elondregan.CsvSplitter.Splitter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        Splitter splitter = new Splitter();
        String fileName = "src/main/resources/input.csv";

        splitter.readFile(fileName);
    }
}
