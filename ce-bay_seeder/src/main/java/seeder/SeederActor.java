package seeder;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import at.jku.ce.bay.api.*;
import at.jku.ce.bay.utils.CEBayHelper;
import java.io.File;
import java.util.List;


/**
 * Created by Romana on 10.01.2017.
 */


public class SeederActor extends UntypedActor {

    public static Props props () {
        return Props.create(SeederActor.class);
    }

    public static class InitPublish {

    }

    private String filePath = "TheNorthRemembers.txt";
    ActorSelection cebay = context().actorSelection(CEBayHelper.GetRegistryActorRef());;

    public void onReceive(Object message) throws Throwable {

        if(message instanceof InitPublish) {
            this.cebay.tell(new Publish(filePath, hashedFileName(), seederAddress()), getSelf());
        } else {


        }
        System.out.println("Received Message");
        System.out.println(message.toString());
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
