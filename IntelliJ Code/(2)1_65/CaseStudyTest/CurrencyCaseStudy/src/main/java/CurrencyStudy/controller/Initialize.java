package CurrencyStudy.controller;

import CurrencyStudy.Launcher;
import CurrencyStudy.model.Currency;
import CurrencyStudy.model.CurrencyEntity;

import java.util.ArrayList;

public class Initialize {
    public static void initialize_app() {
        Currency c = new Currency("USD");

        // Exercise 1 Show the historical exchange rate up to 14 days as requested by the client.
        ArrayList<CurrencyEntity> c_list = FetchData.fetch_range(c.getShortCode(), 14);

        c.setHistData(c_list);
        c.setCurrencyEntity(c_list.get(c_list.size() - 1));

        ArrayList<Currency> currencyList = new ArrayList<>();
        currencyList.add(c);
        Launcher.setCurrencyList(currencyList);
    }
}
