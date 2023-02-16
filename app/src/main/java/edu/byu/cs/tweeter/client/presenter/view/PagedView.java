package edu.byu.cs.tweeter.client.presenter.view;

import java.util.List;

public interface PagedView<T> extends StartActivityView {
    void addItems(List<T> follows);

    void removeLoadingFooter();

    void addLoadingFooter();
}
