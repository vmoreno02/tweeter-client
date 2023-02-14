package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetDataObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetUserTask.
 */
public class GetUserHandler extends BackgroundTaskHandler<GetDataObserver<User>> {
    public GetUserHandler(UserService.GetUserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetDataObserver<User> observer) {
        User user = observer.getData(data);
        observer.handleSuccess(user);
    }
}
