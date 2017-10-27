package frontend.GUI;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class PRres_Controller {

    private final String INSTRUCTION_LABEL = "En herlig oversikt over konserter"; //til

    private VBox instructionBoxContainer;

    private backend.PRres prres;

    private VBox contents = new VBox();



    public void getConcertDetails(){
        ArrayList<String> bookedBands = prres.getBookedBands();
        for(int i = 0; i<bookedBands.size(); i++){

        }

    }

}
