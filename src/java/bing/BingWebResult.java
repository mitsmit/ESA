package bing;

/**
 * An object of this class represents a Bing Search Result of Type: Web.
 *
 * @author Rami Ghorab
 */
public class BingWebResult extends BingResult
{
    private String title;
    private String description;
    private String URL;
    private String displayURL;
    private String lastUpdatedDateTime;
    
    /**
     * All-args constructor.
     * @param id
     * @param title
     * @param description
     * @param URL
     * @param displayURL
     * @param lastUpdatedDateTime
     */
    public BingWebResult(String id, String title, String description, String URL, String displayURL, String lastUpdatedDateTime)
    {
        super(id);
        
        this.title = title;
        this.description = description;
        this.URL = URL;
        this.displayURL = displayURL;
        this.lastUpdatedDateTime = lastUpdatedDateTime;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description (known in the framework as the snippet, not the document summary)
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description (known in the framework as the snippet, not the document summary)

     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the URL
     */
    public String getURL() {
        return URL;
    }

    /**
     * @param URL the URL to set
     */
    public void setURL(String URL) {
        this.URL = URL;
    }

    /**
     * @return the displayURL
     */
    public String getDisplayURL() {
        return displayURL;
    }

    /**
     * @param displayURL the displayURL to set
     */
    public void setDisplayURL(String displayURL) {
        this.displayURL = displayURL;
    }

    /**
     * @return the lastUpdatedDateTime
     */
    public String getLastUpdatedDateTime() {
        return lastUpdatedDateTime;
    }

    /**
     * @param lastUpdatedDateTime the lastUpdatedDateTime to set
     */
    public void setLastUpdatedDateTime(String lastUpdatedDateTime) {
        this.lastUpdatedDateTime = lastUpdatedDateTime;
    }


}
