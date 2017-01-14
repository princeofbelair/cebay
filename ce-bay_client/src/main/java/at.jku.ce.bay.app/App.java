package at.jku.ce.bay.app;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.SystemGuardian;
import at.jku.ce.bay.utils.CEBayHelper;
import client.Client;
import com.typesafe.config.ConfigFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class App {

    public static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    private static Random rnd = new Random();
    private static ActorSystem actorSystem = ActorSystem.create("ClientSystemStud112", ConfigFactory.load("application"));
    private static ActorRef actor = actorSystem.actorOf(Client.props(), "ClientActorStud112");

    public static void main(String[] args) {
        System.out.println("--------------------------------");
        System.out.println("CE BAY Client");
        System.out.println("--------------------------------\n");
        showMenu();
    }

    public static void showMenu() {

        String input = "";

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
            } else if(input.equalsIgnoreCase("exit")) {
                terminateActorSystem(actorSystem);
            } else {
                showMenu();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
