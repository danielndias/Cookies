package com.dndias.cookies;

import prog24178.labs.objects.CookieInventoryItem;
import prog24178.labs.objects.Cookies;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;



/**
 * A class that a models list of objects from the CookieInventoryItem Class.
 *
 * @author Daniel Dias
 * @version 1.0
 */
public class CookieInventoryFile extends ArrayList<CookieInventoryItem> {

    public CookieInventoryFile() {}

    public CookieInventoryFile(File file) throws FileNotFoundException {
        loadFromFile(file);
    }


    /**
     * Retrieves a CookieInventoryItem object passing its flavour ID. If the there's
     * no object with the provided ID,
     *
     * @param flavourId the flavour ID of the cookie
     * @return the CookieInventoryItem object
     */
    public CookieInventoryItem find(int flavourId) {
        CookieInventoryItem cookie = null;
        for (int i = 0; i < size(); i++) {
            if (get(i).cookie.getId() == flavourId) {
                cookie = get(i);
            }
        }
        return cookie;
    }

    /**
     * Loads a File object, creates a CookieInventoryItem object for each record
     * and add the object to the ArrayList
     *
     * @param file the File object
     * @throws FileNotFoundException
     */
    public void loadFromFile(File file) throws FileNotFoundException {
        CookieInventoryItem cookie;
        String[] fields = {};
        int id;
        int qnt;
        try {
            Scanner input = new Scanner(file);
            String line;
            while(input.hasNext()) {
                line = input.next();
                fields = getFields(line);
                id = Integer.parseInt(fields[0]);
                qnt = Integer.parseInt(fields[1]);
                cookie = new CookieInventoryItem(id,qnt);
                add(cookie);
            }
            input.close();
        } catch (Exception ex) {

        }
    }

    /**
     * Gets the data from the ArrayList and save into a File object
     *
     * @param file the File object
     * @throws IOException
     */
    public void writeToFile(File file) throws IOException {
        PrintWriter print = new PrintWriter(file);
//        PrintWriter print = new PrintWriter(new BufferedWriter(
//                new FileWriter(file)));
        for (int i = 0; i < size(); i++) {
            CookieInventoryItem c = get(i);
            print.println(c.toFileString());
        }
        print.close();
    }

    /**
     * Retrieves each field of a record from the File object
     *
     * @param line the Field object record
     *
     * @return an Array with the records fields.
     */
    private String[] getFields(String line) {
        String[] fields = {};
        fields = line.split("\\|");
        return fields;
    }

}
