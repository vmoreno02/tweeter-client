package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.StatusService;
import edu.byu.cs.tweeter.client.backgroundTask.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {
    private View view;

    private StatusService statusService;

    private UserService userService;

    private static final int PAGE_SIZE = 10;

    private Status lastStatus;

    private boolean hasMorePages;
    private boolean isLoading = false;

    public interface View {
        void addLoadingFooter();

        void removeLoadingFooter();

        void addStatuses(List<Status> statuses);

        void displayMessage(String s);

        void startActivity(User user);
    }

    public FeedPresenter(View view) {
        this.view = view;
        statusService = new StatusService();
        userService = new UserService();
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void getUser(String userAlias) {
        userService.getUserTask(userAlias, new FeedObserver());
    }

    public void loadMoreStatuses(User user) {
        if (!isLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.addLoadingFooter();
            statusService.loadMoreStatuses(user, PAGE_SIZE, lastStatus, new FeedObserver());
        }
    }

    private class FeedObserver implements UserService.Observer, StatusService.Observer {

        @Override
        public void addStatuses(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.removeLoadingFooter();
            lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addStatuses(statuses);
        }

        @Override
        public void displayMessage(String s) {
            isLoading = false;
            view.removeLoadingFooter();
            view.displayMessage(s);
        }

        @Override
        public void displayMessageUser(String s) {
            view.displayMessage(s);
        }

        @Override
        public void startActivity(User user) {
            view.startActivity(user);
        }
    }
}
