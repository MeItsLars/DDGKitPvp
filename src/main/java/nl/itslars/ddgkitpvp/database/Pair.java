package nl.itslars.ddgkitpvp.database;

/**
 * Class for managing pairs. Enables returning 2 values from a method.
 * @param <T> First object of pair
 * @param <U> Second object of pair
 */
public class Pair<T, U> {

    /**
     * The first pair element, of type T
     */
    private T t;

    /**
     * The second pair element, of type U
     */
    private U u;

    /**
     * Creates a new pair
     * @param t the first pair element
     * @param u the second pair element
     */
    public Pair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    /**
     * Returns the first pair element
     * @return the first pair element
     */
    public T getFirst() {
        return t;
    }

    /**
     * Returns the second pair element
     * @return the second pair element
     */
    public U getSecond() {
        return u;
    }
}
