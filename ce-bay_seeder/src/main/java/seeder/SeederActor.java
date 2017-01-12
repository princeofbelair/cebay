package seeder;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import at.jku.ce.bay.api.*;
import at.jku.ce.bay.utils.CEBayHelper;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;


/**
 * Created by Romana on 10.01.2017.
 */


public class SeederActor extends UntypedActor {

    public static Props props () {
        return Props.create(SeederActor.class);
    }

    public static class InitPublish {}

    private String filePath = "DieJoffreyDie.txt";
    private String fileName = "DieJoffreyDie.txt";
    private ActorSelection cebay = context().actorSelection(CEBayHelper.GetRegistryActorRef());
    private File file = new File(filePath);

    public void onReceive(Object message) throws Throwable {

        if(message instanceof InitPublish) {
            cebay.tell(new Publish(filePath, hashedFileName(), seederAddress()), getSelf());
        } else if(message instanceof GetFile) {
            GetFile requestedFile = (GetFile) message;
            if(requestedFile.name().equals(this.fileName)) {
                byte[] data = new byte[(int)file.length()];
                FileInputStream fis = new FileInputStream(file);
                if(fis.read(data) != -1) {
                    getSender().tell(new FileRetrieved(data), getSelf());
                }
                fis.close();

            } else {
                getSender().tell(new FileNotFound(requestedFile.name()), getSelf());
            }
        } else {
            System.out.println("Hallo, ich funktioniere!!");
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

    private String seederAddress() {
        return CEBayHelper.GetRemoteActorRef(getSelf());
    }
}
