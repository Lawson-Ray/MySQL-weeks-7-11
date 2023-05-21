package projects;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ProjectsApp
{
    //created a private instance of the ProjectService class for use
    private ProjectService projectService = new ProjectService();
    //create a list of operations that can be performed by the user
    //used formatter to let the ide know to leave this line of code alone when reformatting
    //@formatter:off
    private List<String> operations = List.of("1) add a project", "2) List projects", "3) Select a project", "4) Update project details", "5) Delete a project");
    //@formatter:on
    //created a private instance of Scanner to initialize it
    private Scanner scanner = new Scanner(System.in);

    private Project curProject;

    public static void main(String[] args)
    {
        //calls ProjectsApp method processUserSelections
        new ProjectsApp().processUserSelections();

        //program currently only has add project and close program capabilities
    }
    //prints the user's options and queries the user for a number related to the action
    //calls the relevant method
    //closes if no option is made(presses enter without typing)
        private void processUserSelections()
    {
        //initializes boolean variable for use in while loop
        boolean done = false;
        //loops until given valid input
        while (!done)
        {
            //try catch block to print any error produced by the input
            try
            {
                //gets int value from user
                int selection = getUserSelection();

                //performs action based on given value
                switch (selection)
                {
                    //-1 if null input
                    case -1: done = exitMenu();
                    break;
                    case 1: createProject();
                    break;
                    case 2: listProjects();
                    break;
                    case 3: selectProject();
                    break;
                    case 4: updateProjectDetails();
                    break;
                    case 5: deleteProject();
                    break;
                    default: System.out.print("\n" + selection + " is not a valid selection. try again.");
                    break;
                }
            }
            //catch and print any exception created by the try block
            catch(Exception e)
            {
                System.out.println("\nError" + e + " Try again.");
            }
        }
    }

    private void deleteProject() {
        listProjects();
        Integer ID = getIntInput("Enter the ID of the project to delete: ");
        projectService.deleteProject(ID);
        System.out.println("The project was successfully deleted.");
        if (nonNull(curProject) && curProject.getProjectId().equals(ID)){
            curProject.setProjectId(null);
        }
    }

    private void updateProjectDetails() {
        if (isNull(curProject)){
            System.out.println("\nPlease select a project.");
        }
        else{
            String projectName = getStringInput(("Enter the project name [" + curProject.getProjectName() + "]"));
            BigDecimal actualHours = getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
            BigDecimal estimatedHours = getDecimalInput( "Enter the estimated hours ["+ curProject.getEstimatedHours() + "]");
            String notes = getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
            Integer difficulty = getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");

            Project project = new Project();
            project.setProjectName(Objects.isNull(projectName)
                ? curProject.getProjectName() : projectName);
            project.setActualHours(Objects.isNull(actualHours)
                    ? curProject.getActualHours() : actualHours);
            project.setEstimatedHours(Objects.isNull(estimatedHours)
                    ? curProject.getEstimatedHours() : estimatedHours);
            project.setNotes(Objects.isNull(notes)
                    ? curProject.getNotes() : notes);
            project.setDifficulty(Objects.isNull(difficulty)
                    ? curProject.getDifficulty() : difficulty);
            project.setProjectId(curProject.getProjectId());

            projectService.modifyProjectDetails(project);
            curProject = projectService.fetchProjectById(curProject.getProjectId());
        }
    }

    private void selectProject() {
        listProjects();
        Integer projectId = getIntInput("Enter a project ID to select a project");
        curProject = null;
        curProject = projectService.fetchProjectById(projectId);
        if (isNull(curProject))
        {
            System.out.println("Invalid project ID selected");
        }
    }

    private void listProjects() {
        List<Project> projects = projectService.fetchAllProjects();

        System.out.println("\nProjects:");

        projects.forEach(project -> System.out.println("  " + project.getProjectId() + ": " + project.getProjectName()));
        }


    //queries the user and returns an int
    private int getUserSelection()
    {
        printOperations();
        Integer input = getIntInput("Enter a menu selection");
        //returns -1 if null
        return isNull(input) ? -1 : input;
    }
    //prints all available actions the user can perform
    private void printOperations()
    {
        System.out.println("\nThese are the available selections. Press the Enter key to quit:");
        operations.forEach(line -> System.out.println("  " + line));
        if(isNull(curProject)) {
            System.out.println("\nYour are not working with a project");
        }
        else {
            System.out.println("\nYou are working with projject: " + curProject);
        }
    }
    //receives a String input from getStringInput and converts it to Integer
    private Integer getIntInput(String prompt)
    {
        //calls getStringInput with the relevant user prompt
        String input = getStringInput(prompt);
        //returns an Integer or null if given appropriate input
        if (isNull(input))
        {
            return null;
        }
        //attempts to convert and return String as an Integer
        try
        {
            return Integer.valueOf(input);
        }
        //provide reason why the String could not be converted properly
        catch(NumberFormatException e)
        {
            throw new DbException(input + " is not a valid number.");
        }
    }
    //prompts the user with a relevant message and returns the String value from the console
    private String getStringInput(String prompt)
    {
        System.out.print(prompt + ": ");
        //recieve user input
        String input = scanner.nextLine();
        //returns the input
        //removes any spaces before and after the given value, but not between
        //returns null if no value is given
        return input.isBlank() ? null : input.trim();
    }
    //method prints out the exit message and returns a boolean that ends the program
    private boolean exitMenu()
    {
        System.out.println("Exiting the menu.");
        return true;
    }
    // gets the required values to insert into the project table
    // inserts these values into a Project object
    // sends the object to the addProject method
    private void createProject()
    {
        Integer difficulty;
        String projectName = getStringInput("Enter the project name");
        BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
        BigDecimal actualHours = getDecimalInput("Enter the actual hours");
        do
        {
            difficulty = getIntInput("Enter the project difficulty (1-5)");
        }
        while (difficulty.intValue() < 1 || difficulty.intValue() > 5);
        String notes = getStringInput("Enter the project notes");
        // creates the Project object sets the values and sends it to the addProject method
        Project project = new Project();
        project.setProjectName(projectName);
        project.setEstimatedHours(estimatedHours);
        project.setActualHours(actualHours);
        project.setDifficulty(difficulty);
        project.setNotes(notes);
        Project dbProject = projectService.addProject(project);
        // prints results
        System.out.println("You have successfully created project: " + dbProject);
    }
    // uses getStringInput to return a Decimal value
    // same as getIntegerInput but with Decimals
    private BigDecimal getDecimalInput(String prompt)
    {
        String input = getStringInput(prompt);
        if (isNull(input))
        {
            return null;
        }
        try
        {
            return new BigDecimal(input).setScale(2);
        }
        catch(NumberFormatException e)
        {
            throw new DbException(input + " is not a valid decimal number.");
        }
    }
}
