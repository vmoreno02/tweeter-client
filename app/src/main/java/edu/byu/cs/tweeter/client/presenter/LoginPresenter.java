package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.AuthenticateView;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends AuthenticatePresenter {
    public interface View {
        void startActivity(User user);

        void displayMessage(String s);
    }

    public LoginPresenter(AuthenticateView view) {
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
        userService.login(alias, password, new AuthenticateObserver());
    }

    @Override
    String createMessage() {
        return "login";
    }

 /*   private class LoginObserver extends PresenterObserver implements AuthenticationObserver {
        @Override
        public User getAndSetData(Bundle data) {
            User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            return loggedInUser;
        }

        @Override
        public void startActivity(User user) {
            view.startActivity(user);
        }
    }*/
}
