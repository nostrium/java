/*
 * Convert BoM to Markdown format
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.utils.deployment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import online.nostrium.utils.FileFunctions;

/**
 * @author Brito
 * @date: 2024-09-28
 * @location: Germany
 */
public class BoMconverter {

    static JsonArray components;

    public static void convertBomToMarkdown(String jsonFilePath, String outputFilePath) throws IOException {
        File jsonFile = new File(jsonFilePath);
        if(jsonFile.exists() == false){
            System.err.println("Couldn't find the Bill Of Materials file");
            return;
        }
        Gson gson = new Gson();
        JsonObject rootNode = gson.fromJson(new FileReader(jsonFile), JsonObject.class);

        // StringBuilder for overall document
        StringBuilder markdown = new StringBuilder("# Bill of Materials\n\n");

        String intro = """
                       This document provides a detailed Bill of Materials (BoM)
                       including metadata, component details, and licensing
                       information for the project.
                       
                       We would like to thank the open source community for the
                       available software that permits this platform to exist.
                       """;

        // Introduction to the BoM
        markdown.append(intro)
                .append("\n\n");

        // Add generation timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        markdown.append("Generated on ").append(timestamp).append(".\n\n");

        // Collect license information
        Map<String, Integer> licenseCountMap = new HashMap<>();

        // StringBuilder for license summary
        StringBuilder licenseSummary = new StringBuilder("## Summary of Licenses Used by Components\n\n");
        licenseSummary.append("```\n");
        licenseSummary.append("    +------------------------------+-------+\n");
        licenseSummary.append("    | License                      | Count |\n");
        licenseSummary.append("    +------------------------------+-------+\n");

        // Process components and license counting
        components = rootNode.getAsJsonArray("components");
        for (int i = 0; i < components.size(); i++) {
            JsonObject component = components.get(i).getAsJsonObject();
            if (component.has("licenses")) {
                JsonArray licenses = component.getAsJsonArray("licenses");
                for (JsonElement licenseElement : licenses) {
                    JsonObject license = licenseElement.getAsJsonObject();
                    if (license.has("license")) {
                        JsonObject licenseObj = license.getAsJsonObject("license");
                        if (licenseObj.has("id")) {
                            String licenseId = licenseObj.get("id").getAsString();
                            licenseCountMap.put(licenseId, licenseCountMap.getOrDefault(licenseId, 0) + 1);
                        } else if (licenseObj.has("name")) {
                            String licenseName = licenseObj.get("name").getAsString();
                            licenseCountMap.put(licenseName, licenseCountMap.getOrDefault(licenseName, 0) + 1);
                        }
                    }
                }
            }
        }

        // Sort license counts in descending order and format the output
        licenseCountMap.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Sort by count descending
                .forEach(entry -> {
                    String formattedLicenseName = formatLicenseName(entry.getKey());
                    licenseSummary.append(String.format("    | %-28s | %5d |\n", formattedLicenseName, entry.getValue()));
                });

        licenseSummary.append("    +------------------------------+-------+\n");
        licenseSummary.append("```\n\n");

        // Append license summary to main document
        markdown.append(licenseSummary);

        // StringBuilder for metadata
        StringBuilder metadata = new StringBuilder("## Metadata\n\n");
        metadata.append("- BOM Format: ").append(rootNode.get("bomFormat").getAsString()).append("\n");
        metadata.append("- Spec Version: ").append(rootNode.get("specVersion").getAsString()).append("\n");
        metadata.append("- Serial Number: ").append(rootNode.get("serialNumber").getAsString()).append("\n");
        metadata.append("- Version: ").append(rootNode.get("version").getAsString()).append("\n");

        if (rootNode.has("metadata")) {
            JsonObject meta = rootNode.getAsJsonObject("metadata");
            if (meta.has("timestamp")) {
                metadata.append("- Timestamp: ").append(meta.get("timestamp").getAsString()).append("\n");
            }
            if (meta.has("tools")) {
                JsonArray tools = meta.getAsJsonArray("tools");
                if (tools.size() > 0) {
                    metadata.append("- Tool: ").append(tools.get(0).getAsJsonObject().get("name").getAsString()).append("\n");
                }
            }
            if (meta.has("component")) {
                JsonObject component = meta.getAsJsonObject("component");
                if (component.has("name")) {
                    metadata.append("- Component: ").append(component.get("name").getAsString()).append("\n");
                }
                if (component.has("version")) {
                    metadata.append("  - Version: ").append(component.get("version").getAsString()).append("\n");
                }
                if (component.has("description")) {
                    metadata.append("  - Description: ").append(component.get("description").getAsString()).append("\n");
                }
            }
        }
        markdown.append(metadata).append("\n");

        // Add source code statistics
        markdown.append("## Summary of Source Code\n\n");
        markdown.append(getSourceCodeStatistics("./src/")); // Using ./src/ as root
        markdown.append("\n");

        // Extra line before detailed components
        markdown.append("\n");

        // StringBuilder for detailed component information
        markdown.append("# Detailed Components\n\n");

        // start by listing the components
        for (int i = 0; i < components.size(); i++) {
            JsonObject component = components.get(i).getAsJsonObject();
            String name = component.get("name").getAsString();
            String version = component.get("version").getAsString();
            String license = getLicenseInfo(component);
            String result = "+ " + name + " " + version;
            if(license != null && license.isEmpty() == false){
                result += " (" + license + ")";
            }
            result += "\n";
            markdown.append(result);
        }

        markdown.append("\n");

        for (int i = 0; i < components.size(); i++) {
            JsonObject component = components.get(i).getAsJsonObject();
            markdown.append("### ").append(i + 1).append(". ").append(component.get("name").getAsString()).append("\n");
            markdown.append("- Group: ").append(component.has("group") ? component.get("group").getAsString() : "N/A").append("\n");
            markdown.append("- Type: ").append(component.get("type").getAsString()).append("\n");
            markdown.append("- Version: ").append(component.get("version").getAsString()).append("\n");
            if (component.has("purl")) {
                markdown.append("- PURL: ").append(component.get("purl").getAsString()).append("\n");
            }
            if (component.has("description")) {
                markdown.append("- Description: ").append(component.get("description").getAsString()).append("\n");
            }
            if (component.has("copyright")) {
                markdown.append("- Copyright: ").append(component.get("copyright").getAsString()).append("\n");
            }
            markdown.append("\n");
        }

        // Write to file
        File outputDir = new File(outputFilePath).getParentFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write(markdown.toString());
        }

