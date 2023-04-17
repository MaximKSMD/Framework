package api.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RakutenExchangeApi {
    //Rules
    GET_PROTECTION("/v3/protections/targeting/%s"),
    CREATE_PROTECTION("/v3/protections/targeting"),
    GET_ALL_PROTECTIONS("/v3/protections/targeting"),
    DELETE_PROTECTION("/v3/protections/targeting/%s"),
    UPDATE_PROTECTION("/v3/protections/targeting/%s"),

    //Publisher
    GET_PUBLISHERS("/v2/publishers"),
    CREATE_PUBLISHER("/v2/publishers"),
    DELETE_PUBLISHER("/v2/publishers/%s"),
    UPDATE_PUBLISHER("/v2/publishers/%s"),
    GET_PUBLISHER("/v2/publishers/%s"),

    //Users
    CREATE_USER("/v2/accounts"),
    GET_USER("/v2/accounts/%s"),
    GET_ALL_USERS("/v2/accounts"),
    UPDATE_USER("/v2/accounts/%s"),
    DELETE_USER("/v2/accounts/%s"),

    //Open Pricing
    GET_OPEN_PRICING("/v3/pricing/open/%s"),
    CREATE_OPEN_PRICING("/v3/pricing/open"),
    GET_ALL_OPEN_PRICING("/v3/pricing/open"),
    UPDATE_OPEN_PRICING("/v3/pricing/open/%s"),
    DELETE_OPEN_PRICING("/v3/pricing/open/%s"),
    EXPORT_OPEN_PRICING_FLOOR_PRICE("/v3/pricing/open/export"),

    //Dynamic Pricing
    CREATE_DYNAMIC_PRICING("/v3/pricing/dynamic"),
    UPDATE_DYNAMIC_PRICING("/v3/pricing/dynamic/%s"),
    GET_ALL_DYNAMIC_PRICING("/v3/pricing/dynamic"),
    GET_DYNAMIC_PRICING("/v3/pricing/dynamic/%s"),
    DELETE_DYNAMIC_PRICING("/v3/pricing/dynamic/%s"),

    //Media
    CREATE_MEDIA("/v2/media"),
    GET_MEDIA("/v2/media/%s"),
    GET_ALL_MEDIA("/v2/media"),
    UPDATE_MEDIA("/v2/media/%s"),
    DELETE_MEDIA("/v2/media/%s"),

    //Ad Spot
    CREATE_ADSPOT("/v2/adspots"),
    GET_ADSPOT("/v2/adspots/%s"),
    GET_ALL_ADSPOTS("/v2/adspots"),
    UPDATE_ADSPOT("/v2/adspots/%s"),
    DELETE_ADSPOT("/v2/adspots/%s"),

    //Geos
    GET_ALL_GEOS("/v2/geos/countries"),

    //Currencies
    GET_ALL_CURRENCIES("/v2/currencies"),

    //Dsp
    GET_ALL_DSPS("/v3/dsps"),
    CREATE_DSP("/v3/dsps"),
    DELETE_DSP("/v3/dsps/%s"),

    //AdSize
    GET_ALL_AD_SIZES("/v2/ad-sizes"),

    //AdFormat
    GET_ALL_AD_FORMATS("/v2/formats"),

    //Platform Types
    GET_ALL_PLATFORM_TYPES("/v2/platform-types"),

    //Device
    GET_ALL_DEVICE_TYPES("/v2/device-types"),

    //Operating system
    GET_ALL_OPERATING_SYSTEM("/v2/device-os"),

    //Private auction system
    CREATE_PRIVATE_AUCTION("/v3/pmp/private-auctions"),
    GET_ALL_PRIVATE_AUCTIONS("/v3/pmp/private-auctions"),
    UPDATE_PRIVATE_AUCTION("/v3/pmp/private-auctions/%s"),
    GET_PRIVATE_AUCTION_BY_ID("/v3/pmp/private-auctions/%s"),

    //Deal
    CREATE_DEAL("/v3/pmp/deals"),
    GET_ALL_DEALS("/v3/pmp/deals"),
    UPDATE_DEAL("/v2/pmp/deals/"),

    //Audience
    GET_ALL_AUDIENCES("/v3/audiences"),
    GET_AUDIENCE_BY_ID("/v3/audiences/%s"),
    CREATE_AUDIENCE("/v3/audiences"),
    UPDATE_AUDIENCE("/v3/audiences/%s"),
    DELETE_AUDIENCE("/v3/audiences/%s"),

    //API Version
    GET_API_VERSION("/v3/version");

    private String endpoint;

    public String setParameters(Object... parameters) {

        return parameters.length > 0 ? String.format(endpoint, parameters) : endpoint;
    }

}
