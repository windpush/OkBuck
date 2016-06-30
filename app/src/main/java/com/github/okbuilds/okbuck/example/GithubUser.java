package com.github.okbuilds.okbuck.example;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by Piasy{github.com/Piasy} on 6/28/16.
 */
@AutoValue
public abstract class GithubUser {
    public abstract long id();
    public abstract String login();

    public static TypeAdapter<GithubUser> typeAdapter(final Gson gson) {
        return new AutoValue_GithubUser.GsonTypeAdapter(gson);
    }

    public static GithubUser create(long id, String login) {
        return new AutoValue_GithubUser(id, login);
    }
}
