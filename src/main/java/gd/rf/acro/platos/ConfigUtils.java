package gd.rf.acro.platos;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUtils {

    public static Map<String,String> config = new HashMap<>();


    public static Map<String,String> loadConfigs()
    {
        File file = new File(FabricLoader.getInstance().getConfigDirectory().getPath() + "/PlatosTransporters/config.acfg");
        try {
            List<String> lines = FileUtils.readLines(file,"utf-8");
            lines.forEach(line->
            {
                if(line.charAt(0)!='#')
                {
                    String noSpace = line.replace(" ","");
                    String[] entry = noSpace.split("=");
                    config.put(entry[0],entry[1]);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void generateConfigs(List<String> input)
    {
        File file = new File(FabricLoader.getInstance().getConfigDirectory().getPath() + "/PlatosTransporters/config.acfg");

        try {
            FileUtils.writeLines(file,input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String,String> checkConfigs()
    {
        if(new File(FabricLoader.getInstance().getConfigDirectory().getPath() + "/PlatosTransporters/config.acfg").exists())
        {
            return loadConfigs();
        }
        generateConfigs(makeDefaults());
        return loadConfigs();
    }

    private static List<String> makeDefaults()
    {
        List<String> defaults = new ArrayList<>();
        defaults.add("#radius of blocks to be used in a single ship (default: 20)");
        defaults.add("radius=20");
        defaults.add("#should vehicle material use a whitelist? (default: false)");
        defaults.add("whitelist=false");
        defaults.add("#amount of blocks 1 Airship Balloon can lift (default: 2)");
        defaults.add("balloon=2");
        defaults.add("#amount of blocks 1 Water Float can lift (default: 20)");
        defaults.add("float=20");
        defaults.add("#amount of blocks 1 wheel can lift (default: 10)");
        defaults.add("wheel=10");

        return defaults;
    }

}
