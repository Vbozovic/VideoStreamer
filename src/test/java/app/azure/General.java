package app.azure;

import app.client.AzureClient;
import app.dto.azure.recive.list.FaceListDto;
import app.dto.azure.recive.list.ListsDto;
import app.error_handling.AzureException;
import app.service.AzureService;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class General {

    @Test
    public void clientTest() throws IOException, AzureException {

        AzureService.detectFaces(ImageIO.read(new File("C:\\Users\\ybv\\Desktop\\VideoStreamer\\resources\\contacts.serimages\\Vuk.jpg")));

    }

    @Test
    public void listTest() throws AzureException {

        AzureService.createList("Lista 1", "id1", "");

        ListsDto[] ldto = AzureService.getLists();
        assert ldto[0].faceListId.equals("id1");
        assert ldto[0].name.equals("Lista 1");
        assert ldto[0].userData == null;

        FaceListDto dto = AzureService.getListOfFaces("id1");
        assert dto.name.equals("Lista 1");
        assert dto.faceListId.equals("id1");
        assert dto.userData == null;
        assert dto.persistedFaces.isEmpty();

        AzureService.deleteFaceList("id1");
    }

}
