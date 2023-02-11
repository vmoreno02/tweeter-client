package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import android.os.Bundle;

public interface GetDataObserver<T> extends ServiceObserver {
    public T getData(Bundle data);

    void handleSuccess(T data);
}
