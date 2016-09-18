package ch.habbo.graphics.tools.swf;

import ch.habbo.graphics.furnitures.collections.ItemCollection;
import ch.habbo.graphics.tools.DataExtractor;
import com.jpexs.decompiler.flash.AbortRetryIgnoreHandler;
import com.jpexs.decompiler.flash.EventListener;
import com.jpexs.decompiler.flash.ReadOnlyTagList;
import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.SWFSourceInfo;
import com.jpexs.decompiler.flash.SwfOpenException;
import com.jpexs.decompiler.flash.configuration.Configuration;
import com.jpexs.decompiler.flash.exporters.BinaryDataExporter;
import com.jpexs.decompiler.flash.exporters.ImageExporter;
import com.jpexs.decompiler.flash.exporters.modes.BinaryDataExportMode;
import com.jpexs.decompiler.flash.exporters.modes.ImageExportMode;
import com.jpexs.decompiler.flash.exporters.settings.BinaryDataExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.ImageExportSettings;
import com.jpexs.decompiler.flash.tags.Tag;
import com.jpexs.decompiler.flash.tags.base.CharacterIdTag;
import com.jpexs.decompiler.flash.treeitems.SWFList;
import com.jpexs.helpers.CancellableWorker;
import com.jpexs.helpers.Path;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.json.JSONObject;

public class SWFExport {
    
    private String stdOut = null;
    private String stdErr = null;
    private ImageExporter imageExporter;
    private BinaryDataExporter binaryExporter;
    public SWFExport(){
        imageExporter = new ImageExporter();
        binaryExporter = new BinaryDataExporter();
    }
    
    public void exportClothes(String inputBase, String outputBase, String figureDataDirectory) throws Exception, IOException{
        List<String> files = new ArrayList();
        JSONObject figureMap = new DataExtractor(figureDataDirectory).getLibraries();
        for(String key : figureMap.keySet()){
            JSONObject obj = figureMap.getJSONObject(key);
            for(String key2 : obj.keySet()){
                String value = obj.getString(key2);
                if(!files.contains(value)){
                    files.add(value);
                }
                if(value.contains("_50_") && !files.contains(value.replace("_50_", "_"))){
                    files.add(value.replace("_50_", "_"));
                }
            }
        }
        for(String f : files){
            if(!Files.exists(Paths.get(inputBase.replace("{filename}", f)))){
                System.err.println("File " + f + " does not exist.");
                continue;
            }
            if(!Files.exists(Paths.get(outputBase.replace("{filename}", f)))){
                Files.createDirectories(Paths.get(outputBase.replace("{filename}", f)));
            }
            System.out.println("Exporting  " + f);
            boolean b = this.export(inputBase.replace("{filename}", f), outputBase.replace("{filename}", f));
        }
        //this.fileWalker(files, outputBase);
    }
    
    public void exportFurnis(String inputBase, String outputBase, ItemCollection items) throws Exception{
        ImageIO.setUseCache(false);
        List<String> files = new ArrayList();
        items.getFloorItems().stream().filter((item) -> (!files.contains(item.getClassName().split("[*]")[0]))).forEach((item) -> {
            files.add(item.getClassName().split("[*]")[0]);
        });
        
        items.getWallItems().stream().filter((item) -> (!files.contains(item.getClassName().split("[*]")[0]))).forEach((item) -> {
            files.add(item.getClassName().split("[*]")[0]);
        });
        for(String f : files){
            if(!Files.exists(Paths.get(inputBase.replace("{filename}", f)))){
                System.err.println("File " + f + " does not exist.");
                continue;
            }
            if(Files.exists(Paths.get(outputBase.replace("{filename}", f)))){
                continue;
            }
            Files.createDirectories(Paths.get(outputBase.replace("{filename}", f)));
            System.out.println("Exporting  " + f);
            boolean b = this.export(inputBase.replace("{filename}", f), outputBase.replace("{filename}", f));
        }
        this.fileWalker(files, outputBase);
        System.gc();
    }
    
