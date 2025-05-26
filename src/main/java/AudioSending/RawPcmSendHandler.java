
package AudioSending;

import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RawPcmSendHandler implements AudioSendHandler {
    private final FileInputStream pcmStream;
    private final byte[] buffer = new byte[3840];
    private int lastBytesRead = 0;
    private boolean finished = false;

    public RawPcmSendHandler(String pcmFilePath) throws IOException {
        this.pcmStream = new FileInputStream(pcmFilePath);
    }

    @Override
    public boolean canProvide() {
        if (finished) return false;
        try {
            lastBytesRead = pcmStream.read(buffer, 0, buffer.length);
            if (lastBytesRead == -1) {
                finished = true;
                pcmStream.close();
                return false;
            }
            if (lastBytesRead < buffer.length) {
                java.util.Arrays.fill(buffer, lastBytesRead, buffer.length, (byte) 0);
            }
            return true;
        } catch (IOException e) {
            finished = true;
            return false;
        }
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(buffer, 0, buffer.length);
    }

    @Override
    public boolean isOpus() {
        return false;
    }

    public boolean isDone() {
        return finished;
    }
}