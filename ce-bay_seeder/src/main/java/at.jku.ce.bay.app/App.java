package at.jku.ce.bay.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import at.jku.ce.bay.utils.CEBayHelper;
import scala.concurrent.duration.Duration;
import seeder.SeederActor;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {

        ActorSystem actorSystem = ActorSystem.create("ActorSystemStud112");
        ActorRef actor = actorSystem.actorOf(SeederActor.props(), "ActorStud112");

        actor.tell(new SeederActor.InitPublish(), null);



        /*//liefert den für 'Publish' benötigten Hashwert
        try {
            CEBayHelper.GetHash(new File(""));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //Liefert die Referenz zum CEBay Actor, der vom CE Inst. bereitgestellt wird
        CEBayHelper.GetRegistryActorRef();

        //Wird für die Umwandlung einer ActorRef in die zu versendende Stringrepräsentation benötigt
        CEBayHelper.GetRemoteActorRef(ActorRef.noSender());*/

    }

}
