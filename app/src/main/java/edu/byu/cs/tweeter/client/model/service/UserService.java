package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetDataHandler;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.AuthenticationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.GetDataObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService extends Service {
    public void getUserTask(String userAlias, GetDataObserver<User> observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetDataHandler<>(observer));
        execute(getUserTask);
    }

    public void login(String alias, String password, AuthenticationObserver observer) {
        LoginTask loginTask = new LoginTask(alias, password, new AuthenticationHandler(observer));
        execute(loginTask);
    }

    public void register(String firstName, String lastName, String alias, String password,
                         String imageBytesBase64, AuthenticationObserver observer) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthenticationHandler(observer));

        execute(registerTask);
    }

    public void logout(SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new SimpleNotificationHandler(observer));
        execute(logoutTask);
    }
}
