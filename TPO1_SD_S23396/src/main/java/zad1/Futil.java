package zad1;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {

    public static void processDir(String dirName, String resultFileName) {

        try {
            Path workingDirectory = Paths.get(dirName);
            Path resultPath = Paths.get(resultFileName);
            FileChannel targetChannel = FileChannel.open(resultPath, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            Files.walkFileTree(workingDirectory, new SimpleFileVisitor<>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    FileChannel sourceChannel = FileChannel.open(file);
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(256);

                    while (sourceChannel.read(byteBuffer) != -1) {
                        byteBuffer.rewind();
                        CharBuffer charBuffer = Charset.forName("Cp1250").decode(byteBuffer);
                        targetChannel.write(StandardCharsets.UTF_8.encode(charBuffer));

                        byteBuffer.clear();
                    }
                    sourceChannel.close();

                    return FileVisitResult.CONTINUE;
                }
            });

            targetChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Problem with input or output data.");
        }
    }
}
