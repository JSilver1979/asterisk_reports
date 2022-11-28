package ru.JSilver.asterisk_reports.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.JSilver.asterisk_reports.dto.*;
import ru.JSilver.asterisk_reports.services.NativeCallService;


@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class CallController {
    private final NativeCallService nativeCallService;

    @PostMapping("/date_search")
    public NativeStatsDto searchByDate(@RequestBody DateSearchDto dateSearch)
    {
        return nativeCallService.getNativeStats(dateSearch.getDateFrom(), dateSearch.getDateTo());
    }
}
