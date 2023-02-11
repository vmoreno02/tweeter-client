package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetDataObserver;

public class GetFollowersCountHandler extends BackgroundTaskHandler<GetDataObserver<Integer>> {
    public GetFollowersCountHandler(FollowService.GetFollowersCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetDataObserver<Integer> observer) {
        int count = observer.getData(data);
        observer.handleSuccess(count);
    }
}
