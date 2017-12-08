package newssite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileStub implements MultipartFile {

    private String name;
    private String originalFileName;
    private String contentType;
    private long size;
    private byte[] bytes;

    public MultipartFileStub(String name, String originalFileName, String contentType, long size, byte[] bytes) {
        this.name = name;
        this.originalFileName = originalFileName;
        this.contentType = contentType;
        this.size = size;
        this.bytes = bytes;
    }



    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.originalFileName;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
         throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
