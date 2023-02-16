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
import edu.byu.cs.tweeter.client.presenter.view.MainView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends AbstractMainPresenter {
    private final StatusService statusService;
    private final FollowService followService;

    
    public MainPresenter(MainView view) {
        super(view);
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

    public void getFollowersCount(User selectedUser) {
        followService.getFollowersCount(selectedUser, new GetFollowersCountObserver());
    }

    public void getFollowingCount(User selectedUser) {
        followService.getFollowingCount(selectedUser, new GetFollowingCountObserver());
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

    private class GetFollowingCountObserver extends GetCountObserver {

        @Override
        public void handleSuccess(Integer data) {
            view.setFolloweeCount(data);
        }

        @Override
        String createMessage() {
            return "get following count";
        }
    }

    private class GetFollowersCountObserver extends GetCountObserver {

        @Override
        public void handleSuccess(Integer data) {
            view.setFollowerCount(data);
        }

        @Override
        String createMessage() {
            return "get followers count";
        }
    }

    private class PostStatusObserver extends PresenterObserver implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            view.cancelPostingToast();
            view.displayMessage("Successfully Posted!");
        }

        @Override
        String createMessage() {
            return "post status";
        }

        @Override
        void handleError() {

        }
    }

    private class IsFollowerObserver extends PresenterObserver implements GetDataObserver<Boolean> {

        @Override
        public Boolean getData(Bundle data) {
            return data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        }

        @Override
        public void handleSuccess(Boolean data) {
            view.setFollow(data);
        }

        @Override
        String createMessage() {
            return "determine following relationship";
        }

        @Override
        void handleError() {

        }
    }

    private class LogOutObserver extends PresenterObserver implements SimpleNotificationObserver {

        @Override
        String createMessage() {
            return "log out";
        }

        @Override
        void handleError() {

        }

        @Override
        public void handleSuccess() {
            view.logOutToastAndUser();
        }
    }

    private class UnfollowObserver extends FollowButtonObserver {

        @Override
        String createMessage() {
            return "unfollow";
        }

        @Override
        void updateFollowButton() {
            view.followButtonUpdate(true);
        }
    }
    private class FollowObserver extends FollowButtonObserver {

        @Override
        String createMessage() {
            return "follow";
        }

        @Override
        void updateFollowButton() {
            view.followButtonUpdate(false);
        }
    }
}
