package mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import models.Picture;

import org.apache.commons.io.IOUtils;

import play.Logger;

public class PicturesListMock {

    public static Map<String, Picture> pictures = new HashMap<String, Picture>();

    static {
        // load sample images
        createSamplePicture("/mock/fnac.jpg", "67890", "image/jpeg");
        createSamplePicture("/mock/caissiere.jpg", "13579", "image/jpeg");
        createSamplePicture("/mock/77777.jpg", "77777", "image/jpeg");
        createSamplePicture("/mock/99999.jpg", "99999", "image/jpeg");
        createSamplePicture("/mock/100001.jpg", "100001", "image/jpeg");
        createSamplePicture("/mock/100003.jpg", "100003", "image/jpeg");
        createSamplePicture("/mock/100007.jpg", "100007", "image/jpeg");
        createSamplePicture("/mock/100019.jpg", "100019", "image/jpeg");
        createSamplePicture("/mock/100023.jpg", "100023", "image/jpeg");
        createSamplePicture("/mock/100024.jpg", "100024", "image/jpeg");
        createSamplePicture("/mock/big-mac.jpg", "11111", "image/jpeg");
        createSamplePicture("/mock/midica.jpg", "98765", "image/jpeg");
        createSamplePicture("/mock/celiosport.jpg", "33333", "image/jpeg");

    }

    private static Picture createSamplePicture(final String path, final String id, final String contentType) {
        InputStream picture2AsStream = PicturesListMock.class.getResourceAsStream(path);

        if (picture2AsStream != null) {
            try {
                Logger.debug("Loading mock image");
                byte[] bytes = IOUtils.toByteArray(picture2AsStream);

                return addPicture(id, bytes, contentType);

            } catch (IOException e) {
                Logger.error("Error loading an image", e);
            }
        } else {
            Logger.error("Image not found");
        }
        return null;
    }

    public static Picture addPicture(final String id, final byte[] bytes, final String contentType) {
        Picture picture = new Picture(id, bytes, contentType);

        pictures.put(id, picture);

        return picture;
    }

    public static Picture findPicture(final String id) {

        return pictures.get(id);
    }

}
