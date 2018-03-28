package com.besafx.app.config;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class DropboxManager {

    private static final String ACCESS_TOKEN = "lwXbn73MQTAAAAAAAAAACtvJCtgSD7Rp5hwd7V8jM2V4O9I8c9javetzqM49b1-Y";
    private final Logger log = LoggerFactory.getLogger(DropboxManager.class);
    private DbxRequestConfig config;

    private DbxClientV2 client;

    public void init() {
        // Create Dropbox client
        log.info("Preparing dropbox client...");
        config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").withUserLocale("en_US").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);
        log.info("Connecting with dropbox client successfully");
    }

    public void createFolder(String path) {
        try {
            client.files().createFolder(path);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @Async("threadMultiplePool")
    public Future<Boolean> uploadFile(MultipartFile file, String path) {
        try {
            log.info("Trying to upload file: " + file.getName());
            log.info("Sleeping for 1 seconds...");
            Thread.sleep(1000);
            client.files().uploadBuilder(path)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(file.getInputStream());
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(false);
        }
    }

    @Async("threadMultiplePool")
    public Future<Boolean> uploadFile(File file, String path) {
        try {
            log.info("Trying to upload file: " + file.getName());
            log.info("Sleeping for 1 seconds...");
            Thread.sleep(1000);
            client.files().uploadBuilder(path)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(new FileInputStream(file));
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(false);
        }
    }

    @Async("threadMultiplePool")
    public Future<Boolean> uploadFile(InputStream inputStream, String fileName, String path) {
        try {
            log.info("Trying to upload file: " + fileName);
            log.info("Sleeping for 5 seconds...");
            Thread.sleep(5000);
            client.files().uploadBuilder(path)
                    .withMode(WriteMode.OVERWRITE)
                    .uploadAndFinish(inputStream);
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(false);
        }
    }

    @Async("threadMultiplePool")
    public Future<Boolean> deleteFile(String path) {
        try {
            log.info("Trying to delete file from path: " + path);
            log.info("Sleeping for 1 seconds...");
            Thread.sleep(1000);
            client.files().delete(path);
            return new AsyncResult<>(true);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AsyncResult<>(false);
        }
    }

    @Async("threadMultiplePool")
    public Future<String> shareFile(String path) {
        SharedLinkMetadata metadata;
        String link = null;
        try {
            log.info("Trying to share file from path: " + path);
            log.info("Sleeping for 1 seconds...");
            Thread.sleep(1000);
            List<SharedLinkMetadata> sharedLinkMetadata = client.sharing()
                    .listSharedLinks()
                    .getLinks()
                    .stream()
                    .filter(row -> row.getPathLower().equalsIgnoreCase(path))
                    .collect(Collectors.toList());
            if(sharedLinkMetadata.isEmpty()){
                log.info("File Not Shared");
                metadata = client.sharing().createSharedLinkWithSettings(path);
                link = metadata.getUrl().replaceAll("dl=0", "raw=1");
            }else{
                log.info("File Already Shared");
                link = sharedLinkMetadata.get(0).getUrl().replaceAll("dl=0", "raw=1");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return new AsyncResult<>(link);
    }
}
