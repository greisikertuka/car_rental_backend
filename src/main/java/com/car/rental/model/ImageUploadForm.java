package com.car.rental.model;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

public class ImageUploadForm {
    @FormParam("fileName")
    @PartType(MediaType.TEXT_PLAIN)
    public String fileName;

    @FormParam("file")
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public byte[] data;
}