package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import android.os.Bundle;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public interface AuthenticationObserver extends ServiceObserver {
    User getAndSetData(Bundle data);
    void startActivity(User user);
}
