package app.yaaymina.com.yaaymina.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 24-Nov-17.
 */

public class ProductListModel {

    private String product_id,category_id,product_code,product_tag,product_name,product_rating,product_description;
    private String product_overview,product_specifications,product_minorder,color_option,size_option;
    private String product_cost,product_tax,product_price,product_price_cross,product_currency,product_availability;
    private String product_status,thumbnail,product_updated,product_entered,category_title,category_title_ar,image_url;
    private String product_name_ar,product_description_ar, measurement;
    private String tag_img;
    private Double product_weight;
    private int product_interval;
    private List<TagItemModel> tagItemModels = new ArrayList<>();


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_tag() {
        return product_tag;
    }

    public void setProduct_tag(String product_tag) {
        this.product_tag = product_tag;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_rating() {
        return product_rating;
    }

    public void setProduct_rating(String product_rating) {
        this.product_rating = product_rating;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_overview() {
        return product_overview;
    }

    public void setProduct_overview(String product_overview) {
        this.product_overview = product_overview;
    }

    public String getProduct_specifications() {
        return product_specifications;
    }

    public void setProduct_specifications(String product_specifications) {
        this.product_specifications = product_specifications;
    }

    public Double getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(Double product_weight) {
        this.product_weight = product_weight;
    }

    public String getProduct_minorder() {
        return product_minorder;
    }

    public void setProduct_minorder(String product_minorder) {
        this.product_minorder = product_minorder;
    }

    public String getColor_option() {
        return color_option;
    }

    public void setColor_option(String color_option) {
        this.color_option = color_option;
    }

    public String getSize_option() {
        return size_option;
    }

    public void setSize_option(String size_option) {
        this.size_option = size_option;
    }

    public String getProduct_cost() {
        return product_cost;
    }

    public void setProduct_cost(String product_cost) {
        this.product_cost = product_cost;
    }

    public String getProduct_tax() {
        return product_tax;
    }

    public void setProduct_tax(String product_tax) {
        this.product_tax = product_tax;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_price_cross() {
        return product_price_cross;
    }

    public void setProduct_price_cross(String product_price_cross) {
        this.product_price_cross = product_price_cross;
    }

    public String getProduct_currency() {
        return product_currency;
    }

    public void setProduct_currency(String product_currency) {
        this.product_currency = product_currency;
    }

    public String getProduct_availability() {
        return product_availability;
    }

    public void setProduct_availability(String product_availability) {
        this.product_availability = product_availability;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getProduct_updated() {
        return product_updated;
    }

    public void setProduct_updated(String product_updated) {
        this.product_updated = product_updated;
    }

    public String getProduct_entered() {
        return product_entered;
    }

    public void setProduct_entered(String product_entered) {
        this.product_entered = product_entered;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public String getCategory_title_ar() {
        return category_title_ar;
    }

    public void setCategory_title_ar(String category_title_ar) {
        this.category_title_ar = category_title_ar;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getProduct_name_ar() {
        return product_name_ar;
    }

    public void setProduct_name_ar(String product_name_ar) {
        this.product_name_ar = product_name_ar;
    }

    public String getProduct_description_ar() {
        return product_description_ar;
    }

    public void setProduct_description_ar(String product_description_ar) {
        this.product_description_ar = product_description_ar;
    }

    public void addDataObject(TagItemModel o) {
        tagItemModels.add(o);
    }

    public List<TagItemModel> getTagItemModels() {
        return tagItemModels;
    }

    public void setTagItemModels(List<TagItemModel> tagItemModels) {
        this.tagItemModels = tagItemModels;
    }

    public String getTag_img() {
        return tag_img;
    }

    public void setTag_img(String tag_img) {
        this.tag_img = tag_img;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
