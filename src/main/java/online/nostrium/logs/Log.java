/*
 * Control the log information
 *
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package online.nostrium.logs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import online.nostrium.main.Folder;

import java.io.*;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import static online.nostrium.main.Folder.nameSystem;
import online.nostrium.servers.terminal.TerminalCode;
import static online.nostrium.servers.terminal.TerminalCode.CRASH;
import static online.nostrium.servers.terminal.TerminalCode.INCOMPLETE;
import static online.nostrium.servers.terminal.TerminalCode.INVALID;
import static online.nostrium.servers.terminal.TerminalCode.NOT_FOUND;

/**
 * Logs can get huge. For that reason we compress the log files and make to
 * split them according to day and year.
 *
 * Messages arriving to the log should permit add-on translations. For that
 * reason, it is important to separate the message text from the variable data
 * inside.
 *
 * @author Brito
 * @date: 2024-08-31
 * @location: Germany
 */
public class Log {

    static File folderLog = Folder.getFolderLog();

    /**
     * Get or create the folder for the current year.
     *
     * @return the File representing the folder for the current year.
     */
    public static File getFolderForCurrentYear() {
        File folderYear = new File(folderLog, String.valueOf(Year.now().getValue()));

        if (!folderYear.exists()) {
            folderYear.mkdirs();
        }

        return folderYear;
    }

    /**
     * Generate the ZIP file for the given timestamp.
     *
     * @param timestamp the timestamp for which the ZIP file is generated.
     * @return the File representing the ZIP file for that day.
     */
    public static File getZipFileForTimestamp(long timestamp) {
        LocalDate date = LocalDate.ofEpochDay(timestamp / (24 * 60 * 60 * 1000L));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String filename = "log_" + date.format(formatter) + ".zip";
        return new File(getFolderForCurrentYear(), filename);
    }

    /**
     * Write JSON line to the archive.
     *
     * @param code operation code.
     * @param text template text that can be translated.
     * @param data numeric values or varying data.
     */
    public static void write(TerminalCode code, String text, String data) {
        write(nameSystem, code, text, data);
    }

    /**
     * Write JSON line to the archive.
     *
     * @param id to which app id this message belongs.
     * @param code operation code.
     * @param text template text that can be translated.
     * @param data numeric values or varying data.
     */
    public static void write(String id, TerminalCode code, String text, String data) {
        long timestamp = System.currentTimeMillis();
        write(timestamp, id, code, text, data);
    }

    /**
     * Write JSON line to the archive.
     *
     * @param timestamp when the event happened.
     * @param id to which app id this message belongs.
     * @param code operation code.
     * @param text template text that can be translated.
     * @param data numeric values or varying data.
     */
    public static void write(long timestamp, String id, TerminalCode code, String text, String data) {
        LogItem logItem = new LogItem(timestamp, id, code, text, data);
        File fileZip = getZipFileForTimestamp(timestamp);

        LogItems logItems = loadLogItemsFromZip(fileZip);
        logItems.add(logItem);

        writeLogItemsToZip(fileZip, logItems);
        System.out.println(id + " | " + code + " | " + text + ": " + data);
    }

