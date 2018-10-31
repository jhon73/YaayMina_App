package app.yaaymina.com.yaaymina.Storage;

/**
 * Created by ADMIN on 06-Dec-17.
 */

public class CardType {

    public static final String VISA ="^4[0-9]{6,}$";
    public static final String MASTER_CARD ="^5[1-5][0-9]{5,}|222[1-9][0-9]{3,}|22[3-9][0-9]{4,}|2[3-6][0-9]{5,}|27[01][0-9]{4,}|2720[0-9]{3,}$";
    public static final String AMERICAN_EXPRESS ="^3[47][0-9]{5,}$";
    public static final String DINERS_CLUB ="^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
    public static final String DISCOVER ="^6(?:011|5[0-9]{2})[0-9]{3,}$";
    public static final String JCB ="^(?:2131|1800|35[0-9]{3})[0-9]{3,}$ ";

}
