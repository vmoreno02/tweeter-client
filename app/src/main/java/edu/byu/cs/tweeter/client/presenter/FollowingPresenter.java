package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.backgroundTask.FollowService;
import edu.byu.cs.tweeter.client.backgroundTask.UserService;
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
        userService.getUserTask(userAlias, new FollowingObserver());
    }

    public void loadMoreFollowees(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.addLoadingFooter();

            followService.loadMoreFollowees(user, PAGE_SIZE, lastFollowee, new FollowingObserver());
        }
    }

    private class FollowingObserver implements UserService.Observer, FollowService.Observer {

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
            lastFollowee = (follows.size() > 0) ? follows.get(follows.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(follows);
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
