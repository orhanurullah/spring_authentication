package com.ortim.core.utils;

public class RouteConstants {

    // AUTHENTİCATION
    public static final String authBaseRoute = "/api/v1/auth";
    public static final String authRegisterRoute = authBaseRoute + "/register";
    public static final String authLoginRoute = authBaseRoute + "/login";
    public static final String authActivationRoute= authBaseRoute + "/email-activation";
    public static final String authResendActivationRoute = authBaseRoute + "/re-email-activation";
    public static final String authRefreshTokenRoute = authBaseRoute + "/refresh-token";
    public static final String authLogoutRoute = "/api/v1/user/logout";

    //PUBLIC
    public static final String publicRoute = "api/v1/public";
    public static final String publicHomeRoute = publicRoute + "/home";

    // ROOT ROLE
    public static final String requiredRootAuthority = "hasAuthority('ROLE_ROOT')";
    // =========== users ================================== //
    public static final String primaryRoleBaseRoute = "/api/v1/manager";
    public static final String userDeleteRoute = primaryRoleBaseRoute + "/users/{id}/delete";
    public static final String userAddRoleRoute = primaryRoleBaseRoute + "/users/{id}/add-role";
    public static final String userDeleteRoleRoute = primaryRoleBaseRoute + "/users/{id}/delete-role";
    public static final String userFindAllRoute = primaryRoleBaseRoute + "/users";
    public static final String userFindAllDeletedUsersRoute = primaryRoleBaseRoute + "/users/deleted-users";

    // ============ roles ================================ //
    public static final String roleFindByNameRoute = primaryRoleBaseRoute + "/roles/search-by-name";
    public static final String roleFindByIdRoute = primaryRoleBaseRoute + "/roles/search-by-id";
    public static final String roleCreateRoute = primaryRoleBaseRoute + "/roles/create";
    public static final String roleFindAllRoute = primaryRoleBaseRoute + "/roles";
    public static final String usersHasRoleRoute = primaryRoleBaseRoute + "/roles/users";
    public static final String roleUpdateRoute = primaryRoleBaseRoute + "/roles/{id}/update";
    public static final String roleDeleteRoute = primaryRoleBaseRoute + "/roles/{id}/delete";

