package ru.JSilver.asterisk.reports.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/audio")
@RequiredArgsConstructor
@Slf4j
public class AudioController {

    @Value("${audioserver}")
    private String audioServer;

    @GetMapping(value = "/{fileName}")
    //TODO: create audio service with file download feature
    //TODO: move streaming logic from controller to service
    public ResponseEntity<InputStreamResource> playAudio(@PathVariable String fileName) throws FileNotFoundException {
        String[] fileArray = fileName.split("-");
        if (fileArray.length > 4) {
            String year = fileArray[3].substring(0, 4);
            String month = fileArray[3].substring(4, 6);
            String day = fileArray[3].substring(6);
            String file = audioServer + "\\" + year + "\\" + month + "\\" + day + "\\" + fileName;
            long length = new File(file).length();

            InputStreamResource isr = new InputStreamResource(new FileInputStream(file));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentLength(length);
            httpHeaders.setContentType(MediaType.valueOf("audio/wav"));
            httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
            return new ResponseEntity<>(isr, httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
