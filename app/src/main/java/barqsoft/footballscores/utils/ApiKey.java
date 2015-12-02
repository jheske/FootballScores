/*
 * Created by Jill Heske
 *
 * Copyright (c) 2015
 */
package barqsoft.footballscores.utils;

/**
 * Created by jill on 8/17/2015.
 *
 * This file is in .gitignore so it never gets included in the repo.
 */
public class ApiKey {
    private static final String apiKey = "3f5f9480569645639496c418bb21ca75";

    public static String getApiKey() {
        return apiKey;
    }

    /**
     * Make ApiKey a utility class by preventing instantiation.
     */
    private ApiKey() {
        throw new AssertionError();
    }
}

