package at.jku.ce.bay.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import at.jku.ce.bay.utils.CEBayHelper;
import scala.concurrent.duration.Duration;
import seeder.SeederActor;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) {

        Random rnd = new Random();

        ActorSystem actorSystem = ActorSystem.create("ActorSystemStud" + rnd.nextInt());
        ActorRef actor = actorSystem.actorOf(SeederActor.props(), "ActorStud" + rnd.nextInt());

        publishFile(actor);

    }

    private static void publishFile(ActorRef actor) {
        actor.tell(new SeederActor.InitPublish(), null);
    }

}
