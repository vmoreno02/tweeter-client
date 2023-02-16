package edu.byu.cs.tweeter.client.presenter.view;

import edu.byu.cs.tweeter.model.domain.User;

public interface StartActivityView extends PresenterView {
    void startActivity(User user);
}
