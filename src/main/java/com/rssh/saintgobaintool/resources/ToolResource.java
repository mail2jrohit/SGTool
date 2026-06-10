package com.rssh.saintgobaintool.resources;

import com.rssh.saintgobaintool.dto.RowEntryDto;
import com.rssh.saintgobaintool.dto.RowEntryResponse;
import com.rssh.saintgobaintool.entity.RowEntry;
import com.rssh.saintgobaintool.service.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin("*")
@Slf4j
@RestController
public class ToolResource {

    @Autowired
    private ToolService toolService;

    @GetMapping
    public ResponseEntity<List<RowEntryResponse>> getAllRecords() {
        return new ResponseEntity<List<RowEntryResponse>>(toolService.getAllRecords(), HttpStatus.OK);
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> save(
            @ModelAttribute RowEntryDto request)
            throws IOException {
        toolService.postRecord(request);
        return new ResponseEntity<String>("Created", HttpStatus.CREATED);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(
            @PathVariable Integer id) {

        RowEntry entry = toolService.findImage(id);

        if (entry.getImage() == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = entry.getImageContentType();
        if (contentType == null || contentType.isBlank()) {
            contentType = "image/jpeg"; // fallback
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(entry.getImage());
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel() throws IOException {

        byte[] file = toolService.exportExcel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=row-entry.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
