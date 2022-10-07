package kz.geekbrains.cloud.server.service;

import lombok.extern.log4j.Log4j2;

import java.io.File;

@Log4j2
public class CreateUserRep {
    public static void createFolder(String pathname) {
        File folder = new File(pathname);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                log.info("Folder " + folder.getPath() + " created");
            } else {
                log.error("can't create server repository : " + folder.getPath());
            }

        }
    }
}
