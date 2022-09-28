package kz.geekbrains.cloud.common;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.Arrays;

public class Message implements Serializable {

    private String command;
    private File file;

    private FileInfo fileInfo;
    private byte[] data;

    private Path path;

    public Message(String command, File file, byte[] data) {
        this.command = command;
        this.file = file;
        this.data = data;
    }


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public File getFile() {
        return file;
    }

    public Path getPath() {
return this.path = file.toPath();
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void SetFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }


    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "Message{" +
                "command='" + command + '\'' +
                ", file=" + file +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}