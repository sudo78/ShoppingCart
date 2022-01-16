package test.springboot.shoppingcart.pagination;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class PaginationResult<E> {

    private final List<E> list;
    private final int maxResult;
    private final int currentPage;
    private final int totalPages;
    private final int totalRecords;

    private int maxNavigationPage;

    private List<Integer> navigationPages;

    public PaginationResult(Query<E> query, int page, int maxResult, int maxNavigationPage) {
        final int pageIndex = page - 1 < 0 ? 0 : page - 1;

        int fromRecordIndex = pageIndex * maxResult;
        int maxRecord = fromRecordIndex + maxResult;

        ScrollableResults scroll = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
        boolean hasResult = scroll.first();

        List<E> results = new ArrayList<>();
        if (hasResult) {
            hasResult = scroll.scroll(fromRecordIndex);

            if (hasResult) {
                do {
                    E record = (E) scroll.get(0);
                    results.add(record);
                } while (scroll.next()//
                        && scroll.getRowNumber() >= fromRecordIndex && scroll.getRowNumber() < maxRecord);
            }
            scroll.last();
        }
        this.totalRecords = scroll.getRowNumber() + 1;
        this.currentPage = pageIndex + 1;
        this.list = results;
        this.maxResult = maxResult;

        if (this.totalRecords % this.maxResult == 0) {
            this.totalPages = this.totalRecords / this.maxResult;
        } else {
            this.totalPages = (this.totalRecords / this.maxResult) + 1;
        }
        this.maxNavigationPage = maxNavigationPage;
        if (maxNavigationPage < totalPages) {
            this.maxNavigationPage = maxNavigationPage;
        }
        this.calcNavigationPages();
    }

    private void calcNavigationPages() {
        this.navigationPages = new ArrayList<Integer>();
        int current = this.currentPage > this.totalPages ? this.totalPages : this.currentPage;
        int begin = current - this.maxNavigationPage / 2;
        int end = current + this.maxNavigationPage / 2;
        navigationPages.add(1);
        if (begin > 2) {
            navigationPages.add(-1);
        }

        for (int i = begin; i < end; i++) {
            if (i > 1 && i < this.totalPages) {
                navigationPages.add(i);
            }
        }

        if (end < this.totalPages - 2) {

            navigationPages.add(-1);
        }
        navigationPages.add(this.totalPages);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<E> getList() {
        return list;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public List<Integer> getNavigationPages() {
        return navigationPages;
    }

}
