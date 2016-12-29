package at.jku.ce.bay.client;

import akka.actor.*;
import at.jku.ce.bay.actors.SeedActor;
import at.jku.ce.bay.api.*;
import at.jku.ce.bay.helper.CEBayHelper;

/**
 * Created by Romana on 29.12.2016.
 */

public class Client {

    public void publishSeederActor () {
        ActorSystem actorSystem = ActorSystem.create("ActorSystemStud112");
        ActorRef actor = actorSystem.actorOf(SeedActor.props(), "ActorStud112");

        actor.tell(new SeedActor.InitPublish(), null);
    }
}
