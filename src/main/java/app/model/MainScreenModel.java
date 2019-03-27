
package app.model;

import app.dto.azure.recive.group.GetGroupDto;
import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.GetGroupException;
import app.error_handling.ListPersonsException;
import app.service.AzureService;
import app.service.Config;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class MainScreenModel {

    public String groupId;
    public GetPersonDto[] people;

    public MainScreenModel() throws ListPersonsException, GetGroupException {
        GetGroupDto myGroup = AzureService.getGroup(Config.getInstance().group_id);
        people = AzureService.listPersons(myGroup.personGroupId);
        groupId = myGroup.personGroupId;
    }

    public void fillTree(TreeView<GetPersonDto> tree){
        TreeItem<GetPersonDto> root = new TreeItem<>();
        root.setExpanded(true);
        tree.setRoot(root);
        tree.setShowRoot(false);

        for (GetPersonDto person : people){
            TreeItem<GetPersonDto> contactNode = new TreeItem<GetPersonDto>(person);
            root.getChildren().add(contactNode);
        }
    }

}
