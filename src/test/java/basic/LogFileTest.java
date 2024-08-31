/*
 * Copyright (c) Nostrium contributors
 * License: Apache-2.0
 */
package basic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import online.nostrium.logs.Log;
import online.nostrium.logs.LogItem;
import online.nostrium.logs.LogItems;
import online.nostrium.main.Folder;
import online.nostrium.servers.terminal.TerminalCode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Brito
 * @date: 2024-08-31
 * @location: Germany
 */
public class LogFileTest {

    private static final File folderLog = Folder.getFolderLog();

    @BeforeEach
    public void setUp() {
        // Ensure the test environment is clean before each test
        Log.deleteAllLogs();
    }

    @AfterEach
    public void tearDown() {
        // Clean up after each test: delete all generated log files
        Log.deleteAllLogs();

        // Optionally, delete the folder if it's empty
        if (folderLog.isDirectory() && folderLog.list().length == 0) {
            folderLog.delete();
        }
    }

    @Test
    public void testLogFileCreationAndDeletionForCurrentYear() {
        // Log a message
        Log.write(System.currentTimeMillis(), "app1", TerminalCode.OK, "Test message", "Some data");

        // Check that the folder for the current year was created
        File yearFolder = new File(folderLog, String.valueOf(Year.now().getValue()));
        assertTrue(yearFolder.exists() && yearFolder.isDirectory(), "The folder for the current year should exist.");

        // Check that the log file for today was created
        String logFileName = "log_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".zip";
        File logFile = new File(yearFolder, logFileName);
        assertTrue(logFile.exists(), "The log file for today should exist.");

        // Delete the log folder for the current year
        int currentYear = Year.now().getValue();
        Log.deleteYearLogs(currentYear);

        // Verify the log folder is deleted
        assertFalse(yearFolder.exists(), "The log folder for the current year should be deleted.");
    }

    @Test
    public void testDeleteAllLogs() {
        // Log a message for the current year
        Log.write(System.currentTimeMillis(), "app1", TerminalCode.OK, "Test message 1", "Data 1");
        Log.write(System.currentTimeMillis(), "app2", TerminalCode.OK, "Test message 2", "Data 2");

        // Check that the folder for the current year was created
        File yearFolder = new File(folderLog, String.valueOf(Year.now().getValue()));
        assertTrue(yearFolder.exists() && yearFolder.isDirectory(), "The folder for the current year should exist.");

        // Delete all logs
        Log.deleteAllLogs();

        // Verify all log folders are deleted
        assertFalse(yearFolder.exists(), "The log folder for the current year should be deleted.");
    }

    @Test
    public void testDeleteLogsFromLastNDays() throws Exception {
        // Simulate logging messages for the past 10 days
        for (int i = 10; i > 0; i--) {
            long timestamp = System.currentTimeMillis() - (i * 24 * 60 * 60 * 1000L); // i days ago in milliseconds
            String logMessage = "Log from " + i + " days ago";
            Log.write(timestamp, "app" + i, TerminalCode.OK, logMessage, "Data " + i);
        }

        // Verify that the folder for the current year exists
        File yearFolder = new File(folderLog, String.valueOf(Year.now().getValue()));
        assertTrue(yearFolder.exists() && yearFolder.isDirectory(), "The folder for the current year should exist.");

        // Delete logs from the last 5 days (including today)
        Log.deleteLogsFromLastNDays(5);

        // Verify that logs older than 5 days still exist and logs from the last 5 days (including today) are deleted
        for (int i = 10; i > 0; i--) {
            String logFileName = "log_" + LocalDate.now().minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".zip";
            File logFile = new File(yearFolder, logFileName);
            if (i > 5) {
                assertTrue(logFile.exists(), "The log from " + i + " days ago should still exist.");
            } else {
                assertFalse(logFile.exists(), "The log from " + i + " days ago should have been deleted.");
            }
        }
    }

    @Test
    public void testDeleteNonExistentYearLogs() {
        // Attempt to delete logs for a year with no logs
        int nonExistentYear = Year.now().getValue() - 10;
        Log.deleteYearLogs(nonExistentYear);

        // Verify no exception and no log folder should exist
        File yearFolder = new File(folderLog, String.valueOf(nonExistentYear));
        assertFalse(yearFolder.exists(), "No log folder should exist for the non-existent year.");
    }

