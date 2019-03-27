
package app.model;

import app.dto.azure.recive.group.GetGroupDto;
import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.GetGroupException;
import app.error_handling.ListPersonsException;
import app.service.AzureService;
import app.service.Config;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class MainScreenModel{

    public TreeView<GetPersonDto> tree;
    public String groupId;
    public ArrayList<GetPersonDto> people;


    public MainScreenModel(TreeView<GetPersonDto> tree) throws ListPersonsException, GetGroupException {
        GetGroupDto myGroup = AzureService.getGroup(Config.getInstance().group_id);
        people = new ArrayList<>(Arrays.asList(AzureService.listPersons(myGroup.personGroupId)));
        groupId = myGroup.personGroupId;
        this.tree = tree;
    }

    public void fillTree(){
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
        people.add(person);
        this.tree.getRoot().getChildren().add(new TreeItem<GetPersonDto>(person));
        this.tree.refresh();
        return true;
    }

}
