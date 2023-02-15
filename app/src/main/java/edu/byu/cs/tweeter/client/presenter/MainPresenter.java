package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

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
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetDataObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {
    private final View view;
    
    private final UserService userService;
    private final StatusService statusService;
    private final FollowService followService;

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
        followService.isFollower(selectedUser, new IsFollowerObserver());
    }

    public void unfollow(User selectedUser) {
        followService.unfollow(selectedUser, new UnfollowObserver());
    }

    public void follow(User selectedUser) {
        followService.follow(selectedUser, new FollowObserver());
    }

    public void logout() {
        userService.logout(new LogOutObserver());
    }

    public void postStatus(Status newStatus) {
        statusService.postStatus(newStatus, new PostStatusObserver());
    }

    public void getFollowersCount(User selectedUser, ExecutorService executor) {
        followService.getFollowersCount(selectedUser, executor, new GetFollowersCountObserver());
    }

    public void getFollowingCount(User selectedUser, ExecutorService executor) {
        followService.getFollowingCount(selectedUser, executor, new GetFollowingCountObserver());
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

    private class GetFollowingCountObserver implements GetDataObserver<Integer> {

        @Override
        public Integer getData(Bundle data) {
            return data.getInt(GetFollowingCountTask.COUNT_KEY);
        }

        @Override
        public void handleSuccess(Integer data) {
            view.setFolloweeCount(data);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get following count because of exception: " + exception.getMessage());
        }
    }

    private class GetFollowersCountObserver implements GetDataObserver<Integer> {

        @Override
        public Integer getData(Bundle data) {
            return data.getInt(GetFollowersCountTask.COUNT_KEY);
        }

        @Override
        public void handleSuccess(Integer data) {
            view.setFollowerCount(data);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to get followers count because of exception: " + exception.getMessage());
        }
    }

    private class PostStatusObserver implements SimpleNotificationObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to post status because of exception: " + exception.getMessage());
        }

        @Override
        public void handleSuccess() {
            view.cancelPostingToast();
            view.displayMessage("Successfully Posted!");
        }
    }

    private class IsFollowerObserver implements GetDataObserver<Boolean> {

        @Override
        public Boolean getData(Bundle data) {
            return data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        }

        @Override
        public void handleSuccess(Boolean data) {
            view.setFollow(data);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to determine following relationship because of exception: " + exception.getMessage());
        }
    }

    private class LogOutObserver implements SimpleNotificationObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to logout because of exception: " + exception.getMessage());
        }

        @Override
        public void handleSuccess() {
            view.logOutToastAndUser();
        }
    }

    private class UnfollowObserver implements SimpleNotificationObserver {

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
    private class FollowObserver implements SimpleNotificationObserver {

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
}
