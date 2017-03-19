package com.example.apple.mobileplayer.domain;


/**
 * Created by apple on 17/3/16.
 */

public class ShoppingItemData {
    private int id;
    private String name;
    private String imgUrl;
    private int categoryId;
    private int campaignId;
    private float price;
    private int sale;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        return "ShoppingItemData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", categoryId=" + categoryId +
                ", campaignId=" + campaignId +
                ", price=" + price +
                ", sale=" + sale +
                '}';
    }
}
