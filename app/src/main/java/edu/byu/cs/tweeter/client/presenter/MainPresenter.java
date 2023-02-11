package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private View view;
    
    private UserService userService;
    private StatusService statusService;
    private FollowService followService;

    public interface View {
        void setFollow(boolean isFollower);

        void displayMessage(String s);

        void updateSUFAF();

        void followButtonUpdate(boolean b);

        void enableFollowButton(boolean b);

        void logOutToastAndUser();

        void cancelPostingToast();

        void setFollowerCount(int count);

        void setFolloweeCount(int count);
    }
    
    public MainPresenter(View view) {
        this.view = view;
        userService = new UserService();
        statusService = new StatusService();
        followService = new FollowService();
    }

    public void isFollower(User selectedUser) {
        followService.isFollower(selectedUser, new MainObserver());
    }

    public void unfollow(User selectedUser) {
        followService.unfollow(selectedUser, new UnfollowObserver());
    }

    public void follow(User selectedUser) {
        followService.follow(selectedUser, new FollowObserver());
    }

    public void logout() {
        userService.logout(new MainObserver());
    }

    public void postStatus(Status newStatus) {
        statusService.postStatus(newStatus, new MainObserver());
    }

    public void getFollowersCount(User selectedUser, ExecutorService executor) {
        followService.getFollowersCount(selectedUser, executor, new MainObserver());
    }

    public void getFollowingCount(User selectedUser, ExecutorService executor) {
        followService.getFollowingCount(selectedUser, executor, new MainObserver());
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    private class UnfollowObserver implements FollowService.UnfollowObserver {

        @Override
        public void handleSuccess() {
            view.updateSUFAF();
            view.followButtonUpdate(true);
            view.enableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to unfollow: " + message);
            view.enableFollowButton(true);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to unfollow because of exception: " + exception.getMessage());
            view.enableFollowButton(true);
        }
    }
    private class FollowObserver implements FollowService.FollowObserver {

        @Override
        public void handleSuccess() {
            view.updateSUFAF();
            view.followButtonUpdate(false);
            view.enableFollowButton(true);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to follow: " + message);
            view.enableFollowButton(true);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage(exception.getMessage());
            view.enableFollowButton(true);
        }
    }

    private class MainObserver implements FollowService.MainObserver, UserService.MainObserver, StatusService.MainObserver {

        @Override
        public void setFollow(boolean isFollower) {
            view.setFollow(isFollower);
        }

        @Override
        public void setFollowerCount(int count) {
            view.setFollowerCount(count);
        }

        @Override
        public void setFolloweeCount(int count) {
            view.setFolloweeCount(count);
        }

        @Override
        public void displayMessageFollow(String message) {

        }

        @Override
        public void displayMessage(String s) {
            view.displayMessage(s);
        }

        @Override
        public void handleFailure(String message) {
            displayMessage(message);
        }

        @Override
        public void handleException(Exception exception) {
            displayMessage(exception.getMessage());
        }

        @Override
        public void cancelPostingToast() {
            view.cancelPostingToast();
            view.displayMessage("Successfully Posted!");
        }

        @Override
        public void displayMessageUser(String s) {
            view.displayMessage(s);
        }

        @Override
        public void logOutToastAndUser() {
            view.logOutToastAndUser();
        }
    }
}
