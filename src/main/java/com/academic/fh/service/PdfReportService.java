package com.academic.fh.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfReportService {

    private static final Font TITLE_FONT = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(33, 37, 41));
    private static final Font SUBTITLE_FONT = new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(108, 117, 125));
    private static final Font HEADER_FONT = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font CELL_FONT = new Font(Font.HELVETICA, 9, Font.NORMAL, new Color(33, 37, 41));
    private static final Color PRIMARY_COLOR = new Color(13, 110, 253);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);

    public byte[] generarReporte(String titulo, String subtitulo, String[] headers, List<String[]> datos) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, baos);

            document.open();

            // Encabezado
            agregarEncabezado(document, titulo, subtitulo);

            // Tabla
            if (!datos.isEmpty()) {
                PdfPTable table = crearTabla(headers, datos);
                document.add(table);
            } else {
                Paragraph noData = new Paragraph("No se encontraron registros con los criterios seleccionados.",
                        SUBTITLE_FONT);
                noData.setAlignment(Element.ALIGN_CENTER);
                noData.setSpacingBefore(30);
                document.add(noData);
            }

            // Pie de página
            agregarPiePagina(document, datos.size());

            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }

        return baos.toByteArray();
    }

    private void agregarEncabezado(Document document, String titulo, String subtitulo) throws DocumentException {
        Paragraph titlePara = new Paragraph(titulo, TITLE_FONT);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        titlePara.setSpacingAfter(5);
        document.add(titlePara);

        Paragraph subPara = new Paragraph(subtitulo, SUBTITLE_FONT);
        subPara.setAlignment(Element.ALIGN_CENTER);
        subPara.setSpacingAfter(5);
        document.add(subPara);

        String fechaGeneracion = "Generado el: "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        Paragraph fechaPara = new Paragraph(fechaGeneracion, new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY));
        fechaPara.setAlignment(Element.ALIGN_CENTER);
        fechaPara.setSpacingAfter(20);
        document.add(fechaPara);
    }

    private PdfPTable crearTabla(String[] headers, List<String[]> datos) throws DocumentException {
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(PRIMARY_COLOR);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(8);
            table.addCell(cell);
        }

        boolean alternar = false;
        for (String[] fila : datos) {
            for (String valor : fila) {
                PdfPCell cell = new PdfPCell(new Phrase(valor != null ? valor : "", CELL_FONT));
                cell.setBackgroundColor(alternar ? LIGHT_GRAY : Color.WHITE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(6);
                table.addCell(cell);
            }
            alternar = !alternar;
        }

        return table;
    }

    private void agregarPiePagina(Document document, int totalRegistros) throws DocumentException {
        Paragraph footer = new Paragraph();
        footer.setSpacingBefore(20);

        Chunk totalChunk = new Chunk("Total de registros: " + totalRegistros,
                new Font(Font.HELVETICA, 10, Font.BOLD, PRIMARY_COLOR));
        footer.add(totalChunk);
        footer.setAlignment(Element.ALIGN_RIGHT);

        document.add(footer);
    }
}
