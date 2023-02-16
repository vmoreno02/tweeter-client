package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedStatusPresenter {
    public StoryPresenter(PagedView<Status> view) {
        super(view);
    }

    @Override
    void loadItems(User user) {
        statusService.loadStories(user, PAGE_SIZE, lastItem, new StoryObserver());
    }

    public class StoryObserver extends GetPagedItemsObserver {
        @Override
        String createMessage() {
            return "get story";
        }
    }
}
