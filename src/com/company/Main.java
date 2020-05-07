package com.company;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * Set of applications for running the WolfHospital database system using JDBC and the
 * Maria DB. The application is run through the console and presents the user with various
 * operations that can be run that correspond to the tasks and operations provided in the
 * WolfHospital narrative.
 *
 * @author Will Henline
 * @author Hanyu Luo
 * @author Brandon King
 * @author Pei Liu
 *
 */

class WolfHospital {

    /** Connection the database */
    private static Connection connection = null;
    /** Statement for updating and querying the database */
    private static Statement statement = null;
    /** ResultSet for iterating through query results */
    private static ResultSet result = null;
    /** Prepared Statement to execute updates on the database */
    private static PreparedStatement ps = null;



    /**
     * Starts the program. Initializes the connection to the server and creates the database tables. Presents
     * the user with a menu of options. The user can quit whenever they desire and the connection to the database
     * will be closed.
     *
     * @param args
     */
    public static void main(String[] args) {

        initialize();
        WolfHospital wh = new WolfHospital();
        wh.presentMenu();
    }

    /**
     * Interactive menu for the user to perform task and operations on the wolf hospital database
     */
    public void presentMenu() {
        Scanner input = new Scanner(System.in);
        printOptions();

        //TODO add your case to the switch statement with the calling function you write
        while (true) {
            String in = input.nextLine();
            in = in.toLowerCase();
            switch(in) {

                case "1" :
                    enterNewStaffInformation(input);
                    printReturnToMenu();
                    break;

                case "2" :
                    updateStaffInformation(input);
                    printReturnToMenu();
                    break;

                case "3" :
                    enterNewPatientInformation(input);
                    printReturnToMenu();
                    break;

                case "4" :
                    updatePatientInformation(input);
                    printReturnToMenu();
                    break;

                case "5" :
                    enterNewWardInformation(input);
                    printReturnToMenu();
                    break;

                case "6" :
                    updateWardInformation(input);
                    printReturnToMenu();
                    break;

                case "7" :
                    enterNewCheckInInformation(input);
                    printReturnToMenu();
                    break;

                case "8" :
                    updateCheckInInformation(input);
                    printReturnToMenu();
                    break;

                case "9" :
                    assignWardUponRequest(input);
                    printReturnToMenu();
                    break;

                case "10" :
                    releaseWards(input);
                    printReturnToMenu();
                    break;

                case "11" :
                    enterNewMedicalRecord(input);
                    printReturnToMenu();
                    break;

                case "12" :
                    updateMedicalRecord(input);
                    printReturnToMenu();
                    break;

                case "13" :
                    viewMedicalRecord(input);
                    printReturnToMenu();
                    break;

                case "14" :
                    enterNewBillingAccount(input);
                    printReturnToMenu();
                    break;

                case "15" :
                    updateBillingAccount(input);
                    printReturnToMenu();
                    break;

                case "16" :
                    viewBillingAccount(input);
                    printReturnToMenu();
                    break;

                case "17" :
                    checkAvailableWards(input);
                    printReturnToMenu();
                    break;

                case "18" :
                    viewMonthlyMedicalRecord(input);
                    printReturnToMenu();
                    break;

                case "19" :
                    viewMonthlyPatientList(input);
                    printReturnToMenu();
                    break;

                case "20" :
                    viewWardUsagePercentage(input);
                    printReturnToMenu();
                    break;

                case "21" :
                    viewPatientListOfDoctor(input);
                    printReturnToMenu();
                    break;

                case "22" :
                    viewStaffInformationGroupedByRole(input);
                    printReturnToMenu();
                    break;

                case "p" :
                    printOptions();
                    break;

                case "exit" :
                    System.out.println("Bye");
                    input.close();
                    close();
                    System.exit(0);

                default:
                    System.out.println("Invalid input");
                    printOptions();
                    break;
            }
        }
    }

    /**
     * Prints the menu options for the user
     */
    public void printOptions() {
        //TODO add your menu options here
        System.out.println();
        System.out.println();
        System.out.println("The following commands can be used to perform various task and operations in the WolfHospital database");
        System.out.println();
        System.out.println("1 - Enter a new Staff");
        System.out.println("2 - Update or delete basic information about Staff");
        System.out.println("3 - Enter a new Patient");
        System.out.println("4 - Update or delete basic information about Patient");
        System.out.println("5 - Enter a new Wards");
        System.out.println("6 - Update or delete basic information about Wards");
        System.out.println("7 - Enter a new check-in information");
        System.out.println("8 - Update or delete check-in information");
        System.out.println("9 - Reserve Wards or Assign beds in 1-bed or 2-bed or 4-bed Wards");
        System.out.println("10 - Release Wards");
        System.out.println("11 - Enter a new Medical Record");
        System.out.println("12 - Update an existing Medical Record");
        System.out.println("13 - View Patient Medical Record");
        System.out.println("14 - Generate new Billing Account");
        System.out.println("15 - Update existing Billing Account");
        System.out.println("16 - View Patient Billing Account");
        System.out.println("17 - Check available Wards or Show current usage for all Wards");
        System.out.println("18 - View Medical Record for one patient in given month");
        System.out.println("19 - View the numbers of patients per month");
        System.out.println("20 - View the ward-usage percentage");
        System.out.println("21 - View all patient a doctor is currently responsible for");
        System.out.println("22 - View staff information grouped by role");
        System.out.println("p - Print menu options");
        System.out.println("exit - Exit the application");
    }

    /**
     * Print the return to menu so user knows where they have returned
     */
    public void printReturnToMenu() {
        System.out.println();
        System.out.println();
        System.out.println("Returning to main menu...");
        System.out.println("Enter \"p\" to review menu options");
        System.out.println();
        System.out.println();
    }

