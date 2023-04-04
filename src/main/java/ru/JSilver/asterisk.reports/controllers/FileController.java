package ru.JSilver.asterisk.reports.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import ru.JSilver.asterisk.reports.dto.DateSearchDto;
import ru.JSilver.asterisk.reports.services.DownloadReportService;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final DownloadReportService downloadService;

    @PostMapping("/get_report")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestBody DateSearchDto searchDto) {
        byte[] buf = downloadService.getSomeData(searchDto.getYear(), searchDto.getMonth(), searchDto.getGroup()).getBytes();

        InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(buf));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf("text/csv"));
        httpHeaders.setContentLength(buf.length);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity<>(isr,httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/get_reportV3")
    public ResponseEntity<InputStreamResource> downloadDetailedFile(@RequestBody DateSearchDto searchDto) {
        byte[] buf = downloadService.getDetailedStrings(searchDto.getYear(), searchDto.getMonth(), searchDto.getGroup()).getBytes();

        InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(buf));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.valueOf("text/csv"));
        httpHeaders.setContentLength(buf.length);
        httpHeaders.setCacheControl(CacheControl.noCache().getHeaderValue());
        return new ResponseEntity<>(isr,httpHeaders, HttpStatus.OK);
    }
}
