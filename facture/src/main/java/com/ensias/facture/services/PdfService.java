package com.ensias.facture.services;

import com.ensias.facture.dto.FactureDto;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] genererFacturePdf(FactureDto facture) {
        try {
            // Lire le logo depuis resources/images/logo1.jpg
            InputStream logoStream = getClass().getResourceAsStream("/images/logo1.jpg");
            byte[] logoBytes = logoStream.readAllBytes();
            String base64Logo = Base64.getEncoder().encodeToString(logoBytes);

            // Préparer le contexte Thymeleaf
            Context context = new Context();
            context.setVariable("facture", facture);
            context.setVariable("logoBase64", base64Logo);

            // Générer le HTML depuis le template
            String htmlContent = templateEngine.process("facture", context); // "facture.html" dans templates

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.withHtmlContent(htmlContent, null);
                builder.toStream(outputStream);
                builder.run();
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
}



