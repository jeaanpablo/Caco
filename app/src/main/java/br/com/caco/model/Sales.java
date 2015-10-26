package br.com.caco.model;

import java.util.Calendar;

/**
 * Created by Jean Pablo Bosso on 22/10/2015.
 */
public class Sales {


    private long idSale;

    private int idClient;

    private int idStore;

    private String description;

    private long tradePoints;

    private Calendar expirationDate;


    public Sales(String salesName, long points, Calendar date)
    {
       this.setExpirationDate(date);
       this.setDescription(salesName);
       this.setTradePoints(points);
    }


    public long getIdSale() {
        return idSale;
    }

    public void setIdSale(long idSale) {
        this.idSale = idSale;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTradePoints() {
        return tradePoints;
    }

    public void setTradePoints(long tradePoints) {
        this.tradePoints = tradePoints;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }
}
