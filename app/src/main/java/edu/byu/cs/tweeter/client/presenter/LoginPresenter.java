package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    private View view;

    private UserService userService;

    public interface View {
        void startActivity(User user);

        void displayMessage(String s);
    }

    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
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

    private class LoginObserver implements UserService.Observer {

        @Override
        public void displayMessageUser(String s) {
            view.displayMessage(s);
        }

        @Override
        public void startActivity(User user) {
            view.startActivity(user);
        }
    }
}
