package com.project.stream.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class HlsStreamServiceImpl implements HlsStreamService {

    @Value("${upload.path}")
    private String uploadPath;


    // .ts 세그먼트 파일을 반환
    public File getSegmentFile(String segmentName) {
        return new File(uploadPath + segmentName);
    }


}
