package com.extractor;

import java.io.IOException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;

public class Handler implements RequestHandler<String, List<String>> {

    @Override
    public List<String> handleRequest(String pdfContent, Context context) {
        try {
            InputStream inputStream = new ByteArrayInputStream(pdfContent.getBytes());
            ObjectExtractor extractor = new ObjectExtractor(inputStream);
            int totalPages = extractor.getDocumentMetadata().getPageCount();

            List<String> extractedTables = new ArrayList<>();

            for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
                Page page = extractor.extract(pageNumber);

                List<RectangularTextContainer> tables = page.getTables();
                for (RectangularTextContainer table : tables) {
                    // Process each table
                    String tableText = table.getText();
                    extractedTables.add(tableText);
                }
            }

            return extractedTables;
        } catch (IOException e) {
            // Handle IOException
            throw new RuntimeException("Error extracting tables: " + e.getMessage());
        }
    }
}
