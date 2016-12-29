package at.jku.ce.bay.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import at.jku.ce.bay.actors.SeedActor;
import at.jku.ce.bay.client.Client;
import at.jku.ce.bay.helper.CEBayHelper;
import java.io.File;
import at.jku.ce.bay.api.*;


public class App {

    public static void main(String[] args) {


        publishSeederActor();
        //initClient();

        /*//liefert den für 'Publish' benötigten Hashwert
        CEBayHelper.GetHash(new File(""));

        //Liefert die Referenz zum CEBay Actor, der vom CE Inst. bereitgestellt wird
        CEBayHelper.GetRegistryActorRef();

        //Wird für die Umwandlung einer ActorRef in die zu versendende Stringrepräsentation benötigt
        CEBayHelper.GetRemoteActorRef(ActorRef.noSender());*/

    }

    public static void publishSeederActor () {
        ActorSystem actorSystem = ActorSystem.create("ActorSystemSeedStud112");
        ActorRef actor = actorSystem.actorOf(SeedActor.props(), "ActorSeedStud112");

        actor.tell(new SeedActor.InitPublish(), null);
    }

    public static void initClient() {
        ActorSystem actorSystem = ActorSystem.create("ActorSystemClientStud112");
        ActorRef actor = actorSystem.actorOf(Client.props(), "ActorClientStud112");

        actor.tell(new Client.Initialize(), null);
    }

}
