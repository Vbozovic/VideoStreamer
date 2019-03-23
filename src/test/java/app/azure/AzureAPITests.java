package app.azure;

import app.client.AzureClient;
import app.dto.azure.recive.group.CreatedPersonDto;
import app.dto.azure.recive.group.GetGroupDto;
import app.dto.azure.recive.group.GetPersonDto;
import app.dto.azure.recive.list.FaceListDto;
import app.dto.azure.recive.list.ListsDto;
import app.error_handling.AzureException;
import app.service.AzureService;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class AzureAPITests {

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

    @Test
    public void groupTest() throws AzureException {
        AzureService.createGroup("Group 1","Test data","gr1");
        AzureService.createGroup("Group 2","Test data","gr2");

        GetGroupDto grDto = AzureService.getGroup("gr1");
        assert grDto.name.equals("Group 1");
        assert grDto.userData.equals("Test data");
        assert grDto.personGroupId.equals("gr1");

        GetGroupDto[] groups = AzureService.listGroups();
        assert groups.length == 2;
        assert groups[0].personGroupId.equals("gr1");
        assert groups[1].personGroupId.equals("gr2");

        AzureService.deleteGroup("gr1");
        GetGroupDto[] delGroup = AzureService.listGroups();
        assert delGroup.length==1;
        assert delGroup[0].personGroupId.equals("gr2");

        AzureService.deleteGroup("gr2");
        GetGroupDto[] delGroup2 = AzureService.listGroups();
        assert delGroup2.length == 0;

    }

    @Test
    public void testPersonGroup() throws AzureException {

        AzureService.createGroup("Test group 1","For testing","gr1");
        CreatedPersonDto dto1 = AzureService.createPerson("Vuk","Jebeni kurjak","gr1");
        CreatedPersonDto dto2 = AzureService.createPerson("Marko","","gr1");

        GetPersonDto[] list = AzureService.listPersons("gr1");
        assert list.length == 2;
        assert list[0].name.equals("Vuk") || list[0].name.equals("Marko");
        assert list[1].name.equals("Vuk") || list[1].name.equals("Marko");

        GetPersonDto dto3 = AzureService.getPerson(dto1.personId,"gr1");
        assert dto3.name.equals("Vuk");
        assert dto3.personId.equals(dto1.personId);

        AzureService.deletePerson(dto1.personId,"gr1");
        GetPersonDto[] list2 = AzureService.listPersons("gr1");

        assert list2.length == 1;
        assert list2[0].personId.equals(dto2.personId);
        assert list2[0].name.equals("Marko");

        AzureService.deleteGroup("gr1");
    }

}
