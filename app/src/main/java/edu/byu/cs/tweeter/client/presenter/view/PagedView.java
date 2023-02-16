package edu.byu.cs.tweeter.client.presenter.view;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView<T> extends StartActivityView {
    void addItems(List<T> follows);

    void removeLoadingFooter();

    void addLoadingFooter();
}
