package com.example.ecaa.Model;

public class Products
{
    private String p_name,description,price,image,category,p_id,qty,status,sub_category;

    public Products()
    {

    }

    public Products(String p_name, String description, String price, String image, String category, String p_id, String qty,String status,String sub_category) {

        this.p_name = p_name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.p_id = p_id;
        this.qty = qty;
        this.status=status;
        this.sub_category=sub_category;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }
}
