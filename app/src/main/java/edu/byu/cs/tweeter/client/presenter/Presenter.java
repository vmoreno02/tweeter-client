package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.view.PresenterView;

public abstract class Presenter<T extends PresenterView> {
    protected T view;
    protected UserService userService;

    public Presenter(T view) {
        this.view = view;
        this.userService = new UserService();
    }

    public abstract class PresenterObserver implements ServiceObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to " + createMessage() + ": " + message);
            handleError();
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Failed to " + createMessage() + "because of exception: " + exception.getMessage());
            handleError();
        }
        abstract String createMessage();

        abstract void handleError();
    }


}
