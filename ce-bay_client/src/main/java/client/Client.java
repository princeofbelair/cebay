package client;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import at.jku.ce.bay.api.GetFileNames;
import at.jku.ce.bay.utils.CEBayHelper;

/**
 * Created by Romana on 12.01.2017.
 */
public class Client extends UntypedActor {

    public static Props props () {
        return Props.create(Client.class);
    }

    public static class InitPublish {

    }
    //ref from cebay
    ActorSelection cebay = context().actorSelection(CEBayHelper.GetRegistryActorRef());;

    public void onReceive(Object message) throws Throwable {
        if(message instanceof InitPublish) {
            //ask cebay what files are available
            cebay.tell(new GetFileNames(), getSelf());
        }
    }
}
