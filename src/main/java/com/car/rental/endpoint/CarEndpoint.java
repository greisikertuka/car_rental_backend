package com.car.rental.endpoint;

import com.car.rental.model.Car;
import com.car.rental.service.CarService;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/cars")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CarEndpoint {

    @Inject
    CarService carService;

    @GET
    @APIResponse(
            responseCode = "200",
            description = "List of cars found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Car.class)
            )
    )
    @Path("/all")
    public Response getAllCars() {
        var response = carService.findAllCars();
        if (response != null) {
            return Response.status(200).entity(response).build();
        } else {
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/get/{id}")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Car found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class))),
            @APIResponse(responseCode = "404", description = "Car not found")
    })
    public Response getCarById(@PathParam("id") Long id) {
        var response = carService.findCarById(id);
        if (response != null) {
            return Response.status(200).entity(response).build();
        } else {
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/create")
    public Response insert(Car car) {
        carService.insertCar(car);
        return Response.status(201).build();
    }

    @PUT
    @Path("/update")
    public Response update(Car car) {
        carService.updateCar(car);
        return Response.status(201).build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") Long id) {
        carService.deleteCar(id);
        return Response.status(201).build();
    }
}
