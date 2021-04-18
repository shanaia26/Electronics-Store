package com.example.electronics_store.Common;

import com.example.electronics_store.Model.Users;

public class Common {
    public static Users currentUser;

    public static final String UserPhoneKey = "UserPhone";
    public static final String UserPasswordKey = "UserPassword";


    public static final String ErrorKey = "Something went wrong, Please try again.";
    public static final String EmptyCredentialsKey = "Please provide the full details.";
    public static final String PasswordTooShortKey = "Password too short.";

    public static final String RegisterSuccessKey = "Congratulations! Your account has been made successfully.";
    public static final String RegisterFailKey = "Please try again using another phone number.";
    public static final String NumberRegisteredAlreadyKey = "This number is already registered with an account.";

    public static final String LoginSuccessKey = "Logged in successfully.";
    public static final String LoginFailKey = "Log in failed. Account Does not exist.";

    public static final String ItemRemovedSuccessKey = "Item removed successfully.";
    public static final String OrderPlacedSuccessKey = "Your order has been placed successfully.";

    public static final String ImageRequiredKey = "Product image required.";
    public static final String ImageUploadSuccessKey = "Image uploaded successfully.";
    public static final String ProductAddedSuccessKey = "Product added successfully.";

    public static final String AccountDeletedKey = "Account Deleted.";
    public static final String ProfileUpdateSuccessKey = "Profile info updated.";
    public static final String ChangesSuccessKey = "Changes applied successfully.";

}
