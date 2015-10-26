package br.com.caco.model;

import java.util.Calendar;

/**
 * Created by Jean Pablo Bosso on 26/10/2015.
 */
public class SalesParameter {

    private long pontuation;

    private long discount;

    private String typePontuation;

    public SalesParameter(String salesCondition, long points)
    {
        this.setTypePontuation(salesCondition);
        this.setPontuation(points);
    }


    public long getPontuation() {
        return pontuation;
    }

    public void setPontuation(long pontuation) {
        this.pontuation = pontuation;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public String getTypePontuation() {
        return typePontuation;
    }

    public void setTypePontuation(String typePontuation) {
        this.typePontuation = typePontuation;
    }
}
