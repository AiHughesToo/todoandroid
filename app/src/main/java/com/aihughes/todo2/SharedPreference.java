package com.aihughes.todo2;

/**
 * Created by alexhughes on 10/25/16.
 */


        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.content.SharedPreferences.Editor;
        import com.google.gson.Gson;

public class SharedPreference {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Note> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, Note note) {
        List<Note> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Note>();
        favorites.add(note);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Note note) {
        ArrayList<Note> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(note);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Note> getFavorites(Context context) {
        SharedPreferences settings;
        List<Note> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Note[] favoriteItems = gson.fromJson(jsonFavorites,
                    Note[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Note>(favorites);
        } else
            return null;

        return (ArrayList<Note>) favorites;
    }
}
