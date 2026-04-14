/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cosc202.andie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author loekvanbroekhoven
 */
public class MacroManager {
    private static boolean recording = false;
    private static final ArrayList<ImageOperation> operationsList = new ArrayList<>();
  
    /**
     * starts macro and clears previous operationsList
     */
    public static void start(){
        recording = true;
        operationsList.clear();
    }
    /**
     * stops the recording of the macro and saves the file to a marco if there are recrod operations
     * @param file the file to save the macro to
     * @return true if macro is saved, false if there where no operations and macro wasn't saved
     * @throws java.io.IOException if save(file) fails
     */
    public static boolean stop(File file) throws IOException{
        recording = false;
        
        if(operationsList.isEmpty()){
            return false;
        }
        save(file);
        return true;
        
    }
    
    public static void record(ImageOperation op){
        if(recording){
            operationsList.add(op);
        }
    }
    /**
     * saves macro to file
     * @param file  
     * @throws IOException 
     */
    public static void save(File file) throws IOException{
        try (
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        ){
            objOut.writeObject(operationsList);
        }
    }
    
    public static ArrayList<ImageOperation> loadMacro(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
        try (
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
        ) {
            return (ArrayList<ImageOperation>) objIn.readObject();
    }
    }
}
