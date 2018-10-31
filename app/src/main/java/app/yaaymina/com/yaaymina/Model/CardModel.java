package app.yaaymina.com.yaaymina.Model;

/**
 * Created by ADMIN on 06-Dec-17.
 */

public class CardModel {

    private String card_id, card_title, stripe_token, card_holder_name, card_number, card_exp_month, card_exp_year, card_user_id, customer_id;
    private String prder_id, ref_no, transaction_ref_no,type;

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_title() {
        return card_title;
    }

    public void setCard_title(String card_title) {
        this.card_title = card_title;
    }

    public String getStripe_token() {
        return stripe_token;
    }

    public void setStripe_token(String stripe_token) {
        this.stripe_token = stripe_token;
    }

    public String getCard_holder_name() {
        return card_holder_name;
    }

    public void setCard_holder_name(String card_holder_name) {
        this.card_holder_name = card_holder_name;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_exp_month() {
        return card_exp_month;
    }

    public void setCard_exp_month(String card_exp_month) {
        this.card_exp_month = card_exp_month;
    }

    public String getCard_exp_year() {
        return card_exp_year;
    }

    public void setCard_exp_year(String card_exp_year) {
        this.card_exp_year = card_exp_year;
    }

    public String getCard_user_id() {
        return card_user_id;
    }

    public void setCard_user_id(String card_user_id) {
        this.card_user_id = card_user_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPrder_id() {
        return prder_id;
    }

    public void setPrder_id(String prder_id) {
        this.prder_id = prder_id;
    }

    public String getRef_no() {
        return ref_no;
    }

    public void setRef_no(String ref_no) {
        this.ref_no = ref_no;
    }

    public String getTransaction_ref_no() {
        return transaction_ref_no;
    }

    public void setTransaction_ref_no(String transaction_ref_no) {
        this.transaction_ref_no = transaction_ref_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
