package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import android.os.Bundle;

import java.util.List;

public interface PagedObserver<T> extends ServiceObserver {
    List<T> getItems(Bundle data);
    boolean hasMorePages(Bundle data);
    void handleSuccess(List<T> items, boolean hasMorePages);
}
