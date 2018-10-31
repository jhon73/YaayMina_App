package app.yaaymina.com.yaaymina.CommonInterface;

/**
 * Created by ADMIN on 07-Dec-17.
 */

public interface SendTokenCourousol {

    void onTokenSend(String tag, String card_num, String card_holder, String exp_month, String exp_year, String stripe_token, String customer_id);
}
