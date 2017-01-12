package at.jku.ce.bay.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import at.jku.ce.bay.utils.CEBayHelper;
import client.Client;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class App {

    public static void main(String[] args) {

        startClient();

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

    private static void startClient () {
        ActorSystem actorSystem = ActorSystem.create("ClientSystemStud112");
        ActorRef actor = actorSystem.actorOf(Client.props(), "ClientActorStud112");
        //send startMessage
        actor.tell(new Client.InitPublish(), null);
    }

}
