package com.ortim.core.utils;

import java.util.Arrays;
import java.util.List;

public class DbConstants {

    //BASE MODEL
    public static final String tableUpdatedDate = "updated_date";
    public static final String tableCreatedDate = "created_date";
    public static final String tableId = "id";
    public static final String offSet = "+03:00";

    // USER TABLE
    public static final String userTableName = "users";
    public static final String userEmail = "email";
    public static final String userName = "name";
    public static final String userLastName = "lastName";
    public static final String userPassword = "password";
    public static final String userIsActive = "isActive";
    public static final String userIsDeleted = "isDeleted";
    public static final String userForCancel = "forCancel";
    public static final String userAddressColumnName = "address_id";
    //USER_ROLE TABLE
    public static final String userRoleTableName = "user_roles";
    public static final String userRoleTableNameUserId = "user_id";
    public static final String userRoleTableNameRoleId = "role_id";
    // ROLE TABLE
    public static final String roleTableName = "roles";
    public static final String roleTableColumnName = "name";
    public static final String roleTableColumnDescription = "description";
    //SOCIAL ACCOUNT TABLE
    public static final String socialAccountTableName = "social_accounts";
    public static final String socialAccountTableUserId = "user_id";
    public static final String socialAccountTableProvider = "provider";
    public static final String socialAccountTableProviderId = "provider_id";
    public static final String socialAccountTableProfileImageThumbnail = "thumbnail";
    public static final String socialAccountTableFullName = "full_name";
    // ADDRESS TABLE
    public static final String addressTableName = "addresses";
    public static final String addressCountryColumnName = "country";
    public static final String addressCityColumnName = "city";
    public static final String addressCountyColumnName = "county";
    public static final String addressNeighbourhoodColumnName = "neighbourhood";
    public static final String addressStreetColumnName = "street";
    public static final String addressDetailColumnName = "detail";
    public static final String addressZipCodeColumnName = "zipCode";
    public static final String addressUserColumnName = "user_id";
    // PRODUCT
    public static final String productTableName = "products";
    public static final String productNameColumnName = "name";
    public static final String productTitleColumnName = "title";
    public static final String productDescriptionColumnName = "description";
    public static final String productCurrencyColumnName = "currency";
    public static final String productCategoryColumnName = "category";
    public static final String productIsPublishedColumnName = "is_published";
    public static final String productSkuColumnName = "sku";
    public static final String productBrandColumnName = "brand";
    public static final String productThumbnailUrlColumnName = "thumbnail_url";
    public static final String productIsPriorityColumnName = "is_priority";
    public static final String productTagsColumnName = "tags";
    public static final String productMapped = "product";
    public static final String productTagsCollectionTableName = "product_tags";
    // PRODUCT_IMAGES
    public static final String productVariantImagesTableName = "product_variant_images";
    public static final String productVariantImagesProductColumnName = "product_id";
    public static final String productVariantImagesImageColumnName = "image_id";
    // PRODUCT_VARIANT
    public static final String productVariantTableName="product_variants";
    public static final String productVariantNameColumnName = "variant_name";
    public static final String productVariantPriceColumnName = "variant_price";
    public static final String productVariantPurchasePriceColumnName = "purchase_price";
    public static final String productVariantQuantityColumnName = "variant_quantity";
    public static final String productVariantProductColumnName = "product_id";
    public static final String productVariantDiscountColumnName = "discount";
    public static final String productVariantStockStatusColumnName = "stock_status";
    public static final String productVariantWeightColumnName = "weight";
    public static final String productVariantDimensionsColumnName = "dimensions";
    public static final String productVariantMappedVariantOptions = "productVariant";
    // PRODUCT_VARIANT_OPTION
    public static final String variantOptionTableName = "variant_options";
    public static final String variantOptionKeyColumnName = "option_key";
    public static final String variantOptionValueColumnName = "option_value";
    public static final String variantOptionProductVariantColumnName = "product_variant_id";


    // CATEGORY
    public static final String categoryTableName = "categories";
    public static final String categoryNameColumnName = "name";
    public static final String categoryDescriptionColumnName = "description";
    public static final String categoryIsActiveColumnName = "is_active";
    public static final String categoryParentCategoryColumnName = "parent_category_id";
    public static final String mappedCategoryProduct = "category";
    // IMAGE
    public static final String imageTableName = "images";
    public static final String imagePathName = "path";
    // CART
    public static final String cartTableName = "carts";
    public static final String cartUserColumnName = "user_id";
    public static final String cartsProductsCartColumnName = "cart_id";
    public static final String cartsProductsProductColumnName = "product_id";
    public static final String cartIsActiveColumnName = "is_active";
    //CART ITEM
    public static final String cartItemTableName = "cart_items";
    public static final String cartItemCartIdColumnName = "cart_id";
    public static final String cartItemProductIdColumnName = "product_id";
    public static final String cartItemProductQuantityColumnName = "quantity";

    // ORDER
    public static final String orderTableName = "orders";
    public static final String orderUserColumnName = "user_id";
    //OTHER CONSTANTS
    public static final int minPasswordSize = 8;
    public static final int textShortSize = 30;
    public static final int textTallSize = 50;
    public static final int textGrandeSize = 90;
    public static final int textVentiSize = 250;
    public static final int textLongContentSize=500;
    public static final int currencyPrecisionSize = 4;
    public static final int currencyLengthSize = 10;
    public static final int image_count = 10;
    // FİLE CONSTANTS
    public static final List<String> allowedFileExtension = Arrays.asList("jpeg", "jpg", "gif", "png");
    public static final List<String> allowedVideoFileExtension = Arrays.asList("avi", "mpeg", "3gp", "mp4", "mov");
    public static final List<String> allowedMimeTypeExtension = Arrays.asList("image/jpeg", "image/gif", "image/png", "video/x-msvideo", "video/mpeg", "video/3gpp", "video/mp4", "video/quicktime");

    // RESET PASSWORD TOKEN
    public static final String resetPasswordTokenTableName = "reset_password_token";
    public static final String resetPasswordTokenUserColumnName = "user_id";
    public static final String resetPasswordTokenExpiryDateColumnName = "expiry_date";



}