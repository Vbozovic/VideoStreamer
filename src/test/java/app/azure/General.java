package app.azure;

import app.client.AzureClient;
import app.service.AzureService;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class General {

    @Test
    public void clientTest() throws IOException {

        AzureService.detectFaces(ImageIO.read(new File("C:\\Users\\ybv\\Desktop\\VideoStreamer\\resources\\contacts.serimages\\Vuk.jpg")));

    }

}
