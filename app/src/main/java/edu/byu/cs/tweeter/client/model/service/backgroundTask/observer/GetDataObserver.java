package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import android.os.Bundle;

public interface GetDataObserver<T> extends ServiceObserver {
    T getData(Bundle data);

    void handleSuccess(T data);
}
