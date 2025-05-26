package AudioSending;

public class AudioConverterForDiscord {
    public static boolean convertToDiscordPcm(String inputPath, String outputPath) {
        ProcessBuilder pb = new ProcessBuilder(
            "ffmpeg",
            "-y",
            "-i", inputPath,
            "-ar", "48000",
            "-ac",
            "2",
            "-sample_fmt", "s16",
            "-f", "s16le", // raw PCM format
            "-af", "volume=0.004",
                outputPath
        );
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            process.getInputStream().transferTo(System.out);
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}