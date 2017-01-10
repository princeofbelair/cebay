package at.jku.ce.bay.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import at.jku.ce.bay.helper.CEBayHelper;
import java.io.File;
import at.jku.ce.bay.seedActor.*;

public class App {

    public static void main(String[] args) {

        ActorSystem actorSystem = ActorSystem.create("ActorSystemStud112");
        ActorRef actor = actorSystem.actorOf(SeedActor.props(), "ActorStud112");

        actor.tell(new SeedActor.InitPublish(), null);


        /*//liefert den für 'Publish' benötigten Hashwert
        CEBayHelper.GetHash(new File(""));

        //Liefert die Referenz zum CEBay Actor, der vom CE Inst. bereitgestellt wird
        CEBayHelper.GetRegistryActorRef();

        //Wird für die Umwandlung einer ActorRef in die zu versendende Stringrepräsentation benötigt
        CEBayHelper.GetRemoteActorRef(ActorRef.noSender());*/



    }

}
