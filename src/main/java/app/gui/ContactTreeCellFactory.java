package app.gui;

import app.dto.azure.recive.group.GetPersonDto;
import app.error_handling.AzureException;
import app.service.AzureService;
import app.service.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.util.Optional;

public class ContactTreeCellFactory extends TreeCell<GetPersonDto> {

    private ContextMenu nodeMenu;

    public ContactTreeCellFactory(){
        nodeMenu = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Add face");
        MenuItem deleteMenuItem = new MenuItem("Delete contact");
        nodeMenu.getItems().addAll(addMenuItem,deleteMenuItem);
        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Faca");
            }
        });

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GetPersonDto person = getTreeView().getSelectionModel().getSelectedItems().get(0).getValue();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete contact");
                alert.setHeaderText("You are about to delete a contact");
                alert.setContentText("Are you ok with this?");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    try {
                        AzureService.deletePerson(person.personId, Config.getInstance().group_id);
                        TreeItem<GetPersonDto> personNode = getTreeView().getSelectionModel().getSelectedItems().get(0);
                        personNode.getParent().getChildren().remove(personNode);
                        getTreeView().refresh();
                    } catch (AzureException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    public ContactTreeCellFactory(ContextMenu nodeMenu) {
        this.nodeMenu = nodeMenu;
    }


    @Override
    protected void updateItem(GetPersonDto item, boolean empty) {
        super.updateItem(item,empty);
        if (!empty){
            setText(item.toString());

            setContextMenu(nodeMenu);
        }
    }



}
