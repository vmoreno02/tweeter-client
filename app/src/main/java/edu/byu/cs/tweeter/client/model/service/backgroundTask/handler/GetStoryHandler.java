package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetStoryTask.
 */
public class GetStoryHandler extends BackgroundTaskHandler<PagedObserver<Status>> {
    public GetStoryHandler(StatusService.GetStoryObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver<Status> observer) {
        List<Status> statuses = observer.getItems(data);
        boolean hasMorePages = observer.hasMorePages(data);
        observer.handleSuccess(statuses, hasMorePages);
    }
}
