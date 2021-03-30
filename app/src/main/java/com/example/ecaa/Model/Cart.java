package com.example.ecaa.Model;

public class Cart
{   private String pid,p_name,price,qty;

    public Cart()
    {
    }
    public Cart(String pid, String p_name, String price, String qty)
    {
        this.pid=pid;
        this.p_name=p_name;
        this.price=price;
        this.qty=qty;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
