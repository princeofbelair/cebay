package seeder;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import at.jku.ce.bay.api.*;
import at.jku.ce.bay.utils.CEBayHelper;
import java.io.File;


/**
 * Created by Romana on 10.01.2017.
 */


public class SeederActor extends UntypedActor {

    public static Props props () {
        return Props.create(SeederActor.class);
    }

    public static class InitPublish {

    }

    private String filePath = "test.txt";

    public void onReceive(Object message) throws Throwable {

        if(message instanceof InitPublish) {
            ActorSelection cebay = context().actorSelection(CEBayHelper.GetRegistryActorRef());

            //cebay.tell(new Publish(filePath, hashedFileName(), seederAddress()), getSelf());
            cebay.tell(new GetFileNames(), getSelf());
            System.out.println("Hallo");
        } else if(message instanceof FilesFound) {
            System.out.println("Hallo" + message);
        } else {
            System.out.println("xffggh" + message.toString());
        }

    }

    private String hashedFileName() {
        String hash = null;
        try {
            hash = CEBayHelper.GetHash(new File(this.filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hash;
    }

    private String seederAddress() {
        return CEBayHelper.GetRemoteActorRef(getSelf());
    }
}
