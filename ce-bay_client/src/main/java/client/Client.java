package client;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.stream.impl.fusing.Scan;
import at.jku.ce.bay.api.*;
import at.jku.ce.bay.app.App;
import at.jku.ce.bay.utils.CEBayHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Romana on 12.01.2017.
 */

public class Client extends UntypedActor {

    public static Props props () {
        return Props.create(Client.class);
    }

    public static class InitPublish {}
    public static class InitFindFile {
        private static String name;

        public InitFindFile(String name) {
            this.name = name;
        }
    }

    //ref from cebay
    ActorSelection cebay = context().actorSelection(CEBayHelper.GetRegistryActorRef());
    //BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public void onReceive(Object message) throws Throwable {

        if(message instanceof InitPublish) {
            //ask cebay what files are available
            cebay.tell(new GetFileNames(), getSelf());
        } else if(message instanceof FilesFound) {
            FilesFound files = (FilesFound) message;
            List<String> data = files.fileNames();

            System.out.println("-----------------------------------");
            System.out.println("Files of CEBAY");
            System.out.println("-----------------------------------");

            for (String filename : data) {
                System.out.println("Filename: " + filename);
            }

            App.showMenu();

        } else if(message instanceof InitFindFile)  {
            System.out.println("Filename to find: " + InitFindFile.name);
            cebay.tell(new FindFile(InitFindFile.name), getSelf());
        } else if(message instanceof SeederFound) {
            SeederFound data = (SeederFound) message;
            List<String> seeders = data.seeder();

            System.out.println("\n----------------------------------");
            System.out.println("Seeders that provide this file: ");
            System.out.println("----------------------------------");

            if(seeders.isEmpty()) {
                System.out.println("No Seeders found providing this file.");
                App.showMenu();
            } else {
                int row = 0;
                for (String address : seeders) {
                    System.out.println("Index: [" + row + "]: " + address);
                    row++;
                }
                System.out.print("Enter Index of Seeder: ");
                String index = App.in.readLine();

                System.out.println(seeders.get(Integer.parseInt(index)));
                ActorSelection remoteSeeder = context().actorSelection(seeders.get(Integer.parseInt(index)));
                remoteSeeder.tell(new GetFile(InitFindFile.name), getSelf());
            }
        } else if(message instanceof FileNotFound) {
            System.out.println("The requested file was not found on the server.");
            App.showMenu();
        } else if(message instanceof FileRetrieved) {
            byte[] data = ((FileRetrieved) message).data();
            System.out.print("Save file as: ");
            String path = App.in.readLine();
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(data);
            fos.close();
            System.out.println("Download of file was successfully!");
            App.showMenu();
        } else {
            System.out.println(message.toString());
            App.showMenu();
        }
    }
}