    /**
     * This method was taken from the JPEXS-Decompiler! Exports things from the swf to the destination Directory
     * @param fileName File Name (example: "/swfs/hair_F_braidbun.swf")
     * @param output Output Directory (example "/avatar/assets/hair_F_braidbun/")
     * @param format Which things should be exported (example: "all" or "image,binarydata,script")
     * @param formats can be an empty HashMap
     */
    private boolean export(String fileName, String output) {
        Selection selectionIds = new Selection();
        File outDirBase = new File(output);
        File inFileOrFolder = new File(fileName);
        if (!inFileOrFolder.exists()) {
            System.err.println("Input SWF file does not exist!");
            return false;
        }
        AbortRetryIgnoreHandler handler = new ConsoleAbortRetryIgnoreHandler(0,0);
        try {
            File[] inFiles;
            boolean singleFile = true;
            if (inFileOrFolder.isDirectory()) {
                singleFile = false;
                inFiles = inFileOrFolder.listFiles(getSwfFilter());
            } else {
                inFiles = new File[]{inFileOrFolder};
            }
            for (File inFile : inFiles) {
                String inFileName = Path.getFileNameWithoutExtension(inFile);
                if (stdOut != null) {
                    String outFilePath = stdOut.replace("{swfFile}", inFileName);
                    Path.createDirectorySafe(new File(outFilePath).getParentFile());
                    System.setOut(new PrintStream(new FileOutputStream(outFilePath, true)));
                }
                if (stdErr != null) {
                    String errFilePath = stdErr.replace("{swfFile}", inFileName);
                    Path.createDirectorySafe(new File(errFilePath).getParentFile());
                    System.setErr(new PrintStream(new FileOutputStream(errFilePath, true)));
                }
                SWFSourceInfo sourceInfo = new SWFSourceInfo(null, inFile.getAbsolutePath(), inFile.getName());
                SWF swf;
                try {
                    swf = new SWF(new FileInputStream(inFile), sourceInfo.getFile(), sourceInfo.getFileTitle(), Configuration.parallelSpeedUp.get());
                } catch (FileNotFoundException | SwfOpenException ex) {
                    // FileNotFoundException when anti virus software blocks to open the file
                    System.out.println("Failed to open SWF");
                    continue;
                }
                swf.swfList = new SWFList();
                swf.swfList.sourceInfo = sourceInfo;
                String outDir = outDirBase.getAbsolutePath();
                if (!singleFile) {
                    outDir = Path.combine(outDir, inFile.getName());
                }
                List<Tag> extags = new ArrayList<>();
                for (Tag t : swf.getTags()) {
                    if (t instanceof CharacterIdTag) {
                        CharacterIdTag c = (CharacterIdTag) t;
                        if (selectionIds.contains(c.getCharacterId())) {
                            extags.add(t);
                        }
                    } else if (selectionIds.contains(0)) {
                        extags.add(t);
                    }
                }

                EventListener evl = swf.getExportEventListener();
                imageExporter.exportImages(handler, outDir, new ReadOnlyTagList(extags), new ImageExportSettings(ImageExportMode.PNG_GIF_JPEG), evl);
                binaryExporter.exportBinaryData(handler, outDir , new ReadOnlyTagList(extags), new BinaryDataExportSettings(BinaryDataExportMode.RAW), evl);
                swf.clearAllCache();
                swf = null;
                inFile = null;
                CancellableWorker.cancelBackgroundThreads();
            }
        } catch (OutOfMemoryError | Exception ex) {
            
            System.err.print("FAIL: Exporting Failed on Exception - ");
        }
        return true;
    }
    
    private FilenameFilter getSwfFilter() {
        return (File dir, String name) -> name.toLowerCase().endsWith(".swf");
    }
    
    private <E extends Enum> E enumFromStr(String str, Class<E> cls) {
        E[] vals = cls.getEnumConstants();
        if (str == null) {
            return vals[0];
        }
        for (E e : vals) {
            if (e.toString().toLowerCase().replace("_", "").equals(str.toLowerCase().replace("_", ""))) {
                return e;
            }
        }
        return vals[0];
    }
    
    private Boolean parseBooleanConfigValue(String value) {
        if (value == null) {
            return null;
        }

        Boolean bValue = null;
        value = value.toLowerCase();
        if (value.equals("0") || value.equals("false") || value.equals("no") || value.equals("off")) {
            bValue = false;
        }
        if (value.equals("1") || value.equals("true") || value.equals("yes") || value.equals("on")) {
            bValue = true;
        }
        return bValue;
    }
    
    /**
     * Sadly I have to do this like this because the SWF Decompiler export things like this : 0_file1 1_file2 2_file3
     * @param files 
     */
    private void fileWalker(List<String> files, String outputBase) throws Exception{
        for(String f : files){
            if(!Files.exists(Paths.get(outputBase.replace("{filename}", f)))){
                continue;
            }
            Files.walk(Paths.get(outputBase.replace("{filename}", f))).forEach((path) -> {
                
                if (Files.isRegularFile(path)) {
                    try{
                        String beginSeq = path.toFile().getName().split("_")[0];
                        Integer.parseInt(path.toFile().getName().split("_")[0]);
                        String newName = path.toFile().getName().substring(beginSeq.length() + 1).replace(".bin", ".xml");
                        path.toFile().renameTo(new File(path.toFile().getPath().replace(path.toFile().getName(), newName)));
                    }catch(Exception ex){}
                }
            });
        }
    }
    
    private class Selection {

        public List<Range> ranges;

        public Selection() {
            this.ranges = new ArrayList<>();
            this.ranges.add(new Range(null, null));
        }

        public Selection(List<Range> ranges) {
            this.ranges = ranges;
        }

        public boolean contains(int index) {
            for (Range r : ranges) {
                if (r.contains(index)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private class Range {

        public Integer min;

        public Integer max;

        public Range(Integer min, Integer max) {
            this.min = min;
            this.max = max;
        }

        public boolean contains(int index) {
            int minimum = min == null ? Integer.MIN_VALUE : min;
            int maximum = max == null ? Integer.MAX_VALUE : max;

            return index >= minimum && index <= maximum;
        }
    }
}
