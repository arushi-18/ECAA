package com.example.ecaa.Model;

public class Cart
{   private String pid,p_name,price,quantity;

    public Cart()
    {
    }
    public Cart(String pid, String p_name, String price, String quantity)
    {
        this.pid=pid;
        this.p_name=p_name;
        this.price=price;
        this.quantity=quantity;
    }

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getP_name()
    {
        return p_name;
    }

    public void setP_name(String pname)
    {
        this.p_name = p_name;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getQuantity()
    {
        return quantity;
    }

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }
}
