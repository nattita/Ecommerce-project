package camt.cbsd.controller;

import camt.cbsd.entity.Product;
import camt.cbsd.services.ProductService;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.List;

@Component

@ConfigurationProperties(prefix = "server")
@Path("/product")
public class ProductController {

    ProductService productService;
    String imageServerDir;
    String imageUrl;
    String baseUrl;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageServerDir(String imageServerDir) {
        this.imageServerDir = imageServerDir;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response list() {
        return Response.ok(productService.list()).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Product course) {
        productService.add(course);
        return Response.ok(course).build();

    }
    @POST
    @Path("/image")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces({MediaType.TEXT_PLAIN})
    public Response uploadImage(@FormDataParam("file") InputStream fileInputStream,
                                @FormDataParam("file") FormDataContentDisposition cdh) throws IOException {
        try {
            BufferedImage img = ImageIO.read(fileInputStream);
            String oldFilename = cdh.getFileName();
            String ext = FilenameUtils.getExtension(oldFilename);
            String newFilename = Integer.toString(LocalTime.now().hashCode(), 16) + Integer.toString(oldFilename.hashCode(), 16) + "." + ext;
            File targetFile = Files.createFile(Paths.get(imageServerDir + newFilename)).toFile();
            ImageIO.write(img, ext, targetFile);

            return Response.ok(baseUrl + imageUrl + newFilename).build();
        }catch(NullPointerException e){
            return Response.status(202).build();
        }
    }
    @GET
    @Path("/images/{fileName}")
    @Produces({"image/png", "image/jpg", "image/gif"})
    public Response getStuentImage(@PathParam("fileName") String filename) {
        File file = Paths.get(imageServerDir + filename).toFile();
        if (file.exists()) {
            Response.ResponseBuilder responseBuilder = Response.ok((Object) file);
            responseBuilder.header("Content-Disposition", "attachment; filename=" + filename);
            return responseBuilder.build();
        }else{
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") long id) {
        Product product = productService.findById(id);
        if (product != null)
            return Response.ok(product).build();
        else
            //http code 204
            return Response.status(Response.Status.NO_CONTENT).build();

    }
}
