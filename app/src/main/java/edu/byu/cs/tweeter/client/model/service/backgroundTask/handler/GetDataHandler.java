package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetDataObserver;

public class GetDataHandler<T> extends BackgroundTaskHandler<GetDataObserver<T>> {
    public GetDataHandler(GetDataObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetDataObserver<T> observer) {
        T object = observer.getData(data);
        observer.handleSuccess(object);
    }
}
