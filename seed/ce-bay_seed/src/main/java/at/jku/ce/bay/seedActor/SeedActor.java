package at.jku.ce.bay.seedActor;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import at.jku.ce.bay.api.*;
import at.jku.ce.bay.helper.CEBayHelper;


import java.io.File;

/**
 * Created by Romana on 29.12.2016.
 */
public class SeedActor extends UntypedActor {

    public static Props props () {
        return Props.create(SeedActor.class);
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
        } else if(message instanceof FileRetrieved) {
            System.out.println("Hallo" + message);
        } else {
            System.out.println("xffggh" + message.toString());
        }

    }

    private String hashedFileName() {
        return CEBayHelper.GetHash(new File(this.filePath));
    }

    private String seederAddress() {
        return CEBayHelper.GetRemoteActorRef(getSelf());
    }
}
