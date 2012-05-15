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

	}

	private static Picture createSamplePicture(String path, String id,
			String contentType) {
		InputStream picture2AsStream = PicturesListMock.class
				.getResourceAsStream(path);

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

	public static Picture addPicture(String id, byte[] bytes, String contentType) {
		Picture picture = new Picture(id, bytes, contentType);

		pictures.put(id, picture);

		return picture;
	}

	public static Picture findPicture(String id) {

		return pictures.get(id);
	}

}
