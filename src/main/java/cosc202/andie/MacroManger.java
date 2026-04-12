/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cosc202.andie;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author loekvanbroekhoven
 */
public class MacroManger {
    private boolean recording = false;
    private ArrayList<ImageOperation> operationsList = new ArrayList<>();
    
    public void start(){
        recording = true;
        operationsList.clear();
    }
    /**
     * stops the recording of the macro and saves the file to a marco if there are recrod operations
     * @param file the file to save the macro to
     * @return true if macro is saved, false if there where no operations and macro wasn't saved
     */
    public boolean stop(File file){
        recording = false;
        
        if(operationsList.isEmpty()){
            return false;
        }
        save(file);
        return true;
        
    }
    
    public void record(ImageOperation op){
        if(recording){
            operationsList.add(op);
        }
    }
    
    public void save(File file){
        try{
        } catch(IOException){
            System.out.println("can't save macro file");
        }
    }
    
}
