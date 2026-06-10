package com.rssh.saintgobaintool.service;

import com.rssh.saintgobaintool.dto.RowEntryDto;
import com.rssh.saintgobaintool.dto.RowEntryResponse;
import com.rssh.saintgobaintool.entity.RowEntry;
import com.rssh.saintgobaintool.repository.ToolRecordRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class ToolService {

    @Autowired
    private ToolRecordRepository toolRecordRepository;

//    public void postRecord(RowEntry rowEntry) {
//        toolRecordRepository.save(rowEntry);
//    }

    public void postRecord(RowEntryDto request) throws IOException {

        RowEntry entry = new RowEntry();

        entry.setDate(request.getDate());
        entry.setDay(entry.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        entry.setContractorName(request.getContractorName());
        entry.setPhoneNumber(request.getPhoneNumber());
        entry.setSiteType(request.getSiteType());
        entry.setLocation(request.getLocation());
        entry.setCompany(request.getCompany());
        entry.setReasonOfUsing(request.getReasonOfUsing());
        entry.setProjectStage(request.getProjectStage());
        entry.setAdditionalDescription(
                request.getAdditionalDescription());

        if (request.getImage() != null
                && !request.getImage().isEmpty()) {

            entry.setImage(
                    request.getImage().getBytes());

            entry.setImageContentType(request.getImage().getContentType());
        }

        toolRecordRepository.save(entry);
    }

    public List<RowEntryResponse> getAllRecords() {

        return toolRecordRepository.findAll(Sort.by(Sort.Order.desc("date"), Sort.Order.desc("sNo")))
                .stream()
                .map(entry -> {

                    RowEntryResponse response =
                            new RowEntryResponse();

                    response.setSNo(entry.getSNo());
                    response.setDate(entry.getDate());
                    response.setDay(entry.getDay());
                    response.setContractorName(
                            entry.getContractorName());
                    response.setPhoneNumber(
                            entry.getPhoneNumber());
                    response.setSiteType(
                            entry.getSiteType());
                    response.setLocation(
                            entry.getLocation());
                    response.setCompany(
                            entry.getCompany());
                    response.setReasonOfUsing(
                            entry.getReasonOfUsing());
                    response.setProjectStage(
                            entry.getProjectStage());
                    response.setAdditionalDescription(
                            entry.getAdditionalDescription());

                    response.setImageUrl(
                            "/row-entry/" +
                                    entry.getSNo() +
                                    "/image");

                    return response;
                })
                .toList();
    }

    public RowEntry findImage(Integer id) {
        return toolRecordRepository.findById(id)
                .orElseThrow();
    }

    public byte[] exportExcel() throws IOException {

        List<RowEntry> list = toolRecordRepository.findAll(Sort.by(Sort.Order.desc("date"), Sort.Order.desc("sNo")));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("RowEntries");

        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = workbook.getCreationHelper();

        // Headers
        String[] headers = {
                "ID", "Date", "Day", "Contractor",
                "Phone", "Site Type", "Location",
                "Company", "Reason", "Stage",
                "Description", "Image"
        };

        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        int rowIdx = 1;

        for (RowEntry r : list) {

            Row row = sheet.createRow(rowIdx);
            row.setHeightInPoints(80); // important for image visibility

            row.createCell(0).setCellValue(r.getSNo());
            row.createCell(1).setCellValue(String.valueOf(r.getDate()));
            row.createCell(2).setCellValue(r.getDay());
            row.createCell(3).setCellValue(r.getContractorName());
            row.createCell(4).setCellValue(r.getPhoneNumber());
            row.createCell(5).setCellValue(r.getSiteType());
            row.createCell(6).setCellValue(r.getLocation());
            row.createCell(7).setCellValue(r.getCompany());
            row.createCell(8).setCellValue(r.getReasonOfUsing());
            row.createCell(9).setCellValue(r.getProjectStage());
            row.createCell(10).setCellValue(r.getAdditionalDescription());

            // ✅ IMAGE COLUMN (LAST COLUMN)
            if (r.getImage() != null && r.getImage().length > 0) {

                int pictureIdx = workbook.addPicture(
                        r.getImage(),
                        Workbook.PICTURE_TYPE_JPEG
                );


                ClientAnchor anchor = helper.createClientAnchor();

                anchor.setRow1(rowIdx);
                anchor.setCol1(11);      // image column
                anchor.setRow2(rowIdx + 1);
                anchor.setCol2(12);

                // important: fix positioning inside cell
                anchor.setDx1(10);
                anchor.setDy1(10);
                anchor.setDx2(200);
                anchor.setDy2(200);

                Picture picture = drawing.createPicture(anchor, pictureIdx);

                picture.resize(0.6); // adjust 0.4 - 0.8 based on need
            }

            rowIdx++;
        }

        // auto size columns (except image column)
        for (int i = 0; i < 11; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

}
