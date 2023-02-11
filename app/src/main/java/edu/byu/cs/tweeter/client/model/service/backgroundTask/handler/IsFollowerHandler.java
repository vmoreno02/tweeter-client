package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetDataObserver;

public class IsFollowerHandler extends BackgroundTaskHandler<GetDataObserver<Boolean>> {
    public IsFollowerHandler(FollowService.IsFollowerObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetDataObserver<Boolean> observer) {
        boolean b = observer.getData(data);
        observer.handleSuccess(b);
    }
}
