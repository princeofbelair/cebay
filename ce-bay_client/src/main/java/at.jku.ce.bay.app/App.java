package at.jku.ce.bay.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.SystemGuardian;
import at.jku.ce.bay.utils.CEBayHelper;
import client.Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class App {

    public static void main(String[] args) {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        Random rnd = new Random();
        ActorSystem actorSystem = ActorSystem.create("ClientSystemStud" + rnd.nextInt());
        ActorRef actor = actorSystem.actorOf(Client.props(), "ClientActorStud" + rnd.nextInt());

        System.out.println("--------------------------------");
        System.out.println("CE BAY Client");
        System.out.println("--------------------------------\n");

        do {
            try {
                System.out.println("---------------------------------------------------------------------------------------------------");
                System.out.println("INFO: Type 'get' to list all files. Type 'find' to get file. Type 'exit' to terminate actor system.");
                System.out.println("---------------------------------------------------------------------------------------------------");
                System.out.print("Input: ");
                input = in.readLine();

                if(input.equalsIgnoreCase("get")) {
                        getFileNames(actor);
                } else if(input.equalsIgnoreCase("find")) {
                    System.out.print("Enter filename: ");
                    String fileName = in.readLine();
                    findFile(actor, fileName);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } while(!input.equals("exit"));

        terminateActorSystem(actorSystem);


    }

    private static void getFileNames (ActorRef actor) {
        //send startMessage
        actor.tell(new Client.InitPublish(), null);
    }

    private static void findFile(ActorRef actor, String filename) {
        actor.tell(new Client.InitFindFile(filename), null);
    }

    private static void terminateActorSystem(ActorSystem system) {
        system.shutdown();
    }

}
