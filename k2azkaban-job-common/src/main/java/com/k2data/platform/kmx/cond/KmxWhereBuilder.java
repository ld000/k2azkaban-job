package com.k2data.platform.kmx.cond;

import java.util.List;
import java.util.Set;

/**
 * @author lidong 17-1-11.
 */
public class KmxWhereBuilder {

    private List<Builder> builders;
    private List<Builder> subBuilders;

    public static final class Builder {
        private String deviceid;
        private String sensorid;
        private List<String> cond;

        public Builder lt(String value) {
            this.cond.add("\"$lt\":" + value);
            return this;
        }

        public Builder lte(String value) {
            this.cond.add("\"$lte\":" + value);
            return this;
        }

        public Builder gt(String value) {
            this.cond.add("\"$gt\":" + value);
            return this;
        }

        public Builder gte(String value) {
            this.cond.add("\"$gte\":" + value);
            return this;
        }

        public Builder eq(String value) {
            this.cond.add("\"$eq\":" + value);
            return this;
        }
    }

    public KmxWhereBuilder deviceid(List<String> deviceids) {

        return this;
    }

    public KmxWhereBuilder deviceid(String deviceid) {

        return this;
    }

    public KmxWhereBuilder builder(Builder builder) {
        this.builders.add(builder);
        return this;
    }

    public String build() {

        return "";
    }

}
