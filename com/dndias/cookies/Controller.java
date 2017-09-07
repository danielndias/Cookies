package com.dndias.cookies;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import prog24178.labs.objects.CookieInventoryItem;
import prog24178.labs.objects.Cookies;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for the Cookies Inventory application.
 *
 * @author Daniel Dias
 */
public class Controller implements Initializable{

    //Combo Box with the ENUM Constants
    @FXML
    public ComboBox<Cookies> cookieSelect;

    //ArrayList with the CookieInventoryItem's objects
    @FXML
    CookieInventoryFile cookiesList;

    //Labels where the user types the amount of Cookies Sold or Baked
    @FXML
    private TextField lblSold, lblBaked;


    /**
     * Method executed when the application loads. This method populates the
     * ComboBox with all the ENUM Constants and creates a new File Object that will
     * be used by the application. If the file don't exist, its created.
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Get the ENUM Constants and populates the ComboBox
        cookieSelect.setItems(FXCollections.observableArrayList(Cookies.values()));

        //Creates a new File object for the "cookies.txt" file.
        // If the file don't exist, it's created.
        File file = new File("cookies.txt");
        if (!file.exists()) {
            try {
                PrintWriter outputFile = new PrintWriter(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            cookiesList = new CookieInventoryFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    /**
     * Method responsible for adding an amount of recently baked cookies to the system.
     * The method checks if the user input is valid, and if it is, the cookie's quantity
     * is updated in the correspondent object in the ArrayList. If the user input is not
     * valid, an alert error message is generate with the appropriate message informing the
     * user the type of error.
     *
     * @param event
     */
    @FXML
    private void addCookie (ActionEvent event) {

        //Checks if the user input any value before clicking the add button. If he didn't,
        //an IllegalArgumentException is thrown.
        try {
            if (lblBaked.getText().equals("")) {
                throw new IllegalArgumentException("Please enter the number of cookies added");
            } else {

                //Assign the user input to a variable
                int qnt = Integer.parseInt(lblBaked.getText());

                //Checks if the user input is equal or less than zero. If it is,
                //an IllegalArgumentException is thrown
                if (qnt <= 0) {
                    throw new IllegalArgumentException("You must enter a quantity that is greater than 0");
                } else {

                    //Gets the ID from the Cookie currently selected in the ComboBox
                    int id = cookieSelect.getValue().getId();

                    //Creates a variable to keep track if the Cookie already exist
                    //in the ArrayList. If exists, the quantity is updated. If don't exists,
                    //a new CookieInventoryItem object is created.
                    boolean exist = false;
                    lblBaked.clear();
                    for (int i = 0; i < cookiesList.size(); i++) {
                        if (cookiesList.get(i).cookie.getId() == id) {
                            cookiesList.get(i).setQuantity((cookiesList.get(i).getQuantity() + qnt));
                            exist = true;
                        }
                    }
                    if (!exist) {
                        CookieInventoryItem c = new CookieInventoryItem(id, qnt);
                        cookiesList.add(c);
                    }
                }
            }

            //Catch if the user typed anything other than a integer number.
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Data Format");
            alert.setHeaderText(null);
            alert.setContentText("You must enter a valid numeric value");
            alert.showAndWait();
            lblBaked.requestFocus();

            //Catch if the user left the text field blank or
            //typed an amount equal or less than zero
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Data Entry Error!");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            lblBaked.requestFocus();
        }
    }


    /**
     * Method responsible for remove an amount cookies recently sold from the system.
     * The method checks if the user input is valid, and if it is, the cookie's quantity
     * is updated in the correspondent object in the ArrayList. If the user input is not
     * valid, an alert error message is generate with the appropriate message informing the
     * user the type of error.
     *
     * @param event
     */
    @FXML
    private void sellCookie (ActionEvent event) {
        try {

            //Checks if the user input any value before clicking the add button. If he didn't,
            //an IllegalArgumentException is thrown.
            if (lblSold.getText().trim().equals("")) {
                throw new IllegalArgumentException("Please enter the number of cookies sold");
            } else {

                //Assign the user input to a variable
                int qnt = Integer.parseInt(lblSold.getText());

                //Checks if the user input is equal or less than zero. If it is,
                //an IllegalArgumentException is thrown
                if (qnt <= 0) {
                    throw new IllegalArgumentException("You must enter a quantity that is greater than 0");


                } else {

                    //Gets the ID from the Cookie currently selected in the ComboBox
                    int id = cookieSelect.getValue().getId();
                    int currentQnt;
                    int salledQnt = Integer.parseInt(lblSold.getText());

                    //Creates a variable to keep track if the Cookie already exist
                    //in the ArrayList.
                    boolean exist = false;

                    //Checks if the selected cookie already exist in the ArrayList.
                    for (int i = 0; i < cookiesList.size(); i++) {
                        currentQnt = cookiesList.get(i).getQuantity();
                        if (cookiesList.get(i).cookie.getId() == id) {
                            int newQnt = currentQnt - salledQnt;
                            exist = true;

                            //If the selected cookie exist in the ArrayList, checks the quantity of cookies
                            //being sold is available in the inventory. If it isn't, ArithmeticException
                            //is thrown.
                            if (currentQnt > salledQnt) {
                                cookiesList.get(i).setQuantity(newQnt);
                                lblSold.clear();
                            } else {
                                throw new ArithmeticException("Sorry, not enough " + cookiesList.get(i).cookie.getName() + "cookies to sell. You only have " + currentQnt);
                            }
                        }
                    }

                    //If the selected Cookie don't exists in the ArrayList,
                    //an IllegalArgumentException is thrown
                    if (!exist) {
                        throw new IllegalArgumentException("Sorry, there are no " + cookieSelect.getValue().getName() + " cookies available to sell");
                    }
                }
            }

            //Catch if the user typed anything other than a integer number.
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Format");
            alert.setHeaderText(null);
            alert.setContentText("You must enter a valid numeric value");
            alert.showAndWait();
            lblSold.requestFocus();

            //Catch if the user left the text field blank or
            //typed an amount equal or less than zero
        }   catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Data Entry Error");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            lblSold.requestFocus();

            //Catch if the amount of cookies being sold is not available in the inventory
        } catch (ArithmeticException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient Inventory");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            lblSold.requestFocus();
        }
    }


    /**
     * Method responsible for exiting the application. When the user clicks in the
     * EXIT button, a confirmation window appear. If he confirms, the writeToFile()
     * method is called to save the content of the ArrayList into a file.
     *
     * @param event
     */
    @FXML
    private void exit(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you wish to exit?", ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Exit Program");
        alert.setHeaderText(null);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            File file = new File("cookies.txt");
            cookiesList.writeToFile(file);
            System.exit(0);
        }
    }
}
