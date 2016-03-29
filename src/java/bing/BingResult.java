package bing;

/**
 * This is the superclass of any type of Bing Search Result.
 *
 * @author Rami Ghorab
 */
public class BingResult
{
    private String id;
    
    /**
     * Constructor.
     * @param id
     */
    public BingResult(String id)
    {
        this.id = id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }


}
