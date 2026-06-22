package com.swedbank.backend.model;

public enum ExchangeRate {
    EUR_TO_USD(1.14735541),
    EUR_TO_SEK(10.99891151),
    EUR_TO_GBP(0.86656839),
    EUR_TO_VND(30230.57),

    USD_TO_EUR(0.87156951),
    USD_TO_SEK(9.58631595),
    USD_TO_GBP(0.75527459),
    USD_TO_VND(26348.05),

    SEK_TO_EUR(0.09091808),
    SEK_TO_USD(0.10431536),
    SEK_TO_GBP(0.07878674),
    SEK_TO_VND(2748.5063),

    GBP_TO_EUR(1.15397700),
    GBP_TO_USD(1.32402176),
    GBP_TO_SEK(12.69249094),
    GBP_TO_VND(34885.39),

    VND_TO_EUR(0.00003307),
    VND_TO_USD(0.00003795),
    VND_TO_SEK(0.00036383),
    VND_TO_GBP(0.00002866);

    public final double rate;

    ExchangeRate(double rate) {
        this.rate = rate;
    }
}
