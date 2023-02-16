package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedStatusPresenter {
    public StoryPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    String createMessage() {
        return "get story";
    }

    @Override
    void loadItems(User user, PagedPresenter<Status>.GetPagedItemsObserver getPagedItemsObserver) {
        statusService.loadStories(user, PAGE_SIZE, lastItem, getPagedItemsObserver);
    }
}
