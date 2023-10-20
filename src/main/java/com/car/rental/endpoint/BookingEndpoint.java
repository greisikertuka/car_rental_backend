package com.car.rental.endpoint;

import com.car.rental.model.Booking;
import com.car.rental.service.BookingService;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookingEndpoint {

    @Inject
    BookingService bookingService;

    @GET
    @Path("/all")
    @APIResponse(
            responseCode = "200",
            description = "List of bookings found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Booking.class)
            )
    )
    public Response getAllBookings() {
        var response = bookingService.findAllBookings();
        if (response != null) {
            return Response.status(200).entity(response).build();
        } else {
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/user/{userId}")
    @APIResponse(
            responseCode = "200",
            description = "List of bookings found for a user",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Booking.class)
            )
    )
    public Response getBookingsByUserId(@PathParam("userId") Long userId) {
        var response = bookingService.findBookingsByUserId(userId);
        if (response != null) {
            return Response.status(200).entity(response).build();
        } else {
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/car/{carId}")
    @APIResponse(
            responseCode = "200",
            description = "List of bookings found for a car",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = SchemaType.ARRAY, implementation = Booking.class)
            )
    )
    public Response getBookingsByCarId(@PathParam("carId") Long carId) {
        var response = bookingService.findBookingsByCarId(carId);

        if (response != null) {
            return Response.status(200).entity(response).build();
        } else {
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/create")
    public Response insert(@QueryParam("carId") Long carId, @QueryParam("userId") Long userId, Booking booking) {
        bookingService.insertBooking(userId, carId, booking);
        return Response.status(201).build();
    }

    @PUT
    @Path("/update")
    public Response update(Booking booking) {
        bookingService.updateBooking(booking);
        return Response.status(201).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        bookingService.deleteBooking(id);
        return Response.status(201).build();
    }

    @GET
    @Path("/get/{id}")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Booking found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Booking.class))),
            @APIResponse(responseCode = "404", description = "Booking not found")
    })
    public Response getBookingById(@PathParam("id") Long id) {
        var response = bookingService.getBookingById(id);
        if (response != null) {
            return Response.status(200).entity(response).build();
        } else {
            return Response.status(404).build();
        }
    }
}
