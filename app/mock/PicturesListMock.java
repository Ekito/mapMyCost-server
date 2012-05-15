package mock;

import java.util.HashMap;
import java.util.Map;

import models.Picture;

public class PicturesListMock {

	public static Map<String, Picture> pictures = new HashMap<String, Picture>();

	static {

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