    @Test
    public void testLogFileRetentionWhenDeletingLastNDays() throws Exception {
        // Log messages over a range of dates
        LocalDate startDate = LocalDate.now().minusDays(7);
        File yearFolder = new File(folderLog, String.valueOf(Year.now().getValue()));
        yearFolder.mkdirs();

        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            String logFileName = "log_" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".zip";

            // Adjusting this to use LocalDateTime and avoid ZoneOffset issues
            LocalDateTime dateTime = date.atStartOfDay();
            long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();  // Convert to milliseconds

            LogItem logItem = new LogItem(timestamp, "app" + i, TerminalCode.OK, "Log message " + i, "Data " + i);
            LogItems logItems = new LogItems();
            logItems.add(logItem);

            File fileZip = new File(yearFolder, logFileName);
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(fileZip.toPath()))) {
                zos.putNextEntry(new ZipEntry("data.json"));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                zos.write(gson.toJson(logItems).getBytes("UTF-8"));
                zos.closeEntry();
            }
        }

        // Delete logs from the last 3 days (including today)
        Log.deleteLogsFromLastNDays(3);

        // Check that logs from the last 3 days are deleted and logs older than that are retained
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            String logFileName = "log_" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".zip";
            File logFile = new File(yearFolder, logFileName);

            if (date.isBefore(LocalDate.now().minusDays(3))) {
                assertTrue(logFile.exists(), "Logs older than 3 days should still exist.");
            } else {
                assertFalse(logFile.exists(), "Logs from the last 3 days should have been deleted.");
            }
        }
    }
    
    
    
    
    
    
    @Test
    public void testGetListLastDays() throws Exception {
        // Simulate logging messages for the past 5 days
        for (int i = 5; i > 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String logFileName = "log_" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".zip";

            LocalDateTime dateTime = date.atStartOfDay();
            long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

            LogItem logItem = new LogItem(timestamp, "app" + i, TerminalCode.OK, "Log message " + i, "Data " + i);
            LogItems logItems = new LogItems();
            logItems.add(logItem);

            File yearFolder = new File(folderLog, String.valueOf(Year.now().getValue()));
            yearFolder.mkdirs();
            File fileZip = new File(yearFolder, logFileName);
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(fileZip.toPath()))) {
                zos.putNextEntry(new ZipEntry("data.json"));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                zos.write(gson.toJson(logItems).getBytes("UTF-8"));
                zos.closeEntry();
            }
        }

        // Test getListLastDays for the last 3 days
        ArrayList<LogItem> last3DaysLogs = Log.getListLastDays(3);
        assertEquals(3, last3DaysLogs.size(), "Expected 3 logs from the last 3 days");

        // Test getListLastDays for the last 5 days
        ArrayList<LogItem> last5DaysLogs = Log.getListLastDays(5);
        assertEquals(5, last5DaysLogs.size(), "Expected 5 logs from the last 5 days");
    }

    @Test
    public void testGetListLastDaysSpecific() throws Exception {
        // Simulate logging messages for the past 5 days with different TerminalCode values
        TerminalCode[] codes = {TerminalCode.OK, TerminalCode.CRASH, TerminalCode.INVALID};

        for (int i = 5; i > 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            String logFileName = "log_" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".zip";

            LocalDateTime dateTime = date.atStartOfDay();
            long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();

            TerminalCode code = codes[i % codes.length];
            LogItem logItem = new LogItem(timestamp, "app" + i, code, "Log message " + i, "Data " + i);
            LogItems logItems = new LogItems();
            logItems.add(logItem);

            File yearFolder = new File(folderLog, String.valueOf(Year.now().getValue()));
            yearFolder.mkdirs();
            File fileZip = new File(yearFolder, logFileName);
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(fileZip.toPath()))) {
                zos.putNextEntry(new ZipEntry("data.json"));
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                zos.write(gson.toJson(logItems).getBytes("UTF-8"));
                zos.closeEntry();
            }
        }

        // Test getListLastDaysSpecific for the last 3 days and CRASH, INVALID codes
        TerminalCode[] filterCodes = {TerminalCode.CRASH, TerminalCode.INVALID};
        ArrayList<LogItem> filteredLogs = Log.getListLastDaysSpecific(3, filterCodes);

        // We expect only CRASH and INVALID logs, so their count should be 2 (one for each code)
        assertEquals(2, filteredLogs.size(), "Expected 2 logs from the last 3 days with specific TerminalCode values");
    }

}
