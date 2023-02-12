package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {
    private View view;

    private FollowService followService;

    private UserService userService;

    private static final int PAGE_SIZE = 10;

    private User lastFollower;

    private boolean hasMorePages;
    private boolean isLoading = false;

    public interface View {

        void addItems(List<User> follows);

        void removeLoadingFooter();

        void addLoadingFooter();

        void startActivity(User user);

        void displayMessage(String s);
    }

    public FollowersPresenter(View view) {
        this.view = view;
        this.followService = new FollowService();
        this.userService = new UserService();
    }

    public void getUser(String userAlias) {
        userService.getUserTask(userAlias, new GetUserObserver());
    }

    public void loadMoreFollows(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.addLoadingFooter();
            followService.loadMoreFollowers(user, PAGE_SIZE, lastFollower, new FollowersObserver());
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    private class GetUserObserver implements UserService.GetUserObserver {
        @Override
        public User getData(Bundle data) {
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

        @Override
        public void displayMessageUser(String s) {
            view.displayMessage(s);
        }
    }


    private class FollowersObserver implements UserService.Observer, FollowService.Observer {
        @Override
        public void displayMessageFollow(String s) {
            isLoading = false;
            view.removeLoadingFooter();
            view.displayMessage(s);
        }

        @Override
        public void addFollows(List<User> follows, boolean hasMorePages) {
            isLoading = false;
            view.removeLoadingFooter();
            lastFollower = (follows.size() > 0) ? follows.get(follows.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(follows);
        }
    }
}
