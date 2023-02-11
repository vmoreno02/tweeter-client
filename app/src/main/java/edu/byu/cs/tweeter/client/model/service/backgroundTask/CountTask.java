package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class CountTask extends AuthenticatedTask {
    public static final String COUNT_KEY = "count";

    private static final int count = 20;

    /**
     * The user whose following count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;

    public CountTask(Handler messageHandler, User targetUser, AuthToken authToken) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    @Override
    protected void prepSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, count);
    }

    @Override
    protected void processTask() {

    }
}
