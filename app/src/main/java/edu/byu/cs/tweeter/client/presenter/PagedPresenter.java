package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetDataObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter<PagedView<T>> {
    protected static final int PAGE_SIZE = 10;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading = false;

    public PagedPresenter(PagedView<T> view) {
        super(view);
    }

    @Override
    String createMessage() {
        return "get user's profile";
    }

    @Override
    void handleError() {
        isLoading = false;
        view.removeLoadingFooter();
    }

    public void getUser(String userAlias) {
        userService.getUserTask(userAlias, new GetUserObserver());
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.addLoadingFooter();
            loadItems(user, new GetPagedItemsObserver());
        }
    }

    abstract void loadItems(User user, GetPagedItemsObserver getPagedItemsObserver);

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    protected class GetPagedItemsObserver extends PresenterObserver implements PagedObserver<T> {

        @Override
        public List<T> getItems(Bundle data) {
            return (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        }

        @Override
        public boolean hasMorePages(Bundle data) {
            return data.getBoolean(PagedTask.MORE_PAGES_KEY);
        }

        @Override
        public void handleSuccess(List<T> items, boolean hasMorePages) {
            isLoading = false;
            view.removeLoadingFooter();
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(items);
        }
    }

    protected class GetUserObserver extends PresenterObserver implements GetDataObserver<User> {
        @Override
        public User getData(Bundle data) {
            view.displayMessage("Getting user's profile...");
            return (User) data.getSerializable(GetUserTask.USER_KEY);
        }

        @Override
        public void handleSuccess(User data) {
            view.startActivity(data);
        }
    }
}
