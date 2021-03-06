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
package org.hawkular.datamining.rest;

import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hawkular.datamining.api.Constants;
import org.hawkular.datamining.api.Subscription;
import org.hawkular.datamining.api.SubscriptionManager;
import org.hawkular.datamining.api.base.DataMiningForecaster;
import org.hawkular.datamining.api.base.DataMiningSubscription;
import org.hawkular.datamining.api.model.Metric;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Pavol Loffay
 */
@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/metrics", description = "Subscriptions CRUD.", tags = "Subscription")
public class RestSubscriptions {

    @Inject
    private SubscriptionManager subscriptionManager;

    @HeaderParam(Constants.TENANT_HEADER_NAME)
    private String tenant;


    @GET
    @Path("/metrics")
    @ApiOperation("Get all subscriptions of tenant.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Subscription for given metric not found", response = ApiError.class),
            @ApiResponse(code = 500, message = "Server error",response = ApiError.class)
    })
    public Response getAll() {
        Set<Subscription> tenantsSubscriptions = subscriptionManager.subscriptionsOfTenant(tenant);

        return Response.status(Response.Status.OK).entity(tenantsSubscriptions).build();
    }

    @GET
    @Path("/metrics/{metricId}")
    @ApiOperation("Get a concrete subscription.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Subscription for given metric not found", response = ApiError.class),
            @ApiResponse(code = 500, message = "Server error",response = ApiError.class)
    })
    public Response getOne(@PathParam("metricId") String metricId) {
        Subscription subscription = subscriptionManager.subscription(tenant, metricId);

        return Response.status(Response.Status.OK).entity(subscription).build();
    }

    @POST
    @Path("/metrics")
    @ApiOperation("Subscribe metric for prediction.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success, prediction successfully enabled"),
            @ApiResponse(code = 400, message = "Missing or invalid payload", response = ApiError.class),
            @ApiResponse(code = 409, message = "Subscription for given metric already enabled",
                    response = ApiError.class),
            @ApiResponse(code = 500, message = "Server error",response = ApiError.class)
    })
    public Response subscribe(@ApiParam(required = true) Metric.RestBlueprint blueprint) {

        Metric metric = new Metric(blueprint, tenant, null);
        DataMiningSubscription subscription = new DataMiningSubscription(new DataMiningForecaster(metric),
                Collections.singleton(Subscription.SubscriptionOwner.Metric));

        subscriptionManager.subscribe(subscription);

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/metrics/{id}")
    @ApiOperation("Modify metric metadata.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success, prediction successfully enabled"),
            @ApiResponse(code = 400, message = "Missing or invalid payload", response = ApiError.class),
            @ApiResponse(code = 409, message = "Subscription for given metric already enabled",
                    response = ApiError.class),
            @ApiResponse(code = 500, message = "Server error", response = ApiError.class)
    })
    public Response updateMetric(@PathParam("id") String metricId,
                                 @ApiParam(required = true) Metric.Update update) {

        subscriptionManager.updateMetric(tenant, metricId, update);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/metrics/{id}")
    @ApiOperation("Deletes subscription of given metric.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success, Model successfully disabled"),
            @ApiResponse(code = 400, message = "Missing or invalid payload", response = ApiError.class),
            @ApiResponse(code = 404, message = "Model for given metric not found", response = ApiError.class),
            @ApiResponse(code = 500, message = "Server error",response = ApiError.class)
    })
    public Response unsubscribe(@PathParam("id") String metricId) {

        subscriptionManager.unsubscribeAll(tenant, metricId);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
