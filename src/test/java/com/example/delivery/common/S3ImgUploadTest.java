package com.example.delivery.common;


import com.example.delivery.common.service.ImageUploadService;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;


@SpringBootTest
public class S3ImgUploadTest {

    @Autowired
    private ImageUploadService s3Service;

    @Test
    void 파일_업로드_테스트() throws IOException {
        FileInputStream file = new FileInputStream ("C:/Users/dnjs7/qq.jpg"); // 테스트 위한 로컬 사진 경로
        MockMultipartFile multipartFile = new MockMultipartFile("qq.jpg","qq.jpg","image/jpeg",file);
        String uploadedFileUrl =  s3Service.uploadFile(multipartFile);
        System.out.println("업로드된 링크: " + uploadedFileUrl);
    }

}
