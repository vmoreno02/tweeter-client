package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedUserPresenter {
    public FollowersPresenter(PagedView<User> view) {
        super(view);
    }

    @Override
    String createMessage() {
        return "get followers";
    }

    @Override
    void loadItems(User user, PagedPresenter<User>.GetPagedItemsObserver getPagedItemsObserver) {
        followService.loadMoreFollowers(user, PAGE_SIZE, lastItem, getPagedItemsObserver);
    }
}