    // ADMIN ROLE
    public static final String requiredAdminAuthority = "hasAuthority('ROLE_ADMIN')";
    public static final String secondaryRoleBaseRoute = "/api/v1/admin";
    public static final String userCreateRoute = secondaryRoleBaseRoute + "/users/create";
    public static final String userFindByEmailRoute = secondaryRoleBaseRoute + "/users/search-by-email";
    public static final String userFindByIdRoute = secondaryRoleBaseRoute + "/users/search-by-id";
    public static final String userFindByNameRoute = secondaryRoleBaseRoute + "/users/search-by-name";
    public static final String userFindByLastNameRoute = secondaryRoleBaseRoute + "/users/search-by-lastname";
    public static final String userFindByNameAndLastNameRoute = secondaryRoleBaseRoute + "users/search-by-name-and-lastname";
    public static final String userFindAllActiveUsersRoute = secondaryRoleBaseRoute + "/users/active-users";
    public static final String userFindRolesRoute = secondaryRoleBaseRoute + "/users/{id}/roles";
    public static final String userFindAllAddressRoute = secondaryRoleBaseRoute + "/users/{id}/addresses";
    // ============ categories ============================ //
    public static final String categoryBaseRoute = secondaryRoleBaseRoute + "/categories";
    public static final String categoryCreateRoute = categoryBaseRoute + "/create";
    public static final String categoryFindAllRoute = categoryBaseRoute + "/";
    public static final String categoryChangeActivationRoute = categoryBaseRoute + "/{id}/change-activation";
    public static final String categoryFindAllActiveRoute = categoryBaseRoute + "/active-categories";
    public static final String categoryFindAllNotActiveRoute = categoryBaseRoute + "/not-active-categories";
    public static final String categoryFindSubCategoriesRoute = categoryBaseRoute + "/{id}/sub-categories";
    public static final String categoryCreateSubCategory = categoryBaseRoute + "/add-sub-category";
    public static final String categoryDeleteRoute = categoryBaseRoute + "/{id}/delete";
    public static final String categoryFindByIdRoute = categoryBaseRoute + "/{id}";
    public static final String categoryFindByNameRoute = categoryBaseRoute + "/search-by-name";
    public static final String categoryChangeParentCategoryRoute = categoryBaseRoute + "/change-parent-category";
    // ============ products ============================== //
    public static final String productBaseRoute = secondaryRoleBaseRoute + "/products";
    public static final String productFindAllRoute = productBaseRoute + "/";
    public static final String productFindByIdRoute = productBaseRoute + "/{id}";
    public static final String productFindByNameRoute = productBaseRoute + "/search-by-name";
    public static final String productFindByTitleRoute = productBaseRoute + "/search-by-title";
    public static final String productChangePublishRoute = productBaseRoute + "/{id}/change-product-publish";
    public static final String productCreateRoute = productBaseRoute + "/create";
    public static final String productUpdateRoute = productBaseRoute + "/{id}/update";
    public static final String productAddImageRoute = productBaseRoute + "{id}/add-images";
    public static final String productDeleteRoute = productBaseRoute + "{id}/delete";
    public static final String productFindAllIsPublishedRoute = productBaseRoute + "/all-published-products";
    public static final String productFindAllNotPublishedRoute = productBaseRoute + "/all-not-published-products";
    public static final String productFindByCategoryIdRoute = productBaseRoute +"/find-by-category-id";
    public static final String productDeleteImageRoute = productBaseRoute + "{id}/images/delete";
    public static final String productStockUpdateRoute = productBaseRoute + "/{id}/stock/update-stock";
    public static final String productVariantAddRoute = productBaseRoute + "/{productId}/add-variant";
    public static final String productVariantUpdateRoute = productBaseRoute + "/{productId}/{variantId}/update-variant";
    public static final String productVariantRemoveRoute = productBaseRoute + "/{productId}/{variantId}/delete-variant";
    public static final String productVariantAddVariantOptionRoute = productBaseRoute + "/{productId}/variants/{variantId}/options/add-variant-option";
    public static final String productVariantUpdateVariantOptionRoute = productBaseRoute + "/{productId}/variants/{variantId}/options/{optionId}/update-variant-option";
    public static final String productVariantRemoveVariantOptionRoute = productBaseRoute + "/{productId}/variants/{variantId}/options/{optionId}/delete-variant-option";

    // ============ files ================================= //
    public static final String fileFindAllRoute = "files/all-files";
    public static final String fileDownloadRoute = "files/{filename:.+}";
    public static final String fileUploadRoute = "files/create";
    public static final String fileFindRoute = "/files{filename}";


    // USER ROLE
    public static final String requiredUserAuthority = "hasAuthority('ROLE_USER')";
    public static final String userBaseRoute = "/api/v1/user";
    public static final String userCurrentUserRoute = "api/v1/user/current-user";
    public static final String userProfileRoute = userBaseRoute + "/profile";
    public static final String userSettingsChangePassword = userBaseRoute + "/settings/change-password";
    public static final String userSettingsCancelRegister = userBaseRoute + "/settings/cancel-register";
    public static final String userSettingsUpdateRoute = userBaseRoute + "/settings/update-user";
    public static final String userOwnRolesRoute = userBaseRoute + "/settings/roles";
    public static final String userSettingsUploadProfileImageRoute = userBaseRoute +"/settings/upload/profile-image";
    public static final String userAddAddressRoute = userBaseRoute + "/add-address";
    public static final String userFindAllAddress = userBaseRoute + "/addresses";

    public static final String userCartRoute = "/api/v1/cart";
    public static final String userCartAddItemRoute = userCartRoute + "/add-item";
    public static final String userCartRemoveItemRoute = userCartRoute + "/remove-item";
    public static final String userCartDeleteRoute = userCartRoute + "/delete";

    //PUBLIC ROUTE
    public static final String storeBaseRoute = "/api/v1/store";
    public static final String storeSingleProductRoute = storeBaseRoute + "/{id}";
    public static final String storeByCategoryId = storeBaseRoute + "/category/{id}/products";

}
