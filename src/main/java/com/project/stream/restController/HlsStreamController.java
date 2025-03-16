package com.project.stream.restController;

import com.project.stream.service.HlsStreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
public class HlsStreamController {

    private final HlsStreamService streamService;
    // .m3u8 파일 요청 처리
    @GetMapping("/music/getMp3UrlPlayList/{segmentName}/playList.m3u8")
    public ResponseEntity<Resource> getMp3UrlPlayList(@PathVariable String segmentName) throws IOException {
        log.info("getMp3UrlPlayList");
        File segmentFile = streamService.getSegmentFile(segmentName + "/playList.m3u8");

        try {
            // 파일이 존재하지 않는 경우 처리
            if (!segmentFile.exists()) {
                return ResponseEntity.notFound().build();  // 파일이 없을 때 404 반환
            }

            // MP3 파일을 읽을 수 있는 Resource로 변환
            UrlResource resource = new UrlResource(segmentFile.toURI());

            // ResponseEntity로 MP3 파일 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("audio/mpegurl"))
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 오류 발생 시 500 반환
        }
    }



    // .ts 파일 요청 처리 (미디어 세그먼트)
    @GetMapping("/music/getSegmentName")
    public ResponseEntity<Resource> getSegment(@RequestParam String segmentName) throws IOException {
        log.info("getSegmentName");
        File segmentFile = streamService.getSegmentFile(segmentName);
        if (segmentFile.exists()) {
            UrlResource resource = new UrlResource(segmentFile.toURI());

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("video/mp2t"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + segmentFile.getName() + "\"")
                    .body(resource); // .ts 파일을 ResponseEntity로 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 오류 발생 시 500 반환
        }
    }



}
