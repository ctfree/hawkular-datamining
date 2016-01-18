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
package org.hawkular.datamining.bus;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

/**
 * @author Pavol Loffay
 */
@MessageLogger(projectCode = "HAWKDMING")
public interface BusLogger extends BasicLogger {

    BusLogger LOGGER = Logger.getMessageLogger(BusLogger.class, "org.hawkular.datamining.bus");

    @LogMessage(level = Logger.Level.INFO)
    @Message(value = "Connected to MetricData Topic successfully connected")
    void connectedToMetricDataTopic();

    @LogMessage(level = Logger.Level.ERROR)
    @Message(value = "Dataminig bus failed to connect to bus")
    void failedToStart(@Cause Throwable t);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(value = "Failed to send message to the bus = %s")
    void failedToSendMessage(String message);

    @LogMessage(level = Logger.Level.ERROR)
    @Message(value = "Failed to load metric data from Metrics, url: %s, message: %s")
    void failedToLoadMetricData(String url, String message);
}
