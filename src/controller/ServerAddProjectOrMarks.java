
package controller;

        import model.AddData;
        import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;
        import repository.AddProjectsRepository;
        import repository.AddmaterialmarksRepository;
        import security.DigitalSignature;

        import java.nio.charset.StandardCharsets;
        import java.security.PublicKey;
        import java.sql.SQLException;
public class ServerAddProjectOrMarks {

    AddProjectsRepository projectsRepository = new AddProjectsRepository();
    AddmaterialmarksRepository materialmarksRepository = new AddmaterialmarksRepository();



    public int addProject(AddData newProjectData) {
        return projectsRepository.Addprojects(newProjectData);
    }


    public int addMaterialMarks(AddData newProjectData ,PublicKey key) throws Exception {

        DigitalSignature dd = new DigitalSignature();
      //newProjectData.content ="llllll";

        if(dd.verifySignature(newProjectData.content.getBytes(), Base64.decodeBase64(newProjectData.signatureBytes), key)) {

         //   data.name = "material";
            return materialmarksRepository.insertMaterialData(newProjectData);
        }
        else
            return 0;
    }


}


