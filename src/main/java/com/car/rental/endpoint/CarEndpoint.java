package com.car.rental.endpoint;

import com.car.rental.model.Car;
import com.car.rental.service.CarService;

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
    @Path("/all")
    public Response getAllCars() {
        var response = carService.findAllCars();
        return Response.status(200).entity(response).build();
    }

    @GET
    @Path("/get/{id}")
    public Response getCarById(@PathParam("id") Long id) {
        var response = carService.findCarById(id);
        return Response.status(200).entity(response).build();
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
