package seeder;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import at.jku.ce.bay.api.*;
import at.jku.ce.bay.app.App;
import at.jku.ce.bay.utils.CEBayHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.nio.file.*;

import static org.apache.commons.io.FilenameUtils.getPath;


public class SeederActor extends UntypedActor {

    public static Props props () {
        return Props.create(SeederActor.class);
    }

    public static class InitPublish {}

    private String filePath = "Kaffeeee.txt";
    private String fileName = "Kaffeeee.txt";
    private File file = new File(filePath);
    //get ref from ce-bay for communication
    private ActorSelection cebay = context().actorSelection(CEBayHelper.GetRegistryActorRef());

    //TODO Alex: unsere versuche vl brauchst ja noch iwas davon

    //Path path = Paths.get("C:\\home\\joe\\foo");
    //private File file = new File(getPath("C:"+ File.separator + "Users" + File.separator + "Romana" + File.separator + "Desktop" + File.separator + "CE_Klausur.pdf"));
    //private File fileNew = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    //private String directory = fileNew.getParent().toString();

    /*public static String getAbsolutePath(String fileDirectory, String filename) throws UnsupportedEncodingException {
        String absolutePath = URLDecoder.decode(fileDirectory.toString(),"UTF-8")+File.separator+filename;
        System.out.println(absolutePath);
        return absolutePath;
    }*/

    public void onReceive(Object message) throws Throwable {
        //receiving this message from app
        if(message instanceof InitPublish) {
            //String fileDirectory = getAbsolutePath(directory,fileName);

            //send ce-bay message to publish the given file
            cebay.tell(new Publish(filePath, hashedFileName(), seederAddress()), getSelf());
        //receiving this message if someone wants to download our file
        } else if(message instanceof GetFile) {
            GetFile requestedFile = (GetFile) message;
            if(requestedFile.name().equals(this.fileName)) {
                byte[] data = new byte[(int)file.length()];
                FileInputStream fis = new FileInputStream(file);
                if(fis.read(data) != -1) {
                    //send the sender a message with the requested file as byte[]
                    getSender().tell(new FileRetrieved(data), getSelf());
                }
                fis.close();

            } else {
                //if a file is requested which we don't provide send the sender a fileNotFount-message
                getSender().tell(new FileNotFound(requestedFile.name()), getSelf());
            }
        //receiving this message from ce-bay
        } else if (message instanceof GetStatus) {
            //sending the message statusRetrieved back to ce-bay
            getSender().tell(new StatusRetrieved(), getSelf());
        } else {
            //if unknown message
            getSender().tell("Diese Nachricht konnte nicht verarbeitet werden!", getSelf());
        }
    }

    private String hashedFileName() {
        String hash = null;
        try {
            hash = CEBayHelper.GetHash(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    //to get the string representation of the seeder actor
    private String seederAddress() {
        return CEBayHelper.GetRemoteActorRef(getSelf());
    }
}
