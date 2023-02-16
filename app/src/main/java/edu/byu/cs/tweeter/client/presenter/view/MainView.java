package edu.byu.cs.tweeter.client.presenter.view;

public interface MainView extends PresenterView {
    void setFollow(boolean isFollower);

    void updateSUFAF();

    void followButtonUpdate(boolean b);

    void enableFollowButton(boolean b);

    void logOutToastAndUser();

    void cancelPostingToast();

    void setFollowerCount(int count);

    void setFolloweeCount(int count);
}