        System.out.println("BoM converted to Markdown and saved in " + outputFilePath);
    }

    // Method to calculate source code statistics
    private static String getSourceCodeStatistics(String directoryPath) throws IOException {
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return "Invalid directory path.\n";
        }

        ArrayList<File> files = FileFunctions.searchFiles(directory, ".java");

        int fileCount = 0;
        int totalLines = 0;
        long totalSize = 0;

        // Check files in the directory
        for (File file : files) {
            if (file.isFile()) {
                fileCount++;
                totalSize += file.length();
                totalLines += Files.readAllLines(file.toPath()).size();
            }
        }

        return String.format("- Number of source files: %d\n"
                + "- Lines of Code: %d\n"
                + "- Overall project size: %.2f KB\n"
                + "- 3rd party components: %d\n",
                 fileCount, totalLines, totalSize / 1024.0, components.size());
    }

    // Method to format license names
    private static String formatLicenseName(String licenseName) {
        final int maxLength = 30;
        if (licenseName.length() > maxLength) {
            int mid = maxLength / 2;
            return licenseName.substring(0, mid - 3) + "..." + licenseName.substring(licenseName.length() - mid + 3);
        }
        return licenseName;
    }

    public static void main(String[] args) {
        // Call the conversion method
        try {
            convertBomToMarkdown("target/bom.json", "docs/bom.md");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLicenseInfo(JsonObject component) {
        ArrayList<String> list = new ArrayList<>();
        if (component.has("licenses") == false) {
            return null;
        }
        JsonArray licenses = component.getAsJsonArray("licenses");
        for (JsonElement licenseElement : licenses) {
            JsonObject license = licenseElement.getAsJsonObject();
            if (license.has("license")) {
                JsonObject licenseObj = license.getAsJsonObject("license");
                if (licenseObj.has("id")) {
                    String licenseId = licenseObj.get("id").getAsString();
                    list.add(licenseId);
                } else if (licenseObj.has("name")) {
                    String licenseName = licenseObj.get("name").getAsString();
                    list.add(licenseName);
                }
            }
        }
        return String.join(", ", list);
    }
}
