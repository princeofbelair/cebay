package at.jku.ce.bay.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import at.jku.ce.bay.utils.CEBayHelper;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;
import seeder.SeederActor;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class App {

    public static Random rnd = new Random();
    public static String seederActor = "seederActor" + rnd.nextInt();

    public static void main(String[] args) {


        //create seeder actor system
        ActorSystem actorSystem = ActorSystem.create(seederActor);
        ActorRef actor = actorSystem.actorOf(SeederActor.props(), seederActor);
        //send message to seeder actor
        publishFile(actor);

    }
    //send the seeder actor a message, as reaction on receiving that message he will send a publish message to ce-bay
    private static void publishFile(ActorRef actor) {
        actor.tell(new SeederActor.InitPublish(), null);
    }

}
