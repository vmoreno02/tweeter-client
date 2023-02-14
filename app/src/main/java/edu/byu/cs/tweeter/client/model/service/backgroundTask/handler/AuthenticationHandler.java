package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticationHandler extends BackgroundTaskHandler<AuthenticationObserver> {
    public AuthenticationHandler(AuthenticationObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, AuthenticationObserver observer) {
        User user = observer.getAndSetData(data);
        observer.startActivity(user);
    }
}
