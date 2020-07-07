package hu.progmasters.gmistore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class ImageService {

    private final Cloudinary cloudinaryConfig;

    @Autowired
    public ImageService(Cloudinary cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }

    public String[] uploadImage(MultipartFile imageToUpload) throws IOException {
        Map uploadResult = cloudinaryConfig.uploader().upload(imageToUpload.getBytes(), ObjectUtils.emptyMap());

        String[] result = new String[2];

        result[0] = ((String) uploadResult.get("public_id"));
        result[1] = ((String) uploadResult.get("url"));

        return result;
    }
}
