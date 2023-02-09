package edu.byu.cs.tweeter.client.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.backgroundTask.FollowService;
import edu.byu.cs.tweeter.client.backgroundTask.FollowTask;

public class FollowHandler extends Handler {
    private FollowService.MainObserver observer;

    public FollowHandler(FollowService.MainObserver observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
        if (success) {
            observer.updateSelectedUserFollowingAndFollowers();
            observer.updateFollowButton(false);
        } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
            observer.displayMessageFollow("Failed to follow: " + message);
        } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
            observer.displayMessageFollow("Failed to follow because of exception: " + ex.getMessage());
        }

        observer.enableFollowButton(true);
    }
}
