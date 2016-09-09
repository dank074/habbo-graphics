package ch.habbo.graphics.tools.swf;

import ch.habbo.graphics.tools.DataExtractor;
import com.jpexs.decompiler.flash.AbortRetryIgnoreHandler;
import com.jpexs.decompiler.flash.EventListener;
import com.jpexs.decompiler.flash.ReadOnlyTagList;
import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.SWFSourceInfo;
import com.jpexs.decompiler.flash.SwfOpenException;
import com.jpexs.decompiler.flash.abc.ScriptPack;
import com.jpexs.decompiler.flash.configuration.Configuration;
import com.jpexs.decompiler.flash.exporters.BinaryDataExporter;
import com.jpexs.decompiler.flash.exporters.FontExporter;
import com.jpexs.decompiler.flash.exporters.FrameExporter;
import com.jpexs.decompiler.flash.exporters.ImageExporter;
import com.jpexs.decompiler.flash.exporters.MorphShapeExporter;
import com.jpexs.decompiler.flash.exporters.MovieExporter;
import com.jpexs.decompiler.flash.exporters.ShapeExporter;
import com.jpexs.decompiler.flash.exporters.SoundExporter;
import com.jpexs.decompiler.flash.exporters.TextExporter;
import com.jpexs.decompiler.flash.exporters.modes.BinaryDataExportMode;
import com.jpexs.decompiler.flash.exporters.modes.ButtonExportMode;
import com.jpexs.decompiler.flash.exporters.modes.FontExportMode;
import com.jpexs.decompiler.flash.exporters.modes.FrameExportMode;
import com.jpexs.decompiler.flash.exporters.modes.ImageExportMode;
import com.jpexs.decompiler.flash.exporters.modes.MorphShapeExportMode;
import com.jpexs.decompiler.flash.exporters.modes.MovieExportMode;
import com.jpexs.decompiler.flash.exporters.modes.ScriptExportMode;
import com.jpexs.decompiler.flash.exporters.modes.ShapeExportMode;
import com.jpexs.decompiler.flash.exporters.modes.SoundExportMode;
import com.jpexs.decompiler.flash.exporters.modes.SpriteExportMode;
import com.jpexs.decompiler.flash.exporters.modes.TextExportMode;
import com.jpexs.decompiler.flash.exporters.settings.BinaryDataExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.ButtonExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.FontExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.FrameExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.ImageExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.MorphShapeExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.MovieExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.ScriptExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.ShapeExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.SoundExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.SpriteExportSettings;
import com.jpexs.decompiler.flash.exporters.settings.TextExportSettings;
import com.jpexs.decompiler.flash.helpers.FileTextWriter;
import com.jpexs.decompiler.flash.tags.DefineSpriteTag;
import com.jpexs.decompiler.flash.tags.Tag;
import com.jpexs.decompiler.flash.tags.base.ButtonTag;
import com.jpexs.decompiler.flash.tags.base.CharacterIdTag;
import com.jpexs.decompiler.flash.tags.base.CharacterTag;
import com.jpexs.decompiler.flash.treeitems.SWFList;
import com.jpexs.helpers.CancellableWorker;
import com.jpexs.helpers.Path;
import com.jpexs.helpers.stat.StatisticData;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.json.JSONObject;

public class SWFExport {
    
    private String stdOut = null;
    private String stdErr = null;
    
