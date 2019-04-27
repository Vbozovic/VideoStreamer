
package app.model;

import app.dto.azure.recive.group.GetGroupDto;
import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.GetGroupException;
import app.error_handling.ListPersonsException;
import app.service.AzureService;
import app.service.Config;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class MainScreenModel{

    public TreeView<GetPersonDto> tree;
    public String groupId;


    public MainScreenModel(TreeView<GetPersonDto> tree) throws ListPersonsException, GetGroupException {
        GetGroupDto myGroup = AzureService.getGroup(Config.getInstance().group_id);
        groupId = myGroup.personGroupId;
        this.tree = tree;
        ArrayList<GetPersonDto>people = new ArrayList<>(Arrays.asList(AzureService.listPersons(myGroup.personGroupId)));
        fillTree(people);
    }

    private void fillTree(ArrayList<GetPersonDto> people){
        TreeItem<GetPersonDto> root = new TreeItem<>();
        root.setExpanded(true);
        tree.setRoot(root);
        tree.setShowRoot(false);

        for (GetPersonDto person : people){
            TreeItem<GetPersonDto> contactNode = new TreeItem<GetPersonDto>(person);
            root.getChildren().add(contactNode);
        }
    }

    public boolean addPerson(GetPersonDto person){
        this.tree.getRoot().getChildren().add(new TreeItem<GetPersonDto>(person));
        return true;
    }

    public String getPersonById(String id){
        return this.tree.getRoot().getChildren().stream().filter(node -> node.getValue().personId.equals(id)).findFirst().map(getPersonDtoTreeItem -> getPersonDtoTreeItem.getValue().name).orElse(null);
    }

}