    /**
     * Load LogItems from the specified ZIP file.
     *
     * @param fileZip the ZIP file to read from.
     * @return the LogItems loaded from the ZIP file.
     */
    private static LogItems loadLogItemsFromZip(File fileZip) {
        LogItems logItems = new LogItems();

        if (!fileZip.exists()) {
            return logItems;
        }

        try (ZipFile zipFile = new ZipFile(fileZip)) {
            ZipEntry entry = zipFile.getEntry("data.json");
            if (entry == null) {
                return logItems;
            }

            try (InputStream is = zipFile.getInputStream(entry); InputStreamReader isr = new InputStreamReader(is)) {
                Gson gson = new Gson();
                logItems = gson.fromJson(isr, LogItems.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logItems;
    }

    /**
     * Write LogItems to the specified ZIP file.
     *
     * @param fileZip the ZIP file to write to.
     * @param logItems the LogItems to write.
     */
    private static void writeLogItemsToZip(File fileZip, LogItems logItems) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileZip))) {
            zos.putNextEntry(new ZipEntry("data.json"));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            zos.write(gson.toJson(logItems).getBytes("UTF-8"));
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method 1: Delete a specific year's log folder
    public static void deleteYearLogs(int year) {
        File folderYear = new File(folderLog, String.valueOf(year));
        if (folderYear.exists() && folderYear.isDirectory()) {
            boolean deleted = deleteDirectory(folderYear);
            if (deleted) {
                System.out.println("Log folder for year " + year + " deleted.");
            } else {
                System.out.println("Failed to delete log folder for year " + year + ".");
            }
        } else {
            System.out.println("Log folder for year " + year + " does not exist.");
        }
    }

    // Method 2: Delete all logs from all years
    public static void deleteAllLogs() {
        File[] folders = folderLog.listFiles(File::isDirectory);
        if (folders != null) {
            for (File folder : folders) {
                boolean deleted = deleteDirectory(folder);
                if (deleted) {
                    System.out.println("Deleted: " + folder.getName());
                } else {
                    System.out.println("Failed to delete: " + folder.getName());
                }
            }
        }
    }

    // Method 3: Delete logs from the last NN days
    public static void deleteLogsFromLastNDays(int days) {
        LocalDate thresholdDate = LocalDate.now().minusDays(days);
        int currentYear = Year.now().getValue();

        for (int year = currentYear; year >= thresholdDate.getYear(); year--) {
            File folderYear = new File(folderLog, String.valueOf(year));
            if (folderYear.exists() && folderYear.isDirectory()) {
                File[] zipFiles = folderYear.listFiles((dir, name) -> name.startsWith("log_") && name.endsWith(".zip"));
                if (zipFiles != null) {
                    for (File zipFile : zipFiles) {
                        String datePart = zipFile.getName().substring(4, 14);
                        LocalDate entryDate = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        if (!entryDate.isBefore(thresholdDate)) {
                            zipFile.delete();
                        }
                    }
                }
            }
        }
    }

    /**
     * Recursively deletes a directory and its contents.
     *
     * @param directoryToBeDeleted the directory to delete.
     * @return true if the directory was deleted, false otherwise.
     */
    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    public static ArrayList<LogItem> getListToday() {
        return getListLastDays(1);
    }

    public static ArrayList<LogItem> getListLastDaysErrors(int numberOfDays) {
        TerminalCode[] errors = new TerminalCode[]{
            CRASH, INVALID, INCOMPLETE, NOT_FOUND
        };
        return getListLastDaysSpecific(numberOfDays, errors);
    }

    public static ArrayList<LogItem> getListLastDays(int numberOfDays) {
        ArrayList<LogItem> result = new ArrayList<>();
        LocalDate thresholdDate = LocalDate.now().minusDays(numberOfDays);
        int currentYear = Year.now().getValue();

        for (int year = currentYear; year >= thresholdDate.getYear(); year--) {
            File yearFolder = new File(folderLog, String.valueOf(year));
            if (yearFolder.exists() && yearFolder.isDirectory()) {
                File[] zipFiles = yearFolder.listFiles((dir, name) -> name.startsWith("log_") && name.endsWith(".zip"));
                if (zipFiles != null) {
                    for (File zipFile : zipFiles) {
                        String datePart = zipFile.getName().substring(4, 14);
                        LocalDate entryDate = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        if (!entryDate.isBefore(thresholdDate)) {
                            result.addAll(readLogItemsFromZip(zipFile));
                        }
                    }
                }
            }
        }

        return result;
    }

    public static ArrayList<LogItem> getListLastDaysSpecific(int numberOfDays, TerminalCode[] errors) {
        ArrayList<LogItem> result = new ArrayList<>();
        List<TerminalCode> errorList = List.of(errors);
        LocalDate thresholdDate = LocalDate.now().minusDays(numberOfDays);
        int currentYear = Year.now().getValue();

        for (int year = currentYear; year >= thresholdDate.getYear(); year--) {
            File yearFolder = new File(folderLog, String.valueOf(year));
            if (yearFolder.exists() && yearFolder.isDirectory()) {
                File[] zipFiles = yearFolder.listFiles((dir, name) -> name.startsWith("log_") && name.endsWith(".zip"));
                if (zipFiles != null) {
                    for (File zipFile : zipFiles) {
                        String datePart = zipFile.getName().substring(4, 14);
                        LocalDate entryDate = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        if (!entryDate.isBefore(thresholdDate)) {
                            for (LogItem item : readLogItemsFromZip(zipFile)) {
                                if (errorList.contains(item.getCode())) {
                                    result.add(item);
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    private static ArrayList<LogItem> readLogItemsFromZip(File zipFile) {
        ArrayList<LogItem> logItems = new ArrayList<>();
        try (ZipFile zip = new ZipFile(zipFile)) {
            ZipEntry entry = zip.getEntry("data.json");
            if (entry != null) {
                try (var is = zip.getInputStream(entry); var isr = new InputStreamReader(is)) {
                    Gson gson = new Gson();
                    LogItems loadedItems = gson.fromJson(isr, LogItems.class);
                    logItems.addAll(loadedItems.getList());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logItems;
    }

    public static ArrayList<LogItem> getListLastItems(int numberOfLogItems) {
        ArrayList<LogItem> result = new ArrayList<>();

        File[] yearFolders = folderLog.listFiles(File::isDirectory);
        if (yearFolders != null) {
            // Sort the folders by year in descending order
            Arrays.sort(yearFolders, (a, b) -> b.getName().compareTo(a.getName()));

            for (File yearFolder : yearFolders) {
                File[] logFiles = yearFolder.listFiles((dir, name) -> name.endsWith(".zip"));
                if (logFiles != null) {
                    // Sort the log files by date in descending order
                    Arrays.sort(logFiles, (a, b) -> b.getName().compareTo(a.getName()));

                    for (File logFile : logFiles) {
                        LogItems logItems = loadLogItems(logFile);
                        if (logItems != null && !logItems.getList().isEmpty()) {
                            // Add the items in reverse order to get the most recent first
                            List<LogItem> items = logItems.getList();
                            for (int i = items.size() - 1; i >= 0; i--) {
                                result.add(items.get(i));
                                if (result.size() >= numberOfLogItems) {
                                    return result;
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    private static LogItems loadLogItems(File zipFile) {
        LogItems logItems = new LogItems();

        if (!zipFile.exists()) {
            return logItems;
        }

        try (ZipFile zip = new ZipFile(zipFile)) {
            ZipEntry entry = zip.getEntry("data.json");
            if (entry == null) {
                return logItems;
            }

            try (InputStream is = zip.getInputStream(entry); InputStreamReader isr = new InputStreamReader(is)) {
                Gson gson = new Gson();
                logItems = gson.fromJson(isr, LogItems.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return logItems;
    }

}
