package com.deploytracker.dto;

import java.util.List;

/**
 * Paginated list wrapper used by collection endpoints.
 * Carries both the data payload and pagination metadata so clients
 * can drive cursor/page navigation without extra round-trips.
 */
public class PagedResponse<T> {

    private final List<T> data;
    private final Meta meta;

    public PagedResponse(List<T> data, long total, int page, int size) {
        this.data = data;
        int totalPages = size > 0 ? (int) Math.ceil((double) total / size) : 0;
        this.meta = new Meta(total, page, size, totalPages);
    }

    public List<T> getData() { return data; }
    public Meta getMeta()    { return meta; }

    public static final class Meta {
        private final long total;
        private final int page;
        private final int size;
        private final int totalPages;

        Meta(long total, int page, int size, int totalPages) {
            this.total      = total;
            this.page       = page;
            this.size       = size;
            this.totalPages = totalPages;
        }

        public long getTotal()      { return total; }
        public int getPage()        { return page; }
        public int getSize()        { return size; }
        public int getTotalPages()  { return totalPages; }
    }
}
