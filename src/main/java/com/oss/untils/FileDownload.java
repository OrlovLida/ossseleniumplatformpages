package com.oss.untils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Attachment;

import static com.oss.configuration.Configuration.CONFIGURATION;

public class FileDownload {
    private static final Logger log = LoggerFactory.getLogger(FileDownload.class);

    private FileDownload() {
    }

    public static void attachDownloadedFileToReport(String fileName) {
        if (ifDownloadDirExists()) {
            File directory = new File(CONFIGURATION.getDownloadDir());
            List<File> listFiles = (List<File>) FileUtils.listFiles(directory, new WildcardFileFilter(fileName), null);
            try {
                for (File file : listFiles) {
                    log.info("Attaching file: {}", file.getCanonicalPath());
                    attachFile(file);
                }
            } catch (IOException e) {
                log.error("Failed attaching files: {}", e.getMessage());
            }
        }
    }

    private static boolean ifDownloadDirExists() {
        File tmpDir = new File(CONFIGURATION.getDownloadDir());
        if (tmpDir.exists()) {
            log.info("Download directory was successfully created");
            return true;
        }
        return false;
    }

    public static boolean checkIfFileIsNotEmpty(String fileName) {
        File directory = new File(CONFIGURATION.getDownloadDir());
        List<File> listFiles = (List<File>) FileUtils.listFiles(directory, new WildcardFileFilter(fileName), null);
        boolean fileIsNotEmpty = false;
        for (File file : listFiles) {
            if (file.length() > 0) {
                fileIsNotEmpty = true;
            }
        }
        return fileIsNotEmpty;
    }

    @Attachment(value = "Exported CSV file")
    public static byte[] attachFile(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }
}
