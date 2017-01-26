package client;

import akka.actor.*;
import at.jku.ce.bay.api.*;
import at.jku.ce.bay.app.App;
import at.jku.ce.bay.utils.CEBayHelper;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


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

    //ref from ce-bay, to be able to send messages to ce-bay
    ActorSelection cebay = context().actorSelection(CEBayHelper.GetRegistryActorRef());

    public void onReceive(Object message) throws Throwable {
        //receiving this message if the user of the app writes get in the menu
        if(message instanceof InitPublish) {
            //ask ce-bay what files are available
            cebay.tell(new GetFileNames(), getSelf());
        //answer from ce-bay to the message getFileNames() - list of published files
        } else if(message instanceof FilesFound) {
            FilesFound files = (FilesFound) message;
            List<String> data = new ArrayList<>(files.fileNames());
            //sort the incoming list of files alphabetically for better overwiev
            data.sort(String::compareToIgnoreCase);

            System.out.println("-----------------------------------");
            System.out.println("Files of CEBAY");
            System.out.println("-----------------------------------");

            for (String filename : data) {
                System.out.println("Filename: " + filename);
            }
            //give the user the chance to decide what to do next
            App.showMenu();
        //receiving this message if the user writes find in the menu
        } else if(message instanceof InitFindFile)  {
            System.out.println("Filename to find: " + InitFindFile.name);
            //message to ce-bay to find a specific file (filename - input from user)
            cebay.tell(new FindFile(InitFindFile.name), getSelf());
        //answer from ce-bay on message findFile - returns a list of seeders who are providing the requested file
        } else if(message instanceof SeederFound) {
            SeederFound data = (SeederFound) message;
            List<String> seeders = data.seeder();

            System.out.println("\n----------------------------------");
            System.out.println("Seeders that provide this file: ");
            System.out.println("----------------------------------");

            if(seeders.isEmpty()) {
                System.out.println("No Seeders found providing this file.");
                //user can choose what to do next
                App.showMenu();
            } else {
                int row = 0;
                for (String address : seeders) {
                    System.out.println("Index: [" + row + "]: " + address);
                    row++;
                }
                //choose seeder from whom to download the file
                System.out.print("Enter Index of Seeder: ");
                String index = App.in.readLine();
                //System.out.println(seeders.get(Integer.parseInt(index)));

                ActorRef remoteSeeder = context().actorFor(seeders.get(Integer.parseInt(index)));
                //send message to chosen seeder to download the file

                remoteSeeder.tell(new GetFile(InitFindFile.name), getSelf());
            }
        //answer of the remote seeder if the requested file was not found
        } else if(message instanceof FileNotFound) {
            System.out.println("The requested file was not found on the server.");
            //user can choose what to do next
            App.showMenu();
        //answer of the remote seeder with the requested file as byte[]
        } else if(message instanceof FileRetrieved) {
            //convert to file and save file
            byte[] data = ((FileRetrieved) message).data();
            System.out.print("Save file as: ");
            String path = App.in.readLine();
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(data);
            fos.close();
            System.out.println("Download of file was successfully!");
            //user can choose what to do next
            App.showMenu();
        //if an unknown message is received
        } else {
            //send a message back to the sender, that this message is unknown
            getSender().tell("Diese Nachricht konnte nicht verarbeitet werden!", getSelf());
            //user can choose what to do next
            App.showMenu();
        }
    }
}
