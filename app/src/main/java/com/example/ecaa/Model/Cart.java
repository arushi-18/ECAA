package com.example.ecaa.Model;

public class Cart
{   private String p_id,p_name,price,qty;

    public Cart()
    {
    }
    public Cart(String p_id, String p_name, String price, String qty)
    {
        this.p_id=p_id;
        this.p_name=p_name;
        this.price=price;
        this.qty=qty;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
