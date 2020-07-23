package com.mateusz.jakuszko.roomforyou.mapper;


import com.google.gson.Gson;
import lombok.Getter;

@Getter
public class GsonSingleton {

    private static Gson gson = null;

    private GsonSingleton() {
        gson = new Gson();
    }

    public static Gson getInstance() {
        if (gson == null) {
            synchronized (Gson.class) {
                if (gson == null) {
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}