    public SWFExport(){
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
                return;
            }
            if(!Files.exists(Paths.get(outputBase.replace("{filename}", f)))){
                Files.createDirectories(Paths.get(outputBase.replace("{filename}", f)));
            }
            System.out.println("Exporting  " + f);
            this.export(inputBase.replace("{filename}", f), outputBase.replace("{filename}", f), "image,binarydata", new HashMap<>());
        }
        
        //sadly I have to do this because I don't know where the SWF Extractor sets the 1_ 2_ etc... :(
        for(String f : files){
            Files.walk(Paths.get(outputBase.replace("{filename}", f))).forEach((path) -> {
                
                if (Files.isRegularFile(path)) {
                    try{
                        Integer.parseInt(path.toFile().getName().split("_")[0]);
                        
                        String newName = path.toFile().getName().replace(path.toFile().getName().split("_")[0] + "_", "").replace(".bin", ".xml");
                        path.toFile().renameTo(new File(path.toFile().getPath().replace(path.toFile().getName(), newName)));
                        System.out.println(newName);
                    }catch(Exception ex){}
                }
            });
        }
    }
    
    public void exportFurnis(String inputBase, String outputBase, String furniDataDirectory){
        List<String> files = new ArrayList();
        
    }
    
    /**
     * This method was taken from the JPEXS-Decompiler! Exports things from the swf to the destination Directory
     * @param fileName File Name (example: "hair_F_braidbun.swf")
     * @param output Output Directory (example "/avatar/assets/hair_F_braidbun/")
     * @param format Which things should be exported (example: "all" or "image,binarydata,script")
     * @param formats can be an empty HashMap
     */
    private void export(String fileName, String output, String format, Map<String, String> formats) {
        Selection selection = new Selection();
        Selection selectionIds = new Selection();
        List<String> selectionClasses = null;
        double zoom = 1;
        Level traceLevel = Level.WARNING;
        String[] validExportItems = new String[]{
            "script",
            "script_as2",
            "script_as3",
            "image",
            "shape",
            "morphshape",
            "movie",
            "font",
            "frame",
            "sprite",
            "button",
            "sound",
            "binarydata",
            "text",
            "all",
            "fla",
            "xfl"
        };
        String[] removedExportFormats = new String[]{
            "as", "pcode", "hex", "pcodehex", "all_as", "all_pcode", "all_pcodehex", "all_hex", "textplain"
        };
        String exportFormatString = format;
        List<String> exportFormats = Arrays.asList(exportFormatString.split(","));
        File outDirBase = new File(output);
        File inFileOrFolder = new File(fileName);
        if (!inFileOrFolder.exists()) {
            System.err.println("Input SWF file does not exist!");
            return;
        }
        AbortRetryIgnoreHandler handler = new ConsoleAbortRetryIgnoreHandler(0,0);
        boolean exportOK = true;
        List<String> as3classes = new ArrayList<>();
        if (selectionClasses != null) {
            as3classes.addAll(selectionClasses);
        }
        Map<String, StatisticData> stat = new HashMap<>();
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
                final Level level = traceLevel;
                for (String exportFormat : exportFormats) {
                    if (Arrays.asList(removedExportFormats).contains(exportFormat)) {
                        System.err.println("Error: Export format : " + exportFormat + " was REMOVED. Run application with --help parameter to see available formats.");
                        return;
                    } else if (!Arrays.asList(validExportItems).contains(exportFormat)) {
                        System.err.println("Invalid export item:" + exportFormat);
                        return;
                    }
                }

                boolean exportAll = exportFormats.contains("all");
                boolean multipleExportTypes = false;
                EventListener evl = swf.getExportEventListener();

                if (exportAll || exportFormats.contains("image")) {
                    new ImageExporter().exportImages(handler, outDir + (multipleExportTypes ? File.separator + ImageExportSettings.EXPORT_FOLDER_NAME : ""), new ReadOnlyTagList(extags), new ImageExportSettings(enumFromStr(formats.get("image"), ImageExportMode.class)), evl);
                }

                if (exportAll || exportFormats.contains("shape")) {
                    new ShapeExporter().exportShapes(handler, outDir + (multipleExportTypes ? File.separator + ShapeExportSettings.EXPORT_FOLDER_NAME : ""), new ReadOnlyTagList(extags), new ShapeExportSettings(enumFromStr(formats.get("shape"), ShapeExportMode.class), zoom), evl);
                }

                if (exportAll || exportFormats.contains("morphshape")) {
                    new MorphShapeExporter().exportMorphShapes(handler, outDir + (multipleExportTypes ? File.separator + MorphShapeExportSettings.EXPORT_FOLDER_NAME : ""), new ReadOnlyTagList(extags), new MorphShapeExportSettings(enumFromStr(formats.get("morphshape"), MorphShapeExportMode.class), zoom), evl);
                }

                if (exportAll || exportFormats.contains("movie")) {
                    new MovieExporter().exportMovies(handler, outDir + (multipleExportTypes ? File.separator + MovieExportSettings.EXPORT_FOLDER_NAME : ""), new ReadOnlyTagList(extags), new MovieExportSettings(enumFromStr(formats.get("movie"), MovieExportMode.class)), evl);
                }

                if (exportAll || exportFormats.contains("font")) {
                    new FontExporter().exportFonts(handler, outDir + (multipleExportTypes ? File.separator + FontExportSettings.EXPORT_FOLDER_NAME : ""), new ReadOnlyTagList(extags), new FontExportSettings(enumFromStr(formats.get("font"), FontExportMode.class)), evl);
                }

                if (exportAll || exportFormats.contains("sound")) {
                    new SoundExporter().exportSounds(handler, outDir + (multipleExportTypes ? File.separator + SoundExportSettings.EXPORT_FOLDER_NAME : ""), new ReadOnlyTagList(extags), new SoundExportSettings(enumFromStr(formats.get("sound"), SoundExportMode.class)), evl);
                }

                if (exportAll || exportFormats.contains("binarydata")) {
                    new BinaryDataExporter().exportBinaryData(handler, outDir + (multipleExportTypes ? File.separator + BinaryDataExportSettings.EXPORT_FOLDER_NAME : ""), new ReadOnlyTagList(extags), new BinaryDataExportSettings(enumFromStr(formats.get("binarydata"), BinaryDataExportMode.class)), evl);
                }

                if (exportAll || exportFormats.contains("text")) {
                    Boolean singleTextFile = parseBooleanConfigValue(formats.get("singletext"));
                    if (singleTextFile == null) {
                        singleTextFile = Configuration.textExportSingleFile.get();
                    }
                    new TextExporter().exportTexts(handler, outDir + (multipleExportTypes ? File.separator + TextExportSettings.EXPORT_FOLDER_NAME : ""), new ReadOnlyTagList(extags), new TextExportSettings(enumFromStr(formats.get("text"), TextExportMode.class), singleTextFile, zoom), evl);
                }

                FrameExporter frameExporter = new FrameExporter();

                if (exportAll || exportFormats.contains("frame")) {
                    List<Integer> frames = new ArrayList<>();
                    for (int i = 0; i < swf.frameCount; i++) {
                        if (selection.contains(i + 1)) {
                            frames.add(i);
                        }
                    }
                    FrameExportSettings fes = new FrameExportSettings(enumFromStr(formats.get("frame"), FrameExportMode.class), zoom);
                    frameExporter.exportFrames(handler, outDir + (multipleExportTypes ? File.separator + FrameExportSettings.EXPORT_FOLDER_NAME : ""), swf, 0, frames, fes, evl);
                }

                if (exportAll || exportFormats.contains("sprite")) {
                    SpriteExportSettings ses = new SpriteExportSettings(enumFromStr(formats.get("sprite"), SpriteExportMode.class), zoom);
                    for (CharacterTag c : swf.getCharacters().values()) {
                        if (c instanceof DefineSpriteTag) {
                            frameExporter.exportFrames(handler, outDir + (multipleExportTypes ? File.separator + SpriteExportSettings.EXPORT_FOLDER_NAME : ""), swf, c.getCharacterId(), null, ses, evl);
                        }
                    }
                }

                if (exportAll || exportFormats.contains("button")) {
                    ButtonExportSettings bes = new ButtonExportSettings(enumFromStr(formats.get("button"), ButtonExportMode.class), zoom);
                    for (CharacterTag c : swf.getCharacters().values()) {
                        if (c instanceof ButtonTag) {
                            List<Integer> frameNums = new ArrayList<>();
                            frameNums.add(0); // todo: export all frames
                            frameExporter.exportFrames(handler, outDir + (multipleExportTypes ? File.separator + ButtonExportSettings.EXPORT_FOLDER_NAME : ""), swf, c.getCharacterId(), frameNums, bes, evl);
                        }
                    }
                }

                boolean parallel = Configuration.parallelSpeedUp.get();
                Boolean singleScriptFile = parseBooleanConfigValue(formats.get("singlescript"));
                if (singleScriptFile == null) {
                    singleScriptFile = Configuration.scriptExportSingleFile.get();
                }

                if (parallel && singleScriptFile) {
                    System.out.println("export.script.singleFilePallelModeWarning");
                    singleScriptFile = false;
                }

                ScriptExportSettings scriptExportSettings = new ScriptExportSettings(enumFromStr(formats.get("script"), ScriptExportMode.class), singleScriptFile);
                boolean exportAllScript = exportAll || exportFormats.contains("script");
                boolean exportAs2Script = exportAllScript || exportFormats.contains("script_as2");
                boolean exportAs3Script = exportAllScript || exportFormats.contains("script_as3");
                if (exportAs2Script || exportAs3Script) {
                    String scriptsFolder = Path.combine(outDir, ScriptExportSettings.EXPORT_FOLDER_NAME);
                    Path.createDirectorySafe(new File(scriptsFolder));
                    String singleFileName = Path.combine(scriptsFolder, swf.getShortFileName() + scriptExportSettings.getFileExtension());
                    try (FileTextWriter writer = scriptExportSettings.singleFile ? new FileTextWriter(Configuration.getCodeFormatting(), new FileOutputStream(singleFileName)) : null) {
                        scriptExportSettings.singleFileWriter = writer;
                        List<ScriptPack> as3packs = as3classes.isEmpty() ? null : swf.getScriptPacksByClassNames(as3classes);
                        exportOK = swf.exportActionScript(handler, scriptsFolder, as3classes.isEmpty() ? null : as3packs, scriptExportSettings, parallel, evl, exportAs2Script, exportAs3Script) != null && exportOK;
                    }
                }

                swf.clearAllCache();
                CancellableWorker.cancelBackgroundThreads();
            }
        } catch (OutOfMemoryError | Exception ex) {
            
            System.err.print("FAIL: Exporting Failed on Exception - ");
        }
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
