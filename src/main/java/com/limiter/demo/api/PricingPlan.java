package com.limiter.demo.api;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;
enum PricingPlan {
    FREE {
        public  Bandwidth getLimit() {
            return Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));
        }
    },
    BASIC {
        public  Bandwidth getLimit() {
            return Bandwidth.classic(10, Refill.intervally(10, Duration.ofHours(1)));
        }
    },
    PROFESSIONAL {
       public Bandwidth getLimit() {
            return Bandwidth.classic(100, Refill.intervally(100, Duration.ofHours(1)));
        }
    };
    //..

        static PricingPlan resolvePlanFromApiKey(String apiKey) {
            if (apiKey == null || apiKey.isEmpty()) {
                return FREE;
            } else if (apiKey.startsWith("PX001-")) {
                return PROFESSIONAL;
            } else if (apiKey.startsWith("BX001-")) {
                return BASIC;
            }
            return FREE;
        }
        //..

    public abstract Bandwidth getLimit();

}



