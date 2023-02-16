package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedStatusPresenter {

    public FeedPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    void loadItems(User user) {
        statusService.loadMoreStatuses(user, PAGE_SIZE, lastItem, new FeedObserver());
    }

    public class FeedObserver extends GetPagedItemsObserver {
        @Override
        String createMessage() {
            return "get feed";
        }
    }
}
