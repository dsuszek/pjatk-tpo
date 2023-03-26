package zad1;

public class Request {
    private String wordToBeTranslated;
    private String languageCode;
    private int port;

    Request(String wordToBeTranslated, String languageCode, int port) {
        this.wordToBeTranslated = wordToBeTranslated;
        this.languageCode = languageCode;
        this.port = port;
    }

    public void setWordToBeTranslated(String wordToBeTranslated) {
        this.wordToBeTranslated = wordToBeTranslated;
    }

    public String getWordToBeTranslated() {
        return wordToBeTranslated;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}
