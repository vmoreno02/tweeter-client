package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetDataObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedUserPresenter {
    public FollowingPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    void loadItems(User user) {
        followService.loadMoreFollowees(user, PAGE_SIZE, lastItem, new FollowingObserver());
    }

    public class FollowingObserver extends GetPagedItemsObserver {
        @Override
        String createMessage() {
            return "get following";
        }
    }
}
