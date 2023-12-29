
package controller;

        import model.AddData;
        import repository.AddProjectsRepository;
        import repository.AddmaterialmarksRepository;

        import java.sql.SQLException;
public class ServerAddProjectOrMarks {

    AddProjectsRepository projectsRepository = new AddProjectsRepository();;
    AddmaterialmarksRepository materialmarksRepository = new AddmaterialmarksRepository();



    public int addProject(AddData newProjectData) {
        return projectsRepository.Addprojects(newProjectData);
    }


    public int addMaterialMarks(AddData newProjectData) {
        return materialmarksRepository.insertMaterialData(newProjectData);
    }


}


