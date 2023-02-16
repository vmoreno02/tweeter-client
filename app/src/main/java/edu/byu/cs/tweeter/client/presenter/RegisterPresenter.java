package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.AuthenticateView;

public class RegisterPresenter extends AuthenticatePresenter {
    private final UserService userService;

    public RegisterPresenter(AuthenticateView view) {
        super(view);
        userService = new UserService();
    }

    @Override
    String createMessage() {
        return "register";
    }

    public void register(String firstName, String lastName, String alias, String password,
                         ImageView imageToUpload) {
        userService.register(firstName, lastName, alias, password, imageToBytes(imageToUpload), new AuthenticateObserver());
    }

    public void validateRegistration(String firstName, String lastName, String alias, 
                                     String password, Drawable imageToUpload) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    public String imageToBytes(ImageView imageToUpload) {
        // Convert image to byte array.
        Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /*private class RegisterObserver extends PresenterObserver implements AuthenticationObserver {
        @Override
        public User getAndSetData(Bundle data) {
            User registeredUser = (User) data.getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(RegisterTask.AUTH_TOKEN_KEY);

            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);
            return registeredUser;
        }

        @Override
        public void startActivity(User user) {
            view.startActivity(user);
        }
    }*/
}
