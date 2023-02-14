package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter {
    private static final int PAGE_SIZE = 10;

    private User lastFollowee;
    private boolean hasMorePages;
    private boolean isLoading = false;


    public interface View {
        void addLoadingFooter();

        void displayMessage(String s);

        void addItems(List<User> followees);

        void removeLoadingFooter();

        void startActivity(User user);
    }

    private View view;

    private FollowService followService;

    private UserService userService;

    public FollowingPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
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
        userService.getUserTask(userAlias, new GetUserObserver());
    }

    public void loadMoreFollowees(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.addLoadingFooter();

            followService.loadMoreFollowees(user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());
        }
    }

    private class GetFollowingObserver implements FollowService.GetFollowingObserver {

        @Override
        public List<User> getItems(Bundle data) {
            return (List<User>) data.getSerializable(GetFollowingTask.ITEMS_KEY);
        }

        @Override
        public boolean hasMorePages(Bundle data) {
            return data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
        }

        @Override
        public void handleSuccess(List<User> items, boolean hasMorePages) {
            isLoading = false;
            view.removeLoadingFooter();
            lastFollowee = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(items);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.removeLoadingFooter();
            view.displayMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.removeLoadingFooter();
            view.displayMessage("Failed to get following because of exception: " + exception.getMessage());
        }
    }

    private class GetUserObserver implements UserService.GetDataObserver {
        @Override
        public User getData(Bundle data) {
            view.displayMessage("Getting user's profile...");
            return (User) data.getSerializable(GetUserTask.USER_KEY);
        }

        @Override
        public void handleSuccess(User data) {
            view.startActivity(data);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
