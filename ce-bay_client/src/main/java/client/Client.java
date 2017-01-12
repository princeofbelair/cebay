package client;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import at.jku.ce.bay.api.FilesFound;
import at.jku.ce.bay.api.FindFile;
import at.jku.ce.bay.api.GetFileNames;
import at.jku.ce.bay.api.SeederFound;
import at.jku.ce.bay.utils.CEBayHelper;
import org.omg.CORBA.SystemException;

import java.io.Serializable;
import java.util.List;

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
    ActorSelection cebay = context().actorSelection(CEBayHelper.GetRegistryActorRef());;

    public synchronized void onReceive(Object message) throws Throwable {
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
        } else if(message instanceof InitFindFile)  {
            System.out.println("Filename to find: " + InitFindFile.name);
            cebay.tell(new FindFile(InitFindFile.name), getSelf());
        } else if(message instanceof SeederFound) {
            //TODO call GetFile
        }
        else {
            System.out.println(message.toString());
        }
    }
}