    /**
     * Enters a new Medical record for a given patient. Looks up the given patient ID to ensure that the patient
     * exists, then creates a new medical record for the patient given the start date and end date (optional).
     *
     * @param input Scanner to receive user input
     */
    public void enterNewMedicalRecord(Scanner input) {
        String query = "INSERT INTO MedicalRecord VALUES(?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter patient ID, the patient must already be present in the database.");
        int pId = 0;
        //Get the patient ID from input
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }


        //Query database for given patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient does not exist quit
        try {

            if (!result.next()) {
                System.err.println("That Patient does not exist in the database");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Check if patient already has medical record
        try {
            result = statement.executeQuery("SELECT * FROM MedicalRecord WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient already has medical record quit
        try {

            if (result.next()) {
                System.err.println("This patient already has a Medical Record, you may choose to update this record instead.");
                return;
            } else {
                ps.setInt(1, pId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the start date in the form yyyy-mm-dd");
        String startDate = null;

        //Get the start date from input
        try {
            startDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Parse the given date into a Date object
        Date startDateUtil = null;
        java.sql.Date sqlStartDate = null;
        try {
            startDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            sqlStartDate = new java.sql.Date(startDateUtil.getTime());
            ps.setDate(2, sqlStartDate);
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the end date in the form yyyy-mm-dd, if still active type na (not applicable) and press enter to continue.");
        String endDate = null;

        //Get the end date from input
        try {
            endDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the appropriate end date depending on user input
        Date endDateUtil = null;
        try {
            if (!endDate.toLowerCase().equals("na")) {
                endDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                java.sql.Date sqlEndDate = new java.sql.Date(endDateUtil.getTime());
                if (sqlEndDate.before(sqlStartDate)) {
                    System.err.println("End date must be on or after the start date.");
                    return;
                }
                ps.setDate(3, sqlEndDate);
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter responsible doctor using Staff ID.");
        int sId = 0;
        //Get the doctor ID from input
        try {
            sId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Staff ID must be an integer");
            return;
        }


        //Query database for given doctor
        try {
            result = statement.executeQuery("SELECT * FROM Staff WHERE id = " + sId + " AND jobTitle = 'Doctor'");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //If doctor does not exist quit
        try {

            if (!result.next()) {
                System.err.println("That Doctor does not exist in the database");
                return;
            } else {
                ps.setInt(4, sId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM MedicalRecord WHERE patientId = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0, rD = 0;
        Date sD = null;
        Date eD = null;
        try {
            while (result.next()) {
                id = result.getInt("patientID");
                sD = result.getDate("startDate");
                eD = result.getDate("endDate");
                rD = result.getInt("responsibleDoctor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (eD == null) {
            System.out.println();
            System.out.println("New Medical Record created with: Patient ID = " + id + ", Start Date = " + sD.toString() + ", End Date = NA"
                    + ", Responsible Doctor = " + rD);
        } else {
            System.out.println();
            System.out.println("New Medical Record created with: Patient ID = " + id + ", Start Date = " + sD.toString() + ", End Date = " + eD.toString()
                    + ", Responsible Doctor = " + rD);
        }

    }

    /**
     * Updates an existing medical record through interactive menu given that the patient given exists in the database and the patient
     * has an existing medical record.
     *
     * @param input Scanner for user input
     */
    public void updateMedicalRecord(Scanner input) {

        System.out.println("Please enter patient ID, the patient must already be present in the database.");
        int pId = 0;
        //Get the patient ID from input
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }


        //Query database for given patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient does not exist quit
        try {

            if (!result.next()) {
                System.err.println("That Patient does not exist in the database");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Check if patient already has medical record
        try {
            result = statement.executeQuery("SELECT * FROM MedicalRecord WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient doesn't have medical record quit
        try {

            if (!result.next()) {
                System.err.println("This patient doesn't have a Medical Record, you must create a Medical Record for the patient before it can be updated.");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        printMedicalRecordUpdateOptions();
        while (true) {
            String in = input.nextLine();
            in = in.toLowerCase();
            switch(in) {

                case "1" :
                    updateWithTest(input, pId);
                    printMedicalRecordUpdateOptions();
                    break;


                case "2" :
                    addNewPrescription(input, pId);
                    printMedicalRecordUpdateOptions();
                    break;


                case "3" :
                    addNewCheckInInfo(input, pId);
                    printMedicalRecordUpdateOptions();
                    break;

                case "4" :
                    updateWithDiagnosisDetails(input, pId);
                    printMedicalRecordUpdateOptions();
                    break;

                case "5" :
                    updateEndDateForMedicalRecord(input, pId);
                    printMedicalRecordUpdateOptions();
                    break;

                case "6" :
                    updateEndDateForCheckIn(input, pId);
                    printMedicalRecordUpdateOptions();
                    break;

                case "p" :
                    printMedicalRecordUpdateOptions();
                    break;

                case "exit" :
                    return;

                default:
                    System.out.println("Invalid input");
                    printMedicalRecordUpdateOptions();
                    break;
            }
        }


    }

    /**
     * Prints the options for the medical record update menu
     */
    public void printMedicalRecordUpdateOptions() {

        System.out.println();
        System.out.println();
        System.out.println("What part of this patient's Medical Record would you like to update?");
        System.out.println();
        System.out.println("1 - Add Test");
        System.out.println("2 - Add Prescription/Treatment");
        System.out.println("3 - Update with Check-In information");
        System.out.println("4 - Add Diagnosis Details");
        System.out.println("5 - Update End date for Medical Record");
        System.out.println("6 - Update End date for Check-In");
        System.out.println("p - Print options");
        System.out.println("exit - Return to main menu");
    }

    /**
     * Enters a new Test into the database using user input then adds that new Test
     * for the given patient's medical record
     *
     * @param input Scanner for user input
     */
    public void updateWithTest(Scanner input, int pId) {
        String query = "INSERT INTO Test VALUES(?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter the test ID.");
        int testId = 0;

        //Get the test ID from input
        try {
            testId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Test ID must be an integer");
            return;
        }


        //Query database for given test
        try {
            result = statement.executeQuery("SELECT * FROM Test WHERE id = " + testId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If test exists quit
        try {

            if (result.next()) {
                System.err.println("That Test already exists in the database.");
                return;
            } else {
                ps.setInt(1, testId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the name of the Test.");
        String name = null;

        //Get the Test name from input
        try {
            name = input.nextLine();
            if (name.length() > 128) {
                System.out.println("Test name must be 128 characters or less");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }




        System.out.println("Please enter the results of the Test.");
        String results = null;

        //Get the Test results from input
        try {
            results = input.nextLine();
            if (results.length() > 200) {
                System.out.println("Test results must be 200 characters or less");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the name and results
        try {
            ps.setString(2, name);
            ps.setString(3, results);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM Test WHERE id = " + testId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String n = null;
        String r = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                n = result.getString("name");
                r = result.getString("results");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("New Test created with: ID = " + id + ", Name = " + n + ", Results = " + r);


        query = "INSERT INTO AddTest VALUES(?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1,  testId);
            ps.setInt(2, pId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Test ID " + testId + " added to Medical Record of patient with ID " + pId);

        try {
            result = statement.executeQuery("SELECT MedicalRecord.patientId, startDate, endDate, responsibleDoctor, testId, name AS testName, "
                    + "results AS testResults FROM MedicalRecord JOIN(SELECT * FROM Test JOIN AddTest on AddTest.testId = Test.id)"
                    + " AS T ON MedicalRecord.patientId = T.patientId WHERE MedicalRecord.patientId = " + pId + " AND testId = " + testId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        Date sDate = null;
        Date eDate = null;
        int tid = 0, rD = 0;
        String tname = null;
        String tres = null;
        try {
            while (result.next()) {
                id = result.getInt("patientId");
                sDate = result.getDate("startDate");
                eDate = result.getDate("endDate");
                rD = result.getInt("responsibleDoctor");
                tid = result.getInt("testId");
                tname = result.getString("testName");
                tres = result.getString("testResults");
                System.out.println();
                System.out.println();
                System.out.println("Updated Medical Record:");
                if (eDate == null) {
                    System.out.println("Patient ID: " + id + ", Start Date: " + sDate.toString() + ", End Date: NA, Responsible Doctor: " + rD + ", Test ID: "
                            + tid + ", Test Name: " + tname + ", Test Results: " + tres);
                } else {
                    System.out.println("Patient ID: " + id + ", Start Date: " + sDate.toString() + ", End Date: " + eDate.toString() + ", Responsible Doctor: " + rD
                            + ", Test ID: " + tid + ", Test Name: " + tname + ", Test Results: " + tres);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Enters a new Prescription/Treatment into the database using user input then adds that new
     * Prescription/Treatment for the given patient's medical record
     *
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void addNewPrescription(Scanner input, int pId) {
        String query = "INSERT INTO Prescription VALUES(?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter the prescription ID.");
        int preId = 0;

        //Get the prescription ID from input
        try {
            preId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Prescription ID must be an integer");
            return;
        }


        //Query database for given prescription
        try {
            result = statement.executeQuery("SELECT * FROM Prescription WHERE id = " + preId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If prescription exists quit
        try {

            if (result.next()) {
                System.err.println("That prescription already exists in the database.");
                return;
            } else {
                ps.setInt(1, preId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the name of the prescription.");
        String name = null;

        //Get the Prescription name from input
        try {
            name = input.nextLine();
            if (name.length() > 128) {
                System.out.println("Prescription name must be 128 characters or less");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the name
        try {
            ps.setString(2, name);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM Prescription WHERE id = " + preId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String n = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                n = result.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("New prescription created with: ID = " + id + ", Name = " + n);


        query = "INSERT INTO AddPrescription VALUES(?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1,  preId);
            ps.setInt(2, pId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Prescription ID " + preId + " added to Medical Record of patient with ID " + pId);

        try {
            result = statement.executeQuery("SELECT MedicalRecord.patientId, startDate, endDate, responsibleDoctor, prescriptionId, name AS prescriptionName "
                    + "FROM MedicalRecord JOIN(SELECT * FROM Prescription JOIN AddPrescription on AddPrescription.prescriptionId = Prescription.id)"
                    + " AS P ON MedicalRecord.patientId = P.patientId WHERE MedicalRecord.patientId = " + pId + " AND prescriptionId = " + preId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        Date sDate = null;
        Date eDate = null;
        int pid = 0, rD = 0;
        String pname = null;
        try {
            while (result.next()) {
                id = result.getInt("patientId");
                sDate = result.getDate("startDate");
                eDate = result.getDate("endDate");
                rD = result.getInt("responsibleDoctor");
                pid = result.getInt("prescriptionId");
                pname = result.getString("prescriptionName");
                System.out.println();
                System.out.println();
                System.out.println("Updated Medical Record:");
                if (eDate == null) {
                    System.out.println("Patient ID: " + id + ", Start Date: " + sDate.toString() + ", End Date: NA, Responsible Doctor: " + rD + ", Prescription ID: "
                            + pid + ", Prescription Name: " + pname);
                } else {
                    System.out.println("Patient ID: " + id + ", Start Date: " + sDate.toString() + ", End Date: " + eDate.toString() +
                            ", Responsible Doctor: " + rD + ", Prescription ID: " + pid + ", Prescription Name: " + pname);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Adds new Check-In information using user input and adds the Check-In information
     * to the patients Medical Record
     *
     * @param input Scanner for user input
     * @param pId patient's ID
     */
    public void addNewCheckInInfo(Scanner input, int pId) {

        //Query database for given check in for patient
        try {
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If check-in exists exists quit
        try {

            if (result.next()) {
                System.err.println("This patient already has a check-in associated with their Medical Record.");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        String query = "INSERT INTO CheckInInformation VALUES(?, ?, ?, ?, ?)";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1,  pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the Ward number for Check-In.");
        int wardNum = 0;

        //Get the ward number from input
        try {
            wardNum = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Ward number must be an integer");
            return;
        }


        System.out.println("Please enter the Bed number for Check-In.");
        int bedNum = 0;

        //Get the bed number from input
        try {
            bedNum = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Bed number must be an integer");
            return;
        }


        System.out.println("Please enter the start date in the form yyyy-mm-dd");
        String startDate = null;

        //Get the start date from input
        try {
            startDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Parse the given date into a Date object
        Date startDateUtil = null;
        java.sql.Date sqlStartDate = null;
        try {
            startDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            sqlStartDate = new java.sql.Date(startDateUtil.getTime());
            ps.setInt(2, wardNum);
            ps.setInt(3, bedNum);
            ps.setDate(4, sqlStartDate);
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the end date in the form yyyy-mm-dd, if still active type na (not applicable) and press enter to continue.");
        String endDate = null;

        //Get the end date from input
        try {
            endDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the appropriate end date depending on user input
        Date endDateUtil = null;
        try {
            if (!endDate.toLowerCase().equals("na")) {
                endDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                java.sql.Date sqlEndDate = new java.sql.Date(endDateUtil.getTime());
                if (sqlEndDate.before(sqlStartDate)) {
                    System.err.println("End date must be on or after the start date.");
                    return;
                }
                ps.setDate(5, sqlEndDate);
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }



        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0, wN = 0, bN = 0;
        Date sDate = null, eDate = null;

        try {
            while (result.next()) {
                id = result.getInt("patientId");
                wN = result.getInt("wardNumber");
                bN = result.getInt("bedNumber");
                sDate = result.getDate("startDate");
                eDate = result.getDate("endDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        if (eDate == null) {
            System.out.println("New Check-In created with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = NA");
        } else {
            System.out.println("New Check-In created with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = " + eDate.toString());
        }



        System.out.println("Check-In added to Medical Record of patient with ID " + pId);

        try {
            result = statement.executeQuery("SELECT MedicalRecord.patientId, MedicalRecord.startDate as medicalRecordStartDate, "
                    + "MedicalRecord.endDate as medicalRecordEndDate, responsibleDoctor, wardNumber, bedNumber, CheckInInformation.startDate as checkInStartDate, "
                    + "CheckInInformation.endDate as checkInEndDate FROM MedicalRecord JOIN CheckInInformation on MedicalRecord.patientId = "
                    + "CheckInInformation.patientId");

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int pI = 0, ward = 0, bed = 0, rD = 0;
        Date msDate = null, meDate = null, csDate = null, ceDate = null;

        try {
            while (result.next()) {
                pI = result.getInt("patientId");
                msDate = result.getDate("medicalRecordStartDate");
                meDate = result.getDate("medicalRecordEndDate");
                rD = result.getInt("responsibleDoctor");
                ward = result.getInt("wardNumber");
                bed = result.getInt("bedNumber");
                csDate = result.getDate("checkInStartDate");
                ceDate = result.getDate("CheckInEndDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Updated Medical Record:");
        if (meDate == null && ceDate == null) {
            System.out.println("Patient ID = " + pI + ", Medical Record Start Date = " + msDate.toString() + ", Medical Record End Date = NA"
                    + ", Responsible Doctor = " + rD + ", Ward Number = " + ward + ", Bed Number = " + bed + ", Check-In Start Date: " + csDate.toString() + ", Check-In End Date = NA");
        } else if (meDate != null && ceDate == null){
            System.out.println("Patient ID = " + pI + ", Medical Record Start Date = " + msDate.toString() + ", Medical Record End Date = " + meDate.toString()
                    + ", Responsible Doctor = " + rD + ", Ward Number = " + ward + ", Bed Number = " + bed + ", Check-In Start Date: " + csDate.toString() + ", Check-In End Date = NA");
        } else if (meDate == null && ceDate != null) {
            System.out.println("Patient ID = " + pI + ", Medical Record Start Date = " + msDate.toString() + ", Medical Record End Date = NA"
                    + ", Responsible Doctor = " + rD + ", Ward Number = " + ward + ", Bed Number = " + bed + ", Check-In Start Date: " + csDate.toString() + ", Check-In End Date = " + ceDate.toString());
        } else {
            System.out.println("Patient ID = " + pI + ", Medical Record Start Date = " + msDate.toString() + ", Medical Record End Date = " + meDate.toString()
                    + "Responsible Doctor = " + rD + ", Ward Number = " + ward + ", Bed Number = " + bed + ", Check-In Start Date: " + csDate.toString() + ", Check-In End Date = " + ceDate.toString());
        }

    }

    /**
     * Updates a Patient's Medical Record with diagnosis details
     *
     * @param input Scanner for user input
     * @param pId Patient's ID for update
     */
    public void updateWithDiagnosisDetails(Scanner input, int pId) {
        String query = "INSERT INTO DiagnosisDetails VALUES(?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter the Diagnosis Details ID.");
        int diagId = 0;

        //Get the diagnosis details ID from input
        try {
            diagId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Diagnosis Details ID must be an integer");
            return;
        }


        //Query database for given diagnosis details
        try {
            result = statement.executeQuery("SELECT * FROM DiagnosisDetails WHERE id = " + diagId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If diagnosis details exists quit
        try {

            if (result.next()) {
                System.err.println("The diagnosis details already exist in the database.");
                return;
            } else {
                ps.setInt(1, diagId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the name of the diagnosis details.");
        String details = null;

        //Get the Diagnosis details name from input
        try {
            details = input.nextLine();
            if (details.length() > 200) {
                System.out.println("Diagnosis Details name must be 200 characters or less");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the name
        try {
            ps.setString(2, details);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM DiagnosisDetails WHERE id = " + diagId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String n = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                n = result.getString("details");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("New diagnosis details created with: ID = " + id + ", Details = " + n);


        query = "INSERT INTO AddDiagnosis VALUES(?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1,  diagId);
            ps.setInt(2, pId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Diagnosis Details ID " + diagId + " added to Medical Record of patient with ID " + pId);

        try {
            result = statement.executeQuery("SELECT MedicalRecord.patientId, startDate, endDate, responsibleDoctor, diagnosisId, details AS diagnosisDetails "
                    + "FROM MedicalRecord JOIN(SELECT * FROM DiagnosisDetails JOIN AddDiagnosis on AddDiagnosis.diagnosisId = DiagnosisDetails.id)"
                    + " AS D ON MedicalRecord.patientId = D.patientId WHERE MedicalRecord.patientId = " + pId + " AND diagnosisId = " + diagId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        Date sDate = null;
        Date eDate = null;
        int did = 0, rD = 0;
        String dets = null;
        try {
            while (result.next()) {
                id = result.getInt("patientId");
                sDate = result.getDate("startDate");
                eDate = result.getDate("endDate");
                rD = result.getInt("responsibleDoctor");
                did = result.getInt("diagnosisId");
                dets = result.getString("diagnosisDetails");
                System.out.println();
                System.out.println();
                System.out.println("Updated Medical Record:");
                if (eDate == null) {
                    System.out.println("Patient ID: " + id + ", Start Date: " + sDate.toString() + ", End Date: NA, Responsible Doctor: " + rD +
                            ", Diagnosis Details ID: " + did + ", Diagnosis Details: " + dets);
                } else {
                    System.out.println("Patient ID: " + id + ", Start Date: " + sDate.toString() + ", End Date: " + eDate.toString() + ", Responsible Doctor: " + rD
                            + ", Diagnosis Details ID: " + did + ", Diagnosis Details: " + dets);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the end date for the given Patient's Medical Record
     *
     * @param input Scanner for user input
     * @param pId Patient ID for update
     */
    public void updateEndDateForMedicalRecord(Scanner input, int pId) {
        Date sqlStartDate = null;
        try {
            result = statement.executeQuery("SELECT startDate FROM MedicalRecord WHERE patientId = " + pId);
            if (result.next()) {
                sqlStartDate = result.getDate("startDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the end date in the form yyyy-mm-dd. Must be on or after " + sqlStartDate.toString());
        String endDate = null;

        //Get the end date from input
        try {
            endDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the appropriate end date depending on user input
        Date endDateUtil = null;
        try {
            endDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            java.sql.Date sqlEndDate = new java.sql.Date(endDateUtil.getTime());
            if (sqlEndDate.before(sqlStartDate)) {
                System.err.println("End date must be on or after the start date.");
                return;
            }
            statement.executeUpdate("UPDATE MedicalRecord SET endDate = DATE '" + sqlEndDate.toString() + "' WHERE patientId = " + pId);
            result = statement.executeQuery("SELECT * FROM MedicalRecord WHERE patientId = " + pId);
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0, rD = 0;
        Date sD = null;
        Date eD = null;
        try {
            while (result.next()) {
                id = result.getInt("patientID");
                sD = result.getDate("startDate");
                eD = result.getDate("endDate");
                rD = result.getInt("responsibleDoctor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Update Medical Record: Patient ID = " + id + ", Start Date = " + sD.toString() + ", End Date = " + eD.toString() + ", Responsible Doctor = " + rD);

    }

    /**
     * Updates the end date for the given Patient's Check-In information
     *
     * @param input Scanner for user input
     * @param pId Patient ID for update
     */
    public void updateEndDateForCheckIn(Scanner input, int pId) {
        Date sqlStartDate = null;
        try {
            result = statement.executeQuery("SELECT startDate FROM CheckInInformation WHERE patientId = " + pId);
            if (result.next()) {
                sqlStartDate = result.getDate("startDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the end date in the form yyyy-mm-dd. Must be on or after " + sqlStartDate.toString());
        String endDate = null;

        //Get the end date from input
        try {
            endDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the appropriate end date depending on user input
        Date endDateUtil = null;
        try {
            endDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            java.sql.Date sqlEndDate = new java.sql.Date(endDateUtil.getTime());
            if (sqlEndDate.before(sqlStartDate)) {
                System.err.println("End date must be on or after the start date.");
                return;
            }
            statement.executeUpdate("UPDATE CheckInInformation SET endDate = DATE '" + sqlEndDate.toString() + "' WHERE patientId = " + pId);
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0, wN = 0, bN = 0;
        Date sDate = null, eDate = null;

        try {
            while (result.next()) {
                id = result.getInt("patientId");
                wN = result.getInt("wardNumber");
                bN = result.getInt("bedNumber");
                sDate = result.getDate("startDate");
                eDate = result.getDate("endDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Check-In updated: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                + ", Start Date: " + sDate.toString() + ", End Date = " + eDate.toString());

    }

    /**
     * Shows the full Medical Record for Patient that already has
     * an existing Medical Record in the database
     *
     * @param input Scanner for user input
     */
    public void viewMedicalRecord(Scanner input) {
        System.out.println("Please enter patient ID, the patient must already be present in the database.");
        int pId = 0;
        //Get the patient ID from input
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }


        //Query database for given patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient does not exist quit
        try {

            if (!result.next()) {
                System.err.println("That Patient does not exist in the database");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Get the complete record as shown with demo data
        try {
            result = statement.executeQuery("SELECT MedicalRecord.patientId, startDate, endDate, responsibleDoctor, name AS prescription, details as diagnosisDetails "
                    + "FROM MedicalRecord INNER JOIN (Prescription INNER JOIN AddPrescription ON AddPrescription.prescriptionId = Prescription.id "
                    + "INNER JOIN (DiagnosisDetails INNER JOIN AddDiagnosis ON AddDiagnosis.diagnosisId = DiagnosisDetails.id) ON AddPrescription.patientId = AddDiagnosis.patientId)"
                    + "ON MedicalRecord.patientId = AddPrescription.patientId WHERE MedicalRecord.patientId = " + pId );
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0, rD = 0;
        Date sD = null, eD = null;
        StringBuilder pre = new StringBuilder();
        StringBuilder dets = new StringBuilder();
        String res = null, res2 = null;
        try {
            while (result.next()) {
                id = result.getInt("patientID");
                sD = result.getDate("startDate");
                eD = result.getDate("endDate");
                rD = result.getInt("responsibleDoctor");
                res = result.getString("prescription");
                if (!pre.toString().contains(res)) {
                    pre.append(res + ", ");
                }
                res2 = result.getString("diagnosisDetails");
                if (!dets.toString().contains(res2)) {
                    dets.append(res2 + ", ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        if (eD == null) {
            System.out.println("Medical Record");
            System.out.println("--------------------------");
            System.out.println("Patient ID: " + id);
            System.out.println("Start Date: " + sD.toString());
            System.out.println("End Date: NA");
            System.out.println("Responsible Doctor: " + rD);
            System.out.println("Prescription: " + pre.toString().substring(0, pre.length() - 2));
            System.out.println("Diagnosis Details: " + dets.toString().substring(0, dets.length() - 2));
            System.out.println("--------------------------");
        } else {
            System.out.println("Medical Record");
            System.out.println("--------------------------");
            System.out.println("Patient ID: " + id);
            System.out.println("Start Date: " + sD.toString());
            System.out.println("End Date: " + eD.toString());
            System.out.println("Responsible Doctor: " + rD);
            System.out.println("Prescription: " + pre.toString().substring(0, pre.length() - 2));
            System.out.println("Diagnosis Details: " + dets.toString().substring(0, dets.length() - 2));
            System.out.println("--------------------------");
        }
    }

    /**
     * Enters new staff information
     * 1) Create new tuple in Person table for basic information and
     * 2) Create new tuple in staff table for job information
     *
     * @param input input from command line
     */
    public void enterNewStaffInformation(Scanner input) {
        String query = "INSERT INTO Person VALUES(?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter person ID, the person must NOT be present in the database.");
        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Person ID must be an integer");
            return;
        }

        //Query database for given person, quit if person exists
        try {
            result = statement.executeQuery("SELECT * FROM Person WHERE id = " + pId);
            if (result.next()) {
                System.err.println("That person already exist in the database");
                return;
            } else {
                ps.setInt(1, pId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter name of new staff.");
        String name = null;
        try {
            name = input.nextLine();
            if (name.length() > 128) {
                System.out.println("Name must be 128 characters or less");
                return;
            }
            ps.setString(2, name);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter age of new staff in integer form.");
        int age = 0;
        try {
            age = Integer.parseInt(input.nextLine());
            ps.setInt(3, age);
        } catch (Exception e) {
            System.err.println("Person age must be an integer");
            return;
        }

        System.out.println("Please enter gender of new staff (enter F for female or M for male).");
        String gender = null;
        try {
            gender = input.nextLine();
            if (gender.length() > 2) {
                System.out.println("Gender must be 2 characters or less");
                return;
            }
            ps.setString(4, gender);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter address of new staff, use \",\" to seperate road name with city name.");
        String addr = null;
        try {
            addr = input.nextLine();
            if (addr.length() > 128) {
                System.out.println("Address must be 128 characters or less");
                return;
            }
            ps.setString(5, addr);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter phone number of new staff.");
        String phone = null;
        try {
            phone = input.nextLine();
            if (phone.length() > 13) {
                System.out.println("Phone number must be 13 characters or less");
                return;
            }
            ps.setString(6, phone);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        //Execute the update for person table
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM Person WHERE Id = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pName = null;
        int pAge = 0;
        String pGender = null;
        String pAddr = null;
        String pPhone = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                pName = result.getString("name");
                pAge = result.getInt("age");
                pGender = result.getString("gender");
                pAddr = result.getString("address");
                pPhone = result.getString("phone");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("New Person created with: ID = " + id + ", name = " + pName + ", age = " + pAge + ", gender = " + pGender + ", address = "
                + pAddr + ", phone number = " + pPhone);

        query = "INSERT INTO Staff VALUES(?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter job title of new staff.");
        String jobTitle = null;
        try {
            jobTitle = input.nextLine();
            if (jobTitle.length() > 128) {
                System.out.println("Job title must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter professional title of new staff, enter na if not applicable.");
        String profTitle = null;
        try {
            profTitle = input.nextLine();
            if (profTitle.length() > 128) {
                System.out.println("Professional title must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter department of new staff.");
        String department = null;
        try {
            department = input.nextLine();
            if (department.length() > 128) {
                System.out.println("Department must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Set the staff information
        try {
            ps.setInt(1, pId);
            ps.setString(2, jobTitle);
            ps.setString(3, profTitle);
            ps.setString(4, department);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM Staff WHERE id = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int tId = 0;
        String jT = null;
        String pT = null;
        String dpt = null;
        try {
            while (result.next()) {
                tId = result.getInt("id");
                jT = result.getString("jobTitle");
                pT = result.getString("professionalTitle");
                dpt = result.getString("department");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("New staff created with: ID = " + tId + ", Job title = " + jT + ", Professional title = " + pT + ", department = " + dpt);


    }

    /**
     * Updates information of an existing staff
     * @param input input from command line
     */
    public void updateStaffInformation(Scanner input) {
        System.out.println("Please enter person/staff ID, the person/staff must already be present in the database.");
        int pId = 0;
        //Get the person ID from input
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Person ID must be an integer");
            return;
        }

        //Query database for given person, quit if person does not exist
        try {
            result = statement.executeQuery("SELECT * FROM Person WHERE id = " + pId);
            if (!result.next()) {
                System.err.println("That Person does not exist in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        printPersonInformationUpdateOptions();
        while (true) {
            String in = input.nextLine();
            in = in.toLowerCase();
            switch(in) {

                case "1" :
                    updateName(input, pId);
                    printPersonInformationUpdateOptions();
                    break;

                case "2" :
                    updateAge(input, pId);
                    printPersonInformationUpdateOptions();
                    break;

                case "3" :
                    updateGender(input, pId);
                    printPersonInformationUpdateOptions();
                    break;

                case "4" :
                    updateAddress(input, pId);
                    printPersonInformationUpdateOptions();
                    break;

                case "5" :
                    updatePhoneNumber(input, pId);
                    printPersonInformationUpdateOptions();
                    break;

                case "6" :
                    updateJobTitle(input, pId);
                    printPersonInformationUpdateOptions();
                    break;

                case "7" :
                    updateProfessionalTitle(input, pId);
                    printPersonInformationUpdateOptions();
                    break;

                case "8" :
                    updateDepartment(input, pId);
                    printPersonInformationUpdateOptions();
                    break;

                case "0" :
                    deletePerson(pId);
                    printReturnToMenu();
                    break;

                case "p" :
                    printPersonInformationUpdateOptions();
                    break;

                case "exit" :
                    return;

                default:
                    System.out.println("Invalid input");
                    printPersonInformationUpdateOptions();
                    break;
            }
        }
    }

    /**
     * Prints the options for the information update menu
     */
    public void printPersonInformationUpdateOptions() {

        System.out.println();
        System.out.println();
        System.out.println("What part of this person's Information would you like to update?");
        System.out.println();
        System.out.println("1 - Update Name");
        System.out.println("2 - Update Age");
        System.out.println("3 - Update Gender");
        System.out.println("4 - Update Address");
        System.out.println("5 - Update Phone Number");
        System.out.println("6 - Update Job Title");
        System.out.println("7 - Update Professional Title");
        System.out.println("8 - Update Department");
        System.out.println("0 - Delete Person");
        System.out.println("p - Print options");
        System.out.println("exit - Return to main menu");
    }

    /**
     * Updates name for given person
     * @param input Scanner for user input
     * @param pId person ID
     */
    public void updateName(Scanner input, int pId) {

        System.out.println("Please enter new name of the staff.");
        String name = null;
        try {
            name = input.nextLine();
            if (name.length() > 128) {
                System.out.println("Name must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Person SET name = " + "\"" + name + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Person WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pName = null;
        int pAge = 0;
        String pGender = null;
        String pAddr = null;
        String pPhone = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                pName = result.getString("name");
                pAge = result.getInt("age");
                pGender = result.getString("gender");
                pAddr = result.getString("address");
                pPhone = result.getString("phone");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Person name updated with: ID = " + id + ", name = " + pName + ", age = " + pAge + ", gender = " + pGender + ", address = "
                + pAddr + ", phone number = " + pPhone);

    }

    /**
     * Updates age for given person
     * @param input Scanner for user input
     * @param pId person ID
     */
    public void updateAge(Scanner input, int pId) {

        System.out.println("Please enter new age of the staff.");
        int age = 0;
        try {
            age = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Person age must be an integer");
            return;
        }

        String query = "UPDATE Person SET age = " + age + " WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Person WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pName = null;
        int pAge = 0;
        String pGender = null;
        String pAddr = null;
        String pPhone = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                pName = result.getString("name");
                pAge = result.getInt("age");
                pGender = result.getString("gender");
                pAddr = result.getString("address");
                pPhone = result.getString("phone");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Person age updated with: ID = " + id + ", name = " + pName + ", age = " + pAge + ", gender = " + pGender + ", address = "
                + pAddr + ", phone number = " + pPhone);

    }

    /**
     * Updates gender for given person
     * @param input Scanner for user input
     * @param pId person ID
     */
    public void updateGender(Scanner input, int pId) {

        System.out.println("Please enter new gender of the staff.");
        String gender = null;
        try {
            gender = input.nextLine();
            if (gender.length() > 2) {
                System.out.println("Gender must be 2 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Person SET gender = " + "\"" + gender + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Person WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pName = null;
        int pAge = 0;
        String pGender = null;
        String pAddr = null;
        String pPhone = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                pName = result.getString("name");
                pAge = result.getInt("age");
                pGender = result.getString("gender");
                pAddr = result.getString("address");
                pPhone = result.getString("phone");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Person gender updated with: ID = " + id + ", name = " + pName + ", age = " + pAge + ", gender = " + pGender + ", address = "
                + pAddr + ", phone number = " + pPhone);

    }

    /**
     * Updates address for given person
     * @param input Scanner for user input
     * @param pId person ID
     */
    public void updateAddress(Scanner input, int pId) {

        System.out.println("Please enter new address of the staff.");
        String addr = null;
        try {
            addr = input.nextLine();
            if (addr.length() > 128) {
                System.out.println("Address must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Person SET address = " + "\"" + addr + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Person WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pName = null;
        int pAge = 0;
        String pGender = null;
        String pAddr = null;
        String pPhone = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                pName = result.getString("name");
                pAge = result.getInt("age");
                pGender = result.getString("gender");
                pAddr = result.getString("address");
                pPhone = result.getString("phone");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Person address updated with: ID = " + id + ", name = " + pName + ", age = " + pAge + ", gender = " + pGender + ", address = "
                + pAddr + ", phone number = " + pPhone);

    }

    /**
     * Updates address for given person
     * @param input Scanner for user input
     * @param pId person ID
     */
    public void updatePhoneNumber(Scanner input, int pId) {

        System.out.println("Please enter new phone number of the staff.");
        String phone = null;
        try {
            phone = input.nextLine();
            if (phone.length() > 13) {
                System.out.println("Phone number must be 13 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Person SET phone = " + "\"" + phone + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Person WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pName = null;
        int pAge = 0;
        String pGender = null;
        String pAddr = null;
        String pPhone = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                pName = result.getString("name");
                pAge = result.getInt("age");
                pGender = result.getString("gender");
                pAddr = result.getString("address");
                pPhone = result.getString("phone");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Person phone number updated with: ID = " + id + ", name = " + pName + ", age = " + pAge + ", gender = " + pGender + ", address = "
                + pAddr + ", phone number = " + pPhone);
        System.out.println();


    }

    /**
     * Updates job title for given person in staff table
     * @param input Scanner for user input
     * @param pId person ID
     */
    public void updateJobTitle(Scanner input, int pId) {
        System.out.println("Please enter new job title of the staff.");
        String jobTitle = null;
        try {
            jobTitle = input.nextLine();
            if (jobTitle.length() > 128) {
                System.out.println("Job title must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Staff SET jobTitle = " + "\"" + jobTitle + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Staff WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String jT = null;
        String pT = null;
        String dpt = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                jT = result.getString("jobTitle");
                pT = result.getString("professionalTitle");
                dpt = result.getString("department");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Staff job title updated with: ID = " + id + ", Job title = " + jT + ", Professional title = " + pT + ", department = " + dpt);

    }

    /**
     * Updates professional title for given person in staff table
     * @param input Scanner for user input
     * @param pId person ID
     */
    public void updateProfessionalTitle(Scanner input, int pId) {
        System.out.println("Please enter new professional title of the staff, enter na if not applicable.");
        String profTitle = null;
        try {
            profTitle = input.nextLine();
            if (profTitle.length() > 128) {
                System.out.println("Professional title must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Staff SET professionalTitle = " + "\"" + profTitle + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Staff WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String jT = null;
        String pT = null;
        String dpt = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                jT = result.getString("jobTitle");
                pT = result.getString("professionalTitle");
                dpt = result.getString("department");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Staff professional title updated with: ID = " + id + ", Job title = " + jT + ", Professional title = " + pT + ", department = " + dpt);

    }

    /**
     * Updates department for given person in staff table
     * @param input Scanner for user input
     * @param pId person ID
     */
    public void updateDepartment(Scanner input, int pId) {
        System.out.println("Please enter new department of the staff.");
        String department = null;
        try {
            department = input.nextLine();
            if (department.length() > 128) {
                System.out.println("Department must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Staff SET department = " + "\"" + department + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Staff WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String jT = null;
        String pT = null;
        String dpt = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                jT = result.getString("jobTitle");
                pT = result.getString("professionalTitle");
                dpt = result.getString("department");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Staff department updated with: ID = " + id + ", Job title = " + jT + ", Professional title = " + pT + ", department = " + dpt);

    }

    /**
     * Deletes information in Person table and Staff table
     * @param pId person ID
     */
    public void deletePerson(int pId) {
        String query = "DELETE FROM Person WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        query = "DELETE FROM Staff WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            result = statement.executeQuery("SELECT * FROM Person WHERE id = " + pId);
            if(!result.next()) {
                System.out.println("Person deleted with id = " + pId);
            } else {
                System.err.println("Person fail to delete");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

    }

    /**
     * Enters new patient information
     * Create new tuple in Person table for basic information and
     *
     * @param input input from command line
     */
    public void enterNewPatientInformation(Scanner input) {
        String query = "INSERT INTO Patient VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter patient ID, the patient must NOT be present in the database.");
        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }

        //Query database for given person, quit if person exists
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
            if (result.next()) {
                System.err.println("That person already exist in the database");
                return;
            } else {
                ps.setInt(1, pId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter SSN of new patient, enter na if no SSN is applicable.");
        String ssn = null;
        try {
            ssn = input.nextLine();
            if (ssn.length() > 9) {
                System.out.println("SSN must be 9 digits or less");
                return;
            }
            ps.setString(2, ssn);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter name of new patient.");
        String name = null;
        try {
            name = input.nextLine();
            if (name.length() > 128) {
                System.out.println("Name must be 128 characters or less");
                return;
            }
            ps.setString(3, name);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter the date of birth of new patient in form yyyy-mm-dd");
        String dob = null;
        try {
            dob = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }
        //Parse the given date into a Date object
        Date dateOfBirth = null;
        java.sql.Date sqlDate = null;
        try {
            dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
            sqlDate = new java.sql.Date(dateOfBirth.getTime());
            ps.setDate(4, sqlDate);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter gender of new patient (enter F for female or M for male).");
        String gender = null;
        try {
            gender = input.nextLine();
            if (gender.length() > 1) {
                System.out.println("Please enter F for female or M for male.");
                return;
            }
            ps.setString(5, gender);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter age of new patient in integer form.");
        int age = 0;
        try {
            age = Integer.parseInt(input.nextLine());
            ps.setInt(6, age);
        } catch (Exception e) {
            System.err.println("Patient age must be an integer");
            return;
        }

        System.out.println("Please enter phone number of new patient.");
        String phone = null;
        try {
            phone = input.nextLine();
            if (phone.length() > 10) {
                System.out.println("Phone number must be 10 characters or less");
                return;
            }
            ps.setString(7, phone);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter address of new staff, use \",\" to seperate road name with city name.");
        String addr = null;
        try {
            addr = input.nextLine();
            if (addr.length() > 200) {
                System.out.println("Address must be 200 characters or less");
                return;
            }
            ps.setString(8, addr);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter processing treatment plan of new patient in integer form.");
        int processingTreatment = 0;
        try {
            processingTreatment = Integer.parseInt(input.nextLine());
            ps.setInt(9, processingTreatment);
        } catch (Exception e) {
            System.err.println("Processing treatment plan must be an integer");
            return;
        }

        System.out.println("Please indicate whether new patient is in ward (yes or no).");
        String isInWard = null;
        try {
            isInWard = input.nextLine();
            if (isInWard.length() > 3) {
                System.out.println("Input must be 3 characters or less");
                return;
            }
            ps.setString(10, isInWard);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please indicate whether new patient has completed treatment (yes or no).");
        String completedTreatment = null;
        try {
            completedTreatment = input.nextLine();
            if (completedTreatment.length() > 3) {
                System.out.println("Input must be 3 characters or less");
                return;
            }
            ps.setString(11, completedTreatment);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        //Execute the update for person table
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM Patient WHERE Id = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("New Patient created with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);

    }

    /**
     * Updates information of an existing staff
     * @param input input from command line
     */
    public void updatePatientInformation(Scanner input) {
        System.out.println("Please enter patient ID, the patient must already be present in the database.");
        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Person ID must be an integer");
            return;
        }

        //Query database for given patient, quit if patient does not exist
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
            if (!result.next()) {
                System.err.println("That Patient does not exist in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        printPatientInformationUpdateOptions();
        while (true) {
            String in = input.nextLine();
            in = in.toLowerCase();
            switch(in) {

                case "1" :
                    updatePatientName(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "2" :
                    updatePatientAge(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "3" :
                    updatePatientGender(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "4" :
                    updatePatientAddress(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "5" :
                    updatePatientPhoneNumber(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "6" :
                    updatePatientSSN(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "7" :
                    updatePatientDateOfBirth(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "8" :
                    updateTreatmentPlan(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "9" :
                    updatePatientInWard(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "10" :
                    updateTreatmentStatus(input, pId);
                    printPatientInformationUpdateOptions();
                    break;

                case "0" :
                    deletePatient(pId);
                    printReturnToMenu();
                    break;

                case "p" :
                    printPatientInformationUpdateOptions();
                    break;

                case "exit" :
                    return;

                default:
                    System.out.println("Invalid input");
                    printPatientInformationUpdateOptions();
                    break;
            }
        }
    }

    /**
     * Prints the options for the information update menu
     */
    public void printPatientInformationUpdateOptions() {

        System.out.println();
        System.out.println();
        System.out.println("What part of this patient's Information would you like to update?");
        System.out.println();
        System.out.println("1 - Update Name");
        System.out.println("2 - Update Age");
        System.out.println("3 - Update Gender");
        System.out.println("4 - Update Address");
        System.out.println("5 - Update Phone Number");
        System.out.println("6 - Update SSN");
        System.out.println("7 - Update Date of Birth");
        System.out.println("8 - Update treatment plan");
        System.out.println("9 - Update whether patient is in ward");
        System.out.println("10 - Update whether patient is ccompleting treatment");
        System.out.println("0 - Delete Patient");
        System.out.println("p - Print options");
        System.out.println("exit - Return to main menu");
    }

    /**
     * Updates name for given patient
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void updatePatientName(Scanner input, int pId) {

        System.out.println("Please enter new name of the patient.");
        String name = null;
        try {
            name = input.nextLine();
            if (name.length() > 128) {
                System.out.println("Name must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Patient SET name = " + "\"" + name + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient name updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Updates age for given patient
     * @param input Scanner for user input
     * @param pId person ID
     */
    public void updatePatientAge(Scanner input, int pId) {

        System.out.println("Please enter new age of the patient.");
        int age = 0;
        try {
            age = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient age must be an integer");
            return;
        }

        String query = "UPDATE Patient SET age = " + age + " WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient age updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Updates gender for given patient
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void updatePatientGender(Scanner input, int pId) {

        System.out.println("Please enter new gender of the patient.");
        String gender = null;
        try {
            gender = input.nextLine();
            if (gender.length() > 2) {
                System.out.println("Gender must be 2 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Patient SET gender = " + "\"" + gender + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient gender updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Updates address for given patient
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void updatePatientAddress(Scanner input, int pId) {

        System.out.println("Please enter new address of the patient.");
        String addr = null;
        try {
            addr = input.nextLine();
            if (addr.length() > 128) {
                System.out.println("Address must be 128 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Patient SET address = " + "\"" + addr + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient address updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Updates address for given patient
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void updatePatientPhoneNumber(Scanner input, int pId) {

        System.out.println("Please enter new phone number of the patient.");
        String phone = null;
        try {
            phone = input.nextLine();
            if (phone.length() > 13) {
                System.out.println("Phone number must be 13 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Patient SET phone = " + "\"" + phone + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given Patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient phone number updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Updates SSN for given patient
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void updatePatientSSN(Scanner input, int pId) {

        System.out.println("Please enter new SSN for patient.");
        String ssn = null;
        try {
            ssn = input.nextLine();
            if (ssn.length() > 9) {
                System.out.println("SSN must be 9 digits or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Patient SET ssn = " + "\"" + ssn + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient gender updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Updates date of birth for given patient
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void updatePatientDateOfBirth(Scanner input, int pId) {

        System.out.println("Please enter the new date of birth of patient in form yyyy-mm-dd");
        String dob = null;
        try {
            dob = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }
        //Parse the given date into a Date object
        Date dateOfBirth = null;
        java.sql.Date sqlDate = null;
        try {
            dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
            sqlDate = new java.sql.Date(dateOfBirth.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String query = "UPDATE Patient SET dob = " + "\"" + sqlDate + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given person
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient date of birth updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Updates treatment plan for given patient
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void updateTreatmentPlan(Scanner input, int pId) {

        System.out.println("Please enter new processing treatment plan of patient in integer form.");
        int processingTreatment = 0;
        try {
            processingTreatment = Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            System.err.println("Processing treatment plan must be an integer");
            return;
        }

        String query = "UPDATE Patient SET treatment = " + "\"" + processingTreatment + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given Patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient phone number updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Updates whether patient is in ward for given patient
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void updatePatientInWard(Scanner input, int pId) {
        System.out.println("Please indicate whether patient is in ward (yes or no).");
        String isInWard = null;
        try {
            isInWard = input.nextLine();
            if (isInWard.length() > 3) {
                System.out.println("Input must be 3 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Patient SET inWard = " + "\"" + isInWard + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given Patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient phone number updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Updates address for given patient
     * @param input Scanner for user input
     * @param pId patient ID
     */
    public void updateTreatmentStatus(Scanner input, int pId) {

        System.out.println("Please indicate whether patient has completed treatment (yes or no).");
        String completedTreatment = null;
        try {
            completedTreatment = input.nextLine();
            if (completedTreatment.length() > 3) {
                System.out.println("Input must be 3 characters or less");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Patient SET status = " + "\"" + completedTreatment + "\"" + "WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given Patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String pSSN = null;
        String pName = null;
        Date pDOB = null;
        String pGender = null;
        int pAge = 0;
        String pPhone = null;
        String pAddr = null;
        int pPlan = 0;
        String pIsInWard = null;
        String pComp = null;

        try {
            while (result.next()) {
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Patient phone number updated with: ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);


    }

    /**
     * Deletes given patient
     * @param pId
     */
    public void deletePatient(int pId) {
        String query = "DELETE FROM Patient WHERE id = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
            if(!result.next()) {
                System.out.println("Patient deleted with id = " + pId);
            } else {
                System.err.println("Patient fail to delete");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Enters new ward information in ward table
     *
     * @param input input from command line
     */
    public void enterNewWardInformation(Scanner input) {
        String query = "INSERT INTO Ward VALUES(?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter ward number, the ward must NOT be present in the database.");
        int number = 0;
        try {
            number = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Ward number must be an integer");
            return;
        }

        //Query database for given ward, quit if ward exists
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);
            if (result.next()) {
                System.err.println("That ward already exist in the database");
                return;
            } else {
                ps.setInt(1, number);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter capacity of new ward.");
        int capacity = 0;
        try {
            capacity = Integer.parseInt(input.nextLine());
            ps.setInt(2, capacity);
        } catch (Exception e) {
            System.err.println("Ward capacity must be an integer");
            return;
        }

        System.out.println("Please enter patient ids of new ward, use only \",\" to seperate different patient ids");
        String patientIds = null;
        try {
            patientIds = input.nextLine();
            if (patientIds.length() > 200) {
                System.out.println("Input must be patient Ids");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        List<String> patientList = Arrays.asList(patientIds.split(","));
        for(String id : patientList) {
            int pId = 0;
            try {
                pId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                System.err.println("Patient ID must be an integer");
                return;
            }

            //Query database for given patient, quit if patient does not exist
            try {
                result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
                if (!result.next()) {
                    System.err.println("That Patient does not exist in the database");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }

        try {
            ps.setString(3, patientIds);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter charges of new ward.");
        double charge = 0.0;
        try {
            charge = Double.parseDouble(input.nextLine());
            ps.setDouble(4, charge);
        } catch (SQLException e) {
            System.err.println("Invalid input");
            return;
        }

        System.out.println("Please enter number of occcupants of new ward.");
        int occupants = 0;
        try {
            occupants = Integer.parseInt(input.nextLine());
            ps.setInt(5, occupants);
        } catch (Exception e) {
            System.err.println("Number of occupants must be an integer");
            return;
        }

        System.out.println("Please enter id of responsible nurse of new ward in integer form.");
        int nId = 0;
        try {
            nId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Responsible nurse id must be an integer");
            return;
        }

        //quit if staff is not nurse or nurse is not in the database
        try{
            result = statement.executeQuery("SELECT * FROM Staff WHERE Id = " + nId);
            if(!result.next()) {
                System.err.println("Responsible nurse must be in the database");
                return;
            }
            String job = result.getString("jobTitle");
            if(job == null || !job.toLowerCase().equals("nurse")) {
                System.err.println("Responsible staff must be a nurse");
                return;
            }
            ps.setInt(6, nId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //Execute the update for ward table
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int wN = 0;
        int wC = 0;
        String pList = null;
        double wCharges = 0;
        int wO = 0;
        int wRN = 0;
        try {
            while (result.next()) {
                wN = result.getInt("number");
                wC = result.getInt("capacity");
                pList = result.getString("patientIds");
                wCharges = result.getDouble("chargesPerDay");
                wO = result.getInt("occupants");
                wRN = result.getInt("responsibleNurse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("New ward created with: number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                + wO + ", responsible nurse = " + wRN);

    }

    /**
     * Updates information of an existing ward
     * @param input input from command line
     */
    public void updateWardInformation(Scanner input) {
        System.out.println("Please enter ward number, the ward must already be present in the database.");
        int number = 0;
        try {
            number = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Ward number must be an integer");
            return;
        }

        //Query database for given ward, quit if ward does not exist
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);
            if (!result.next()) {
                System.err.println("That ward does not exist in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        printWardInformationUpdateOptions();
        while (true) {
            String in = input.nextLine();
            in = in.toLowerCase();
            switch(in) {

                case "1" :
                    updateCapacity(input, number);
                    printWardInformationUpdateOptions();
                    break;

                case "2" :
                    updateWardPatientList(input, number);
                    printWardInformationUpdateOptions();
                    break;

                case "3" :
                    updateChargesPerDay(input, number);
                    printWardInformationUpdateOptions();
                    break;

                case "4" :
                    updateOccupantNumber(input, number);
                    printWardInformationUpdateOptions();
                    break;

                case "5" :
                    updateResponsibleNurse(input, number);
                    printWardInformationUpdateOptions();
                    break;

                case "0" :
                    deleteWard(number);
                    printReturnToMenu();
                    break;

                case "p" :
                    printWardInformationUpdateOptions();
                    break;

                case "exit" :
                    return;

                default:
                    System.out.println("Invalid input");
                    printWardInformationUpdateOptions();
                    break;
            }
        }
    }

    /**
     * Prints the options for the information update menu
     */
    public void printWardInformationUpdateOptions() {

        System.out.println();
        System.out.println();
        System.out.println("What part of this ward's Information would you like to update?");
        System.out.println();
        System.out.println("1 - Update Capacity");
        System.out.println("2 - Update Patient List");
        System.out.println("3 - Update Charges per day");
        System.out.println("4 - Update Occupants Number");
        System.out.println("5 - Update Responsible Nurse");
        System.out.println("0 - Delete Ward");
        System.out.println("p - Print options");
        System.out.println("exit - Return to main menu");
    }

    /**
     * Updates capacity for given ward
     * @param input Scanner for user input
     * @param pId ward number
     */
    public void updateCapacity(Scanner input, int pId) {

        System.out.println("Please enter new capacity of the ward.");
        int capacity = 0;
        try {
            capacity = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Ward capacity must be an integer");
            return;
        }

        String query = "UPDATE Ward SET capacity = "  + capacity + " WHERE number = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given ward
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int wN = 0;
        int wC = 0;
        String pList = null;
        double wCharges = 0;
        int wO = 0;
        int wRN = 0;
        try {
            while (result.next()) {
                wN = result.getInt("number");
                wC = result.getInt("capacity");
                pList = result.getString("patientIds");
                wCharges = result.getDouble("chargesPerDay");
                wO = result.getInt("occupants");
                wRN = result.getInt("responsibleNurse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Ward capacity updated with: number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                + wO + ", responsible nurse = " + wRN);
    }

    /**
     * Updates patient list for given ward
     * @param input Scanner for user input
     * @param pId ward number
     */
    public void updateWardPatientList(Scanner input, int pId) {
        System.out.println("Please enter new patient ids of new ward, use only \",\" to seperate different patient ids");
        String patientIds = null;
        try {
            patientIds = input.nextLine();
            if (patientIds.length() > 200) {
                System.out.println("Input must be patient Ids");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        List<String> patientList = Arrays.asList(patientIds.split(","));
        for(String id : patientList) {
            int patient = 0;
            try {
                patient = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                System.err.println("Patient ID must be an integer");
                return;
            }

            //Query database for given patient, quit if patient does not exist
            try {
                result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + patient);
                if (!result.next()) {
                    System.err.println("That Patient does not exist in the database");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }

        String query = "UPDATE Ward SET patientIds = "  + "\"" + patientIds + "\"" + " WHERE number = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given ward
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int wN = 0;
        int wC = 0;
        String pList = null;
        double wCharges = 0;
        int wO = 0;
        int wRN = 0;
        try {
            while (result.next()) {
                wN = result.getInt("number");
                wC = result.getInt("capacity");
                pList = result.getString("patientIds");
                wCharges = result.getDouble("chargesPerDay");
                wO = result.getInt("occupants");
                wRN = result.getInt("responsibleNurse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Ward patient list updated with: number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                + wO + ", responsible nurse = " + wRN);
    }

    /**
     * Updates charges per day for given ward
     * @param input Scanner for user input
     * @param pId ward number
     */
    public void updateChargesPerDay(Scanner input, int pId) {

        System.out.println("Please enter new charges per day of the ward.");
        double charge = 0.0;
        try {
            charge = Double.parseDouble(input.nextLine());
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        String query = "UPDATE Ward SET chargesPerDay  = "  + charge + " WHERE number = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given ward
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int wN = 0;
        int wC = 0;
        String pList = null;
        double wCharges = 0;
        int wO = 0;
        int wRN = 0;
        try {
            while (result.next()) {
                wN = result.getInt("number");
                wC = result.getInt("capacity");
                pList = result.getString("patientIds");
                wCharges = result.getDouble("chargesPerDay");
                wO = result.getInt("occupants");
                wRN = result.getInt("responsibleNurse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Ward charges per day updated with: number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                + wO + ", responsible nurse = " + wRN);
    }

    /**
     * Updates occupant number for given ward
     * @param input Scanner for user input
     * @param pId ward number
     */
    public void updateOccupantNumber(Scanner input, int pId) {

        System.out.println("Please enter new occupant number of the ward.");
        int occupants = 0;
        try {
            occupants = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Number of occupants must be an integer");
            return;
        }

        String query = "UPDATE Ward SET occupants = "  + occupants + " WHERE number = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given ward
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int wN = 0;
        int wC = 0;
        String pList = null;
        double wCharges = 0;
        int wO = 0;
        int wRN = 0;
        try {
            while (result.next()) {
                wN = result.getInt("number");
                wC = result.getInt("capacity");
                pList = result.getString("patientIds");
                wCharges = result.getDouble("chargesPerDay");
                wO = result.getInt("occupants");
                wRN = result.getInt("responsibleNurse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Ward occupants updated with: number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                + wO + ", responsible nurse = " + wRN);
    }

    /**
     * Updates responsible nurse for given ward
     * @param input Scanner for user input
     * @param pId ward number
     */
    public void updateResponsibleNurse(Scanner input, int pId) {

        System.out.println("Please enter new responsible nurse of the ward.");
        int nId = 0;
        try {
            nId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Responsible nurse id must be an integer");
            return;
        }

        //quit if staff is not nurse or nurse is not in the database
        try{
            result = statement.executeQuery("SELECT * FROM Staff WHERE Id = " + nId);
            if(!result.next()) {
                System.err.println("Responsible nurse must be in the database");
                return;
            }
            String job = result.getString("jobTitle");
            if(job == null || !job.toLowerCase().equals("nurse")) {
                System.err.println("Responsible staff must be a nurse");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String query = "UPDATE Ward SET responsibleNurse = "  + nId + " WHERE number = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given ward
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int wN = 0;
        int wC = 0;
        String pList = null;
        double wCharges = 0;
        int wO = 0;
        int wRN = 0;
        try {
            while (result.next()) {
                wN = result.getInt("number");
                wC = result.getInt("capacity");
                pList = result.getString("patientIds");
                wCharges = result.getDouble("chargesPerDay");
                wO = result.getInt("occupants");
                wRN = result.getInt("responsibleNurse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("Ward responsible nurse updated with: number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                + wO + ", responsible nurse = " + wRN);
    }

    /**
     * Deletes given ward
     * @param pId ward number
     */
    public void deleteWard(int pId) {
        String query = "DELETE FROM Ward WHERE number = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + pId);
            if(!result.next()) {
                System.out.println("Ward deleted with id = " + pId);
            } else {
                System.err.println("Ward fail to delete");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Enters new check-in information for patient
     * @param input
     */
    public void enterNewCheckInInformation(Scanner input) {
        //whether first-time patient
        System.out.println("Please indicate whether patient has visited WolfHospital. (yes or no)");
        String firstTime = null;
        try {
            firstTime = input.nextLine();
            if(firstTime.length() > 3) {
                System.err.println("Please enter yes or no.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //if first-time patient, create patient first
        if(firstTime.toLowerCase().equals("no")) {
            System.out.println("Please follow the process to enter patient information first.");
            enterNewPatientInformation(input);
        }
        System.out.println("Please reenter patient ID to check in, the patient must already be present in the database.");
        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }
        //Query database for given check in for patient
        try {
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If check-in exists exists quit
        try {

            if (result.next()) {
                System.err.println("This patient already has a check-in associated with their Medical Record.");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        String query = "INSERT INTO CheckInInformation VALUES(?, ?, ?, ?, ?)";

        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1,  pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the Ward number for Check-In.");
        int wardNum = 0;

        //Get the ward number from input
        try {
            wardNum = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Ward number must be an integer");
            return;
        }


        System.out.println("Please enter the Bed number for Check-In.");
        int bedNum = 0;

        //Get the bed number from input
        try {
            bedNum = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Bed number must be an integer");
            return;
        }


        System.out.println("Please enter the start date in the form yyyy-mm-dd");
        String startDate = null;

        //Get the start date from input
        try {
            startDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Parse the given date into a Date object
        Date startDateUtil = null;
        java.sql.Date sqlStartDate = null;
        try {
            startDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            sqlStartDate = new java.sql.Date(startDateUtil.getTime());
            ps.setInt(2, wardNum);
            ps.setInt(3, bedNum);
            ps.setDate(4, sqlStartDate);
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the end date in the form yyyy-mm-dd, if still active type na (not applicable) and press enter to continue.");
        String endDate = null;

        //Get the end date from input
        try {
            endDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the appropriate end date depending on user input
        Date endDateUtil = null;
        try {
            if (!endDate.toLowerCase().equals("na")) {
                endDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
                java.sql.Date sqlEndDate = new java.sql.Date(endDateUtil.getTime());
                if (sqlEndDate.before(sqlStartDate)) {
                    System.err.println("End date must be on or after the start date.");
                    return;
                }
                ps.setDate(5, sqlEndDate);
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }



        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0, wN = 0, bN = 0;
        Date sDate = null, eDate = null;

        try {
            while (result.next()) {
                id = result.getInt("patientId");
                wN = result.getInt("wardNumber");
                bN = result.getInt("bedNumber");
                sDate = result.getDate("startDate");
                eDate = result.getDate("endDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        if (eDate == null) {
            System.out.println("New Check-In created with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = NA");
        } else {
            System.out.println("New Check-In created with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = " + eDate.toString());
        }

    }

    /**
     * Updates check-in information
     * @param input input from command line
     */
    public void updateCheckInInformation(Scanner input) {

        System.out.println("Please enter patient ID, the patient must already be present in the database.");
        int pId = 0;
        //Get the patient ID from input
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }


        //Query database for given patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient does not exist quit
        try {

            if (!result.next()) {
                System.err.println("That Patient does not exist in the database");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Check if patient already has medical record
        try {
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient doesn't have medical record quit
        try {

            if (!result.next()) {
                System.err.println("This patient doesn't have a Check-in Information, you must create a Check-in Information for the patient before it can be updated.");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        printCheckInInformationUpdateOptions();
        while (true) {
            String in = input.nextLine();
            in = in.toLowerCase();
            switch(in) {

                case "1" :
                    updateWardInCheckIn(input, pId);
                    printCheckInInformationUpdateOptions();
                    break;


                case "2" :
                    updateBedCheckIn(input, pId);
                    printCheckInInformationUpdateOptions();
                    break;

                case "3" :
                    updateStartDateForCheckIn(input, pId);
                    printCheckInInformationUpdateOptions();
                    break;

                case "4" :
                    updateEndDateForCheckIn(input, pId);
                    printCheckInInformationUpdateOptions();
                    break;

                case "0" :
                    deleteCheckIn(pId);
                    printReturnToMenu();
                    break;

                case "p" :
                    printCheckInInformationUpdateOptions();
                    break;

                case "exit" :
                    return;

                default:
                    System.out.println("Invalid input");
                    printCheckInInformationUpdateOptions();
                    break;
            }
        }
    }

    /**
     * Prints the options for the check-in information update menu
     */
    public void printCheckInInformationUpdateOptions() {

        System.out.println();
        System.out.println();
        System.out.println("What part of this patient's Medical Record would you like to update?");
        System.out.println();
        System.out.println("1 - Update Ward number");
        System.out.println("2 - Update Bed number");
        System.out.println("3 - Update Start date for Check-In");
        System.out.println("4 - Update End date for Check-In");
        System.out.println("0 - Delete Check-In information");
        System.out.println("p - Print options");
        System.out.println("exit - Return to main menu");
    }

    /**
     * Updates ward number in check-in information
     * @param input input from command line
     * @param pId patient ID
     */
    public void updateWardInCheckIn(Scanner input, int pId) {
        System.out.println("Please enter new ward number, the ward must already be present in the database.");
        int number = 0;
        try {
            number = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Ward number must be an integer");
            return;
        }

        //Query database for given ward, quit if ward does not exist
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);
            if (!result.next()) {
                System.err.println("That ward does not exist in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //update ward number in check-in information
        String query = "UPDATE CheckInInformation SET wardNumber = "  + number + " WHERE patientId = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0, wN = 0, bN = 0;
        Date sDate = null, eDate = null;

        try {
            while (result.next()) {
                id = result.getInt("patientId");
                wN = result.getInt("wardNumber");
                bN = result.getInt("bedNumber");
                sDate = result.getDate("startDate");
                eDate = result.getDate("endDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        if (eDate == null) {
            System.out.println("Check-In updated with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = NA");
        } else {
            System.out.println("Check-In updated with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = " + eDate.toString());
        }

    }

    /**
     * Updates bed number in check-in information
     * @param input input from command line
     * @param pId patient id
     */
    public void updateBedCheckIn(Scanner input, int pId) {
        System.out.println("Please enter the Bed number for Check-In.");
        int bedNum = 0;

        //Get the bed number from input
        try {
            bedNum = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Bed number must be an integer");
            return;
        }

        //update ward number in check-in information
        String query = "UPDATE CheckInInformation SET bedNumber = "  + bedNum + " WHERE patientId = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0, wN = 0, bN = 0;
        Date sDate = null, eDate = null;

        try {
            while (result.next()) {
                id = result.getInt("patientId");
                wN = result.getInt("wardNumber");
                bN = result.getInt("bedNumber");
                sDate = result.getDate("startDate");
                eDate = result.getDate("endDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        if (eDate == null) {
            System.out.println("Check-In updated with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = NA");
        } else {
            System.out.println("Check-In updated with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = " + eDate.toString());
        }

    }

    /**
     * Updates the start date for the given Patient's Check-In information
     *
     * @param input Scanner for user input
     * @param pId Patient ID for update
     */
    public void updateStartDateForCheckIn(Scanner input, int pId) {
        java.sql.Date sqlEndDate = null;
        try {
            result = statement.executeQuery("SELECT endDate FROM CheckInInformation WHERE patientId = " + pId);
            if (result.next()) {
                sqlEndDate = result.getDate("endDate");
            }
            if(sqlEndDate == null) {
                LocalDate localDate = LocalDate.now();
                sqlEndDate = java.sql.Date.valueOf(localDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter new start date in the form yyyy-mm-dd. Must be on or before " + sqlEndDate.toString());
        String startDate = null;

        //Get the end date from input
        try {
            startDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the appropriate start date depending on user input
        Date startDateUtil = null;
        try {
            startDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            java.sql.Date sqlStartDate = new java.sql.Date(startDateUtil.getTime());
            if (sqlEndDate.before(sqlStartDate)) {
                System.err.println("End date must be on or after the start date.");
                return;
            }
            statement.executeUpdate("UPDATE CheckInInformation SET startDate = DATE '" + sqlStartDate.toString() + "' WHERE patientId = " + pId);
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0, wN = 0, bN = 0;
        Date sDate = null, eDate = null;

        try {
            while (result.next()) {
                id = result.getInt("patientId");
                wN = result.getInt("wardNumber");
                bN = result.getInt("bedNumber");
                sDate = result.getDate("startDate");
                eDate = result.getDate("endDate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        if (eDate == null) {
            System.out.println("Check-In updated with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = NA");
        } else {
            System.out.println("Check-In updated with: Patient ID = " + id + ", Ward Number = " + wN + ", Bed Number = " + bN
                    + ", Start Date = " + sDate.toString() + ", End Date = " + eDate.toString());
        }
    }

    /**
     * Deletes check-in information for given patient
     * @param pId patient ID
     */
    public void deleteCheckIn(int pId) {
        String query = "DELETE FROM CheckInInformation WHERE patientId  = " + pId;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            result = statement.executeQuery("SELECT * FROM CheckInInformation WHERE patientId = " + pId);
            if(!result.next()) {
                System.out.println("CheckIn Information deleted with patient id = " + pId);
            } else {
                System.err.println("CheckIn Information fail to delete");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Checks for available wards
     * Displays information for each wards and display the ward number which have at least one available bed
     *
     * @param input input from command line
     */
    public void checkAvailableWards(Scanner input) {
        displayAllWardUsage();

        //Query database for ward which have available beds
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE capacity > occupants");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the results in the database via query
        System.out.println();
        System.out.println();
        System.out.println("Display Available Wards Information: ");
        try {
            while(result.next()) {
                int wN = result.getInt("number");
                int wC = result.getInt("capacity");
                String pList = result.getString("patientIds");
                double wCharges = result.getDouble("chargesPerDay");
                int wO = result.getInt("occupants");
                int wRN = result.getInt("responsibleNurse");
                System.out.println("Ward number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                        + wO + ", responsible nurse = " + wRN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays information for all wards
     */
    private void displayAllWardUsage() {

        //Query database for given ward
        try {
            result = statement.executeQuery("SELECT * FROM Ward");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the results in the database via query
        System.out.println();
        System.out.println();
        System.out.println("Display All Wards Information: ");
        try {
            while(result.next()) {
                int wN = result.getInt("number");
                int wC = result.getInt("capacity");
                String pList = result.getString("patientIds");
                double wCharges = result.getDouble("chargesPerDay");
                int wO = result.getInt("occupants");
                int wRN = result.getInt("responsibleNurse");
                System.out.println("Ward number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                        + wO + ", responsible nurse = " + wRN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Assigns ward based on patients' request (1-bed ward, 2-bed ward or 4-bed ward)
     * Increases occupants by one and adds patient id to ward
     * @param input input from command line
     */
    public void assignWardUponRequest(Scanner input) {
        //Get patient request for ward capacity
        System.out.println("Please enter the capacity of ward you desire in integer form (Options: 1, 2, 4).");
        int cp = 0;
        try {
            cp = Integer.parseInt(input.nextLine());
            if (cp != 1 && cp != 2 && cp != 4) {
                System.err.println("Please choose capacity from 1, 2, 4.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //query for all desired wards that have available beds
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE capacity = " + cp + " AND capacity > occupants");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the results in the database via query, use hashset to store ward number
        System.out.println();
        System.out.println("Display All Available Wards: ");
        HashSet<Integer> wardSet = new HashSet<Integer>();
        try {
            while(result.next()) {
                int wN = result.getInt("number");
                wardSet.add(wN);
                int wC = result.getInt("capacity");
                String pList = result.getString("patientIds");
                double wCharges = result.getDouble("chargesPerDay");
                int wO = result.getInt("occupants");
                int wRN = result.getInt("responsibleNurse");
                System.out.println("Ward number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                        + wO + ", responsible nurse = " + wRN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Quit if no available ward
        if(wardSet.size() == 0) {
            System.out.println("No ward available for given request.");
            return;
        }

        //Get the ward number patient chosen, quit if not in displayed list(hashset)
        System.out.println();
        System.out.println("Please enter one of the desired ward number in ABOVE LIST.");
        int number = 0;
        try {
            number = Integer.parseInt(input.nextLine());
            if(!wardSet.contains(number)) {
                System.err.println("Ward number must be in above displayed list");
            }
        } catch (Exception e) {
            System.err.println("Ward number must be an integer");
            return;
        }

        //Query database for given ward
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);
            if (!result.next()) {
                System.err.println("That ward does not exist in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Get patient ID
        System.out.println("Please enter patient ID, the patient must be present in the database.");
        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }

        //Query database for given patient, quit if patient not exists
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
            if (!result.next()) {
                System.err.println("That patient does not exist in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Check if patient is in patient list
        String patientList = null;
        HashSet<Integer> patients = new HashSet<Integer>();
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);
            if (!result.next()) {
                System.err.println("That ward does not exist in the database");
                return;
            }
            patientList = result.getString("patientIds");
            List<String> list = Arrays.asList(patientList.split(","));
            for (String s: list) {
                patients.add(Integer.parseInt(s));
            }
            if (patients.contains(pId)) {
                System.err.println("Given patient is already in that ward.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Update occupants and patient list for given ward
        int occupants = 0;
        try {
            occupants = result.getInt("occupants");
            occupants++;
            patientList += "," + pId;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String query = "UPDATE Ward SET patientIds = "  + "\"" + patientList + "\", occupants = " + occupants + " WHERE number = " + number;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for updated ward
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int wN = 0;
        int wC = 0;
        String pList = null;
        double wCharges = 0;
        int wO = 0;
        int wRN = 0;
        try {
            while (result.next()) {
                wN = result.getInt("number");
                wC = result.getInt("capacity");
                pList = result.getString("patientIds");
                wCharges = result.getDouble("chargesPerDay");
                wO = result.getInt("occupants");
                wRN = result.getInt("responsibleNurse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Patient assigned to ward: number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                + wO + ", responsible nurse = " + wRN);
        System.out.println();

        //Proceed to update patient inward status
        System.out.println("Plase update patient in ward status accordingly");
        updatePatientInformation(input);

    }

    /**
     * Releases wards: delete patient id from ward and decrease occupants by 1
     * @param input
     */
    public void releaseWards(Scanner input) {
        //Get the ward number patient chosen, quit if not in displayed list(hashset)
        System.out.println("Please enter ward number to release.");
        int number = 0;
        try {
            number = Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            System.err.println("Ward number must be an integer");
            return;
        }

        //Query database for given ward, quit if ward not exists
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);
            if (!result.next()) {
                System.err.println("Ward must be in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Get patient ID
        System.out.println("Please enter patient ID, the patient must be present in the database.");
        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }

        //Query database for given patient, quit if patient not exists
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
            if (!result.next()) {
                System.err.println("That patient does not exist in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Check if patient is in patient list
        String patientList = null;
        String updatedList = null;
        HashSet<Integer> patients = new HashSet<Integer>();
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);
            if (!result.next()) {
                System.err.println("Ward must be in the database");
                return;
            }
            patientList = result.getString("patientIds");
            List<String> list = Arrays.asList(patientList.split(","));
            for (String s: list) {
                int id = Integer.parseInt(s);
                if(id != pId && updatedList == null) {
                    updatedList = "" + s;
                } else if(id != pId && updatedList != null) {
                    updatedList = updatedList + "," + s;
                }
                patients.add(id);
            }
            if (!patients.contains(pId)) {
                System.err.println("Given patient is not in that ward.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        //Update occupants and patient list for given ward
        int occupants = 0;
        try {
            occupants = result.getInt("occupants");
            occupants--;
            patients.remove(pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        String query = "UPDATE Ward SET patientIds = "  + "\"" + updatedList + "\", occupants = " + occupants + " WHERE number = " + number;
        try {
            ps = connection.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for updated ward
        try {
            result = statement.executeQuery("SELECT * FROM Ward WHERE number = " + number);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int wN = 0;
        int wC = 0;
        String pList = null;
        double wCharges = 0;
        int wO = 0;
        int wRN = 0;
        try {
            while (result.next()) {
                wN = result.getInt("number");
                wC = result.getInt("capacity");
                pList = result.getString("patientIds");
                wCharges = result.getDouble("chargesPerDay");
                wO = result.getInt("occupants");
                wRN = result.getInt("responsibleNurse");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Released bed from ward: number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                + wO + ", responsible nurse = " + wRN);
        System.out.println();

        //Proceed to update patient inward status
        System.out.println("Plase update patient in ward status accordingly");
        updatePatientInformation(input);
    }

    /**
     * Enters a new Billing Account into the database. The new Billing Account
     * must correspond to a Patient that already exists in the database
     *
     * @param input Scanner for use input
     */
    public void enterNewBillingAccount(Scanner input) {

        String query = "INSERT INTO BillingAccount VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter patient ID, the patient must already be present in the database.");
        int pId = 0;
        //Get the patient ID from input
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }


        //Query database for given patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient does not exist quit
        try {

            if (!result.next()) {
                System.err.println("That Patient does not exist in the database");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Check if patient already has billing account
        try {
            result = statement.executeQuery("SELECT * FROM BillingAccount WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient already has billing account quit
        try {

            if (result.next()) {
                System.err.println("This patient already has a Billing Account, you may choose to update this account instead.");
                return;
            } else {
                ps.setInt(1, pId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Get the responsible SSN
        System.out.println("Please enter the SSN of person responsible for Billing Account.");
        String respSSN = null;

        //Get the responsible SSN from input
        try {
            respSSN = input.nextLine();
            if (respSSN.length() != 9) {
                System.out.println("SSN must be exactly 9 digits.");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the responsible SSN
        try {
            ps.setString(2, respSSN);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the billing address for Billing Account.");
        String addr = null;

        //Get the billing address from input
        try {
            addr = input.nextLine();
            if (addr.length() > 200) {
                System.out.println("Billing address must be 200 characters or less.");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the address
        try {
            ps.setString(3, addr);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the visit date in the form yyyy-mm-dd");
        String visitDate = null;

        //Get the visit date from input
        try {
            visitDate = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Parse the given date into a Date object
        Date startDateUtil = null;
        java.sql.Date sqlVisitDate = null;
        try {
            startDateUtil = new SimpleDateFormat("yyyy-MM-dd").parse(visitDate);
            sqlVisitDate = new java.sql.Date(startDateUtil.getTime());
            ps.setDate(4, sqlVisitDate);
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }

        //Get the payment method
        System.out.println("Please enter the payment method for Billing Account.");
        String paymentMethod = null;

        //Get the payment method from input
        try {
            paymentMethod = input.nextLine();
            if (paymentMethod.length() > 128) {
                System.out.println("Payment method must be 128 characters or less.");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the payment method
        try {
            ps.setString(5, paymentMethod);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the card number for payment method, if not applicable type na and press enter to continue.");
        String cardNumber = null;

        //Get the card number from input
        try {
            cardNumber = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the card number
        try {
            if (!cardNumber.toLowerCase().equals("na")) {
                if (cardNumber.length() != 16) {
                    System.err.println("Card number must be exactly 16 digits.");
                    return;
                }
                ps.setString(6, cardNumber);
            } else {
                ps.setNull(6, java.sql.Types.CHAR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Get the registration fee from input
        System.out.println("Please enter the Registration fee for the Billing Account.");
        int regFee = 0;
        //Get the patient ID from input
        try {
            regFee = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Registration fee must be an integer");
            return;
        }


        //Set the registration fee
        try {
            ps.setInt(7, regFee);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //Get medication prescribed
        System.out.println("Please enter yes or no indicating medication has been prescribed for this Billing Account.");
        String meds = null;

        //Get the medication prescribed from input
        try {
            meds = input.nextLine();
            if (meds.length() > 3) {
                System.out.println("Input should not be more than 3 characters.");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Set the medication prescribed
        try {
            ps.setString(8, meds);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //Get the accommodation fee from input
        System.out.println("Please enter the Accomodation fee for the Billing Account. If not applicable type na and press enter to continue.");
        String accFee = null;
        //Get the patient ID from input
        try {
            accFee = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input.");
            return;
        }


        if (accFee.toLowerCase().equals("na")) {
            //Set the registration fee
            try {
                ps.setNull(9, java.sql.Types.INTEGER);
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        } else {
            try {
                ps.setInt(9, Integer.parseInt(accFee));
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }
        }



        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM BillingAccount WHERE patientId = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the update in the database via query
        int id = 0, rF = 0;
        Integer aF = null;
        String rSSN = null, billAddr = null, payMeth = null, cardNum = null, medsPre = null;
        Date vD = null;
        try {
            while (result.next()) {
                id = result.getInt("patientId");
                rSSN = result.getString("responsibleSSN");
                billAddr = result.getString("billingAddress");
                vD = result.getDate("visitDate");
                payMeth = result.getString("paymentMethod");
                cardNum = result.getString("cardNumber");
                rF = result.getInt("registrationFee");
                medsPre = result.getString("medicationPrescribed");
                aF = result.getInt("accommodationFee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (cardNum == null && aF == null) {
            System.out.println();
            System.out.println("New Billing Account created: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else if (cardNum == null && aF != null) {
            System.out.println();
            System.out.println("New Billing Account created: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        } else if (cardNum != null && aF == null) {
            System.out.println();
            System.out.println("New Billing Account created: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else {
            System.out.println();
            System.out.println("New Billing Account created: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        }

    }

    /**
     * Updates an existing Billing Account for a Patient that already
     * has an existing Billing Account
     *
     * @param input Scanner for user input
     */
    public void updateBillingAccount(Scanner input) {

        System.out.println("Please enter patient ID, the patient must already be present in the database.");
        int pId = 0;
        //Get the patient ID from input
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }


        //Query database for given patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient does not exist quit
        try {

            if (!result.next()) {
                System.err.println("That Patient does not exist in the database");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Check if patient already has billing account
        try {
            result = statement.executeQuery("SELECT * FROM BillingAccount WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient doesn't have billing account quit
        try {

            if (!result.next()) {
                System.err.println("This patient doesn't have a Billing Account, you must create the Billing Account before it can be updated.");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        printBillingAccountUpdateOptions();
        while (true) {
            String in = input.nextLine();
            in = in.toLowerCase();
            switch(in) {

                case "1" :
                    updateResponsibleSSN(input, pId);
                    printBillingAccountUpdateOptions();
                    break;

                case "2" :
                    updateBillingAddress(input, pId);
                    printBillingAccountUpdateOptions();
                    break;


                case "3" :
                    updatePaymentMethod(input, pId);
                    printBillingAccountUpdateOptions();
                    break;

                case "4" :
                    updateCardNumber(input, pId);
                    printBillingAccountUpdateOptions();
                    break;

                case "5" :
                    updateMedicationPrescribed(input, pId);
                    printBillingAccountUpdateOptions();
                    break;

                case "6" :
                    addFeeToBillingAccount(input, pId);
                    printBillingAccountUpdateOptions();
                    break;

                case "p" :
                    printBillingAccountUpdateOptions();
                    break;

                case "exit" :
                    return;

                default:
                    System.out.println("Invalid input");
                    printBillingAccountUpdateOptions();
                    break;
            }
        }

    }



    /**
     * Prints the options for the billing account update menu
     */
    public void printBillingAccountUpdateOptions() {

        System.out.println();
        System.out.println();
        System.out.println("What part of this patient's Billing Account would you like to update?");
        System.out.println();
        System.out.println("1 - Update responsible SSN");
        System.out.println("2 - Update billing address");
        System.out.println("3 - Update payment method");
        System.out.println("4 - Update card number");
        System.out.println("5 - Update medication prescribed");
        System.out.println("6 - Add fee to billing account");
        System.out.println("p - Print options");
        System.out.println("exit - Return to main menu");
    }

    /**
     * Updates the responsible SSN attribute for given patient's billing account.
     *
     * @param input Scanner for user input
     * @param pId patient ID for update
     */
    public void updateResponsibleSSN(Scanner input, int pId) {
        //Get the responsible SSN
        System.out.println("Please enter the SSN of person responsible for Billing Account.");
        String respSSN = null;

        //Get the responsible SSN from input
        try {
            respSSN = input.nextLine();
            if (respSSN.length() != 9) {
                System.out.println("SSN must be exactly 9 digits.");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        try {
            statement.executeUpdate("UPDATE BillingAccount SET responsibleSSN = '" + respSSN + "' WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query for update
        try {
            result = statement.executeQuery("SELECT * FROM BillingAccount WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the update in the database via query
        int id = 0, rF = 0;
        Integer aF = null;
        String rSSN = null, billAddr = null, payMeth = null, cardNum = null, medsPre = null;
        Date vD = null;
        try {
            while (result.next()) {
                id = result.getInt("patientId");
                rSSN = result.getString("responsibleSSN");
                billAddr = result.getString("billingAddress");
                vD = result.getDate("visitDate");
                payMeth = result.getString("paymentMethod");
                cardNum = result.getString("cardNumber");
                rF = result.getInt("registrationFee");
                medsPre = result.getString("medicationPrescribed");
                aF = result.getInt("accommodationFee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (cardNum == null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else if (cardNum == null && aF != null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        } else if (cardNum != null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        }

    }


    /**
     * Updates the billing address attribute for given patient's billing account.
     *
     * @param input Scanner for user input
     * @param pId patient ID for update
     */
    public void updateBillingAddress(Scanner input, int pId) {
        //Get the billing address
        System.out.println("Please enter the billing address for Billing Account.");
        String addr = null;

        //Get the billing address from input
        try {
            addr = input.nextLine();
            if (addr.length() > 200) {
                System.out.println("Billing address must be 200 characters or less.");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        try {
            statement.executeUpdate("UPDATE BillingAccount SET billingAddress = '" + addr + "' WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query for update
        try {
            result = statement.executeQuery("SELECT * FROM BillingAccount WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the update in the database via query
        int id = 0, rF = 0;
        Integer aF = null;
        String rSSN = null, billAddr = null, payMeth = null, cardNum = null, medsPre = null;
        Date vD = null;
        try {
            while (result.next()) {
                id = result.getInt("patientId");
                rSSN = result.getString("responsibleSSN");
                billAddr = result.getString("billingAddress");
                vD = result.getDate("visitDate");
                payMeth = result.getString("paymentMethod");
                cardNum = result.getString("cardNumber");
                rF = result.getInt("registrationFee");
                medsPre = result.getString("medicationPrescribed");
                aF = result.getInt("accommodationFee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (cardNum == null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else if (cardNum == null && aF != null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        } else if (cardNum != null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        }

    }


    /**
     * Updates the payment method attribute for given patient's billing account.
     *
     * @param input Scanner for user input
     * @param pId patient ID for update
     */
    public void updatePaymentMethod(Scanner input, int pId) {
        //Get the payment method
        System.out.println("Please enter the payment method for Billing Account.");
        String paymentMethod = null;

        //Get the payment method from input
        try {
            paymentMethod = input.nextLine();
            if (paymentMethod.length() > 128) {
                System.out.println("Payment method must be 128 characters or less.");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        try {
            statement.executeUpdate("UPDATE BillingAccount SET paymentMethod = '" + paymentMethod + "' WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query for update
        try {
            result = statement.executeQuery("SELECT * FROM BillingAccount WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the update in the database via query
        int id = 0, rF = 0;
        Integer aF = null;
        String rSSN = null, billAddr = null, payMeth = null, cardNum = null, medsPre = null;
        Date vD = null;
        try {
            while (result.next()) {
                id = result.getInt("patientId");
                rSSN = result.getString("responsibleSSN");
                billAddr = result.getString("billingAddress");
                vD = result.getDate("visitDate");
                payMeth = result.getString("paymentMethod");
                cardNum = result.getString("cardNumber");
                rF = result.getInt("registrationFee");
                medsPre = result.getString("medicationPrescribed");
                aF = result.getInt("accommodationFee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (cardNum == null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else if (cardNum == null && aF != null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        } else if (cardNum != null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        }

    }


    /**
     * Updates the card number attribute for given patient's billing account.
     *
     * @param input Scanner for user input
     * @param pId patient ID for update
     */
    public void updateCardNumber(Scanner input, int pId) {
        String query = "UPDATE BillingAccount SET cardNumber = ? WHERE patientId = " + pId;
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Please enter the card number for payment method, if not applicable type na and press enter to continue.");
        String cardNumber = null;

        //Get the card number from input
        try {
            cardNumber = input.nextLine();
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        //Set the card number
        try {
            if (!cardNumber.toLowerCase().equals("na")) {
                if (cardNumber.length() != 16) {
                    System.err.println("Card number must be exactly 16 digits.");
                    return;
                }
                ps.setString(1,  cardNumber);
            } else {
                ps.setNull(1, java.sql.Types.CHAR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query for update
        try {
            result = statement.executeQuery("SELECT * FROM BillingAccount WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the update in the database via query
        int id = 0, rF = 0, aF = 0;
        String rSSN = null, billAddr = null, payMeth = null, cardNum = null, medsPre = null;
        Date vD = null;
        try {
            while (result.next()) {
                id = result.getInt("patientId");
                rSSN = result.getString("responsibleSSN");
                billAddr = result.getString("billingAddress");
                vD = result.getDate("visitDate");
                payMeth = result.getString("paymentMethod");
                cardNum = result.getString("cardNumber");
                rF = result.getInt("registrationFee");
                medsPre = result.getString("medicationPrescribed");
                aF = result.getInt("accommodationFee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (cardNum == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        } else {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        }
    }


    /**
     * Updates the medication prescribed attribute for given patient's billing account.
     *
     * @param input Scanner for user input
     * @param pId patient ID for update
     */
    public void updateMedicationPrescribed(Scanner input, int pId) {
        //Get medication prescribed
        System.out.println("Please enter yes or no indicating medication has been prescribed for this Billing Account.");
        String meds = null;

        //Get the medication prescribed from input
        try {
            meds = input.nextLine();
            if (meds.length() > 3) {
                System.out.println("Input should not be more than 3 characters.");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        try {
            statement.executeUpdate("UPDATE BillingAccount SET medicationPrescribed = '" + meds + "' WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query for update
        try {
            result = statement.executeQuery("SELECT * FROM BillingAccount WHERE patientId = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the update in the database via query
        int id = 0, rF = 0;
        Integer aF = null;
        String rSSN = null, billAddr = null, payMeth = null, cardNum = null, medsPre = null;
        Date vD = null;
        try {
            while (result.next()) {
                id = result.getInt("patientId");
                rSSN = result.getString("responsibleSSN");
                billAddr = result.getString("billingAddress");
                vD = result.getDate("visitDate");
                payMeth = result.getString("paymentMethod");
                cardNum = result.getString("cardNumber");
                rF = result.getInt("registrationFee");
                medsPre = result.getString("medicationPrescribed");
                aF = result.getInt("accommodationFee");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (cardNum == null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else if (cardNum == null && aF != null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        } else if (cardNum != null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA");
        } else {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + id + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF);
        }

    }

    /**
     * Adds a fee to the given patient's billing account.
     *
     * @param input Scanner for user input
     * @param pId patient ID for update
     */
    public void addFeeToBillingAccount(Scanner input, int pId) {
        String query = "INSERT INTO Fee VALUES(?, ?, ?)";
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Please enter the Fee ID.");
        int feeId = 0;

        //Get the fee ID from input
        try {
            feeId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Fee ID must be an integer");
            return;
        }


        //Query database for given fee
        try {
            result = statement.executeQuery("SELECT * FROM Fee WHERE id = " + feeId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If fee exists quit
        try {

            if (result.next()) {
                System.err.println("That Fee already exists in the database.");
                return;
            } else {
                ps.setInt(1, feeId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        System.out.println("Please enter the name of the Fee.");
        String name = null;

        //Get the fee name from input
        try {
            name = input.nextLine();
            if (name.length() > 128) {
                System.out.println("Fee name must be 128 characters or less");
                return;
            }

        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }


        System.out.println("Please enter the Fee amount.");
        Double amount = null;

        //Get the Fee amount from input
        try {
            amount = Double.parseDouble(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Fee amount must be a valid number");
            return;
        }


        //Set the name and amount
        try {
            ps.setString(2, name);
            ps.setDouble(3, amount);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //Execute the update
        try {
            ps.executeUpdate();
            result = statement.executeQuery("SELECT * FROM Fee WHERE id = " + feeId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        int id = 0;
        String n = null;
        Double amt = null;
        try {
            while (result.next()) {
                id = result.getInt("id");
                n = result.getString("name");
                amt = result.getDouble("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("New Fee created with: ID = " + id + ", Name = " + n + ", Amount = " + amt);


        query = "INSERT INTO AddFee VALUES(?, ?)";
        try {
            ps = connection.prepareStatement(query);
            ps.setInt(1,  feeId);
            ps.setInt(2, pId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Fee ID " + feeId + " added to BillingAccount of patient with ID " + pId);

        try {
            result = statement.executeQuery("SELECT BillingAccount.patientId, responsibleSSN, billingAddress, visitDate, "
                    + "paymentMethod, cardNumber, registrationFee, medicationPrescribed, accommodationFee, name AS feeName, amount as feeAmount "
                    + "FROM BillingAccount JOIN(SELECT * FROM Fee JOIN AddFee on AddFee.feeId = Fee.id)"
                    + " AS F ON BillingAccount.patientId = F.patientId WHERE BillingAccount.patientId = " + pId + " AND feeId = " + feeId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the update in the database via query
        int pid = 0, rF = 0;
        Integer aF = null;
        String rSSN = null, billAddr = null, payMeth = null, cardNum = null, medsPre = null, fName = null;
        Date vD = null;
        Double fAmt = null;
        try {
            while (result.next()) {
                pid = result.getInt("patientId");
                rSSN = result.getString("responsibleSSN");
                billAddr = result.getString("billingAddress");
                vD = result.getDate("visitDate");
                payMeth = result.getString("paymentMethod");
                cardNum = result.getString("cardNumber");
                rF = result.getInt("registrationFee");
                medsPre = result.getString("medicationPrescribed");
                aF = result.getInt("accommodationFee");
                fName = result.getString("feeName");
                fAmt = result.getDouble("feeAmount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (cardNum == null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + pid + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA, Fee Name = " + fName + ", Fee Amount = " + fAmt);
        } else if (cardNum == null && aF != null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + pid + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = NA, Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF + ", Fee Name = " + fName + ", Fee Amount = " + fAmt);
        } else if (cardNum != null && aF == null) {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + pid + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = NA, Fee Name = " + fName + ", Fee Amount = " + fAmt);
        } else {
            System.out.println();
            System.out.println("Updated Billing Account: Patient ID = " + pid + ", Responsible SSN = " + rSSN + ", Billing Address = " + billAddr
                    + ", Visit Date = " + vD.toString() + ", Payment Method = " + payMeth + ", Card Number = " + cardNum + ", Registration Fee = " + rF +
                    ", Medication Prescribed = " + medsPre + ", Accommodation Fee = " + aF + "Fee Name = " + fName + ", Fee Amount = " + fAmt);
        }
    }


    /**
     * Shows the Billing Account for a Patient that already has an existing Billing
     * Account in the database.
     *
     * @param input Scanner for user input
     */
    public void viewBillingAccount(Scanner input) {
        System.out.println("Please enter patient ID, the patient must already be present in the database.");
        int pId = 0;
        //Get the patient ID from input
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }


        //Query database for given patient
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }


        //If patient does not exist quit
        try {

            if (!result.next()) {
                System.err.println("That Patient does not exist in the database");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Get the complete record as shown with demo data
        try {
            result = statement.executeQuery("SELECT BillingAccount.patientId, responsibleSSN, billingAddress, visitDate, "
                    + "paymentMethod, cardNumber, registrationFee, medicationPrescribed, accommodationFee, name AS feeName, amount as feeAmount "
                    + "FROM BillingAccount JOIN(SELECT * FROM Fee JOIN AddFee on AddFee.feeId = Fee.id)"
                    + " AS F ON BillingAccount.patientId = F.patientId WHERE BillingAccount.patientId = " + pId);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the update in the database via query
        int pid = 0, rF = 0;
        Integer aF = null;
        String rSSN = null, billAddr = null, payMeth = null, cardNum = null, medsPre = null, fN = null, fA = null;
        Date vD = null;
        Double fAmt = null;
        StringBuilder fName = new StringBuilder();
        StringBuilder feeAmounts = new StringBuilder();
        try {
            while (result.next()) {
                pid = result.getInt("patientId");
                rSSN = result.getString("responsibleSSN");
                billAddr = result.getString("billingAddress");
                vD = result.getDate("visitDate");
                payMeth = result.getString("paymentMethod");
                cardNum = result.getString("cardNumber");
                rF = result.getInt("registrationFee");
                medsPre = result.getString("medicationPrescribed");
                aF = result.getInt("accommodationFee");
                fN = result.getString("feeName");
                if (!fName.toString().contains(fN)) {
                    fName.append(fN + ", ");
                }
                fAmt = result.getDouble("feeAmount");
                fA = fAmt.toString();
                if (!feeAmounts.toString().contains(fA)) {
                    feeAmounts.append(fA + ", ");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (cardNum == null && aF == null) {
            System.out.println();
            System.out.println();
            System.out.println("Billing Account");
            System.out.println("--------------------------");
            System.out.println("Patient ID: " + pid);
            System.out.println("Responsible SSN: " + rSSN);
            System.out.println("Billing Address: " + billAddr);
            System.out.println("Visit Date: " + vD.toString());
            System.out.println("Payment Method: " + payMeth);
            System.out.println("Card Number: NA");
            System.out.println("Registration Fee: " + rF);
            System.out.println("Medication Prescribed: " + medsPre);
            System.out.println("Accommodation Fee: NA");
            System.out.println("Additional Fees: " + fName.toString().substring(0, fName.length() - 2));
            System.out.println("Fee Amounts: " + feeAmounts.toString().substring(0, feeAmounts.length() - 2));
            System.out.println("--------------------------");
        } else if (cardNum == null && aF != null) {
            System.out.println();
            System.out.println();
            System.out.println("Billing Account");
            System.out.println("--------------------------");
            System.out.println("Patient ID: " + pid);
            System.out.println("Responsible SSN: " + rSSN);
            System.out.println("Billing Address: " + billAddr);
            System.out.println("Visit Date: " + vD.toString());
            System.out.println("Payment Method: " + payMeth);
            System.out.println("Card Number: NA");
            System.out.println("Registration Fee: " + rF);
            System.out.println("Medication Prescribed: " + medsPre);
            System.out.println("Accommodation Fee: " + aF);
            System.out.println("Additional Fees: " + fName.toString().substring(0, fName.length() - 2));
            System.out.println("Fee Amounts: " + feeAmounts.toString().substring(0, feeAmounts.length() - 2));
            System.out.println("--------------------------");
        } else if (cardNum != null && aF == null) {
            System.out.println();
            System.out.println();
            System.out.println("Billing Account");
            System.out.println("--------------------------");
            System.out.println("Patient ID: " + pid);
            System.out.println("Responsible SSN: " + rSSN);
            System.out.println("Billing Address: " + billAddr);
            System.out.println("Visit Date: " + vD.toString());
            System.out.println("Payment Method: " + payMeth);
            System.out.println("Card Number: " + cardNum);
            System.out.println("Registration Fee: " + rF);
            System.out.println("Medication Prescribed: " + medsPre);
            System.out.println("Accommodation Fee: NA");
            System.out.println("Additional Fees: " + fName.toString().substring(0, fName.length() - 2));
            System.out.println("Fee Amounts: " + feeAmounts.toString().substring(0, feeAmounts.length() - 2));
            System.out.println("--------------------------");
        } else {
            System.out.println();
            System.out.println();
            System.out.println("Billing Account");
            System.out.println("--------------------------");
            System.out.println("Patient ID: " + pid);
            System.out.println("Responsible SSN: " + rSSN);
            System.out.println("Billing Address: " + billAddr);
            System.out.println("Visit Date: " + vD.toString());
            System.out.println("Payment Method: " + payMeth);
            System.out.println("Card Number: " + cardNum);
            System.out.println("Registration Fee: " + rF);
            System.out.println("Medication Prescribed: " + medsPre);
            System.out.println("Accommodation Fee: " + aF);
            System.out.println("Additional Fees: " + fName.toString().substring(0, fName.length() - 2));
            System.out.println("Fee Amounts: " + feeAmounts.toString().substring(0, feeAmounts.length() - 2));
            System.out.println("--------------------------");
        }
    }

    /**
     * Generates report for monthly medical record for given patient
     * @param input input from command line
     */
    public void viewMonthlyMedicalRecord(Scanner input) {
        System.out.println("Please enter the patient ID for medical record.");
        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Patient ID must be an integer");
            return;
        }

        //Query database for given patient, quit if patient does not exist
        try {
            result = statement.executeQuery("SELECT * FROM Patient WHERE id = " + pId);
            if (!result.next()) {
                System.err.println("That Patient does not exist in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Get the year for medical record
        System.out.println("Please enter the year for medical record in form yyyy.");
        String year = null;
        try {
            year = input.nextLine();
            if(year.length() > 4) {
                System.err.println("Please enter year in form yyyy. No space allowed");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Get the month for medical record
        System.out.println("Please enter the month for medical record in form mm.");
        String month = null;
        try {
            month = input.nextLine();
            if(month.length() != 2) {
                System.err.println("Please enter month in form mm. No space allowed");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Parse the start and end date
        String nextMonth = String.valueOf(Integer.parseInt(month) + 1);
        String start = year + "-" + month + "-01";
        String end = year + "-" + nextMonth + "-01";
        Date startUtil = null;
        Date endUtil = null;
        try {
            startUtil = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            endUtil = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            java.sql.Date sqlStartDate = new java.sql.Date(startUtil.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endUtil.getTime());
            result = statement.executeQuery("SELECT * FROM MedicalRecord WHERE startDate between Date' " + sqlStartDate.toString() + "' and Date '" + sqlEndDate.toString() + "' AND ( endDate is NULL OR endDate between Date '" + sqlStartDate.toString() + "' and Date'" + sqlEndDate.toString() + "') AND patientId = " + pId);
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the updates in the database via query
        try {
            while (result.next()) {
                int id = 0, rD = 0;
                Date sD = null, eD = null;
                StringBuilder pre = new StringBuilder();
                StringBuilder dets = new StringBuilder();
                String res = null, res2 = null;
                id = result.getInt("patientID");
                sD = result.getDate("startDate");
                eD = result.getDate("endDate");
//				rD = result.getInt("responsibleDoctor");
//				res = result.getString("prescription");
//				if (!pre.toString().contains(res)) {
//					pre.append(res + ", ");
//				}
//				res2 = result.getString("diagnosisDetails");
//				if (!dets.toString().contains(res2)) {
//					dets.append(res2 + ", ");
//				}
                System.out.println();
                System.out.println();
                if (eD == null) {
                    System.out.println("Medical Record");
                    System.out.println("--------------------------");
                    System.out.println("Patient ID: " + id);
                    System.out.println("Start Date: " + sD.toString());
                    System.out.println("End Date: NA");
//					System.out.println("Responsible Doctor: " + rD);
//					System.out.println("Prescription: " + pre.toString().substring(0, pre.length() - 2));
//					System.out.println("Diagnosis Details: " + dets.toString().substring(0, dets.length() - 2));
                    System.out.println("--------------------------");
                } else {
                    System.out.println("Medical Record");
                    System.out.println("--------------------------");
                    System.out.println("Patient ID: " + id);
                    System.out.println("Start Date: " + sD.toString());
                    System.out.println("End Date: " + eD.toString());
//					System.out.println("Responsible Doctor: " + rD);
//					System.out.println("Prescription: " + pre.toString().substring(0, pre.length() - 2));
//					System.out.println("Diagnosis Details: " + dets.toString().substring(0, dets.length() - 2));
                    System.out.println("--------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generates report for monthly patient List from check-in information table
     * @param input input from command line
     */
    public void viewMonthlyPatientList(Scanner input) {

        //Get the year for patient list
        System.out.println("Please enter the year for patient list record in form yyyy.");
        String year = null;
        try {
            year = input.nextLine();
            if(year.length() > 4) {
                System.err.println("Please enter year in form yyyy. No space allowed");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Get the month for medical record
        System.out.println("Please enter the month for patient list record in form mm.");
        String month = null;
        try {
            month = input.nextLine();
            if(month.length() != 2) {
                System.err.println("Please enter month in form mm. No space allowed");
                return;
            }
        } catch (Exception e) {
            System.err.println("Invalid input");
            return;
        }

        //Parse the start and end date
        String nextMonth = String.valueOf(Integer.parseInt(month) + 1);
        String start = year + "-" + month + "-01";
        String end = year + "-" + nextMonth + "-01";
        Date startUtil = null;
        Date endUtil = null;
        try {
            startUtil = new SimpleDateFormat("yyyy-MM-dd").parse(start);
            endUtil = new SimpleDateFormat("yyyy-MM-dd").parse(end);
            System.out.println("Start Date " + startUtil + " End Date " + endUtil);
            java.sql.Date sqlStartDate = new java.sql.Date(startUtil.getTime());
            java.sql.Date sqlEndDate = new java.sql.Date(endUtil.getTime());
            result = statement.executeQuery("SELECT * FROM Patient INNER JOIN CheckInInformation ON Patient.id = CheckInInformation.patientId and (startDate BETWEEN Date '" + sqlStartDate.toString() + "' AND Date '" + sqlEndDate.toString() + "') AND ((endDate BETWEEN Date '" + sqlStartDate.toString() + "' AND '" + sqlEndDate.toString() + "') OR endDate is NULL )");
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Generating patient list in " + year + "-" + month);
        try {
            while (result.next()) {
                int id = 0;
                String pSSN = null;
                String pName = null;
                Date pDOB = null;
                String pGender = null;
                int pAge = 0;
                String pPhone = null;
                String pAddr = null;
                int pPlan = 0;
                String pIsInWard = null;
                String pComp = null;
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
                System.out.println("Patient ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                        + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();

    }

    /**
     * Generates report for ward usage percentage report
     * @param input input from command line
     */
    public void viewWardUsagePercentage(Scanner input) {
        try {
            result = statement.executeQuery("SELECT * FROM Ward");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the results in the database via query
        System.out.println();
        System.out.println();
        System.out.println("Generating report for ward usage");
        try {
            while(result.next()) {
                int wN = result.getInt("number");
                int wC = result.getInt("capacity");
                String pList = result.getString("patientIds");
                double wCharges = result.getDouble("chargesPerDay");
                int wO = result.getInt("occupants");
                int wRN = result.getInt("responsibleNurse");
                double percentage = (double)wO / wC;
                System.out.println("Ward number = " + wN + ", capacity = " + wC + ", patient IDs = " + pList + ", charges per day  = " + wCharges + ", occupants = "
                        + wO + ", responsible nurse = " + wRN + ", useage percentage = " + percentage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates patient list of responsible doctor
     * @param input
     */
    public void viewPatientListOfDoctor(Scanner input) {
        System.out.println("Please enter the staff ID of responsible doctor, doctor must be in the database");
        int pId = 0;
        try {
            pId = Integer.parseInt(input.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Doctor ID must be an integer");
            return;
        }

        //Query database for given doctor, quit if doctor does not exist
        try {
            result = statement.executeQuery("SELECT * FROM Staff WHERE id = " + pId);
            if (!result.next()) {
                System.err.println("That Doctor does not exist in the database");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database for given doctor, quit if staff is not a doctor
        try {
            result = statement.executeQuery("SELECT * FROM Staff WHERE id = " + pId + " AND jobTitle = 'Doctor'");
            if (!result.next()) {
                System.err.println("That staff is not a doctor");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Query database in medical record for patient have the responsible doctor
        try {
            result = statement.executeQuery("SELECT * FROM Patient INNER JOIN MedicalRecord ON Patient.id = MedicalRecord.patientId AND MedicalRecord.responsibleDoctor = " + pId );
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Generating patient list for doctor Id " + pId);
        try {
            while (result.next()) {
                int id = 0;
                String pSSN = null;
                String pName = null;
                Date pDOB = null;
                String pGender = null;
                int pAge = 0;
                String pPhone = null;
                String pAddr = null;
                int pPlan = 0;
                String pIsInWard = null;
                String pComp = null;
                id = result.getInt("id");
                pSSN = result.getString("ssn");
                pName = result.getString("name");
                pDOB = result.getDate("dob");
                pGender = result.getString("gender");
                pAge = result.getInt("age");
                pPhone = result.getString("phone");
                pAddr = result.getString("address");
                pPlan = result.getInt("treatment");
                pIsInWard = result.getString("inWard");
                pComp = result.getString("status");
                System.out.println("Patient ID = " + id + ", SSN = " + pSSN + ", name = " + pName + ", date of birth = " + pDOB + ", gender = " + pGender + ", age = " + pAge + ", phone number = " + pPhone
                        + ", address = " + pAddr + ", processing treatment plan = " + pPlan + ", is in ward = " + pIsInWard + ", completing treatment = " + pComp);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();

    }

    /**
     * Generates staff information grouped by role
     * @param input input from command line
     */
    public void viewStaffInformationGroupedByRole(Scanner input) {
        //Query database for nurse
        try {
            result = statement.executeQuery("SELECT jobTitle, COUNT(id) FROM Staff GROUP BY jobTitle");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        //Show the doctors in the database via query
        System.out.println("Generating staff information grouped by role");
        try {
            while (result.next()) {
                String jT = result.getString("jobTitle");
                int id = result.getInt("COUNT(id)");
                System.out.println("Job title = " + jT + ", count = " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();
    }




    /**
     * Initializes the program by establishing a connection to the database and creating the tables
     * for the database.
     */
    private static void initialize() {
        try {
            connectToDatabase();

            //TODO remove the commented block to create the table, if you have tables in your db that have the same name you will get an error, you need to drop any existing tables. Then re-add the block after you've created the tables.
            //Note that ward has a new attribute "occupants" and there is a new table DoctorResponsible so your old tables from the last report need to be updated. If you have all other tables just drop ward and create the new one
            //then create the new DoctorResponsible table and it should be fine.

            statement.executeUpdate("CREATE TABLE Person" +
                "(id INT NOT NULL, name VARCHAR(128) NOT NULL, age INT NOT NULL," +
                "gender CHAR (1) NOT NULL, address VARCHAR(200) NOT NULL, phone CHAR (10), "
                + "PRIMARY KEY(id))");

            statement.executeUpdate("CREATE TABLE Patient" +
                    "(id INT NOT NULL, ssn CHAR (9), name VARCHAR(128) NOT NULL, dob DATE NOT NULL, gender CHAR (1) NOT NULL, "
                    + "age INT NOT NULL, phone CHAR (10), address VARCHAR(200) NOT NULL, treatment INT NOT NULL, inWard VARCHAR(3) NOT NULL, status VARCHAR(3) NOT NULL, "
                    + "primary key(id))");

            statement.executeUpdate("CREATE TABLE Staff" +
            		"(id INT NOT NULL, jobTitle VARCHAR(128) NOT NULL, professionalTitle VARCHAR(128) NOT NULL, department VARCHAR(128) NOT NULL, "
                    + "primary key(id))");


            statement.executeUpdate("CREATE TABLE Treats" +
            		"(patientId INT NOT NULL, staffId INT NOT NULL, "
                    + "primary key(patientId), foreign key(staffId) references Staff(id))");

            statement.executeUpdate("CREATE TABLE Test" +
                    "(id INT NOT NULL, name VARCHAR (128) NOT NULL, results VARCHAR(200) NOT NULL, "
                    + "primary key(id))");

            statement.executeUpdate("CREATE TABLE Recommends" +
            		"(doctorId INT NOT NULL, testId INT NOT NULL, "
                    + "primary key(doctorId), foreign key(testId) references Test(id))");

            statement.executeUpdate("CREATE TABLE Performs" +
            		"(specialistId INT NOT NULL, testId INT NOT NULL, "
                    + "primary key(specialistId), foreign key(testId) references Test(id))");

            statement.executeUpdate("CREATE TABLE AddTest" +
            		"(testId INT NOT NULL, patientId INT NOT NULL, "
                    + "primary key(testId), foreign key(patientId) references Patient(id))");

            statement.executeUpdate("CREATE TABLE Prescription" +
                    "(id INT NOT NULL, name VARCHAR (128) NOT NULL, "
                    + "primary key(id))");

            statement.executeUpdate("CREATE TABLE AddPrescription" +
            		"(prescriptionId INT NOT NULL, patientId INT NOT NULL, "
                    + "primary key(prescriptionId), foreign key(patientId) references Patient(id))");

            statement.executeUpdate("CREATE TABLE DiagnosisDetails" +
                    "(id INT NOT NULL, details VARCHAR (200) NOT NULL, "
                    + "primary key(id))");

            statement.executeUpdate("CREATE TABLE AddDiagnosis" +
            		"(diagnosisId INT NOT NULL, patientId INT NOT NULL, "
                    + "primary key(diagnosisId), foreign key(patientId) references Patient(id))");

            statement.executeUpdate("CREATE TABLE MedicalRecord" +
                    "(patientId INT NOT NULL, startDate DATE NOT NULL, endDate DATE, responsibleDoctor INT NOT NULL, "
                    + "primary key(patientId))");

            statement.executeUpdate("CREATE TABLE AssignPatient" +
            		"(patientId INT NOT NULL, operatorId INT NOT NULL, wardNumber INT NOT NULL, "
                    + "primary key(patientId), foreign key(operatorId) references Staff(id))");

            statement.executeUpdate("CREATE TABLE Ward" +
            		"(number INT NOT NULL, capacity INT NOT NULL, patientIds VARCHAR(200), chargesPerDay DOUBLE NOT NULL, occupants INT NOT NULL, responsibleNurse INT NOT NULL, "
                    + "primary key(number))");

            statement.executeUpdate("CREATE TABLE FirstTimePatient" +
            		"(id INT NOT NULL, primary key(id))");

            statement.executeUpdate("CREATE TABLE Registers" +
            		"(firstTimePatientId INT NOT NULL, operatorId INT NOT NULL, "
                    + "primary key(firstTimePatientId), foreign key(operatorId) references Staff(id))");

            statement.executeUpdate("CREATE TABLE Bills" +
            		"(patientId INT NOT NULL, operatorId INT NOT NULL, "
                    + "primary key(patientId), foreign key(operatorId) references Staff(id))");

            statement.executeUpdate("CREATE TABLE CheckInInformation" +
                    "(patientId INT NOT NULL, wardNumber INT NOT NULL, bedNumber INT NOT NULL, "
                    + "startDate DATE NOT NULL, endDate DATE, "
                    + "primary key(patientId))");

            statement.executeUpdate("CREATE TABLE ProcessCheckIn" +
            		"(patientId INT NOT NULL, operatorId INT NOT NULL, "
                    + "primary key(patientId), foreign key(operatorId) references Staff(id))");

            statement.executeUpdate("CREATE TABLE TracksBilling" +
            		"(patientId INT NOT NULL, operatorId INT NOT NULL, "
                    + "primary key(patientId), foreign key(operatorId) references Staff(id))");

            statement.executeUpdate("CREATE TABLE BillingAccount" +
                    "(patientId INT NOT NULL, responsibleSSN CHAR(9) NOT NULL, billingAddress VARCHAR(200) NOT NULL, "
                    + "visitDate DATE NOT NULL, paymentMethod VARCHAR(128) NOT NULL, cardNumber CHAR(16), registrationFee INT NOT NULL, "
                    + "medicationPrescribed VARCHAR(3) NOT NULL, accommodationFee INT, "
                    + "primary key(patientId))");

            statement.executeUpdate("CREATE TABLE BillingRecord" +
            		"(id INT NOT NULL, total DOUBLE NOT NULL, primary key(id))");

            statement.executeUpdate("CREATE TABLE TrackFees" +
            		"(billingRecordId INT NOT NULL, patientId INT NOT NULL, "
                    + "primary key(billingRecordId), foreign key(patientId) references Patient(id))");

            statement.executeUpdate("CREATE TABLE Fee" +
            		"(id INT NOT NULL, name VARCHAR(128) NOT NULL, amount DOUBLE NOT NULL, primary key(id))");

            statement.executeUpdate("CREATE TABLE AddFee" +
            		"(feeId INT NOT NULL, billingRecordId INT NOT NULL, "
                    + "primary key(feeId), foreign key(billingRecordId) references BillingRecord(id))");

            statement.executeUpdate("CREATE TABLE NurseResponsible" +
            		"(wardNumber INT NOT NULL, nurseId INT NOT NULL, "
                    + "primary key(wardNumber), foreign key(nurseId) references Staff(id))");



        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    /**
     * Connects to the Maria DB using JDBC with the given user name and password. Establishes the connection
     * creates a new statement using the connection then cleans the database for use.
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        //TODO change the user and password for your account
        String user = "dbuser";
        String password = "dbpassword";

        connection = DriverManager.getConnection("jdbc:mysql://localhost/wolfHospital", user, password);
        statement = connection.createStatement();

        //This will drop all tables in the database, only uncomment this and run if you want to clean your db, will only work with the sequence of tables created in this code
//
//		try {
//            statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
//            statement.executeUpdate("DROP TABLE Treats");
//            statement.executeUpdate("DROP TABLE Recommends");
//            statement.executeUpdate("DROP TABLE Performs");
//            statement.executeUpdate("DROP TABLE AddTest");
//            statement.executeUpdate("DROP TABLE Prescription");
//            statement.executeUpdate("DROP TABLE AddPrescription");
//            statement.executeUpdate("DROP TABLE AddDiagnosis");
//            statement.executeUpdate("DROP TABLE AssignPatient");
//            statement.executeUpdate("DROP TABLE Registers");
//            statement.executeUpdate("DROP TABLE Bills");
//            statement.executeUpdate("DROP TABLE ProcessCheckIn");
//            statement.executeUpdate("DROP TABLE CheckInInformation");
//            statement.executeUpdate("DROP TABLE AddFee");
//            statement.executeUpdate("DROP TABLE TracksBilling");
//            statement.executeUpdate("DROP TABLE BillingRecord");
//            statement.executeUpdate("DROP TABLE TrackFees");
//            statement.executeUpdate("DROP TABLE NurseResponsible");
//            statement.executeUpdate("DROP TABLE Fee");
//            statement.executeUpdate("DROP TABLE Test");
//            statement.executeUpdate("DROP TABLE DiagnosisDetails");
//            statement.executeUpdate("DROP TABLE MedicalRecord");
//            statement.executeUpdate("DROP TABLE Ward");
//            statement.executeUpdate("DROP TABLE FirstTimePatient");
//            statement.executeUpdate("DROP TABLE BillingAccount");
//            statement.executeUpdate("DROP TABLE Person");
//            statement.executeUpdate("DROP TABLE Patient");
//            statement.executeUpdate("DROP TABLE Staff");
//            statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


    }

    /**
     * Closes the Connection, Statement, and ResultSet
     * associated with the connection to the database.
     *
     */
    private static void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
