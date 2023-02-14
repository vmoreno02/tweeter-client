package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFollowersTask.
 */
public class GetFollowersHandler extends BackgroundTaskHandler<PagedObserver<User>> {
    public GetFollowersHandler(FollowService.GetFollowersObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver<User> observer) {
        List<User> users = observer.getItems(data);
        boolean hasMorePages = observer.hasMorePages(data);
        observer.handleSuccess(users, hasMorePages);
    }
}
