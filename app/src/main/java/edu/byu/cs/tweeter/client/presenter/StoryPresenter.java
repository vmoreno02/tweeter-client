package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {
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

    public StoryPresenter(View view) {
        this.view = view;
        statusService = new StatusService();
        userService = new UserService();
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

    public void getUser(String userAlias) {
        userService.getUserTask(userAlias, new GetUserObserver());
    }

    public void loadStories(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.addLoadingFooter();

            statusService.loadStories(user, PAGE_SIZE, lastStatus, new GetStoryObserver());
        }
    }

    private class GetStoryObserver implements StatusService.GetStoryObserver {

        @Override
        public List<Status> getItems(Bundle data) {
            return (List<Status>) data.getSerializable(GetStoryTask.ITEMS_KEY);
        }

        @Override
        public boolean hasMorePages(Bundle data) {
            return data.getBoolean(GetStoryTask.MORE_PAGES_KEY);
        }

        @Override
        public void handleSuccess(List<Status> items, boolean hasMorePages) {
            isLoading = false;
            view.removeLoadingFooter();
            lastStatus = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addStatuses(items);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.removeLoadingFooter();
            view.displayMessage("Failed to get story: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.removeLoadingFooter();
            view.displayMessage("Failed to get story because of exception: " + exception.getMessage());
        }
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
}
