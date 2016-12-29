package at.jku.ce.bay.client;

import akka.actor.*;
import at.jku.ce.bay.actors.SeedActor;
import at.jku.ce.bay.api.*;
import at.jku.ce.bay.helper.CEBayHelper;

/**
 * Created by Romana on 29.12.2016.
 */

public class Client extends UntypedActor{

    public static class Initialize {

    }

    public static Props props () {
        return Props.create(SeedActor.class);
    }

    public void onReceive(Object message) throws Throwable {
        if(message instanceof Initialize) {
            ActorSelection cebay = context().actorSelection(CEBayHelper.GetRegistryActorRef());
            cebay.tell(new GetFileNames(), getSelf());
        } else {
            System.out.println(message.toString());
        }
    }
}
