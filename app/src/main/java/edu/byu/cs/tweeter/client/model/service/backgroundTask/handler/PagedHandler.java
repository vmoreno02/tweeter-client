package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;

public class PagedHandler<T> extends BackgroundTaskHandler<PagedObserver<T>> {
    public PagedHandler(PagedObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver<T> observer) {
        List<T> items = observer.getItems(data);
        boolean hasMorePages = observer.hasMorePages(data);
        observer.handleSuccess(items, hasMorePages);
    }
}
