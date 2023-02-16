package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.AuthenticateTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.client.presenter.view.StartActivityView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthenticatePresenter extends Presenter<StartActivityView> {
    public AuthenticatePresenter(StartActivityView view) {
        super(view);
    }

    @Override
    String createMessage() {
        return null;
    }

    @Override
    void handleError() {

    }

    protected class AuthenticateObserver extends PresenterObserver implements AuthenticationObserver {

        @Override
        public User getAndSetData(Bundle data) {
            User authenticatedUser = (User) data.getSerializable(AuthenticateTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(AuthenticateTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(authenticatedUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            return authenticatedUser;
        }

        @Override
        public void startActivity(User user) {
            view.startActivity(user);
        }
    }
}
