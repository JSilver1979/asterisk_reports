package ru.JSilver.asterisk.reports.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.JSilver.asterisk.reports.repos.CallRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/audio")
@Slf4j
public class AudioController {
    private final CallRepository callRepository;

    public AudioController(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    @Value("${audioserver}")
    private String audioServer;

    @GetMapping(value = "/{fileName}")
    public ResponseEntity<?> playAudio(@PathVariable String fileName) throws FileNotFoundException {
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
