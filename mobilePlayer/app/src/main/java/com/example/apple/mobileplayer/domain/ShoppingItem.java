package com.example.apple.mobileplayer.domain;

import java.util.List;

/**
 * Created by apple on 17/3/16.
 */

public class ShoppingItem {

    private String copyright;
    private int totalCount;
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private List<String> orders;
    private List<ShoppingItemData> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<ShoppingItemData> getList() {
        return list;
    }

    public void setList(List<ShoppingItemData> list) {
        this.list = list;
    }
}
