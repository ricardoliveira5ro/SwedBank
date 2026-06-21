package com.swedbank.backend.model;

public enum ExchangeRate {
    EUR_TO_USD("EUR_USD", 1.14735541),
    EUR_TO_SEK("EUR_SEK", 10.99891151),
    EUR_TO_GBP("EUR_GBP", 0.86656839),
    EUR_TO_VND("EUR_VND", 30230.57),

    USD_TO_EUR("USD_EUR", 0.87156951),
    USD_TO_SEK("USD_SEK", 9.58631595),
    USD_TO_GBP("USD_GBP", 0.75527459),
    USD_TO_VND("USD_VND", 26348.05),

    SEK_TO_EUR("SEK_EUR", 0.09091808),
    SEK_TO_USD("SEK_USD", 0.10431536),
    SEK_TO_GBP("SEK_GBP", 0.07878674),
    SEK_TO_VND("SEK_VND", 2748.5063),

    GBP_TO_EUR("GBP_EUR", 1.15397700),
    GBP_TO_USD("GBP_USD", 1.32402176),
    GBP_TO_SEK("GBP_SEK", 12.69249094),
    GBP_TO_VND("GBP_VND", 34885.39),

    VND_TO_EUR("VND_EUR", 0.00003307),
    VND_TO_USD("VND_USD", 0.00003795),
    VND_TO_SEK("VND_SEK", 0.00036383),
    VND_TO_GBP("VND_GBP", 0.00002866);

    public final String key;
    public final double rate;

    ExchangeRate(String key, double rate) {
        this.key = key;
        this.rate = rate;
    }
}
