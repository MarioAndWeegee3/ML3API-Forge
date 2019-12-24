package marioandweegee3.ml3api;

import marioandweegee3.ml3api.registry.RegistryHelper;
import net.minecraftforge.fml.common.Mod;

@Mod("ml3api")
public class ML3API {
    public static RegistryHelper helper;
    public static ML3API instance;

    public ML3API(){
        helper = new RegistryHelper("ml3api");
        instance = this;
    }
}