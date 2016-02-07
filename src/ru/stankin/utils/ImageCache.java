package ru.stankin.utils;

import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.HashMap;

/**
 * Created by Dislike on 07.02.2016.
 */
public class ImageCache {
    private static final ImageCache ourInstance = new ImageCache();
    private static final String IMAGE_PATH = "resources/image/";


    public static ImageCache getInstance() {
        return ourInstance;
    }

    private final HashMap<String, ImageView> images;

    private ImageCache() {
        images = new HashMap<>();
        load();
    }

    private void load() {
        try {
            File imagesDirectory = new File(IMAGE_PATH);
            if (!imagesDirectory.exists())
                throw new FileNotFoundException("dirrectory " + IMAGE_PATH + " is not found");
            final FilenameFilter filter = (dir, name) -> name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
            for (File image : imagesDirectory.listFiles(filter))
                images.put(image.getName(), new ImageView(image.toURI().toURL().toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ImageView getByName(String name) {
        return images.getOrDefault(name, null);
    }

}
