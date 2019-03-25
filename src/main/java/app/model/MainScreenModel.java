
package app.model;

import app.dto.azure.recive.group.GetGroupDto;
import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.GetGroupException;
import app.error_handling.ListPersonsException;
import app.service.AzureService;
import app.service.Config;

public class MainScreenModel {

    public String groupId;
    public GetPersonDto[] people;

    public MainScreenModel() throws ListPersonsException, GetGroupException {
        GetGroupDto myGroup = AzureService.getGroup(Config.getInstance().group_id);
        people = AzureService.listPersons(myGroup.personGroupId);
        groupId = myGroup.personGroupId;
    }

}
