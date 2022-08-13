package ipl;

import ipl.classes.IPL;
import ipl.exceptionHandlers.IPLException;
import java.io.IOException;


/**
 * Driver class for entire ipl.classes.IPL Java based application.
 */
public class IPLDriver {
    public static void main(String[] args) throws IPLException, IOException {
        IPL ipl = new IPL("Indian Premier League (ipl.classes.IPL)", 2021);

        // Reading from the IPL_2021_data.csv file into ipl object.
        ipl.readInputFile("csv", "./IPL_2021_data.csv");

        ipl.createMatchFixtures();
        ipl.writeMatchFixtureToCsvFile(ipl.getYear().toString() + "_fixtures.csv");

        // Checking if the fixtures are proper i.e. no team plays 2 consecutive matches
        ipl.checkIfFixturesAreProper();

        // All the functionalities are present here.
        IPL.menuDrivenProgram(ipl);
    }
}
