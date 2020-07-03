package hu.progmasters.gmistore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class ImageService {

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "des0anwfu",
            "api_key", "797962421181434",
            "api_secret", "luBbkMUxj-XblHRnnOtFl11u5fI"));


    public String[] uploadImage(MultipartFile imageToUpload) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(imageToUpload.getBytes(), ObjectUtils.emptyMap());

        String[] result = new String[2];

        result[0] = ((String) uploadResult.get("public_id"));
        result[1] = ((String) uploadResult.get("url"));

        return result;
    }
}
