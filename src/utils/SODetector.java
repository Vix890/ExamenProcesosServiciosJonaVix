package utils;

public class SODetector {

    public String detectarSO() {
        String sistemaOperativo = System.getProperty("os.name").toLowerCase();

        if (sistemaOperativo.contains("win")) {
            return "win";
        } else if (sistemaOperativo.contains("nix") || sistemaOperativo.contains("nux")) {
            return "lin";
        } else if (sistemaOperativo.contains("mac")) {
            return "mac";
        } else {
            return "none";
        }
    }
}
