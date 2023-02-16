package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.StartActivityView;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthenticatePresenter {
    public LoginPresenter(StartActivityView view) {
        super(view);
    }

    public void validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public void login(String alias, String password) {
        userService.login(alias, password, new LoginObserver());
    }

    public class LoginObserver extends AuthenticateObserver {
        @Override
        String createMessage() {
            return "login";
        }
    }
}
