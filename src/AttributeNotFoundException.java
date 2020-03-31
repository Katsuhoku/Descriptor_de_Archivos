package src;

public class AttributeNotFoundException extends Exception {
    private String notFoundAttributeName;

    public AttributeNotFoundException(String msg, String notFoundAttributeName) {
        super(msg);
        this.notFoundAttributeName = notFoundAttributeName;
    }

    public String getName() {
        return notFoundAttributeName;
    }


    /*
     * Ignore
     */
    private static final long serialVersionUID = 1L;
}