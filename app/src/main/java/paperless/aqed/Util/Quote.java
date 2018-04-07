package paperless.aqed.Util;

public class Quote {
    private int id;
    private int readStatus;
    private String quoteText;
    private String author;
    private String wikiLink;

    public Quote() {
    }

    public Quote(int id, String text, String author, String link) {
        this.id = id;
        this.quoteText = text;
        this.author = author;
        this.wikiLink = link;
    }

    public Quote(String text, String author, String link, int readStatus) {
        this.quoteText = text;
        this.author = author;
        this.wikiLink = link;
        this.readStatus = readStatus;
    }

    public Quote(int id, String text, String author) {
        this.id = id;
        this.quoteText = text;
        this.author = author;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public int getReadStatus() {
        return this.readStatus;
    }

    public String getQuoteText() {
        return this.quoteText;
    }

    public void setQuoteText(String text) {
        this.quoteText = text;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWikiLink() {
        return this.wikiLink;
    }

    public void setWikiLink(String link) {
        this.wikiLink = link;
    }
}
