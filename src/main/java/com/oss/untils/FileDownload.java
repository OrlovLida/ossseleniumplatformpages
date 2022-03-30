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
            try {
                for (File file : getFilesList(fileName)) {
                    log.info("Attaching file: {}", file.getCanonicalPath());
                    attachFileWithExtension(file);
                }
            } catch (IOException e) {
                log.error("Failed attaching files: {}", e.getMessage());
            }
        }
    }

    private static boolean ifDownloadDirExists() {
        if (downloadDirectory().exists()) {
            log.info("Download directory was successfully created");
            return true;
        }
        return false;
    }

    private static File downloadDirectory() {
        return new File(CONFIGURATION.getDownloadDir());
    }

    private static List<File> getFilesList(String fileName) {
        return (List<File>) FileUtils.listFiles(downloadDirectory(), new WildcardFileFilter(fileName), null);
    }

    private static String getFileExtension(File file) throws IOException {
        return file.getCanonicalPath().split("\\.")[1];
    }

    private static void attachFileWithExtension(File file) throws IOException {
        if (getFileExtension(file).equalsIgnoreCase("xlsx")) {
            attachXlsxFile(file);
        } else {
            attachFile(file);
        }
    }

    public static boolean checkIfFileIsNotEmpty(String fileName) {
        boolean fileIsNotEmpty = false;
        for (File file : getFilesList(fileName)) {
            if (file.length() > 0) {
                fileIsNotEmpty = true;
            }
        }
        return fileIsNotEmpty;
    }

    @Attachment(value = "Exported XLSX file", fileExtension = ".xlsx")
    public static byte[] attachXlsxFile(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    @Attachment(value = "Exported file")
    public static byte[] attachFile(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }
}
