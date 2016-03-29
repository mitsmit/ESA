package bing;

import java.util.Vector;

/**
 * This is a collection of Bing Results that belong to a certain type.
 *
 * @author Rami Ghorab
 */
public class BingResultList
{
    private String type;
    private Vector<? extends BingResult> results;
    
    /**
     * Default Constructor. Craetes an empty ResultList with size 50 (can grow beyound this).
     * The type is left as null.
     */
    public BingResultList()
    {
        results = new Vector<BingResult>(50,20);
    }

    /**
     * All-args constructor.
     *
     * @param type
     * @param results
     *
     */
    public BingResultList(String type, Vector<? extends BingResult> results)
    {
        this.type = type;
        this.results = results;
    }

     /**
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Gets the vector that contains the <code>BingResult</code> objects stored in this BingResultList
     *
     * @return the results
     */
    public Vector<? extends BingResult> getResults()
    {
        return results;
    }

    /**
     * Sets the vector that contains <code>BingResult</code> objects.
     * 
     * @param results the results to set
     */
    public void setResults(Vector<? extends BingResult> results)
    {
        this.results = results;
    }

    /**
     * Counts the number of results stored in this BingResultList
     * 
     * @return number of results in this BingResultList
     */
    public int getCount()
    {
        return results.size();
    }

    /**
     * Gets the <code>BingResult</code> at the specified index.
     * This is just a cover method for the internal data structure's get method.
     * <br>It is recommended to use this cover method, just in case the
     * implementation changes sometime and the underlying data structure becomes
     * something else other than a vector.
     *
     * @param index
     * @return the <code>BingResult</code> object at the specified index.
     */
    public BingResult get(int index)
    {
        return results.get(index);
    }
}
