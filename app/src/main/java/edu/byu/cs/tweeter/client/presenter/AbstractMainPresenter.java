package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetDataObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.view.MainView;

public abstract class AbstractMainPresenter extends Presenter<MainView> {
    public AbstractMainPresenter(MainView view) {
        super(view);
    }

    public abstract class GetCountObserver extends PresenterObserver implements GetDataObserver<Integer> {
        @Override
        public Integer getData(Bundle data) {
            return data.getInt(GetFollowersCountTask.COUNT_KEY);
        }

        @Override
        void handleError() {

        }
    }

    public abstract class FollowButtonObserver extends PresenterObserver implements SimpleNotificationObserver {
        @Override
        public void handleSuccess() {
            view.updateSUFAF();
            updateFollowButton();
            view.enableFollowButton(true);
        }
        @Override
        void handleError() {
            view.enableFollowButton(true);
        }

        abstract void updateFollowButton();
    }
}
