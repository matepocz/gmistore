package hu.progmasters.gmistore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class ImageService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
    private final Cloudinary cloudinaryConfig;

    @Autowired
    public ImageService(Cloudinary cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }

    /**
     * Uploads an image to the cloud
     *
     * @param imageToUpload The actual image file.
     * @return A String array, containing a public id and
     * the actual url for the image uploaded to the cloud
     * @throws IOException This method can throw IOException
     */
    public String[] uploadImage(MultipartFile imageToUpload) throws IOException {
        Map uploadResult = cloudinaryConfig.uploader().upload(imageToUpload.getBytes(), ObjectUtils.emptyMap());

        String[] result = new String[2];

        result[0] = ((String) uploadResult.get("public_id"));
        result[1] = ((String) uploadResult.get("secure_url"));

        return result;
    }

    /**
     * Attempts to destroy an image in the cloud.
     *
     * @param imageUrl The given image's URL
     * @return A boolean, true if successful, false otherwise
     */
    public boolean destroyImage(String imageUrl) {
        String publicId = getPublicId(imageUrl);
        try {
            Map destroy = cloudinaryConfig.uploader().destroy(publicId, ObjectUtils.emptyMap());
            if (destroy.get("result").equals("ok")) {
                LOGGER.debug("Image has been deleted, {}", publicId);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.debug("Could not delete the given image from the cloud, public id: {}", publicId);
            return false;
        }
    }

    /**
     * Retrieves the public id from the image URL
     *
     * @param imageUrl The given image URL
     * @return A public_id for the cloudinary Service (String)
     */
    private String getPublicId(String imageUrl) {
        String fromUpload = imageUrl.substring(imageUrl.indexOf("upload"));
        int afterSlash = fromUpload.indexOf('/', 7);
        String fromLastSlash = fromUpload.substring(afterSlash);
        int indexOfDot = fromLastSlash.indexOf('.');
        return fromLastSlash.substring(1, indexOfDot);
    }
}
