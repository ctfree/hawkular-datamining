/*
 * Copyright 2015-2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hawkular.datamining.api.json;

import org.hawkular.datamining.api.base.DataMiningSubscription;
import org.hawkular.datamining.forecast.AutomaticForecaster;
import org.hawkular.datamining.forecast.models.TimeSeriesModel;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Pavol Loffay
 */
public class ObjectMapperConfig {

    public static void config(ObjectMapper objectMapper) {
        objectMapper.addMixIn(AutomaticForecaster.class, ForecasterMixin.class);
        objectMapper.addMixIn(DataMiningSubscription.class, SubscriptionMixin.class);
        objectMapper.addMixIn(TimeSeriesModel.class, TimeSeriesMixin.class);
        objectMapper.addMixIn(AutomaticForecaster.ConceptDriftStrategy.class, ConceptDriftStrategyMixin.class);
    }
}
