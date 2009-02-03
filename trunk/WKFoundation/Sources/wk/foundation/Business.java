package wk.foundation;

/**
 * An interface for objects having the business name attribute 'business'.
 *
 * @author kieran
 *
 */
public interface Business {
    public static final String BUSINESS = "business";

    public String business();

    public void setBusiness(String value);
}
