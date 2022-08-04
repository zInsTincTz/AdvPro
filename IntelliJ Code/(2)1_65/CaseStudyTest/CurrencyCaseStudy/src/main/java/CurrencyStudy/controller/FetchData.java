package CurrencyStudy.controller;

import CurrencyStudy.model.CurrencyEntity;
import javafx.scene.control.Alert;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class FetchData {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static ArrayList<CurrencyEntity> fetch_range(String src, int N) {

        ArrayList<CurrencyEntity> histList = new ArrayList<>();

        for (int i=N; i>0; i--) {
            String retrievedJson = null;
            try {
                String dateI = LocalDate.now().minusDays(i).format(formatter);
                String url_str = String.format("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/%s/currencies/thb/%s.json", dateI, src.toLowerCase());
                retrievedJson = IOUtils.toString(new URL(url_str), Charset.defaultCharset());
            } catch (MalformedURLException e) {
                System.out.println("Encountered a Malformed URL exception");
            }
            catch (IOException e) {
                System.out.println("Encountered an IO exception");
            }
            JSONObject jsonObj2 = new JSONObject(retrievedJson);
            String date = jsonObj2.getString("date");

            //Exercise 4 Let the user use either small or capital letters for the currency short code.
            double rate = jsonObj2.getDouble(src.toLowerCase());

            histList.add(new CurrencyEntity(rate, date));
        }
        return histList;
    }

    //Exercise 5 The application should be able to notify the user
    // if the input currency short code is invalid and let the user re-enter the new one.
    // Hint:a try-catch-finally block should be useful.
    // All the valid currency short code is available at:https://free.currconv.com/api/v7/currencies?apiKey=XXXXXXXX,
    // where XXXXXXXX is the API key received from the provider by email at the beginning of the casestudy.
    public static boolean isCurrency(String src) {
        try {
            JSONObject jsonObj1 = new JSONObject(IOUtils.toString(new URL("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies.json"), Charset.defaultCharset()));
            jsonObj1.getString(src.toLowerCase());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Currency Code not found!! re-enter the currency code");
            alert.showAndWait();
            AllEventHandlers.onAdd();
        }
        return true;
    }
}
