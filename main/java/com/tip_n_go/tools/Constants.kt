package com.tip_n_go.tools

// request codes
const val RESPONSE_CODE_200 = 200
const val RESPONSE_CODE_400 = 400
const val RESPONSE_CODE_401 = 401

// request urls
const val ACCOUNT_GET_JWT_TOKEN = "Account/GetJwtToken"
const val ACCOUNT_CHECK_PHONE = "Account/CheckPhone"
const val ACCOUNT_REFRESH_JWT_TOKEN = "Account/RefreshJwtToken"
const val ACCOUNT_RECOVER_PASSWORD_BY_EMAIL = "Account/RecoverPasswordByEmail"
const val REGISTRATION_CHECK_PHONE = "Registration/CheckPhone"
const val REGISTRATION_CHECK_EMAIL = "Registration/CheckEmail"
const val REGISTRATION_REGISTER_USER = "Registration/RegisterUser"
const val REGISTRATION_REGISTER_USER_TO_ORGANIZATION = "Registration/RegisterUserToOrganization"
const val USERS = "Users"
const val USERS_UPDATE_USER = "$USERS/UpdateUser"
const val USERS_UPLOAD_AVATAR = "$USERS/UploadAvatar"
const val REPORTS = "Reports/Data"
const val TIPS = "Tips"
const val REVIEWS = "Reviews"
const val FINANCE = "Finance"
const val FINANCE_BANK_CARDS = "$FINANCE/BankCards"
const val FINANCE_BALANCE = "$FINANCE/Balance"
const val FINANCE_PAYOUT = "$FINANCE/PayOut"
const val FINANCE_PAYOUTS = "$FINANCE/Payouts"
const val FINANCE_TIP_DISTRIBUTIONS = "$FINANCE/TipDistributions"
const val FINANCE_ORGANIZATION_UNDISTRIBUTED_TIPS = "$FINANCE/OrganizationUndistributedTips"
const val FINANCE_ORGANIZATION_BALANCE = "$FINANCE/OrganizationBalance"
const val FINANCE_DISTRIBUTE_TIPS = "$FINANCE/DistributeTips"
const val MOBILE_DEVICES = "MobileDevices"
const val ECOMMPAY = "Ecommpay/GetEcommpayRequestConfig"
const val NOTIFICATIONS = "Notifications"
const val ORGANIZATIONS = "Organizations"
const val ORGANIZATIONS_AVAILABLE_ROLES = "$ORGANIZATIONS/AvailableRoles"
const val ORGANIZATION_SETTINGS = "$ORGANIZATIONS/Settings"
const val ORGANIZATION_USERS = "OrganizationUsers"
const val ORGANIZATION_USERS_BY_HASH = "$ORGANIZATION_USERS/ByOrganization"
const val QR_GET_QR_CODE = "QR/GetQRCode"
const val BRANDS = "Brands"
const val BRANDS_UPLOAD_LOGO = "$BRANDS/UploadLogo"
const val SETTINGS_GET_COUNTRIES = "Settings/GetCountries"
const val SETTINGS_GET_TIPS_DISTRIBUTION_METHODS = "Settings/GetTipsDistributionMethods"

// request params
const val PERIOD = "period"
const val ORGANIZATION_HASH = "organizationHash"
const val TIPS_DISTRIBUTION_METHOD = "TipsDistributionMethod"

// shared tags
const val TOKEN = "token"
const val CURRENT_USER = "current_user"
const val USER_LANGUAGE = "user_language"
const val CURRENT_ORGANIZATION_HASH = "current_organization_hash"
const val ORGANIZATION_USER_HASH = "organization_user_hash"
const val ORGANIZATION_USER_ROLE = "organization_user_role"
const val CURRENT_USER_HASH = "current_user_hash"
const val STAFF_HASH = "staff_hash"
const val FCM_TOKEN = "fcm_token"

const val REVIEW_TAG = "review_tag"
const val BALANCE_TAG = "balance_tag"
const val BANK_CARDS_TAG = "bank_cards_tag"
const val DISTRIBUTED_TIP_TAG = "distributed_tip_tag"
const val ORGANIZATION_USER_TAG = "organization_user_tag"
const val ORGANIZATION_USER_HASH_TAG = "organization_user_hash_tag"
const val ROLES_LIST_TAG = "roles_list_tag"
const val POSITION_TAG = "position_tag"
const val TIP_DISTRIBUTION_METHOD_TAG = "tip_distribution_method_tag"
const val REPORT_DATA_TAG = "report_data_tag"
const val REPORT_DATA_PAGE_TAG = "report_data_page_tag"

// chart type
const val COLUMN_CHART = "column"

const val REQUEST_CODE = "requestCode"
const val RESULT_CODE = "resultCode"
const val INTENT_IMAGE_TYPE = "image/*"
const val TEMP_FILE_NAME = "temp"
