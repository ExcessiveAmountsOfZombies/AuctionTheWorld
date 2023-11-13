package com.epherical.auctionworld.object;

public class Page {

    private int page;
    private int pageSize;


    public Page(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageOffset() {
        return page * pageSize;
    }

    public int getPagedItems() {
        return (page* pageSize) + pageSize;
    }


}
